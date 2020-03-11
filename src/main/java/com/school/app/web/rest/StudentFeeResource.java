package com.school.app.web.rest;

import com.school.app.domain.StudentFee;
import com.school.app.repository.StudentFeeRepository;
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
        return ResponseEntity.created(new URI("/api/student-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-fees} : Updates an existing studentFee.
     *
     * @param studentFee the studentFee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentFee,
     * or with status {@code 400 (Bad Request)} if the studentFee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentFee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-fees")
    public ResponseEntity<StudentFee> updateStudentFee(@RequestBody StudentFee studentFee) throws URISyntaxException {
        log.debug("REST request to update StudentFee : {}", studentFee);
        if (studentFee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentFee result = studentFeeRepository.save(studentFee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentFee.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
