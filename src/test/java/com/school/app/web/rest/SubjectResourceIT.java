package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.Subject;
import com.school.app.repository.SubjectRepository;

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
 * Integration tests for the {@link SubjectResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SubjectResourceIT {

    private static final String DEFAULT_CLASSNAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASSNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_TEACHER = "BBBBBBBBBB";

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectMockMvc;

    private Subject subject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subject createEntity(EntityManager em) {
        Subject subject = new Subject()
            .classname(DEFAULT_CLASSNAME)
            .section(DEFAULT_SECTION)
            .subjectName(DEFAULT_SUBJECT_NAME)
            .subjectCode(DEFAULT_SUBJECT_CODE)
            .subjectTeacher(DEFAULT_SUBJECT_TEACHER);
        return subject;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subject createUpdatedEntity(EntityManager em) {
        Subject subject = new Subject()
            .classname(UPDATED_CLASSNAME)
            .section(UPDATED_SECTION)
            .subjectName(UPDATED_SUBJECT_NAME)
            .subjectCode(UPDATED_SUBJECT_CODE)
            .subjectTeacher(UPDATED_SUBJECT_TEACHER);
        return subject;
    }

    @BeforeEach
    public void initTest() {
        subject = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubject() throws Exception {
        int databaseSizeBeforeCreate = subjectRepository.findAll().size();

        // Create the Subject
        restSubjectMockMvc.perform(post("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isCreated());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeCreate + 1);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getClassname()).isEqualTo(DEFAULT_CLASSNAME);
        assertThat(testSubject.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testSubject.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
        assertThat(testSubject.getSubjectCode()).isEqualTo(DEFAULT_SUBJECT_CODE);
        assertThat(testSubject.getSubjectTeacher()).isEqualTo(DEFAULT_SUBJECT_TEACHER);
    }

    @Test
    @Transactional
    public void createSubjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subjectRepository.findAll().size();

        // Create the Subject with an existing ID
        subject.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectMockMvc.perform(post("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSubjects() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList
        restSubjectMockMvc.perform(get("/api/subjects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subject.getId().intValue())))
            .andExpect(jsonPath("$.[*].classname").value(hasItem(DEFAULT_CLASSNAME)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)))
            .andExpect(jsonPath("$.[*].subjectCode").value(hasItem(DEFAULT_SUBJECT_CODE)))
            .andExpect(jsonPath("$.[*].subjectTeacher").value(hasItem(DEFAULT_SUBJECT_TEACHER)));
    }
    
    @Test
    @Transactional
    public void getSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get the subject
        restSubjectMockMvc.perform(get("/api/subjects/{id}", subject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subject.getId().intValue()))
            .andExpect(jsonPath("$.classname").value(DEFAULT_CLASSNAME))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.subjectName").value(DEFAULT_SUBJECT_NAME))
            .andExpect(jsonPath("$.subjectCode").value(DEFAULT_SUBJECT_CODE))
            .andExpect(jsonPath("$.subjectTeacher").value(DEFAULT_SUBJECT_TEACHER));
    }

    @Test
    @Transactional
    public void getNonExistingSubject() throws Exception {
        // Get the subject
        restSubjectMockMvc.perform(get("/api/subjects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();

        // Update the subject
        Subject updatedSubject = subjectRepository.findById(subject.getId()).get();
        // Disconnect from session so that the updates on updatedSubject are not directly saved in db
        em.detach(updatedSubject);
        updatedSubject
            .classname(UPDATED_CLASSNAME)
            .section(UPDATED_SECTION)
            .subjectName(UPDATED_SUBJECT_NAME)
            .subjectCode(UPDATED_SUBJECT_CODE)
            .subjectTeacher(UPDATED_SUBJECT_TEACHER);

        restSubjectMockMvc.perform(put("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubject)))
            .andExpect(status().isOk());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getClassname()).isEqualTo(UPDATED_CLASSNAME);
        assertThat(testSubject.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testSubject.getSubjectCode()).isEqualTo(UPDATED_SUBJECT_CODE);
        assertThat(testSubject.getSubjectTeacher()).isEqualTo(UPDATED_SUBJECT_TEACHER);
    }

    @Test
    @Transactional
    public void updateNonExistingSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();

        // Create the Subject

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMockMvc.perform(put("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeDelete = subjectRepository.findAll().size();

        // Delete the subject
        restSubjectMockMvc.perform(delete("/api/subjects/{id}", subject.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
