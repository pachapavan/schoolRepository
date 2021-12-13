package com.school.app.web.rest;

import com.school.app.domain.BusRoute;
import com.school.app.repository.BusRouteRepository;
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
        return ResponseEntity
            .created(new URI("/api/bus-routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-routes/:id} : Updates an existing busRoute.
     *
     * @param id the id of the busRoute to save.
     * @param busRoute the busRoute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busRoute,
     * or with status {@code 400 (Bad Request)} if the busRoute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busRoute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-routes/{id}")
    public ResponseEntity<BusRoute> updateBusRoute(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusRoute busRoute
    ) throws URISyntaxException {
        log.debug("REST request to update BusRoute : {}, {}", id, busRoute);
        if (busRoute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busRoute.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRouteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusRoute result = busRouteRepository.save(busRoute);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busRoute.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-routes/:id} : Partial updates given fields of an existing busRoute, field will ignore if it is null
     *
     * @param id the id of the busRoute to save.
     * @param busRoute the busRoute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busRoute,
     * or with status {@code 400 (Bad Request)} if the busRoute is not valid,
     * or with status {@code 404 (Not Found)} if the busRoute is not found,
     * or with status {@code 500 (Internal Server Error)} if the busRoute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-routes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusRoute> partialUpdateBusRoute(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusRoute busRoute
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusRoute partially : {}, {}", id, busRoute);
        if (busRoute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busRoute.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRouteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusRoute> result = busRouteRepository
            .findById(busRoute.getId())
            .map(existingBusRoute -> {
                if (busRoute.getRouteName() != null) {
                    existingBusRoute.setRouteName(busRoute.getRouteName());
                }
                if (busRoute.getRouteDriver() != null) {
                    existingBusRoute.setRouteDriver(busRoute.getRouteDriver());
                }
                if (busRoute.getBusNumber() != null) {
                    existingBusRoute.setBusNumber(busRoute.getBusNumber());
                }
                if (busRoute.getYear() != null) {
                    existingBusRoute.setYear(busRoute.getYear());
                }
                if (busRoute.getStatus() != null) {
                    existingBusRoute.setStatus(busRoute.getStatus());
                }
                if (busRoute.getComments() != null) {
                    existingBusRoute.setComments(busRoute.getComments());
                }

                return existingBusRoute;
            })
            .map(busRouteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, busRoute.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
