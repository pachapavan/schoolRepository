package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.StudentFee;
import com.school.app.repository.StudentFeeRepository;

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
 * Integration tests for the {@link StudentFeeResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StudentFeeResourceIT {

    private static final Long DEFAULT_TOTAL_ACADEMIC_FEE = 1L;
    private static final Long UPDATED_TOTAL_ACADEMIC_FEE = 2L;

    private static final Long DEFAULT_ACADEMIC_FEEWAVE_OFF = 1L;
    private static final Long UPDATED_ACADEMIC_FEEWAVE_OFF = 2L;

    private static final Long DEFAULT_ACADEMIC_FEE_PAID = 1L;
    private static final Long UPDATED_ACADEMIC_FEE_PAID = 2L;

    private static final Long DEFAULT_TOTAL_ACADEMIC_FEE_PAID = 1L;
    private static final Long UPDATED_TOTAL_ACADEMIC_FEE_PAID = 2L;

    private static final Long DEFAULT_ACADEMIC_FEEPENDING = 1L;
    private static final Long UPDATED_ACADEMIC_FEEPENDING = 2L;

    private static final Boolean DEFAULT_BUS_ALLOTED = false;
    private static final Boolean UPDATED_BUS_ALLOTED = true;

    private static final Boolean DEFAULT_HOSTEL_ALLOTED = false;
    private static final Boolean UPDATED_HOSTEL_ALLOTED = true;

    private static final Long DEFAULT_TOTAL_BUS_FEE = 1L;
    private static final Long UPDATED_TOTAL_BUS_FEE = 2L;

    private static final Long DEFAULT_BUS_FEEWAVE_OFF = 1L;
    private static final Long UPDATED_BUS_FEEWAVE_OFF = 2L;

    private static final Long DEFAULT_BUS_FEE_PAID = 1L;
    private static final Long UPDATED_BUS_FEE_PAID = 2L;

    private static final Long DEFAULT_TOTAL_BUS_FEE_PAID = 1L;
    private static final Long UPDATED_TOTAL_BUS_FEE_PAID = 2L;

    private static final Long DEFAULT_BUS_FEEPENDING = 1L;
    private static final Long UPDATED_BUS_FEEPENDING = 2L;

    private static final Long DEFAULT_TOTAL_HOSTEL_FEE = 1L;
    private static final Long UPDATED_TOTAL_HOSTEL_FEE = 2L;

    private static final Long DEFAULT_HOSTEL_FEEWAVE_OFF = 1L;
    private static final Long UPDATED_HOSTEL_FEEWAVE_OFF = 2L;

    private static final Long DEFAULT_TOTAL_HOSTEL_FEE_PAID = 1L;
    private static final Long UPDATED_TOTAL_HOSTEL_FEE_PAID = 2L;

    private static final Long DEFAULT_HOSTEL_FEE_PAID = 1L;
    private static final Long UPDATED_HOSTEL_FEE_PAID = 2L;

    private static final Long DEFAULT_HOSTEL_FEEPENDING = 1L;
    private static final Long UPDATED_HOSTEL_FEEPENDING = 2L;

    private static final Long DEFAULT_HOSTEL_EXPENSES = 1L;
    private static final Long UPDATED_HOSTEL_EXPENSES = 2L;

    private static final Long DEFAULT_YEAR = 1L;
    private static final Long UPDATED_YEAR = 2L;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentFeeMockMvc;

    private StudentFee studentFee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentFee createEntity(EntityManager em) {
        StudentFee studentFee = new StudentFee()
            .totalAcademicFee(DEFAULT_TOTAL_ACADEMIC_FEE)
            .academicFeewaveOff(DEFAULT_ACADEMIC_FEEWAVE_OFF)
            .academicFeePaid(DEFAULT_ACADEMIC_FEE_PAID)
            .totalAcademicFeePaid(DEFAULT_TOTAL_ACADEMIC_FEE_PAID)
            .academicFeepending(DEFAULT_ACADEMIC_FEEPENDING)
            .busAlloted(DEFAULT_BUS_ALLOTED)
            .hostelAlloted(DEFAULT_HOSTEL_ALLOTED)
            .totalBusFee(DEFAULT_TOTAL_BUS_FEE)
            .busFeewaveOff(DEFAULT_BUS_FEEWAVE_OFF)
            .busFeePaid(DEFAULT_BUS_FEE_PAID)
            .totalBusFeePaid(DEFAULT_TOTAL_BUS_FEE_PAID)
            .busFeepending(DEFAULT_BUS_FEEPENDING)
            .totalHostelFee(DEFAULT_TOTAL_HOSTEL_FEE)
            .hostelFeewaveOff(DEFAULT_HOSTEL_FEEWAVE_OFF)
            .totalHostelFeePaid(DEFAULT_TOTAL_HOSTEL_FEE_PAID)
            .hostelFeePaid(DEFAULT_HOSTEL_FEE_PAID)
            .hostelFeepending(DEFAULT_HOSTEL_FEEPENDING)
            .hostelExpenses(DEFAULT_HOSTEL_EXPENSES)
            .year(DEFAULT_YEAR)
            .comments(DEFAULT_COMMENTS);
        return studentFee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentFee createUpdatedEntity(EntityManager em) {
        StudentFee studentFee = new StudentFee()
            .totalAcademicFee(UPDATED_TOTAL_ACADEMIC_FEE)
            .academicFeewaveOff(UPDATED_ACADEMIC_FEEWAVE_OFF)
            .academicFeePaid(UPDATED_ACADEMIC_FEE_PAID)
            .totalAcademicFeePaid(UPDATED_TOTAL_ACADEMIC_FEE_PAID)
            .academicFeepending(UPDATED_ACADEMIC_FEEPENDING)
            .busAlloted(UPDATED_BUS_ALLOTED)
            .hostelAlloted(UPDATED_HOSTEL_ALLOTED)
            .totalBusFee(UPDATED_TOTAL_BUS_FEE)
            .busFeewaveOff(UPDATED_BUS_FEEWAVE_OFF)
            .busFeePaid(UPDATED_BUS_FEE_PAID)
            .totalBusFeePaid(UPDATED_TOTAL_BUS_FEE_PAID)
            .busFeepending(UPDATED_BUS_FEEPENDING)
            .totalHostelFee(UPDATED_TOTAL_HOSTEL_FEE)
            .hostelFeewaveOff(UPDATED_HOSTEL_FEEWAVE_OFF)
            .totalHostelFeePaid(UPDATED_TOTAL_HOSTEL_FEE_PAID)
            .hostelFeePaid(UPDATED_HOSTEL_FEE_PAID)
            .hostelFeepending(UPDATED_HOSTEL_FEEPENDING)
            .hostelExpenses(UPDATED_HOSTEL_EXPENSES)
            .year(UPDATED_YEAR)
            .comments(UPDATED_COMMENTS);
        return studentFee;
    }

    @BeforeEach
    public void initTest() {
        studentFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentFee() throws Exception {
        int databaseSizeBeforeCreate = studentFeeRepository.findAll().size();

        // Create the StudentFee
        restStudentFeeMockMvc.perform(post("/api/student-fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentFee)))
            .andExpect(status().isCreated());

        // Validate the StudentFee in the database
        List<StudentFee> studentFeeList = studentFeeRepository.findAll();
        assertThat(studentFeeList).hasSize(databaseSizeBeforeCreate + 1);
        StudentFee testStudentFee = studentFeeList.get(studentFeeList.size() - 1);
        assertThat(testStudentFee.getTotalAcademicFee()).isEqualTo(DEFAULT_TOTAL_ACADEMIC_FEE);
        assertThat(testStudentFee.getAcademicFeewaveOff()).isEqualTo(DEFAULT_ACADEMIC_FEEWAVE_OFF);
        assertThat(testStudentFee.getAcademicFeePaid()).isEqualTo(DEFAULT_ACADEMIC_FEE_PAID);
        assertThat(testStudentFee.getTotalAcademicFeePaid()).isEqualTo(DEFAULT_TOTAL_ACADEMIC_FEE_PAID);
        assertThat(testStudentFee.getAcademicFeepending()).isEqualTo(DEFAULT_ACADEMIC_FEEPENDING);
        assertThat(testStudentFee.isBusAlloted()).isEqualTo(DEFAULT_BUS_ALLOTED);
        assertThat(testStudentFee.isHostelAlloted()).isEqualTo(DEFAULT_HOSTEL_ALLOTED);
        assertThat(testStudentFee.getTotalBusFee()).isEqualTo(DEFAULT_TOTAL_BUS_FEE);
        assertThat(testStudentFee.getBusFeewaveOff()).isEqualTo(DEFAULT_BUS_FEEWAVE_OFF);
        assertThat(testStudentFee.getBusFeePaid()).isEqualTo(DEFAULT_BUS_FEE_PAID);
        assertThat(testStudentFee.getTotalBusFeePaid()).isEqualTo(DEFAULT_TOTAL_BUS_FEE_PAID);
        assertThat(testStudentFee.getBusFeepending()).isEqualTo(DEFAULT_BUS_FEEPENDING);
        assertThat(testStudentFee.getTotalHostelFee()).isEqualTo(DEFAULT_TOTAL_HOSTEL_FEE);
        assertThat(testStudentFee.getHostelFeewaveOff()).isEqualTo(DEFAULT_HOSTEL_FEEWAVE_OFF);
        assertThat(testStudentFee.getTotalHostelFeePaid()).isEqualTo(DEFAULT_TOTAL_HOSTEL_FEE_PAID);
        assertThat(testStudentFee.getHostelFeePaid()).isEqualTo(DEFAULT_HOSTEL_FEE_PAID);
        assertThat(testStudentFee.getHostelFeepending()).isEqualTo(DEFAULT_HOSTEL_FEEPENDING);
        assertThat(testStudentFee.getHostelExpenses()).isEqualTo(DEFAULT_HOSTEL_EXPENSES);
        assertThat(testStudentFee.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testStudentFee.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createStudentFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentFeeRepository.findAll().size();

        // Create the StudentFee with an existing ID
        studentFee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentFeeMockMvc.perform(post("/api/student-fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentFee)))
            .andExpect(status().isBadRequest());

        // Validate the StudentFee in the database
        List<StudentFee> studentFeeList = studentFeeRepository.findAll();
        assertThat(studentFeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudentFees() throws Exception {
        // Initialize the database
        studentFeeRepository.saveAndFlush(studentFee);

        // Get all the studentFeeList
        restStudentFeeMockMvc.perform(get("/api/student-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalAcademicFee").value(hasItem(DEFAULT_TOTAL_ACADEMIC_FEE.intValue())))
            .andExpect(jsonPath("$.[*].academicFeewaveOff").value(hasItem(DEFAULT_ACADEMIC_FEEWAVE_OFF.intValue())))
            .andExpect(jsonPath("$.[*].academicFeePaid").value(hasItem(DEFAULT_ACADEMIC_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].totalAcademicFeePaid").value(hasItem(DEFAULT_TOTAL_ACADEMIC_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].academicFeepending").value(hasItem(DEFAULT_ACADEMIC_FEEPENDING.intValue())))
            .andExpect(jsonPath("$.[*].busAlloted").value(hasItem(DEFAULT_BUS_ALLOTED.booleanValue())))
            .andExpect(jsonPath("$.[*].hostelAlloted").value(hasItem(DEFAULT_HOSTEL_ALLOTED.booleanValue())))
            .andExpect(jsonPath("$.[*].totalBusFee").value(hasItem(DEFAULT_TOTAL_BUS_FEE.intValue())))
            .andExpect(jsonPath("$.[*].busFeewaveOff").value(hasItem(DEFAULT_BUS_FEEWAVE_OFF.intValue())))
            .andExpect(jsonPath("$.[*].busFeePaid").value(hasItem(DEFAULT_BUS_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].totalBusFeePaid").value(hasItem(DEFAULT_TOTAL_BUS_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].busFeepending").value(hasItem(DEFAULT_BUS_FEEPENDING.intValue())))
            .andExpect(jsonPath("$.[*].totalHostelFee").value(hasItem(DEFAULT_TOTAL_HOSTEL_FEE.intValue())))
            .andExpect(jsonPath("$.[*].hostelFeewaveOff").value(hasItem(DEFAULT_HOSTEL_FEEWAVE_OFF.intValue())))
            .andExpect(jsonPath("$.[*].totalHostelFeePaid").value(hasItem(DEFAULT_TOTAL_HOSTEL_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].hostelFeePaid").value(hasItem(DEFAULT_HOSTEL_FEE_PAID.intValue())))
            .andExpect(jsonPath("$.[*].hostelFeepending").value(hasItem(DEFAULT_HOSTEL_FEEPENDING.intValue())))
            .andExpect(jsonPath("$.[*].hostelExpenses").value(hasItem(DEFAULT_HOSTEL_EXPENSES.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getStudentFee() throws Exception {
        // Initialize the database
        studentFeeRepository.saveAndFlush(studentFee);

        // Get the studentFee
        restStudentFeeMockMvc.perform(get("/api/student-fees/{id}", studentFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentFee.getId().intValue()))
            .andExpect(jsonPath("$.totalAcademicFee").value(DEFAULT_TOTAL_ACADEMIC_FEE.intValue()))
            .andExpect(jsonPath("$.academicFeewaveOff").value(DEFAULT_ACADEMIC_FEEWAVE_OFF.intValue()))
            .andExpect(jsonPath("$.academicFeePaid").value(DEFAULT_ACADEMIC_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.totalAcademicFeePaid").value(DEFAULT_TOTAL_ACADEMIC_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.academicFeepending").value(DEFAULT_ACADEMIC_FEEPENDING.intValue()))
            .andExpect(jsonPath("$.busAlloted").value(DEFAULT_BUS_ALLOTED.booleanValue()))
            .andExpect(jsonPath("$.hostelAlloted").value(DEFAULT_HOSTEL_ALLOTED.booleanValue()))
            .andExpect(jsonPath("$.totalBusFee").value(DEFAULT_TOTAL_BUS_FEE.intValue()))
            .andExpect(jsonPath("$.busFeewaveOff").value(DEFAULT_BUS_FEEWAVE_OFF.intValue()))
            .andExpect(jsonPath("$.busFeePaid").value(DEFAULT_BUS_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.totalBusFeePaid").value(DEFAULT_TOTAL_BUS_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.busFeepending").value(DEFAULT_BUS_FEEPENDING.intValue()))
            .andExpect(jsonPath("$.totalHostelFee").value(DEFAULT_TOTAL_HOSTEL_FEE.intValue()))
            .andExpect(jsonPath("$.hostelFeewaveOff").value(DEFAULT_HOSTEL_FEEWAVE_OFF.intValue()))
            .andExpect(jsonPath("$.totalHostelFeePaid").value(DEFAULT_TOTAL_HOSTEL_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.hostelFeePaid").value(DEFAULT_HOSTEL_FEE_PAID.intValue()))
            .andExpect(jsonPath("$.hostelFeepending").value(DEFAULT_HOSTEL_FEEPENDING.intValue()))
            .andExpect(jsonPath("$.hostelExpenses").value(DEFAULT_HOSTEL_EXPENSES.intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    public void getNonExistingStudentFee() throws Exception {
        // Get the studentFee
        restStudentFeeMockMvc.perform(get("/api/student-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentFee() throws Exception {
        // Initialize the database
        studentFeeRepository.saveAndFlush(studentFee);

        int databaseSizeBeforeUpdate = studentFeeRepository.findAll().size();

        // Update the studentFee
        StudentFee updatedStudentFee = studentFeeRepository.findById(studentFee.getId()).get();
        // Disconnect from session so that the updates on updatedStudentFee are not directly saved in db
        em.detach(updatedStudentFee);
        updatedStudentFee
            .totalAcademicFee(UPDATED_TOTAL_ACADEMIC_FEE)
            .academicFeewaveOff(UPDATED_ACADEMIC_FEEWAVE_OFF)
            .academicFeePaid(UPDATED_ACADEMIC_FEE_PAID)
            .totalAcademicFeePaid(UPDATED_TOTAL_ACADEMIC_FEE_PAID)
            .academicFeepending(UPDATED_ACADEMIC_FEEPENDING)
            .busAlloted(UPDATED_BUS_ALLOTED)
            .hostelAlloted(UPDATED_HOSTEL_ALLOTED)
            .totalBusFee(UPDATED_TOTAL_BUS_FEE)
            .busFeewaveOff(UPDATED_BUS_FEEWAVE_OFF)
            .busFeePaid(UPDATED_BUS_FEE_PAID)
            .totalBusFeePaid(UPDATED_TOTAL_BUS_FEE_PAID)
            .busFeepending(UPDATED_BUS_FEEPENDING)
            .totalHostelFee(UPDATED_TOTAL_HOSTEL_FEE)
            .hostelFeewaveOff(UPDATED_HOSTEL_FEEWAVE_OFF)
            .totalHostelFeePaid(UPDATED_TOTAL_HOSTEL_FEE_PAID)
            .hostelFeePaid(UPDATED_HOSTEL_FEE_PAID)
            .hostelFeepending(UPDATED_HOSTEL_FEEPENDING)
            .hostelExpenses(UPDATED_HOSTEL_EXPENSES)
            .year(UPDATED_YEAR)
            .comments(UPDATED_COMMENTS);

        restStudentFeeMockMvc.perform(put("/api/student-fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentFee)))
            .andExpect(status().isOk());

        // Validate the StudentFee in the database
        List<StudentFee> studentFeeList = studentFeeRepository.findAll();
        assertThat(studentFeeList).hasSize(databaseSizeBeforeUpdate);
        StudentFee testStudentFee = studentFeeList.get(studentFeeList.size() - 1);
        assertThat(testStudentFee.getTotalAcademicFee()).isEqualTo(UPDATED_TOTAL_ACADEMIC_FEE);
        assertThat(testStudentFee.getAcademicFeewaveOff()).isEqualTo(UPDATED_ACADEMIC_FEEWAVE_OFF);
        assertThat(testStudentFee.getAcademicFeePaid()).isEqualTo(UPDATED_ACADEMIC_FEE_PAID);
        assertThat(testStudentFee.getTotalAcademicFeePaid()).isEqualTo(UPDATED_TOTAL_ACADEMIC_FEE_PAID);
        assertThat(testStudentFee.getAcademicFeepending()).isEqualTo(UPDATED_ACADEMIC_FEEPENDING);
        assertThat(testStudentFee.isBusAlloted()).isEqualTo(UPDATED_BUS_ALLOTED);
        assertThat(testStudentFee.isHostelAlloted()).isEqualTo(UPDATED_HOSTEL_ALLOTED);
        assertThat(testStudentFee.getTotalBusFee()).isEqualTo(UPDATED_TOTAL_BUS_FEE);
        assertThat(testStudentFee.getBusFeewaveOff()).isEqualTo(UPDATED_BUS_FEEWAVE_OFF);
        assertThat(testStudentFee.getBusFeePaid()).isEqualTo(UPDATED_BUS_FEE_PAID);
        assertThat(testStudentFee.getTotalBusFeePaid()).isEqualTo(UPDATED_TOTAL_BUS_FEE_PAID);
        assertThat(testStudentFee.getBusFeepending()).isEqualTo(UPDATED_BUS_FEEPENDING);
        assertThat(testStudentFee.getTotalHostelFee()).isEqualTo(UPDATED_TOTAL_HOSTEL_FEE);
        assertThat(testStudentFee.getHostelFeewaveOff()).isEqualTo(UPDATED_HOSTEL_FEEWAVE_OFF);
        assertThat(testStudentFee.getTotalHostelFeePaid()).isEqualTo(UPDATED_TOTAL_HOSTEL_FEE_PAID);
        assertThat(testStudentFee.getHostelFeePaid()).isEqualTo(UPDATED_HOSTEL_FEE_PAID);
        assertThat(testStudentFee.getHostelFeepending()).isEqualTo(UPDATED_HOSTEL_FEEPENDING);
        assertThat(testStudentFee.getHostelExpenses()).isEqualTo(UPDATED_HOSTEL_EXPENSES);
        assertThat(testStudentFee.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testStudentFee.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentFee() throws Exception {
        int databaseSizeBeforeUpdate = studentFeeRepository.findAll().size();

        // Create the StudentFee

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentFeeMockMvc.perform(put("/api/student-fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentFee)))
            .andExpect(status().isBadRequest());

        // Validate the StudentFee in the database
        List<StudentFee> studentFeeList = studentFeeRepository.findAll();
        assertThat(studentFeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudentFee() throws Exception {
        // Initialize the database
        studentFeeRepository.saveAndFlush(studentFee);

        int databaseSizeBeforeDelete = studentFeeRepository.findAll().size();

        // Delete the studentFee
        restStudentFeeMockMvc.perform(delete("/api/student-fees/{id}", studentFee.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentFee> studentFeeList = studentFeeRepository.findAll();
        assertThat(studentFeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
