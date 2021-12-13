package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.FlexBox;
import com.school.app.repository.FlexBoxRepository;
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
 * Integration tests for the {@link FlexBoxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlexBoxResourceIT {

    private static final String DEFAULT_JUSTIFY_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFY_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flex-boxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexBoxRepository flexBoxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexBoxMockMvc;

    private FlexBox flexBox;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexBox createEntity(EntityManager em) {
        FlexBox flexBox = new FlexBox().justifyContent(DEFAULT_JUSTIFY_CONTENT);
        return flexBox;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexBox createUpdatedEntity(EntityManager em) {
        FlexBox flexBox = new FlexBox().justifyContent(UPDATED_JUSTIFY_CONTENT);
        return flexBox;
    }

    @BeforeEach
    public void initTest() {
        flexBox = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexBox() throws Exception {
        int databaseSizeBeforeCreate = flexBoxRepository.findAll().size();
        // Create the FlexBox
        restFlexBoxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexBox)))
            .andExpect(status().isCreated());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeCreate + 1);
        FlexBox testFlexBox = flexBoxList.get(flexBoxList.size() - 1);
        assertThat(testFlexBox.getJustifyContent()).isEqualTo(DEFAULT_JUSTIFY_CONTENT);
    }

    @Test
    @Transactional
    void createFlexBoxWithExistingId() throws Exception {
        // Create the FlexBox with an existing ID
        flexBox.setId(1L);

        int databaseSizeBeforeCreate = flexBoxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexBoxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexBox)))
            .andExpect(status().isBadRequest());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlexBoxes() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        // Get all the flexBoxList
        restFlexBoxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].justifyContent").value(hasItem(DEFAULT_JUSTIFY_CONTENT)));
    }

    @Test
    @Transactional
    void getFlexBox() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        // Get the flexBox
        restFlexBoxMockMvc
            .perform(get(ENTITY_API_URL_ID, flexBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexBox.getId().intValue()))
            .andExpect(jsonPath("$.justifyContent").value(DEFAULT_JUSTIFY_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingFlexBox() throws Exception {
        // Get the flexBox
        restFlexBoxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlexBox() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();

        // Update the flexBox
        FlexBox updatedFlexBox = flexBoxRepository.findById(flexBox.getId()).get();
        // Disconnect from session so that the updates on updatedFlexBox are not directly saved in db
        em.detach(updatedFlexBox);
        updatedFlexBox.justifyContent(UPDATED_JUSTIFY_CONTENT);

        restFlexBoxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlexBox.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlexBox))
            )
            .andExpect(status().isOk());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
        FlexBox testFlexBox = flexBoxList.get(flexBoxList.size() - 1);
        assertThat(testFlexBox.getJustifyContent()).isEqualTo(UPDATED_JUSTIFY_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexBox.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexBox))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexBox))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flexBox)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexBoxWithPatch() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();

        // Update the flexBox using partial update
        FlexBox partialUpdatedFlexBox = new FlexBox();
        partialUpdatedFlexBox.setId(flexBox.getId());

        restFlexBoxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexBox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexBox))
            )
            .andExpect(status().isOk());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
        FlexBox testFlexBox = flexBoxList.get(flexBoxList.size() - 1);
        assertThat(testFlexBox.getJustifyContent()).isEqualTo(DEFAULT_JUSTIFY_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateFlexBoxWithPatch() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();

        // Update the flexBox using partial update
        FlexBox partialUpdatedFlexBox = new FlexBox();
        partialUpdatedFlexBox.setId(flexBox.getId());

        partialUpdatedFlexBox.justifyContent(UPDATED_JUSTIFY_CONTENT);

        restFlexBoxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexBox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexBox))
            )
            .andExpect(status().isOk());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
        FlexBox testFlexBox = flexBoxList.get(flexBoxList.size() - 1);
        assertThat(testFlexBox.getJustifyContent()).isEqualTo(UPDATED_JUSTIFY_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexBox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexBox))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexBox))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexBox() throws Exception {
        int databaseSizeBeforeUpdate = flexBoxRepository.findAll().size();
        flexBox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexBoxMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flexBox)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexBox in the database
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexBox() throws Exception {
        // Initialize the database
        flexBoxRepository.saveAndFlush(flexBox);

        int databaseSizeBeforeDelete = flexBoxRepository.findAll().size();

        // Delete the flexBox
        restFlexBoxMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexBox.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexBox> flexBoxList = flexBoxRepository.findAll();
        assertThat(flexBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
