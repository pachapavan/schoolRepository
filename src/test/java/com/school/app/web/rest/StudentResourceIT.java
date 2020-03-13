package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.Student;
import com.school.app.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StudentResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;

    private static final String DEFAULT_STUDENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .studentId(DEFAULT_STUDENT_ID)
            .studentName(DEFAULT_STUDENT_NAME)
            .parentName(DEFAULT_PARENT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .status(DEFAULT_STATUS)
            .comments(DEFAULT_COMMENTS);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME)
            .parentName(UPDATED_PARENT_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testStudent.getStudentName()).isEqualTo(DEFAULT_STUDENT_NAME);
        assertThat(testStudent.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStudent.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testStudent.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testStudent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStudent.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentName").value(hasItem(DEFAULT_STUDENT_NAME)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.studentName").value(DEFAULT_STUDENT_NAME))
            .andExpect(jsonPath("$.parentName").value(DEFAULT_PARENT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME)
            .parentName(UPDATED_PARENT_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testStudent.getStudentName()).isEqualTo(UPDATED_STUDENT_NAME);
        assertThat(testStudent.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStudent.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testStudent.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testStudent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStudent.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
