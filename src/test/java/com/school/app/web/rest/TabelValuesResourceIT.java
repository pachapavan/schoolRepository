package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.TabelValues;
import com.school.app.repository.TabelValuesRepository;
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
 * Integration tests for the {@link TabelValuesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TabelValuesResourceIT {

    private static final String ENTITY_API_URL = "/api/tabel-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TabelValuesRepository tabelValuesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTabelValuesMockMvc;

    private TabelValues tabelValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelValues createEntity(EntityManager em) {
        TabelValues tabelValues = new TabelValues();
        return tabelValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelValues createUpdatedEntity(EntityManager em) {
        TabelValues tabelValues = new TabelValues();
        return tabelValues;
    }

    @BeforeEach
    public void initTest() {
        tabelValues = createEntity(em);
    }

    @Test
    @Transactional
    void createTabelValues() throws Exception {
        int databaseSizeBeforeCreate = tabelValuesRepository.findAll().size();
        // Create the TabelValues
        restTabelValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelValues)))
            .andExpect(status().isCreated());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeCreate + 1);
        TabelValues testTabelValues = tabelValuesList.get(tabelValuesList.size() - 1);
    }

    @Test
    @Transactional
    void createTabelValuesWithExistingId() throws Exception {
        // Create the TabelValues with an existing ID
        tabelValues.setId(1L);

        int databaseSizeBeforeCreate = tabelValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTabelValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelValues)))
            .andExpect(status().isBadRequest());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTabelValues() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        // Get all the tabelValuesList
        restTabelValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabelValues.getId().intValue())));
    }

    @Test
    @Transactional
    void getTabelValues() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        // Get the tabelValues
        restTabelValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, tabelValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tabelValues.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTabelValues() throws Exception {
        // Get the tabelValues
        restTabelValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTabelValues() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();

        // Update the tabelValues
        TabelValues updatedTabelValues = tabelValuesRepository.findById(tabelValues.getId()).get();
        // Disconnect from session so that the updates on updatedTabelValues are not directly saved in db
        em.detach(updatedTabelValues);

        restTabelValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTabelValues.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTabelValues))
            )
            .andExpect(status().isOk());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
        TabelValues testTabelValues = tabelValuesList.get(tabelValuesList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tabelValues.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelValues)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTabelValuesWithPatch() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();

        // Update the tabelValues using partial update
        TabelValues partialUpdatedTabelValues = new TabelValues();
        partialUpdatedTabelValues.setId(tabelValues.getId());

        restTabelValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelValues))
            )
            .andExpect(status().isOk());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
        TabelValues testTabelValues = tabelValuesList.get(tabelValuesList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTabelValuesWithPatch() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();

        // Update the tabelValues using partial update
        TabelValues partialUpdatedTabelValues = new TabelValues();
        partialUpdatedTabelValues.setId(tabelValues.getId());

        restTabelValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelValues))
            )
            .andExpect(status().isOk());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
        TabelValues testTabelValues = tabelValuesList.get(tabelValuesList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tabelValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTabelValues() throws Exception {
        int databaseSizeBeforeUpdate = tabelValuesRepository.findAll().size();
        tabelValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelValuesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tabelValues))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelValues in the database
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTabelValues() throws Exception {
        // Initialize the database
        tabelValuesRepository.saveAndFlush(tabelValues);

        int databaseSizeBeforeDelete = tabelValuesRepository.findAll().size();

        // Delete the tabelValues
        restTabelValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, tabelValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TabelValues> tabelValuesList = tabelValuesRepository.findAll();
        assertThat(tabelValuesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
