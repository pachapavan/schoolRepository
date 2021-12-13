package com.school.app.web.rest;

import com.school.app.domain.ClassName;
import com.school.app.repository.ClassNameRepository;
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
        return ResponseEntity
            .created(new URI("/api/class-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-names/:id} : Updates an existing className.
     *
     * @param id the id of the className to save.
     * @param className the className to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated className,
     * or with status {@code 400 (Bad Request)} if the className is not valid,
     * or with status {@code 500 (Internal Server Error)} if the className couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-names/{id}")
    public ResponseEntity<ClassName> updateClassName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClassName className
    ) throws URISyntaxException {
        log.debug("REST request to update ClassName : {}, {}", id, className);
        if (className.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, className.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClassName result = classNameRepository.save(className);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, className.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /class-names/:id} : Partial updates given fields of an existing className, field will ignore if it is null
     *
     * @param id the id of the className to save.
     * @param className the className to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated className,
     * or with status {@code 400 (Bad Request)} if the className is not valid,
     * or with status {@code 404 (Not Found)} if the className is not found,
     * or with status {@code 500 (Internal Server Error)} if the className couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/class-names/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassName> partialUpdateClassName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClassName className
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassName partially : {}, {}", id, className);
        if (className.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, className.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassName> result = classNameRepository
            .findById(className.getId())
            .map(existingClassName -> {
                if (className.getName() != null) {
                    existingClassName.setName(className.getName());
                }
                if (className.getClassNumber() != null) {
                    existingClassName.setClassNumber(className.getClassNumber());
                }

                return existingClassName;
            })
            .map(classNameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, className.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
