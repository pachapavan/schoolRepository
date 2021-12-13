package com.school.app.web.rest;

import com.school.app.domain.BusRouteName;
import com.school.app.repository.BusRouteNameRepository;
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
 * REST controller for managing {@link com.school.app.domain.BusRouteName}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BusRouteNameResource {

    private final Logger log = LoggerFactory.getLogger(BusRouteNameResource.class);

    private static final String ENTITY_NAME = "busRouteName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusRouteNameRepository busRouteNameRepository;

    public BusRouteNameResource(BusRouteNameRepository busRouteNameRepository) {
        this.busRouteNameRepository = busRouteNameRepository;
    }

    /**
     * {@code POST  /bus-route-names} : Create a new busRouteName.
     *
     * @param busRouteName the busRouteName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busRouteName, or with status {@code 400 (Bad Request)} if the busRouteName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-route-names")
    public ResponseEntity<BusRouteName> createBusRouteName(@RequestBody BusRouteName busRouteName) throws URISyntaxException {
        log.debug("REST request to save BusRouteName : {}", busRouteName);
        if (busRouteName.getId() != null) {
            throw new BadRequestAlertException("A new busRouteName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusRouteName result = busRouteNameRepository.save(busRouteName);
        return ResponseEntity
            .created(new URI("/api/bus-route-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-route-names/:id} : Updates an existing busRouteName.
     *
     * @param id the id of the busRouteName to save.
     * @param busRouteName the busRouteName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busRouteName,
     * or with status {@code 400 (Bad Request)} if the busRouteName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busRouteName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-route-names/{id}")
    public ResponseEntity<BusRouteName> updateBusRouteName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusRouteName busRouteName
    ) throws URISyntaxException {
        log.debug("REST request to update BusRouteName : {}, {}", id, busRouteName);
        if (busRouteName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busRouteName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRouteNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusRouteName result = busRouteNameRepository.save(busRouteName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busRouteName.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-route-names/:id} : Partial updates given fields of an existing busRouteName, field will ignore if it is null
     *
     * @param id the id of the busRouteName to save.
     * @param busRouteName the busRouteName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busRouteName,
     * or with status {@code 400 (Bad Request)} if the busRouteName is not valid,
     * or with status {@code 404 (Not Found)} if the busRouteName is not found,
     * or with status {@code 500 (Internal Server Error)} if the busRouteName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-route-names/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusRouteName> partialUpdateBusRouteName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusRouteName busRouteName
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusRouteName partially : {}, {}", id, busRouteName);
        if (busRouteName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busRouteName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRouteNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusRouteName> result = busRouteNameRepository
            .findById(busRouteName.getId())
            .map(existingBusRouteName -> {
                if (busRouteName.getRouteName() != null) {
                    existingBusRouteName.setRouteName(busRouteName.getRouteName());
                }

                return existingBusRouteName;
            })
            .map(busRouteNameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busRouteName.getId().toString())
        );
    }

    /**
     * {@code GET  /bus-route-names} : get all the busRouteNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busRouteNames in body.
     */
    @GetMapping("/bus-route-names")
    public List<BusRouteName> getAllBusRouteNames() {
        log.debug("REST request to get all BusRouteNames");
        return busRouteNameRepository.findAll();
    }

    /**
     * {@code GET  /bus-route-names/:id} : get the "id" busRouteName.
     *
     * @param id the id of the busRouteName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busRouteName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-route-names/{id}")
    public ResponseEntity<BusRouteName> getBusRouteName(@PathVariable Long id) {
        log.debug("REST request to get BusRouteName : {}", id);
        Optional<BusRouteName> busRouteName = busRouteNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(busRouteName);
    }

    /**
     * {@code DELETE  /bus-route-names/:id} : delete the "id" busRouteName.
     *
     * @param id the id of the busRouteName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-route-names/{id}")
    public ResponseEntity<Void> deleteBusRouteName(@PathVariable Long id) {
        log.debug("REST request to delete BusRouteName : {}", id);
        busRouteNameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
