package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.BusRouteName;
import com.school.app.repository.BusRouteNameRepository;
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
 * Integration tests for the {@link BusRouteNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusRouteNameResourceIT {

    private static final String DEFAULT_ROUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bus-route-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusRouteNameRepository busRouteNameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusRouteNameMockMvc;

    private BusRouteName busRouteName;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusRouteName createEntity(EntityManager em) {
        BusRouteName busRouteName = new BusRouteName().routeName(DEFAULT_ROUTE_NAME);
        return busRouteName;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusRouteName createUpdatedEntity(EntityManager em) {
        BusRouteName busRouteName = new BusRouteName().routeName(UPDATED_ROUTE_NAME);
        return busRouteName;
    }

    @BeforeEach
    public void initTest() {
        busRouteName = createEntity(em);
    }

    @Test
    @Transactional
    void createBusRouteName() throws Exception {
        int databaseSizeBeforeCreate = busRouteNameRepository.findAll().size();
        // Create the BusRouteName
        restBusRouteNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isCreated());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeCreate + 1);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(DEFAULT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void createBusRouteNameWithExistingId() throws Exception {
        // Create the BusRouteName with an existing ID
        busRouteName.setId(1L);

        int databaseSizeBeforeCreate = busRouteNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusRouteNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusRouteNames() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        // Get all the busRouteNameList
        restBusRouteNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busRouteName.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeName").value(hasItem(DEFAULT_ROUTE_NAME)));
    }

    @Test
    @Transactional
    void getBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        // Get the busRouteName
        restBusRouteNameMockMvc
            .perform(get(ENTITY_API_URL_ID, busRouteName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busRouteName.getId().intValue()))
            .andExpect(jsonPath("$.routeName").value(DEFAULT_ROUTE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBusRouteName() throws Exception {
        // Get the busRouteName
        restBusRouteNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();

        // Update the busRouteName
        BusRouteName updatedBusRouteName = busRouteNameRepository.findById(busRouteName.getId()).get();
        // Disconnect from session so that the updates on updatedBusRouteName are not directly saved in db
        em.detach(updatedBusRouteName);
        updatedBusRouteName.routeName(UPDATED_ROUTE_NAME);

        restBusRouteNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusRouteName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusRouteName))
            )
            .andExpect(status().isOk());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busRouteName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busRouteName))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busRouteName))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusRouteNameWithPatch() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();

        // Update the busRouteName using partial update
        BusRouteName partialUpdatedBusRouteName = new BusRouteName();
        partialUpdatedBusRouteName.setId(busRouteName.getId());

        partialUpdatedBusRouteName.routeName(UPDATED_ROUTE_NAME);

        restBusRouteNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusRouteName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusRouteName))
            )
            .andExpect(status().isOk());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBusRouteNameWithPatch() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();

        // Update the busRouteName using partial update
        BusRouteName partialUpdatedBusRouteName = new BusRouteName();
        partialUpdatedBusRouteName.setId(busRouteName.getId());

        partialUpdatedBusRouteName.routeName(UPDATED_ROUTE_NAME);

        restBusRouteNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusRouteName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusRouteName))
            )
            .andExpect(status().isOk());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busRouteName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busRouteName))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busRouteName))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();
        busRouteName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(busRouteName))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeDelete = busRouteNameRepository.findAll().size();

        // Delete the busRouteName
        restBusRouteNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, busRouteName.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
