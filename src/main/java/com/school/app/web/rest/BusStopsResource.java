package com.school.app.web.rest;

import com.school.app.domain.BusStops;
import com.school.app.repository.BusStopsRepository;
import com.school.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.school.app.domain.BusStops}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BusStopsResource {

    private final Logger log = LoggerFactory.getLogger(BusStopsResource.class);

    private static final String ENTITY_NAME = "busStops";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusStopsRepository busStopsRepository;

    public BusStopsResource(BusStopsRepository busStopsRepository) {
        this.busStopsRepository = busStopsRepository;
    }

    /**
     * {@code POST  /bus-stops} : Create a new busStops.
     *
     * @param busStops the busStops to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busStops, or with status {@code 400 (Bad Request)} if the busStops has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-stops")
    public ResponseEntity<BusStops> createBusStops(@RequestBody BusStops busStops) throws URISyntaxException {
        log.debug("REST request to save BusStops : {}", busStops);
        if (busStops.getId() != null) {
            throw new BadRequestAlertException("A new busStops cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusStops result = busStopsRepository.save(busStops);
        return ResponseEntity
            .created(new URI("/api/bus-stops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-stops/:id} : Updates an existing busStops.
     *
     * @param id the id of the busStops to save.
     * @param busStops the busStops to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busStops,
     * or with status {@code 400 (Bad Request)} if the busStops is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busStops couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-stops/{id}")
    public ResponseEntity<BusStops> updateBusStops(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusStops busStops
    ) throws URISyntaxException {
        log.debug("REST request to update BusStops : {}, {}", id, busStops);
        if (busStops.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busStops.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busStopsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusStops result = busStopsRepository.save(busStops);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busStops.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-stops/:id} : Partial updates given fields of an existing busStops, field will ignore if it is null
     *
     * @param id the id of the busStops to save.
     * @param busStops the busStops to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busStops,
     * or with status {@code 400 (Bad Request)} if the busStops is not valid,
     * or with status {@code 404 (Not Found)} if the busStops is not found,
     * or with status {@code 500 (Internal Server Error)} if the busStops couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-stops/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusStops> partialUpdateBusStops(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusStops busStops
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusStops partially : {}, {}", id, busStops);
        if (busStops.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busStops.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busStopsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusStops> result = busStopsRepository
            .findById(busStops.getId())
            .map(existingBusStops -> {
                if (busStops.getRouteName() != null) {
                    existingBusStops.setRouteName(busStops.getRouteName());
                }
                if (busStops.getBusStops() != null) {
                    existingBusStops.setBusStops(busStops.getBusStops());
                }

                return existingBusStops;
            })
            .map(busStopsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busStops.getId().toString())
        );
    }

    /**
     * {@code GET  /bus-stops} : get all the busStops.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busStops in body.
     */
    @GetMapping("/bus-stops")
    public List<BusStops> getAllBusStops() {
        log.debug("REST request to get all BusStops");
        return busStopsRepository.findAll();
    }

    /**
     * {@code GET  /bus-stops/:id} : get the "id" busStops.
     *
     * @param id the id of the busStops to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busStops, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-stops/{id}")
    public ResponseEntity<BusStops> getBusStops(@PathVariable Long id) {
        log.debug("REST request to get BusStops : {}", id);
        Optional<BusStops> busStops = busStopsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(busStops);
    }

    /**
     * {@code DELETE  /bus-stops/:id} : delete the "id" busStops.
     *
     * @param id the id of the busStops to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-stops/{id}")
    public ResponseEntity<Void> deleteBusStops(@PathVariable Long id) {
        log.debug("REST request to delete BusStops : {}", id);
        busStopsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
