package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.StaffSalary;
import com.school.app.repository.StaffSalaryRepository;
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
 * Integration tests for the {@link StaffSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StaffSalaryResourceIT {

    private static final Long DEFAULT_SALARY_PAID = 1L;
    private static final Long UPDATED_SALARY_PAID = 2L;

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/staff-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffSalaryRepository staffSalaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffSalaryMockMvc;

    private StaffSalary staffSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffSalary createEntity(EntityManager em) {
        StaffSalary staffSalary = new StaffSalary().salaryPaid(DEFAULT_SALARY_PAID).month(DEFAULT_MONTH);
        return staffSalary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffSalary createUpdatedEntity(EntityManager em) {
        StaffSalary staffSalary = new StaffSalary().salaryPaid(UPDATED_SALARY_PAID).month(UPDATED_MONTH);
        return staffSalary;
    }

    @BeforeEach
    public void initTest() {
        staffSalary = createEntity(em);
    }

    @Test
    @Transactional
    void createStaffSalary() throws Exception {
        int databaseSizeBeforeCreate = staffSalaryRepository.findAll().size();
        // Create the StaffSalary
        restStaffSalaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffSalary)))
            .andExpect(status().isCreated());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeCreate + 1);
        StaffSalary testStaffSalary = staffSalaryList.get(staffSalaryList.size() - 1);
        assertThat(testStaffSalary.getSalaryPaid()).isEqualTo(DEFAULT_SALARY_PAID);
        assertThat(testStaffSalary.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    void createStaffSalaryWithExistingId() throws Exception {
        // Create the StaffSalary with an existing ID
        staffSalary.setId(1L);

        int databaseSizeBeforeCreate = staffSalaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffSalaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffSalary)))
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStaffSalaries() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        // Get all the staffSalaryList
        restStaffSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaryPaid").value(hasItem(DEFAULT_SALARY_PAID.intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    void getStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        // Get the staffSalary
        restStaffSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, staffSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffSalary.getId().intValue()))
            .andExpect(jsonPath("$.salaryPaid").value(DEFAULT_SALARY_PAID.intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    void getNonExistingStaffSalary() throws Exception {
        // Get the staffSalary
        restStaffSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();

        // Update the staffSalary
        StaffSalary updatedStaffSalary = staffSalaryRepository.findById(staffSalary.getId()).get();
        // Disconnect from session so that the updates on updatedStaffSalary are not directly saved in db
        em.detach(updatedStaffSalary);
        updatedStaffSalary.salaryPaid(UPDATED_SALARY_PAID).month(UPDATED_MONTH);

        restStaffSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStaffSalary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStaffSalary))
            )
            .andExpect(status().isOk());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
        StaffSalary testStaffSalary = staffSalaryList.get(staffSalaryList.size() - 1);
        assertThat(testStaffSalary.getSalaryPaid()).isEqualTo(UPDATED_SALARY_PAID);
        assertThat(testStaffSalary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void putNonExistingStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffSalary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffSalary)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaffSalaryWithPatch() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();

        // Update the staffSalary using partial update
        StaffSalary partialUpdatedStaffSalary = new StaffSalary();
        partialUpdatedStaffSalary.setId(staffSalary.getId());

        partialUpdatedStaffSalary.month(UPDATED_MONTH);

        restStaffSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffSalary))
            )
            .andExpect(status().isOk());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
        StaffSalary testStaffSalary = staffSalaryList.get(staffSalaryList.size() - 1);
        assertThat(testStaffSalary.getSalaryPaid()).isEqualTo(DEFAULT_SALARY_PAID);
        assertThat(testStaffSalary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void fullUpdateStaffSalaryWithPatch() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();

        // Update the staffSalary using partial update
        StaffSalary partialUpdatedStaffSalary = new StaffSalary();
        partialUpdatedStaffSalary.setId(staffSalary.getId());

        partialUpdatedStaffSalary.salaryPaid(UPDATED_SALARY_PAID).month(UPDATED_MONTH);

        restStaffSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffSalary))
            )
            .andExpect(status().isOk());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
        StaffSalary testStaffSalary = staffSalaryList.get(staffSalaryList.size() - 1);
        assertThat(testStaffSalary.getSalaryPaid()).isEqualTo(UPDATED_SALARY_PAID);
        assertThat(testStaffSalary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void patchNonExistingStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();
        staffSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(staffSalary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeDelete = staffSalaryRepository.findAll().size();

        // Delete the staffSalary
        restStaffSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, staffSalary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
