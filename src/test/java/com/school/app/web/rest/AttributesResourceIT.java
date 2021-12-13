package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Attributes;
import com.school.app.repository.AttributesRepository;
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
 * Integration tests for the {@link AttributesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributesResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributesRepository attributesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributesMockMvc;

    private Attributes attributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attributes createEntity(EntityManager em) {
        Attributes attributes = new Attributes().className(DEFAULT_CLASS_NAME);
        return attributes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attributes createUpdatedEntity(EntityManager em) {
        Attributes attributes = new Attributes().className(UPDATED_CLASS_NAME);
        return attributes;
    }

    @BeforeEach
    public void initTest() {
        attributes = createEntity(em);
    }

    @Test
    @Transactional
    void createAttributes() throws Exception {
        int databaseSizeBeforeCreate = attributesRepository.findAll().size();
        // Create the Attributes
        restAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributes)))
            .andExpect(status().isCreated());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeCreate + 1);
        Attributes testAttributes = attributesList.get(attributesList.size() - 1);
        assertThat(testAttributes.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
    }

    @Test
    @Transactional
    void createAttributesWithExistingId() throws Exception {
        // Create the Attributes with an existing ID
        attributes.setId(1L);

        int databaseSizeBeforeCreate = attributesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributes)))
            .andExpect(status().isBadRequest());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttributes() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        // Get all the attributesList
        restAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)));
    }

    @Test
    @Transactional
    void getAttributes() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        // Get the attributes
        restAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, attributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributes.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAttributes() throws Exception {
        // Get the attributes
        restAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttributes() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();

        // Update the attributes
        Attributes updatedAttributes = attributesRepository.findById(attributes.getId()).get();
        // Disconnect from session so that the updates on updatedAttributes are not directly saved in db
        em.detach(updatedAttributes);
        updatedAttributes.className(UPDATED_CLASS_NAME);

        restAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributes))
            )
            .andExpect(status().isOk());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
        Attributes testAttributes = attributesList.get(attributesList.size() - 1);
        assertThat(testAttributes.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributesWithPatch() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();

        // Update the attributes using partial update
        Attributes partialUpdatedAttributes = new Attributes();
        partialUpdatedAttributes.setId(attributes.getId());

        restAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributes))
            )
            .andExpect(status().isOk());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
        Attributes testAttributes = attributesList.get(attributesList.size() - 1);
        assertThat(testAttributes.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAttributesWithPatch() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();

        // Update the attributes using partial update
        Attributes partialUpdatedAttributes = new Attributes();
        partialUpdatedAttributes.setId(attributes.getId());

        partialUpdatedAttributes.className(UPDATED_CLASS_NAME);

        restAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributes))
            )
            .andExpect(status().isOk());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
        Attributes testAttributes = attributesList.get(attributesList.size() - 1);
        assertThat(testAttributes.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributes() throws Exception {
        int databaseSizeBeforeUpdate = attributesRepository.findAll().size();
        attributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attributes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attributes in the database
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributes() throws Exception {
        // Initialize the database
        attributesRepository.saveAndFlush(attributes);

        int databaseSizeBeforeDelete = attributesRepository.findAll().size();

        // Delete the attributes
        restAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attributes> attributesList = attributesRepository.findAll();
        assertThat(attributesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
