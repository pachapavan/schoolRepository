package com.school.app.web.rest;

import com.school.app.domain.Attendence;
import com.school.app.repository.AttendenceRepository;
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
        return ResponseEntity
            .created(new URI("/api/attendences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendences/:id} : Updates an existing attendence.
     *
     * @param id the id of the attendence to save.
     * @param attendence the attendence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendence,
     * or with status {@code 400 (Bad Request)} if the attendence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attendences/{id}")
    public ResponseEntity<Attendence> updateAttendence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attendence attendence
    ) throws URISyntaxException {
        log.debug("REST request to update Attendence : {}, {}", id, attendence);
        if (attendence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Attendence result = attendenceRepository.save(attendence);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attendences/:id} : Partial updates given fields of an existing attendence, field will ignore if it is null
     *
     * @param id the id of the attendence to save.
     * @param attendence the attendence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendence,
     * or with status {@code 400 (Bad Request)} if the attendence is not valid,
     * or with status {@code 404 (Not Found)} if the attendence is not found,
     * or with status {@code 500 (Internal Server Error)} if the attendence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attendences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attendence> partialUpdateAttendence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attendence attendence
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attendence partially : {}, {}", id, attendence);
        if (attendence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attendence> result = attendenceRepository
            .findById(attendence.getId())
            .map(existingAttendence -> {
                if (attendence.getMonth() != null) {
                    existingAttendence.setMonth(attendence.getMonth());
                }
                if (attendence.getTotalWorkingDays() != null) {
                    existingAttendence.setTotalWorkingDays(attendence.getTotalWorkingDays());
                }
                if (attendence.getDayspresent() != null) {
                    existingAttendence.setDayspresent(attendence.getDayspresent());
                }

                return existingAttendence;
            })
            .map(attendenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendence.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
