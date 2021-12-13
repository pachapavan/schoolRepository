package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Text;
import com.school.app.repository.TextRepository;
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
 * Integration tests for the {@link TextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TextResourceIT {

    private static final Boolean DEFAULT_IS_FUNCTION = false;
    private static final Boolean UPDATED_IS_FUNCTION = true;

    private static final String DEFAULT_DISPLAY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_FONT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_FONT_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_GENERIC_OBJECT = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_OBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCTION = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTextMockMvc;

    private Text text;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Text createEntity(EntityManager em) {
        Text text = new Text()
            .isFunction(DEFAULT_IS_FUNCTION)
            .displayText(DEFAULT_DISPLAY_TEXT)
            .fontSize(DEFAULT_FONT_SIZE)
            .genericObject(DEFAULT_GENERIC_OBJECT)
            .function(DEFAULT_FUNCTION);
        return text;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Text createUpdatedEntity(EntityManager em) {
        Text text = new Text()
            .isFunction(UPDATED_IS_FUNCTION)
            .displayText(UPDATED_DISPLAY_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .genericObject(UPDATED_GENERIC_OBJECT)
            .function(UPDATED_FUNCTION);
        return text;
    }

    @BeforeEach
    public void initTest() {
        text = createEntity(em);
    }

    @Test
    @Transactional
    void createText() throws Exception {
        int databaseSizeBeforeCreate = textRepository.findAll().size();
        // Create the Text
        restTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(text)))
            .andExpect(status().isCreated());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeCreate + 1);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getIsFunction()).isEqualTo(DEFAULT_IS_FUNCTION);
        assertThat(testText.getDisplayText()).isEqualTo(DEFAULT_DISPLAY_TEXT);
        assertThat(testText.getFontSize()).isEqualTo(DEFAULT_FONT_SIZE);
        assertThat(testText.getGenericObject()).isEqualTo(DEFAULT_GENERIC_OBJECT);
        assertThat(testText.getFunction()).isEqualTo(DEFAULT_FUNCTION);
    }

    @Test
    @Transactional
    void createTextWithExistingId() throws Exception {
        // Create the Text with an existing ID
        text.setId(1L);

        int databaseSizeBeforeCreate = textRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(text)))
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTexts() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList
        restTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
            .andExpect(jsonPath("$.[*].isFunction").value(hasItem(DEFAULT_IS_FUNCTION.booleanValue())))
            .andExpect(jsonPath("$.[*].displayText").value(hasItem(DEFAULT_DISPLAY_TEXT)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)))
            .andExpect(jsonPath("$.[*].genericObject").value(hasItem(DEFAULT_GENERIC_OBJECT)))
            .andExpect(jsonPath("$.[*].function").value(hasItem(DEFAULT_FUNCTION)));
    }

    @Test
    @Transactional
    void getText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get the text
        restTextMockMvc
            .perform(get(ENTITY_API_URL_ID, text.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(text.getId().intValue()))
            .andExpect(jsonPath("$.isFunction").value(DEFAULT_IS_FUNCTION.booleanValue()))
            .andExpect(jsonPath("$.displayText").value(DEFAULT_DISPLAY_TEXT))
            .andExpect(jsonPath("$.fontSize").value(DEFAULT_FONT_SIZE))
            .andExpect(jsonPath("$.genericObject").value(DEFAULT_GENERIC_OBJECT))
            .andExpect(jsonPath("$.function").value(DEFAULT_FUNCTION));
    }

    @Test
    @Transactional
    void getNonExistingText() throws Exception {
        // Get the text
        restTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text
        Text updatedText = textRepository.findById(text.getId()).get();
        // Disconnect from session so that the updates on updatedText are not directly saved in db
        em.detach(updatedText);
        updatedText
            .isFunction(UPDATED_IS_FUNCTION)
            .displayText(UPDATED_DISPLAY_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .genericObject(UPDATED_GENERIC_OBJECT)
            .function(UPDATED_FUNCTION);

        restTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedText.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedText))
            )
            .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getIsFunction()).isEqualTo(UPDATED_IS_FUNCTION);
        assertThat(testText.getDisplayText()).isEqualTo(UPDATED_DISPLAY_TEXT);
        assertThat(testText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testText.getGenericObject()).isEqualTo(UPDATED_GENERIC_OBJECT);
        assertThat(testText.getFunction()).isEqualTo(UPDATED_FUNCTION);
    }

    @Test
    @Transactional
    void putNonExistingText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, text.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(text))
            )
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(text))
            )
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(text)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTextWithPatch() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text using partial update
        Text partialUpdatedText = new Text();
        partialUpdatedText.setId(text.getId());

        partialUpdatedText.isFunction(UPDATED_IS_FUNCTION).displayText(UPDATED_DISPLAY_TEXT).fontSize(UPDATED_FONT_SIZE);

        restTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedText))
            )
            .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getIsFunction()).isEqualTo(UPDATED_IS_FUNCTION);
        assertThat(testText.getDisplayText()).isEqualTo(UPDATED_DISPLAY_TEXT);
        assertThat(testText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testText.getGenericObject()).isEqualTo(DEFAULT_GENERIC_OBJECT);
        assertThat(testText.getFunction()).isEqualTo(DEFAULT_FUNCTION);
    }

    @Test
    @Transactional
    void fullUpdateTextWithPatch() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text using partial update
        Text partialUpdatedText = new Text();
        partialUpdatedText.setId(text.getId());

        partialUpdatedText
            .isFunction(UPDATED_IS_FUNCTION)
            .displayText(UPDATED_DISPLAY_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .genericObject(UPDATED_GENERIC_OBJECT)
            .function(UPDATED_FUNCTION);

        restTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedText))
            )
            .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getIsFunction()).isEqualTo(UPDATED_IS_FUNCTION);
        assertThat(testText.getDisplayText()).isEqualTo(UPDATED_DISPLAY_TEXT);
        assertThat(testText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testText.getGenericObject()).isEqualTo(UPDATED_GENERIC_OBJECT);
        assertThat(testText.getFunction()).isEqualTo(UPDATED_FUNCTION);
    }

    @Test
    @Transactional
    void patchNonExistingText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, text.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(text))
            )
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(text))
            )
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();
        text.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(text)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeDelete = textRepository.findAll().size();

        // Delete the text
        restTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, text.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
