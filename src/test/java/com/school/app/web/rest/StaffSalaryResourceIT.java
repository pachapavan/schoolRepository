package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.StaffSalary;
import com.school.app.repository.StaffSalaryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StaffSalaryResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StaffSalaryResourceIT {

    private static final Long DEFAULT_SALARY_PAID = 1L;
    private static final Long UPDATED_SALARY_PAID = 2L;

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

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
        StaffSalary staffSalary = new StaffSalary()
            .salaryPaid(DEFAULT_SALARY_PAID)
            .month(DEFAULT_MONTH);
        return staffSalary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffSalary createUpdatedEntity(EntityManager em) {
        StaffSalary staffSalary = new StaffSalary()
            .salaryPaid(UPDATED_SALARY_PAID)
            .month(UPDATED_MONTH);
        return staffSalary;
    }

    @BeforeEach
    public void initTest() {
        staffSalary = createEntity(em);
    }

    @Test
    @Transactional
    public void createStaffSalary() throws Exception {
        int databaseSizeBeforeCreate = staffSalaryRepository.findAll().size();

        // Create the StaffSalary
        restStaffSalaryMockMvc.perform(post("/api/staff-salaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(staffSalary)))
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
    public void createStaffSalaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = staffSalaryRepository.findAll().size();

        // Create the StaffSalary with an existing ID
        staffSalary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffSalaryMockMvc.perform(post("/api/staff-salaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(staffSalary)))
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStaffSalaries() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        // Get all the staffSalaryList
        restStaffSalaryMockMvc.perform(get("/api/staff-salaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaryPaid").value(hasItem(DEFAULT_SALARY_PAID.intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }
    
    @Test
    @Transactional
    public void getStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        // Get the staffSalary
        restStaffSalaryMockMvc.perform(get("/api/staff-salaries/{id}", staffSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffSalary.getId().intValue()))
            .andExpect(jsonPath("$.salaryPaid").value(DEFAULT_SALARY_PAID.intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingStaffSalary() throws Exception {
        // Get the staffSalary
        restStaffSalaryMockMvc.perform(get("/api/staff-salaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();

        // Update the staffSalary
        StaffSalary updatedStaffSalary = staffSalaryRepository.findById(staffSalary.getId()).get();
        // Disconnect from session so that the updates on updatedStaffSalary are not directly saved in db
        em.detach(updatedStaffSalary);
        updatedStaffSalary
            .salaryPaid(UPDATED_SALARY_PAID)
            .month(UPDATED_MONTH);

        restStaffSalaryMockMvc.perform(put("/api/staff-salaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStaffSalary)))
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
    public void updateNonExistingStaffSalary() throws Exception {
        int databaseSizeBeforeUpdate = staffSalaryRepository.findAll().size();

        // Create the StaffSalary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffSalaryMockMvc.perform(put("/api/staff-salaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(staffSalary)))
            .andExpect(status().isBadRequest());

        // Validate the StaffSalary in the database
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStaffSalary() throws Exception {
        // Initialize the database
        staffSalaryRepository.saveAndFlush(staffSalary);

        int databaseSizeBeforeDelete = staffSalaryRepository.findAll().size();

        // Delete the staffSalary
        restStaffSalaryMockMvc.perform(delete("/api/staff-salaries/{id}", staffSalary.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaffSalary> staffSalaryList = staffSalaryRepository.findAll();
        assertThat(staffSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
