package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.ObjectContainingString;
import com.school.app.repository.ObjectContainingStringRepository;
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
 * Integration tests for the {@link ObjectContainingStringResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjectContainingStringResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/object-containing-strings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectContainingStringRepository objectContainingStringRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjectContainingStringMockMvc;

    private ObjectContainingString objectContainingString;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObjectContainingString createEntity(EntityManager em) {
        ObjectContainingString objectContainingString = new ObjectContainingString().name(DEFAULT_NAME);
        return objectContainingString;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObjectContainingString createUpdatedEntity(EntityManager em) {
        ObjectContainingString objectContainingString = new ObjectContainingString().name(UPDATED_NAME);
        return objectContainingString;
    }

    @BeforeEach
    public void initTest() {
        objectContainingString = createEntity(em);
    }

    @Test
    @Transactional
    void createObjectContainingString() throws Exception {
        int databaseSizeBeforeCreate = objectContainingStringRepository.findAll().size();
        // Create the ObjectContainingString
        restObjectContainingStringMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isCreated());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeCreate + 1);
        ObjectContainingString testObjectContainingString = objectContainingStringList.get(objectContainingStringList.size() - 1);
        assertThat(testObjectContainingString.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createObjectContainingStringWithExistingId() throws Exception {
        // Create the ObjectContainingString with an existing ID
        objectContainingString.setId(1L);

        int databaseSizeBeforeCreate = objectContainingStringRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjectContainingStringMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObjectContainingStrings() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        // Get all the objectContainingStringList
        restObjectContainingStringMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objectContainingString.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getObjectContainingString() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        // Get the objectContainingString
        restObjectContainingStringMockMvc
            .perform(get(ENTITY_API_URL_ID, objectContainingString.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objectContainingString.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingObjectContainingString() throws Exception {
        // Get the objectContainingString
        restObjectContainingStringMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewObjectContainingString() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();

        // Update the objectContainingString
        ObjectContainingString updatedObjectContainingString = objectContainingStringRepository
            .findById(objectContainingString.getId())
            .get();
        // Disconnect from session so that the updates on updatedObjectContainingString are not directly saved in db
        em.detach(updatedObjectContainingString);
        updatedObjectContainingString.name(UPDATED_NAME);

        restObjectContainingStringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedObjectContainingString.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedObjectContainingString))
            )
            .andExpect(status().isOk());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingString testObjectContainingString = objectContainingStringList.get(objectContainingStringList.size() - 1);
        assertThat(testObjectContainingString.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objectContainingString.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjectContainingStringWithPatch() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();

        // Update the objectContainingString using partial update
        ObjectContainingString partialUpdatedObjectContainingString = new ObjectContainingString();
        partialUpdatedObjectContainingString.setId(objectContainingString.getId());

        restObjectContainingStringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjectContainingString.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectContainingString))
            )
            .andExpect(status().isOk());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingString testObjectContainingString = objectContainingStringList.get(objectContainingStringList.size() - 1);
        assertThat(testObjectContainingString.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateObjectContainingStringWithPatch() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();

        // Update the objectContainingString using partial update
        ObjectContainingString partialUpdatedObjectContainingString = new ObjectContainingString();
        partialUpdatedObjectContainingString.setId(objectContainingString.getId());

        partialUpdatedObjectContainingString.name(UPDATED_NAME);

        restObjectContainingStringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjectContainingString.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectContainingString))
            )
            .andExpect(status().isOk());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingString testObjectContainingString = objectContainingStringList.get(objectContainingStringList.size() - 1);
        assertThat(testObjectContainingString.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objectContainingString.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjectContainingString() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingStringRepository.findAll().size();
        objectContainingString.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectContainingStringMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objectContainingString))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ObjectContainingString in the database
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjectContainingString() throws Exception {
        // Initialize the database
        objectContainingStringRepository.saveAndFlush(objectContainingString);

        int databaseSizeBeforeDelete = objectContainingStringRepository.findAll().size();

        // Delete the objectContainingString
        restObjectContainingStringMockMvc
            .perform(delete(ENTITY_API_URL_ID, objectContainingString.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ObjectContainingString> objectContainingStringList = objectContainingStringRepository.findAll();
        assertThat(objectContainingStringList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
