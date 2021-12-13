package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Spacing;
import com.school.app.repository.SpacingRepository;
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
 * Integration tests for the {@link SpacingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpacingResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spacings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpacingRepository spacingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpacingMockMvc;

    private Spacing spacing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spacing createEntity(EntityManager em) {
        Spacing spacing = new Spacing().className(DEFAULT_CLASS_NAME);
        return spacing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spacing createUpdatedEntity(EntityManager em) {
        Spacing spacing = new Spacing().className(UPDATED_CLASS_NAME);
        return spacing;
    }

    @BeforeEach
    public void initTest() {
        spacing = createEntity(em);
    }

    @Test
    @Transactional
    void createSpacing() throws Exception {
        int databaseSizeBeforeCreate = spacingRepository.findAll().size();
        // Create the Spacing
        restSpacingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isCreated());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeCreate + 1);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
    }

    @Test
    @Transactional
    void createSpacingWithExistingId() throws Exception {
        // Create the Spacing with an existing ID
        spacing.setId(1L);

        int databaseSizeBeforeCreate = spacingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpacingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpacings() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        // Get all the spacingList
        restSpacingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spacing.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)));
    }

    @Test
    @Transactional
    void getSpacing() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        // Get the spacing
        restSpacingMockMvc
            .perform(get(ENTITY_API_URL_ID, spacing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spacing.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpacing() throws Exception {
        // Get the spacing
        restSpacingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpacing() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing
        Spacing updatedSpacing = spacingRepository.findById(spacing.getId()).get();
        // Disconnect from session so that the updates on updatedSpacing are not directly saved in db
        em.detach(updatedSpacing);
        updatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpacing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spacing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpacingWithPatch() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing using partial update
        Spacing partialUpdatedSpacing = new Spacing();
        partialUpdatedSpacing.setId(spacing.getId());

        partialUpdatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSpacingWithPatch() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing using partial update
        Spacing partialUpdatedSpacing = new Spacing();
        partialUpdatedSpacing.setId(spacing.getId());

        partialUpdatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpacing() throws Exception {
        // Initialize the database
        spacingRepository.saveAndFlush(spacing);

        int databaseSizeBeforeDelete = spacingRepository.findAll().size();

        // Delete the spacing
        restSpacingMockMvc
            .perform(delete(ENTITY_API_URL_ID, spacing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
