package com.school.app.web.rest;

import com.school.app.domain.BusRoute;
import com.school.app.repository.BusRouteRepository;
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
 * REST controller for managing {@link com.school.app.domain.BusRoute}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BusRouteResource {

    private final Logger log = LoggerFactory.getLogger(BusRouteResource.class);

    private static final String ENTITY_NAME = "busRoute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusRouteRepository busRouteRepository;

    public BusRouteResource(BusRouteRepository busRouteRepository) {
        this.busRouteRepository = busRouteRepository;
    }

    /**
     * {@code POST  /bus-routes} : Create a new busRoute.
     *
     * @param busRoute the busRoute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busRoute, or with status {@code 400 (Bad Request)} if the busRoute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-routes")
    public ResponseEntity<BusRoute> createBusRoute(@RequestBody BusRoute busRoute) throws URISyntaxException {
        log.debug("REST request to save BusRoute : {}", busRoute);
        if (busRoute.getId() != null) {
            throw new BadRequestAlertException("A new busRoute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusRoute result = busRouteRepository.save(busRoute);
        return ResponseEntity.created(new URI("/api/bus-routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-routes} : Updates an existing busRoute.
     *
     * @param busRoute the busRoute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busRoute,
     * or with status {@code 400 (Bad Request)} if the busRoute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busRoute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-routes")
    public ResponseEntity<BusRoute> updateBusRoute(@RequestBody BusRoute busRoute) throws URISyntaxException {
        log.debug("REST request to update BusRoute : {}", busRoute);
        if (busRoute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BusRoute result = busRouteRepository.save(busRoute);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busRoute.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bus-routes} : get all the busRoutes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busRoutes in body.
     */
    @GetMapping("/bus-routes")
    public List<BusRoute> getAllBusRoutes() {
        log.debug("REST request to get all BusRoutes");
        return busRouteRepository.findAll();
    }

    /**
     * {@code GET  /bus-routes/:id} : get the "id" busRoute.
     *
     * @param id the id of the busRoute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busRoute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-routes/{id}")
    public ResponseEntity<BusRoute> getBusRoute(@PathVariable Long id) {
        log.debug("REST request to get BusRoute : {}", id);
        Optional<BusRoute> busRoute = busRouteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(busRoute);
    }

    /**
     * {@code DELETE  /bus-routes/:id} : delete the "id" busRoute.
     *
     * @param id the id of the busRoute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-routes/{id}")
    public ResponseEntity<Void> deleteBusRoute(@PathVariable Long id) {
        log.debug("REST request to delete BusRoute : {}", id);
        busRouteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
