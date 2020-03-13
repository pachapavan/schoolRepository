package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.ClassName;
import com.school.app.repository.ClassNameRepository;

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
 * Integration tests for the {@link ClassNameResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClassNameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CLASS_NUMBER = 1L;
    private static final Long UPDATED_CLASS_NUMBER = 2L;

    @Autowired
    private ClassNameRepository classNameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassNameMockMvc;

    private ClassName className;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassName createEntity(EntityManager em) {
        ClassName className = new ClassName()
            .name(DEFAULT_NAME)
            .classNumber(DEFAULT_CLASS_NUMBER);
        return className;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassName createUpdatedEntity(EntityManager em) {
        ClassName className = new ClassName()
            .name(UPDATED_NAME)
            .classNumber(UPDATED_CLASS_NUMBER);
        return className;
    }

    @BeforeEach
    public void initTest() {
        className = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassName() throws Exception {
        int databaseSizeBeforeCreate = classNameRepository.findAll().size();

        // Create the ClassName
        restClassNameMockMvc.perform(post("/api/class-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(className)))
            .andExpect(status().isCreated());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeCreate + 1);
        ClassName testClassName = classNameList.get(classNameList.size() - 1);
        assertThat(testClassName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassName.getClassNumber()).isEqualTo(DEFAULT_CLASS_NUMBER);
    }

    @Test
    @Transactional
    public void createClassNameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classNameRepository.findAll().size();

        // Create the ClassName with an existing ID
        className.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassNameMockMvc.perform(post("/api/class-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(className)))
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassNames() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        // Get all the classNameList
        restClassNameMockMvc.perform(get("/api/class-names?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(className.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].classNumber").value(hasItem(DEFAULT_CLASS_NUMBER.intValue())));
    }
    
    @Test
    @Transactional
    public void getClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        // Get the className
        restClassNameMockMvc.perform(get("/api/class-names/{id}", className.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(className.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.classNumber").value(DEFAULT_CLASS_NUMBER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClassName() throws Exception {
        // Get the className
        restClassNameMockMvc.perform(get("/api/class-names/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();

        // Update the className
        ClassName updatedClassName = classNameRepository.findById(className.getId()).get();
        // Disconnect from session so that the updates on updatedClassName are not directly saved in db
        em.detach(updatedClassName);
        updatedClassName
            .name(UPDATED_NAME)
            .classNumber(UPDATED_CLASS_NUMBER);

        restClassNameMockMvc.perform(put("/api/class-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassName)))
            .andExpect(status().isOk());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
        ClassName testClassName = classNameList.get(classNameList.size() - 1);
        assertThat(testClassName.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassName.getClassNumber()).isEqualTo(UPDATED_CLASS_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();

        // Create the ClassName

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassNameMockMvc.perform(put("/api/class-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(className)))
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeDelete = classNameRepository.findAll().size();

        // Delete the className
        restClassNameMockMvc.perform(delete("/api/class-names/{id}", className.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
