package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Head;
import com.school.app.repository.HeadRepository;
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
 * Integration tests for the {@link HeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HeadResourceIT {

    private static final String ENTITY_API_URL = "/api/heads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HeadRepository headRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHeadMockMvc;

    private Head head;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Head createEntity(EntityManager em) {
        Head head = new Head();
        return head;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Head createUpdatedEntity(EntityManager em) {
        Head head = new Head();
        return head;
    }

    @BeforeEach
    public void initTest() {
        head = createEntity(em);
    }

    @Test
    @Transactional
    void createHead() throws Exception {
        int databaseSizeBeforeCreate = headRepository.findAll().size();
        // Create the Head
        restHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isCreated());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeCreate + 1);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    @Transactional
    void createHeadWithExistingId() throws Exception {
        // Create the Head with an existing ID
        head.setId(1L);

        int databaseSizeBeforeCreate = headRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHeads() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        // Get all the headList
        restHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(head.getId().intValue())));
    }

    @Test
    @Transactional
    void getHead() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        // Get the head
        restHeadMockMvc
            .perform(get(ENTITY_API_URL_ID, head.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(head.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingHead() throws Exception {
        // Get the head
        restHeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHead() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head
        Head updatedHead = headRepository.findById(head.getId()).get();
        // Disconnect from session so that the updates on updatedHead are not directly saved in db
        em.detach(updatedHead);

        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHead.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, head.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHeadWithPatch() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head using partial update
        Head partialUpdatedHead = new Head();
        partialUpdatedHead.setId(head.getId());

        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateHeadWithPatch() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head using partial update
        Head partialUpdatedHead = new Head();
        partialUpdatedHead.setId(head.getId());

        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, head.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHead() throws Exception {
        // Initialize the database
        headRepository.saveAndFlush(head);

        int databaseSizeBeforeDelete = headRepository.findAll().size();

        // Delete the head
        restHeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, head.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
