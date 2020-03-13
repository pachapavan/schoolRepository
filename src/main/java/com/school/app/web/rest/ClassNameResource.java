package com.school.app.web.rest;

import com.school.app.domain.ClassName;
import com.school.app.repository.ClassNameRepository;
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
 * REST controller for managing {@link com.school.app.domain.ClassName}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClassNameResource {

    private final Logger log = LoggerFactory.getLogger(ClassNameResource.class);

    private static final String ENTITY_NAME = "className";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassNameRepository classNameRepository;

    public ClassNameResource(ClassNameRepository classNameRepository) {
        this.classNameRepository = classNameRepository;
    }

    /**
     * {@code POST  /class-names} : Create a new className.
     *
     * @param className the className to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new className, or with status {@code 400 (Bad Request)} if the className has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-names")
    public ResponseEntity<ClassName> createClassName(@RequestBody ClassName className) throws URISyntaxException {
        log.debug("REST request to save ClassName : {}", className);
        if (className.getId() != null) {
            throw new BadRequestAlertException("A new className cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassName result = classNameRepository.save(className);
        return ResponseEntity.created(new URI("/api/class-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-names} : Updates an existing className.
     *
     * @param className the className to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated className,
     * or with status {@code 400 (Bad Request)} if the className is not valid,
     * or with status {@code 500 (Internal Server Error)} if the className couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-names")
    public ResponseEntity<ClassName> updateClassName(@RequestBody ClassName className) throws URISyntaxException {
        log.debug("REST request to update ClassName : {}", className);
        if (className.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassName result = classNameRepository.save(className);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, className.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-names} : get all the classNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classNames in body.
     */
    @GetMapping("/class-names")
    public List<ClassName> getAllClassNames() {
        log.debug("REST request to get all ClassNames");
        return classNameRepository.findAll();
    }

    /**
     * {@code GET  /class-names/:id} : get the "id" className.
     *
     * @param id the id of the className to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the className, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-names/{id}")
    public ResponseEntity<ClassName> getClassName(@PathVariable Long id) {
        log.debug("REST request to get ClassName : {}", id);
        Optional<ClassName> className = classNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(className);
    }

    /**
     * {@code DELETE  /class-names/:id} : delete the "id" className.
     *
     * @param id the id of the className to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-names/{id}")
    public ResponseEntity<Void> deleteClassName(@PathVariable Long id) {
        log.debug("REST request to delete ClassName : {}", id);
        classNameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
