package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Body;
import com.school.app.repository.BodyRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BodyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodyResourceIT {

    private static final String ENTITY_API_URL = "/api/bodies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodyMockMvc;

    private Body body;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Body createEntity(EntityManager em) {
        Body body = new Body();
        return body;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Body createUpdatedEntity(EntityManager em) {
        Body body = new Body();
        return body;
    }

    @BeforeEach
    public void initTest() {
        body = createEntity(em);
    }

    @Test
    @Transactional
    void createBody() throws Exception {
        int databaseSizeBeforeCreate = bodyRepository.findAll().size();
        // Create the Body
        restBodyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isCreated());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeCreate + 1);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    @Transactional
    void createBodyWithExistingId() throws Exception {
        // Create the Body with an existing ID
        body.setId(1L);

        int databaseSizeBeforeCreate = bodyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBodies() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        // Get all the bodyList
        restBodyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(body.getId().intValue())));
    }

    @Test
    @Transactional
    void getBody() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        // Get the body
        restBodyMockMvc
            .perform(get(ENTITY_API_URL_ID, body.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(body.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBody() throws Exception {
        // Get the body
        restBodyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBody() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body
        Body updatedBody = bodyRepository.findById(body.getId()).get();
        // Disconnect from session so that the updates on updatedBody are not directly saved in db
        em.detach(updatedBody);

        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBody.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, body.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodyWithPatch() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body using partial update
        Body partialUpdatedBody = new Body();
        partialUpdatedBody.setId(body.getId());

        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBody.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateBodyWithPatch() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body using partial update
        Body partialUpdatedBody = new Body();
        partialUpdatedBody.setId(body.getId());

        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBody.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, body.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBody() throws Exception {
        // Initialize the database
        bodyRepository.saveAndFlush(body);

        int databaseSizeBeforeDelete = bodyRepository.findAll().size();

        // Delete the body
        restBodyMockMvc
            .perform(delete(ENTITY_API_URL_ID, body.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
