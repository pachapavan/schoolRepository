package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.StudentMarkes;
import com.school.app.repository.StudentMarkesRepository;
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
 * Integration tests for the {@link StudentMarkesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentMarkesResourceIT {

    private static final String DEFAULT_EXAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXAM_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_MARKES = 1L;
    private static final Long UPDATED_TOTAL_MARKES = 2L;

    private static final Long DEFAULT_MARKES = 1L;
    private static final Long UPDATED_MARKES = 2L;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/student-markes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentMarkesRepository studentMarkesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMarkesMockMvc;

    private StudentMarkes studentMarkes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentMarkes createEntity(EntityManager em) {
        StudentMarkes studentMarkes = new StudentMarkes()
            .examName(DEFAULT_EXAM_NAME)
            .totalMarkes(DEFAULT_TOTAL_MARKES)
            .markes(DEFAULT_MARKES)
            .comments(DEFAULT_COMMENTS);
        return studentMarkes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentMarkes createUpdatedEntity(EntityManager em) {
        StudentMarkes studentMarkes = new StudentMarkes()
            .examName(UPDATED_EXAM_NAME)
            .totalMarkes(UPDATED_TOTAL_MARKES)
            .markes(UPDATED_MARKES)
            .comments(UPDATED_COMMENTS);
        return studentMarkes;
    }

    @BeforeEach
    public void initTest() {
        studentMarkes = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentMarkes() throws Exception {
        int databaseSizeBeforeCreate = studentMarkesRepository.findAll().size();
        // Create the StudentMarkes
        restStudentMarkesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentMarkes)))
            .andExpect(status().isCreated());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeCreate + 1);
        StudentMarkes testStudentMarkes = studentMarkesList.get(studentMarkesList.size() - 1);
        assertThat(testStudentMarkes.getExamName()).isEqualTo(DEFAULT_EXAM_NAME);
        assertThat(testStudentMarkes.getTotalMarkes()).isEqualTo(DEFAULT_TOTAL_MARKES);
        assertThat(testStudentMarkes.getMarkes()).isEqualTo(DEFAULT_MARKES);
        assertThat(testStudentMarkes.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createStudentMarkesWithExistingId() throws Exception {
        // Create the StudentMarkes with an existing ID
        studentMarkes.setId(1L);

        int databaseSizeBeforeCreate = studentMarkesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMarkesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentMarkes)))
            .andExpect(status().isBadRequest());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentMarkes() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        // Get all the studentMarkesList
        restStudentMarkesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentMarkes.getId().intValue())))
            .andExpect(jsonPath("$.[*].examName").value(hasItem(DEFAULT_EXAM_NAME)))
            .andExpect(jsonPath("$.[*].totalMarkes").value(hasItem(DEFAULT_TOTAL_MARKES.intValue())))
            .andExpect(jsonPath("$.[*].markes").value(hasItem(DEFAULT_MARKES.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getStudentMarkes() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        // Get the studentMarkes
        restStudentMarkesMockMvc
            .perform(get(ENTITY_API_URL_ID, studentMarkes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentMarkes.getId().intValue()))
            .andExpect(jsonPath("$.examName").value(DEFAULT_EXAM_NAME))
            .andExpect(jsonPath("$.totalMarkes").value(DEFAULT_TOTAL_MARKES.intValue()))
            .andExpect(jsonPath("$.markes").value(DEFAULT_MARKES.intValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingStudentMarkes() throws Exception {
        // Get the studentMarkes
        restStudentMarkesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentMarkes() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();

        // Update the studentMarkes
        StudentMarkes updatedStudentMarkes = studentMarkesRepository.findById(studentMarkes.getId()).get();
        // Disconnect from session so that the updates on updatedStudentMarkes are not directly saved in db
        em.detach(updatedStudentMarkes);
        updatedStudentMarkes
            .examName(UPDATED_EXAM_NAME)
            .totalMarkes(UPDATED_TOTAL_MARKES)
            .markes(UPDATED_MARKES)
            .comments(UPDATED_COMMENTS);

        restStudentMarkesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentMarkes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentMarkes))
            )
            .andExpect(status().isOk());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
        StudentMarkes testStudentMarkes = studentMarkesList.get(studentMarkesList.size() - 1);
        assertThat(testStudentMarkes.getExamName()).isEqualTo(UPDATED_EXAM_NAME);
        assertThat(testStudentMarkes.getTotalMarkes()).isEqualTo(UPDATED_TOTAL_MARKES);
        assertThat(testStudentMarkes.getMarkes()).isEqualTo(UPDATED_MARKES);
        assertThat(testStudentMarkes.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentMarkes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentMarkes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentMarkes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentMarkes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentMarkesWithPatch() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();

        // Update the studentMarkes using partial update
        StudentMarkes partialUpdatedStudentMarkes = new StudentMarkes();
        partialUpdatedStudentMarkes.setId(studentMarkes.getId());

        partialUpdatedStudentMarkes.totalMarkes(UPDATED_TOTAL_MARKES).comments(UPDATED_COMMENTS);

        restStudentMarkesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentMarkes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentMarkes))
            )
            .andExpect(status().isOk());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
        StudentMarkes testStudentMarkes = studentMarkesList.get(studentMarkesList.size() - 1);
        assertThat(testStudentMarkes.getExamName()).isEqualTo(DEFAULT_EXAM_NAME);
        assertThat(testStudentMarkes.getTotalMarkes()).isEqualTo(UPDATED_TOTAL_MARKES);
        assertThat(testStudentMarkes.getMarkes()).isEqualTo(DEFAULT_MARKES);
        assertThat(testStudentMarkes.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateStudentMarkesWithPatch() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();

        // Update the studentMarkes using partial update
        StudentMarkes partialUpdatedStudentMarkes = new StudentMarkes();
        partialUpdatedStudentMarkes.setId(studentMarkes.getId());

        partialUpdatedStudentMarkes
            .examName(UPDATED_EXAM_NAME)
            .totalMarkes(UPDATED_TOTAL_MARKES)
            .markes(UPDATED_MARKES)
            .comments(UPDATED_COMMENTS);

        restStudentMarkesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentMarkes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentMarkes))
            )
            .andExpect(status().isOk());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
        StudentMarkes testStudentMarkes = studentMarkesList.get(studentMarkesList.size() - 1);
        assertThat(testStudentMarkes.getExamName()).isEqualTo(UPDATED_EXAM_NAME);
        assertThat(testStudentMarkes.getTotalMarkes()).isEqualTo(UPDATED_TOTAL_MARKES);
        assertThat(testStudentMarkes.getMarkes()).isEqualTo(UPDATED_MARKES);
        assertThat(testStudentMarkes.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentMarkes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentMarkes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentMarkes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentMarkes() throws Exception {
        int databaseSizeBeforeUpdate = studentMarkesRepository.findAll().size();
        studentMarkes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMarkesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentMarkes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentMarkes in the database
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentMarkes() throws Exception {
        // Initialize the database
        studentMarkesRepository.saveAndFlush(studentMarkes);

        int databaseSizeBeforeDelete = studentMarkesRepository.findAll().size();

        // Delete the studentMarkes
        restStudentMarkesMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentMarkes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentMarkes> studentMarkesList = studentMarkesRepository.findAll();
        assertThat(studentMarkesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
