package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.FormWrap;
import com.school.app.repository.FormWrapRepository;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link FormWrapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormWrapResourceIT {

    private static final String ENTITY_API_URL = "/api/form-wraps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FormWrapRepository formWrapRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormWrapMockMvc;

    private FormWrap formWrap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormWrap createEntity(EntityManager em) {
        FormWrap formWrap = new FormWrap();
        return formWrap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormWrap createUpdatedEntity(EntityManager em) {
        FormWrap formWrap = new FormWrap();
        return formWrap;
    }

    @BeforeEach
    public void initTest() {
        formWrap = createEntity(em);
    }

    @Test
    @Transactional
    void createFormWrap() throws Exception {
        int databaseSizeBeforeCreate = formWrapRepository.findAll().size();
        // Create the FormWrap
        restFormWrapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formWrap)))
            .andExpect(status().isCreated());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeCreate + 1);
        FormWrap testFormWrap = formWrapList.get(formWrapList.size() - 1);
    }

    @Test
    @Transactional
    void createFormWrapWithExistingId() throws Exception {
        // Create the FormWrap with an existing ID
        formWrap.setId("existing_id");

        int databaseSizeBeforeCreate = formWrapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormWrapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formWrap)))
            .andExpect(status().isBadRequest());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormWraps() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        // Get all the formWrapList
        restFormWrapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formWrap.getId())));
    }

    @Test
    @Transactional
    void getFormWrap() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        // Get the formWrap
        restFormWrapMockMvc
            .perform(get(ENTITY_API_URL_ID, formWrap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formWrap.getId()));
    }

    @Test
    @Transactional
    void getNonExistingFormWrap() throws Exception {
        // Get the formWrap
        restFormWrapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormWrap() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();

        // Update the formWrap
        FormWrap updatedFormWrap = formWrapRepository.findById(formWrap.getId()).get();
        // Disconnect from session so that the updates on updatedFormWrap are not directly saved in db
        em.detach(updatedFormWrap);

        restFormWrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormWrap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormWrap))
            )
            .andExpect(status().isOk());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
        FormWrap testFormWrap = formWrapList.get(formWrapList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formWrap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formWrap))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formWrap))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formWrap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormWrapWithPatch() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();

        // Update the formWrap using partial update
        FormWrap partialUpdatedFormWrap = new FormWrap();
        partialUpdatedFormWrap.setId(formWrap.getId());

        restFormWrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormWrap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormWrap))
            )
            .andExpect(status().isOk());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
        FormWrap testFormWrap = formWrapList.get(formWrapList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFormWrapWithPatch() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();

        // Update the formWrap using partial update
        FormWrap partialUpdatedFormWrap = new FormWrap();
        partialUpdatedFormWrap.setId(formWrap.getId());

        restFormWrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormWrap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormWrap))
            )
            .andExpect(status().isOk());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
        FormWrap testFormWrap = formWrapList.get(formWrapList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formWrap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formWrap))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formWrap))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormWrap() throws Exception {
        int databaseSizeBeforeUpdate = formWrapRepository.findAll().size();
        formWrap.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormWrapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formWrap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormWrap in the database
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormWrap() throws Exception {
        // Initialize the database
        formWrap.setId(UUID.randomUUID().toString());
        formWrapRepository.saveAndFlush(formWrap);

        int databaseSizeBeforeDelete = formWrapRepository.findAll().size();

        // Delete the formWrap
        restFormWrapMockMvc
            .perform(delete(ENTITY_API_URL_ID, formWrap.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormWrap> formWrapList = formWrapRepository.findAll();
        assertThat(formWrapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
