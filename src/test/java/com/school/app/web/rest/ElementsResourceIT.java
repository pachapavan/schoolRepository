package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Elements;
import com.school.app.domain.enumeration.ElementType;
import com.school.app.repository.ElementsRepository;
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
 * Integration tests for the {@link ElementsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElementsResourceIT {

    private static final ElementType DEFAULT_TYPE = ElementType.Text;
    private static final ElementType UPDATED_TYPE = ElementType.Button;

    private static final String ENTITY_API_URL = "/api/elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ElementsRepository elementsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restElementsMockMvc;

    private Elements elements;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elements createEntity(EntityManager em) {
        Elements elements = new Elements().type(DEFAULT_TYPE);
        return elements;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elements createUpdatedEntity(EntityManager em) {
        Elements elements = new Elements().type(UPDATED_TYPE);
        return elements;
    }

    @BeforeEach
    public void initTest() {
        elements = createEntity(em);
    }

    @Test
    @Transactional
    void createElements() throws Exception {
        int databaseSizeBeforeCreate = elementsRepository.findAll().size();
        // Create the Elements
        restElementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isCreated());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeCreate + 1);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createElementsWithExistingId() throws Exception {
        // Create the Elements with an existing ID
        elements.setId(1L);

        int databaseSizeBeforeCreate = elementsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllElements() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        // Get all the elementsList
        restElementsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elements.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getElements() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        // Get the elements
        restElementsMockMvc
            .perform(get(ENTITY_API_URL_ID, elements.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(elements.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingElements() throws Exception {
        // Get the elements
        restElementsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewElements() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements
        Elements updatedElements = elementsRepository.findById(elements.getId()).get();
        // Disconnect from session so that the updates on updatedElements are not directly saved in db
        em.detach(updatedElements);
        updatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedElements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateElementsWithPatch() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements using partial update
        Elements partialUpdatedElements = new Elements();
        partialUpdatedElements.setId(elements.getId());

        partialUpdatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateElementsWithPatch() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements using partial update
        Elements partialUpdatedElements = new Elements();
        partialUpdatedElements.setId(elements.getId());

        partialUpdatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, elements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteElements() throws Exception {
        // Initialize the database
        elementsRepository.saveAndFlush(elements);

        int databaseSizeBeforeDelete = elementsRepository.findAll().size();

        // Delete the elements
        restElementsMockMvc
            .perform(delete(ENTITY_API_URL_ID, elements.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
