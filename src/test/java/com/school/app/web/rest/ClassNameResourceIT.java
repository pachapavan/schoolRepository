package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.ClassName;
import com.school.app.repository.ClassNameRepository;
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
 * Integration tests for the {@link ClassNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassNameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CLASS_NUMBER = 1L;
    private static final Long UPDATED_CLASS_NUMBER = 2L;

    private static final String ENTITY_API_URL = "/api/class-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        ClassName className = new ClassName().name(DEFAULT_NAME).classNumber(DEFAULT_CLASS_NUMBER);
        return className;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassName createUpdatedEntity(EntityManager em) {
        ClassName className = new ClassName().name(UPDATED_NAME).classNumber(UPDATED_CLASS_NUMBER);
        return className;
    }

    @BeforeEach
    public void initTest() {
        className = createEntity(em);
    }

    @Test
    @Transactional
    void createClassName() throws Exception {
        int databaseSizeBeforeCreate = classNameRepository.findAll().size();
        // Create the ClassName
        restClassNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(className)))
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
    void createClassNameWithExistingId() throws Exception {
        // Create the ClassName with an existing ID
        className.setId(1L);

        int databaseSizeBeforeCreate = classNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(className)))
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClassNames() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        // Get all the classNameList
        restClassNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(className.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].classNumber").value(hasItem(DEFAULT_CLASS_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        // Get the className
        restClassNameMockMvc
            .perform(get(ENTITY_API_URL_ID, className.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(className.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.classNumber").value(DEFAULT_CLASS_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingClassName() throws Exception {
        // Get the className
        restClassNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();

        // Update the className
        ClassName updatedClassName = classNameRepository.findById(className.getId()).get();
        // Disconnect from session so that the updates on updatedClassName are not directly saved in db
        em.detach(updatedClassName);
        updatedClassName.name(UPDATED_NAME).classNumber(UPDATED_CLASS_NUMBER);

        restClassNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassName))
            )
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
    void putNonExistingClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, className.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(className))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(className))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(className)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassNameWithPatch() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();

        // Update the className using partial update
        ClassName partialUpdatedClassName = new ClassName();
        partialUpdatedClassName.setId(className.getId());

        partialUpdatedClassName.classNumber(UPDATED_CLASS_NUMBER);

        restClassNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassName))
            )
            .andExpect(status().isOk());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
        ClassName testClassName = classNameList.get(classNameList.size() - 1);
        assertThat(testClassName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassName.getClassNumber()).isEqualTo(UPDATED_CLASS_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateClassNameWithPatch() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();

        // Update the className using partial update
        ClassName partialUpdatedClassName = new ClassName();
        partialUpdatedClassName.setId(className.getId());

        partialUpdatedClassName.name(UPDATED_NAME).classNumber(UPDATED_CLASS_NUMBER);

        restClassNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassName))
            )
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
    void patchNonExistingClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, className.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(className))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(className))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassName() throws Exception {
        int databaseSizeBeforeUpdate = classNameRepository.findAll().size();
        className.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassNameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(className))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassName in the database
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassName() throws Exception {
        // Initialize the database
        classNameRepository.saveAndFlush(className);

        int databaseSizeBeforeDelete = classNameRepository.findAll().size();

        // Delete the className
        restClassNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, className.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassName> classNameList = classNameRepository.findAll();
        assertThat(classNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
