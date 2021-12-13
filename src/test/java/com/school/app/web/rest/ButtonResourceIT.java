package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.Button;
import com.school.app.repository.ButtonRepository;
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
 * Integration tests for the {@link ButtonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ButtonResourceIT {

    private static final Boolean DEFAULT_IS_LINK = false;
    private static final Boolean UPDATED_IS_LINK = true;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_FONT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_FONT_SIZE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/buttons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ButtonRepository buttonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restButtonMockMvc;

    private Button button;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Button createEntity(EntityManager em) {
        Button button = new Button()
            .isLink(DEFAULT_IS_LINK)
            .link(DEFAULT_LINK)
            .displayText(DEFAULT_DISPLAY_TEXT)
            .fontSize(DEFAULT_FONT_SIZE);
        return button;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Button createUpdatedEntity(EntityManager em) {
        Button button = new Button()
            .isLink(UPDATED_IS_LINK)
            .link(UPDATED_LINK)
            .displayText(UPDATED_DISPLAY_TEXT)
            .fontSize(UPDATED_FONT_SIZE);
        return button;
    }

    @BeforeEach
    public void initTest() {
        button = createEntity(em);
    }

    @Test
    @Transactional
    void createButton() throws Exception {
        int databaseSizeBeforeCreate = buttonRepository.findAll().size();
        // Create the Button
        restButtonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(button)))
            .andExpect(status().isCreated());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeCreate + 1);
        Button testButton = buttonList.get(buttonList.size() - 1);
        assertThat(testButton.getIsLink()).isEqualTo(DEFAULT_IS_LINK);
        assertThat(testButton.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testButton.getDisplayText()).isEqualTo(DEFAULT_DISPLAY_TEXT);
        assertThat(testButton.getFontSize()).isEqualTo(DEFAULT_FONT_SIZE);
    }

    @Test
    @Transactional
    void createButtonWithExistingId() throws Exception {
        // Create the Button with an existing ID
        button.setId(1L);

        int databaseSizeBeforeCreate = buttonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restButtonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(button)))
            .andExpect(status().isBadRequest());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllButtons() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        // Get all the buttonList
        restButtonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(button.getId().intValue())))
            .andExpect(jsonPath("$.[*].isLink").value(hasItem(DEFAULT_IS_LINK.booleanValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].displayText").value(hasItem(DEFAULT_DISPLAY_TEXT)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)));
    }

    @Test
    @Transactional
    void getButton() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        // Get the button
        restButtonMockMvc
            .perform(get(ENTITY_API_URL_ID, button.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(button.getId().intValue()))
            .andExpect(jsonPath("$.isLink").value(DEFAULT_IS_LINK.booleanValue()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.displayText").value(DEFAULT_DISPLAY_TEXT))
            .andExpect(jsonPath("$.fontSize").value(DEFAULT_FONT_SIZE));
    }

    @Test
    @Transactional
    void getNonExistingButton() throws Exception {
        // Get the button
        restButtonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewButton() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();

        // Update the button
        Button updatedButton = buttonRepository.findById(button.getId()).get();
        // Disconnect from session so that the updates on updatedButton are not directly saved in db
        em.detach(updatedButton);
        updatedButton.isLink(UPDATED_IS_LINK).link(UPDATED_LINK).displayText(UPDATED_DISPLAY_TEXT).fontSize(UPDATED_FONT_SIZE);

        restButtonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedButton.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedButton))
            )
            .andExpect(status().isOk());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
        Button testButton = buttonList.get(buttonList.size() - 1);
        assertThat(testButton.getIsLink()).isEqualTo(UPDATED_IS_LINK);
        assertThat(testButton.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testButton.getDisplayText()).isEqualTo(UPDATED_DISPLAY_TEXT);
        assertThat(testButton.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void putNonExistingButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, button.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(button))
            )
            .andExpect(status().isBadRequest());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(button))
            )
            .andExpect(status().isBadRequest());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(button)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateButtonWithPatch() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();

        // Update the button using partial update
        Button partialUpdatedButton = new Button();
        partialUpdatedButton.setId(button.getId());

        partialUpdatedButton.link(UPDATED_LINK).fontSize(UPDATED_FONT_SIZE);

        restButtonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedButton.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedButton))
            )
            .andExpect(status().isOk());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
        Button testButton = buttonList.get(buttonList.size() - 1);
        assertThat(testButton.getIsLink()).isEqualTo(DEFAULT_IS_LINK);
        assertThat(testButton.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testButton.getDisplayText()).isEqualTo(DEFAULT_DISPLAY_TEXT);
        assertThat(testButton.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void fullUpdateButtonWithPatch() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();

        // Update the button using partial update
        Button partialUpdatedButton = new Button();
        partialUpdatedButton.setId(button.getId());

        partialUpdatedButton.isLink(UPDATED_IS_LINK).link(UPDATED_LINK).displayText(UPDATED_DISPLAY_TEXT).fontSize(UPDATED_FONT_SIZE);

        restButtonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedButton.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedButton))
            )
            .andExpect(status().isOk());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
        Button testButton = buttonList.get(buttonList.size() - 1);
        assertThat(testButton.getIsLink()).isEqualTo(UPDATED_IS_LINK);
        assertThat(testButton.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testButton.getDisplayText()).isEqualTo(UPDATED_DISPLAY_TEXT);
        assertThat(testButton.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void patchNonExistingButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, button.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(button))
            )
            .andExpect(status().isBadRequest());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(button))
            )
            .andExpect(status().isBadRequest());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamButton() throws Exception {
        int databaseSizeBeforeUpdate = buttonRepository.findAll().size();
        button.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restButtonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(button)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Button in the database
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteButton() throws Exception {
        // Initialize the database
        buttonRepository.saveAndFlush(button);

        int databaseSizeBeforeDelete = buttonRepository.findAll().size();

        // Delete the button
        restButtonMockMvc
            .perform(delete(ENTITY_API_URL_ID, button.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Button> buttonList = buttonRepository.findAll();
        assertThat(buttonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
