package com.school.app.web.rest;

import com.school.app.domain.StudentFee;
import com.school.app.repository.StudentFeeRepository;
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
 * REST controller for managing {@link com.school.app.domain.StudentFee}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StudentFeeResource {

    private final Logger log = LoggerFactory.getLogger(StudentFeeResource.class);

    private static final String ENTITY_NAME = "studentFee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentFeeRepository studentFeeRepository;

    public StudentFeeResource(StudentFeeRepository studentFeeRepository) {
        this.studentFeeRepository = studentFeeRepository;
    }

    /**
     * {@code POST  /student-fees} : Create a new studentFee.
     *
     * @param studentFee the studentFee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentFee, or with status {@code 400 (Bad Request)} if the studentFee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-fees")
    public ResponseEntity<StudentFee> createStudentFee(@RequestBody StudentFee studentFee) throws URISyntaxException {
        log.debug("REST request to save StudentFee : {}", studentFee);
        if (studentFee.getId() != null) {
            throw new BadRequestAlertException("A new studentFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentFee result = studentFeeRepository.save(studentFee);
        return ResponseEntity
            .created(new URI("/api/student-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-fees/:id} : Updates an existing studentFee.
     *
     * @param id the id of the studentFee to save.
     * @param studentFee the studentFee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentFee,
     * or with status {@code 400 (Bad Request)} if the studentFee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentFee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-fees/{id}")
    public ResponseEntity<StudentFee> updateStudentFee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentFee studentFee
    ) throws URISyntaxException {
        log.debug("REST request to update StudentFee : {}, {}", id, studentFee);
        if (studentFee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentFee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentFeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentFee result = studentFeeRepository.save(studentFee);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentFee.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-fees/:id} : Partial updates given fields of an existing studentFee, field will ignore if it is null
     *
     * @param id the id of the studentFee to save.
     * @param studentFee the studentFee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentFee,
     * or with status {@code 400 (Bad Request)} if the studentFee is not valid,
     * or with status {@code 404 (Not Found)} if the studentFee is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentFee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-fees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentFee> partialUpdateStudentFee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentFee studentFee
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentFee partially : {}, {}", id, studentFee);
        if (studentFee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentFee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentFeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentFee> result = studentFeeRepository
            .findById(studentFee.getId())
            .map(existingStudentFee -> {
                if (studentFee.getTotalAcademicFee() != null) {
                    existingStudentFee.setTotalAcademicFee(studentFee.getTotalAcademicFee());
                }
                if (studentFee.getAcademicFeewaveOff() != null) {
                    existingStudentFee.setAcademicFeewaveOff(studentFee.getAcademicFeewaveOff());
                }
                if (studentFee.getAcademicFeePaid() != null) {
                    existingStudentFee.setAcademicFeePaid(studentFee.getAcademicFeePaid());
                }
                if (studentFee.getTotalAcademicFeePaid() != null) {
                    existingStudentFee.setTotalAcademicFeePaid(studentFee.getTotalAcademicFeePaid());
                }
                if (studentFee.getAcademicFeepending() != null) {
                    existingStudentFee.setAcademicFeepending(studentFee.getAcademicFeepending());
                }
                if (studentFee.getBusAlloted() != null) {
                    existingStudentFee.setBusAlloted(studentFee.getBusAlloted());
                }
                if (studentFee.getHostelAlloted() != null) {
                    existingStudentFee.setHostelAlloted(studentFee.getHostelAlloted());
                }
                if (studentFee.getTotalBusFee() != null) {
                    existingStudentFee.setTotalBusFee(studentFee.getTotalBusFee());
                }
                if (studentFee.getBusFeewaveOff() != null) {
                    existingStudentFee.setBusFeewaveOff(studentFee.getBusFeewaveOff());
                }
                if (studentFee.getBusFeePaid() != null) {
                    existingStudentFee.setBusFeePaid(studentFee.getBusFeePaid());
                }
                if (studentFee.getTotalBusFeePaid() != null) {
                    existingStudentFee.setTotalBusFeePaid(studentFee.getTotalBusFeePaid());
                }
                if (studentFee.getBusFeepending() != null) {
                    existingStudentFee.setBusFeepending(studentFee.getBusFeepending());
                }
                if (studentFee.getTotalHostelFee() != null) {
                    existingStudentFee.setTotalHostelFee(studentFee.getTotalHostelFee());
                }
                if (studentFee.getHostelFeewaveOff() != null) {
                    existingStudentFee.setHostelFeewaveOff(studentFee.getHostelFeewaveOff());
                }
                if (studentFee.getTotalHostelFeePaid() != null) {
                    existingStudentFee.setTotalHostelFeePaid(studentFee.getTotalHostelFeePaid());
                }
                if (studentFee.getHostelFeePaid() != null) {
                    existingStudentFee.setHostelFeePaid(studentFee.getHostelFeePaid());
                }
                if (studentFee.getHostelFeepending() != null) {
                    existingStudentFee.setHostelFeepending(studentFee.getHostelFeepending());
                }
                if (studentFee.getHostelExpenses() != null) {
                    existingStudentFee.setHostelExpenses(studentFee.getHostelExpenses());
                }
                if (studentFee.getYear() != null) {
                    existingStudentFee.setYear(studentFee.getYear());
                }
                if (studentFee.getComments() != null) {
                    existingStudentFee.setComments(studentFee.getComments());
                }

                return existingStudentFee;
            })
            .map(studentFeeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentFee.getId().toString())
        );
    }

    /**
     * {@code GET  /student-fees} : get all the studentFees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentFees in body.
     */
    @GetMapping("/student-fees")
    public List<StudentFee> getAllStudentFees() {
        log.debug("REST request to get all StudentFees");
        return studentFeeRepository.findAll();
    }

    /**
     * {@code GET  /student-fees/:id} : get the "id" studentFee.
     *
     * @param id the id of the studentFee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentFee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-fees/{id}")
    public ResponseEntity<StudentFee> getStudentFee(@PathVariable Long id) {
        log.debug("REST request to get StudentFee : {}", id);
        Optional<StudentFee> studentFee = studentFeeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentFee);
    }

    /**
     * {@code DELETE  /student-fees/:id} : delete the "id" studentFee.
     *
     * @param id the id of the studentFee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-fees/{id}")
    public ResponseEntity<Void> deleteStudentFee(@PathVariable Long id) {
        log.debug("REST request to delete StudentFee : {}", id);
        studentFeeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
