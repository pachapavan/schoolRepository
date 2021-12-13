package com.school.app.web.rest;

import com.school.app.domain.StaffSalary;
import com.school.app.repository.StaffSalaryRepository;
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
 * REST controller for managing {@link com.school.app.domain.StaffSalary}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StaffSalaryResource {

    private final Logger log = LoggerFactory.getLogger(StaffSalaryResource.class);

    private static final String ENTITY_NAME = "staffSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaffSalaryRepository staffSalaryRepository;

    public StaffSalaryResource(StaffSalaryRepository staffSalaryRepository) {
        this.staffSalaryRepository = staffSalaryRepository;
    }

    /**
     * {@code POST  /staff-salaries} : Create a new staffSalary.
     *
     * @param staffSalary the staffSalary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staffSalary, or with status {@code 400 (Bad Request)} if the staffSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staff-salaries")
    public ResponseEntity<StaffSalary> createStaffSalary(@RequestBody StaffSalary staffSalary) throws URISyntaxException {
        log.debug("REST request to save StaffSalary : {}", staffSalary);
        if (staffSalary.getId() != null) {
            throw new BadRequestAlertException("A new staffSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaffSalary result = staffSalaryRepository.save(staffSalary);
        return ResponseEntity
            .created(new URI("/api/staff-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /staff-salaries/:id} : Updates an existing staffSalary.
     *
     * @param id the id of the staffSalary to save.
     * @param staffSalary the staffSalary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffSalary,
     * or with status {@code 400 (Bad Request)} if the staffSalary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staffSalary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staff-salaries/{id}")
    public ResponseEntity<StaffSalary> updateStaffSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StaffSalary staffSalary
    ) throws URISyntaxException {
        log.debug("REST request to update StaffSalary : {}, {}", id, staffSalary);
        if (staffSalary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffSalary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaffSalary result = staffSalaryRepository.save(staffSalary);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffSalary.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /staff-salaries/:id} : Partial updates given fields of an existing staffSalary, field will ignore if it is null
     *
     * @param id the id of the staffSalary to save.
     * @param staffSalary the staffSalary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffSalary,
     * or with status {@code 400 (Bad Request)} if the staffSalary is not valid,
     * or with status {@code 404 (Not Found)} if the staffSalary is not found,
     * or with status {@code 500 (Internal Server Error)} if the staffSalary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/staff-salaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaffSalary> partialUpdateStaffSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StaffSalary staffSalary
    ) throws URISyntaxException {
        log.debug("REST request to partial update StaffSalary partially : {}, {}", id, staffSalary);
        if (staffSalary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffSalary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaffSalary> result = staffSalaryRepository
            .findById(staffSalary.getId())
            .map(existingStaffSalary -> {
                if (staffSalary.getSalaryPaid() != null) {
                    existingStaffSalary.setSalaryPaid(staffSalary.getSalaryPaid());
                }
                if (staffSalary.getMonth() != null) {
                    existingStaffSalary.setMonth(staffSalary.getMonth());
                }

                return existingStaffSalary;
            })
            .map(staffSalaryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffSalary.getId().toString())
        );
    }

    /**
     * {@code GET  /staff-salaries} : get all the staffSalaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staffSalaries in body.
     */
    @GetMapping("/staff-salaries")
    public List<StaffSalary> getAllStaffSalaries() {
        log.debug("REST request to get all StaffSalaries");
        return staffSalaryRepository.findAll();
    }

    /**
     * {@code GET  /staff-salaries/:id} : get the "id" staffSalary.
     *
     * @param id the id of the staffSalary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staffSalary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staff-salaries/{id}")
    public ResponseEntity<StaffSalary> getStaffSalary(@PathVariable Long id) {
        log.debug("REST request to get StaffSalary : {}", id);
        Optional<StaffSalary> staffSalary = staffSalaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staffSalary);
    }

    /**
     * {@code DELETE  /staff-salaries/:id} : delete the "id" staffSalary.
     *
     * @param id the id of the staffSalary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staff-salaries/{id}")
    public ResponseEntity<Void> deleteStaffSalary(@PathVariable Long id) {
        log.debug("REST request to delete StaffSalary : {}", id);
        staffSalaryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
