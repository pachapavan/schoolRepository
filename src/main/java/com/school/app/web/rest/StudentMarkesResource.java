package com.school.app.web.rest;

import com.school.app.domain.StudentMarkes;
import com.school.app.repository.StudentMarkesRepository;
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
 * REST controller for managing {@link com.school.app.domain.StudentMarkes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StudentMarkesResource {

    private final Logger log = LoggerFactory.getLogger(StudentMarkesResource.class);

    private static final String ENTITY_NAME = "studentMarkes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentMarkesRepository studentMarkesRepository;

    public StudentMarkesResource(StudentMarkesRepository studentMarkesRepository) {
        this.studentMarkesRepository = studentMarkesRepository;
    }

    /**
     * {@code POST  /student-markes} : Create a new studentMarkes.
     *
     * @param studentMarkes the studentMarkes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentMarkes, or with status {@code 400 (Bad Request)} if the studentMarkes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-markes")
    public ResponseEntity<StudentMarkes> createStudentMarkes(@RequestBody StudentMarkes studentMarkes) throws URISyntaxException {
        log.debug("REST request to save StudentMarkes : {}", studentMarkes);
        if (studentMarkes.getId() != null) {
            throw new BadRequestAlertException("A new studentMarkes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentMarkes result = studentMarkesRepository.save(studentMarkes);
        return ResponseEntity.created(new URI("/api/student-markes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-markes} : Updates an existing studentMarkes.
     *
     * @param studentMarkes the studentMarkes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentMarkes,
     * or with status {@code 400 (Bad Request)} if the studentMarkes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentMarkes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-markes")
    public ResponseEntity<StudentMarkes> updateStudentMarkes(@RequestBody StudentMarkes studentMarkes) throws URISyntaxException {
        log.debug("REST request to update StudentMarkes : {}", studentMarkes);
        if (studentMarkes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentMarkes result = studentMarkesRepository.save(studentMarkes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentMarkes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /student-markes} : get all the studentMarkes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentMarkes in body.
     */
    @GetMapping("/student-markes")
    public List<StudentMarkes> getAllStudentMarkes() {
        log.debug("REST request to get all StudentMarkes");
        return studentMarkesRepository.findAll();
    }

    /**
     * {@code GET  /student-markes/:id} : get the "id" studentMarkes.
     *
     * @param id the id of the studentMarkes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentMarkes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-markes/{id}")
    public ResponseEntity<StudentMarkes> getStudentMarkes(@PathVariable Long id) {
        log.debug("REST request to get StudentMarkes : {}", id);
        Optional<StudentMarkes> studentMarkes = studentMarkesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentMarkes);
    }

    /**
     * {@code DELETE  /student-markes/:id} : delete the "id" studentMarkes.
     *
     * @param id the id of the studentMarkes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-markes/{id}")
    public ResponseEntity<Void> deleteStudentMarkes(@PathVariable Long id) {
        log.debug("REST request to delete StudentMarkes : {}", id);
        studentMarkesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
