package com.school.app.web.rest;

import com.school.app.domain.Attendence;
import com.school.app.repository.AttendenceRepository;
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
 * REST controller for managing {@link com.school.app.domain.Attendence}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttendenceResource {

    private final Logger log = LoggerFactory.getLogger(AttendenceResource.class);

    private static final String ENTITY_NAME = "attendence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendenceRepository attendenceRepository;

    public AttendenceResource(AttendenceRepository attendenceRepository) {
        this.attendenceRepository = attendenceRepository;
    }

    /**
     * {@code POST  /attendences} : Create a new attendence.
     *
     * @param attendence the attendence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendence, or with status {@code 400 (Bad Request)} if the attendence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attendences")
    public ResponseEntity<Attendence> createAttendence(@RequestBody Attendence attendence) throws URISyntaxException {
        log.debug("REST request to save Attendence : {}", attendence);
        if (attendence.getId() != null) {
            throw new BadRequestAlertException("A new attendence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attendence result = attendenceRepository.save(attendence);
        return ResponseEntity.created(new URI("/api/attendences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendences} : Updates an existing attendence.
     *
     * @param attendence the attendence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendence,
     * or with status {@code 400 (Bad Request)} if the attendence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attendences")
    public ResponseEntity<Attendence> updateAttendence(@RequestBody Attendence attendence) throws URISyntaxException {
        log.debug("REST request to update Attendence : {}", attendence);
        if (attendence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Attendence result = attendenceRepository.save(attendence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendence.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attendences} : get all the attendences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendences in body.
     */
    @GetMapping("/attendences")
    public List<Attendence> getAllAttendences() {
        log.debug("REST request to get all Attendences");
        return attendenceRepository.findAll();
    }

    /**
     * {@code GET  /attendences/:id} : get the "id" attendence.
     *
     * @param id the id of the attendence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attendences/{id}")
    public ResponseEntity<Attendence> getAttendence(@PathVariable Long id) {
        log.debug("REST request to get Attendence : {}", id);
        Optional<Attendence> attendence = attendenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attendence);
    }

    /**
     * {@code DELETE  /attendences/:id} : delete the "id" attendence.
     *
     * @param id the id of the attendence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attendences/{id}")
    public ResponseEntity<Void> deleteAttendence(@PathVariable Long id) {
        log.debug("REST request to delete Attendence : {}", id);
        attendenceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
