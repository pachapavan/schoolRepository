package com.school.app.web.rest;

import com.school.app.domain.BusStops;
import com.school.app.repository.BusStopsRepository;
import com.school.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(new URI("/api/bus-stops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-stops} : Updates an existing busStops.
     *
     * @param busStops the busStops to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busStops,
     * or with status {@code 400 (Bad Request)} if the busStops is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busStops couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-stops")
    public ResponseEntity<BusStops> updateBusStops(@RequestBody BusStops busStops) throws URISyntaxException {
        log.debug("REST request to update BusStops : {}", busStops);
        if (busStops.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BusStops result = busStopsRepository.save(busStops);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busStops.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
