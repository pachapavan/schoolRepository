package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.BadgeType;
import com.school.app.domain.enumeration.ColorEnum;
import com.school.app.repository.BadgeTypeRepository;
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
 * Integration tests for the {@link BadgeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BadgeTypeResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ColorEnum DEFAULT_TYPE = ColorEnum.Plain;
    private static final ColorEnum UPDATED_TYPE = ColorEnum.Grey;

    private static final String ENTITY_API_URL = "/api/badge-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBadgeTypeMockMvc;

    private BadgeType badgeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BadgeType createEntity(EntityManager em) {
        BadgeType badgeType = new BadgeType().status(DEFAULT_STATUS).type(DEFAULT_TYPE);
        return badgeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BadgeType createUpdatedEntity(EntityManager em) {
        BadgeType badgeType = new BadgeType().status(UPDATED_STATUS).type(UPDATED_TYPE);
        return badgeType;
    }

    @BeforeEach
    public void initTest() {
        badgeType = createEntity(em);
    }

    @Test
    @Transactional
    void createBadgeType() throws Exception {
        int databaseSizeBeforeCreate = badgeTypeRepository.findAll().size();
        // Create the BadgeType
        restBadgeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isCreated());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createBadgeTypeWithExistingId() throws Exception {
        // Create the BadgeType with an existing ID
        badgeType.setId(1L);

        int databaseSizeBeforeCreate = badgeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBadgeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBadgeTypes() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        // Get all the badgeTypeList
        restBadgeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badgeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        // Get the badgeType
        restBadgeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, badgeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(badgeType.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBadgeType() throws Exception {
        // Get the badgeType
        restBadgeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType
        BadgeType updatedBadgeType = badgeTypeRepository.findById(badgeType.getId()).get();
        // Disconnect from session so that the updates on updatedBadgeType are not directly saved in db
        em.detach(updatedBadgeType);
        updatedBadgeType.status(UPDATED_STATUS).type(UPDATED_TYPE);

        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBadgeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, badgeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBadgeTypeWithPatch() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType using partial update
        BadgeType partialUpdatedBadgeType = new BadgeType();
        partialUpdatedBadgeType.setId(badgeType.getId());

        partialUpdatedBadgeType.status(UPDATED_STATUS);

        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBadgeTypeWithPatch() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType using partial update
        BadgeType partialUpdatedBadgeType = new BadgeType();
        partialUpdatedBadgeType.setId(badgeType.getId());

        partialUpdatedBadgeType.status(UPDATED_STATUS).type(UPDATED_TYPE);

        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, badgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.saveAndFlush(badgeType);

        int databaseSizeBeforeDelete = badgeTypeRepository.findAll().size();

        // Delete the badgeType
        restBadgeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, badgeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
