package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.DisplayAtt;
import com.school.app.domain.enumeration.ElementType;
import com.school.app.repository.DisplayAttRepository;
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
 * Integration tests for the {@link DisplayAttResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisplayAttResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ElementType DEFAULT_TYPE = ElementType.Text;
    private static final ElementType UPDATED_TYPE = ElementType.Button;

    private static final String ENTITY_API_URL = "/api/display-atts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisplayAttRepository displayAttRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisplayAttMockMvc;

    private DisplayAtt displayAtt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisplayAtt createEntity(EntityManager em) {
        DisplayAtt displayAtt = new DisplayAtt().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return displayAtt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisplayAtt createUpdatedEntity(EntityManager em) {
        DisplayAtt displayAtt = new DisplayAtt().name(UPDATED_NAME).type(UPDATED_TYPE);
        return displayAtt;
    }

    @BeforeEach
    public void initTest() {
        displayAtt = createEntity(em);
    }

    @Test
    @Transactional
    void createDisplayAtt() throws Exception {
        int databaseSizeBeforeCreate = displayAttRepository.findAll().size();
        // Create the DisplayAtt
        restDisplayAttMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isCreated());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeCreate + 1);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createDisplayAttWithExistingId() throws Exception {
        // Create the DisplayAtt with an existing ID
        displayAtt.setId(1L);

        int databaseSizeBeforeCreate = displayAttRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisplayAttMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisplayAtts() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        // Get all the displayAttList
        restDisplayAttMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(displayAtt.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        // Get the displayAtt
        restDisplayAttMockMvc
            .perform(get(ENTITY_API_URL_ID, displayAtt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(displayAtt.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDisplayAtt() throws Exception {
        // Get the displayAtt
        restDisplayAttMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt
        DisplayAtt updatedDisplayAtt = displayAttRepository.findById(displayAtt.getId()).get();
        // Disconnect from session so that the updates on updatedDisplayAtt are not directly saved in db
        em.detach(updatedDisplayAtt);
        updatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisplayAtt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, displayAtt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisplayAttWithPatch() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt using partial update
        DisplayAtt partialUpdatedDisplayAtt = new DisplayAtt();
        partialUpdatedDisplayAtt.setId(displayAtt.getId());

        partialUpdatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisplayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDisplayAttWithPatch() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt using partial update
        DisplayAtt partialUpdatedDisplayAtt = new DisplayAtt();
        partialUpdatedDisplayAtt.setId(displayAtt.getId());

        partialUpdatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisplayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, displayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.saveAndFlush(displayAtt);

        int databaseSizeBeforeDelete = displayAttRepository.findAll().size();

        // Delete the displayAtt
        restDisplayAttMockMvc
            .perform(delete(ENTITY_API_URL_ID, displayAtt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
