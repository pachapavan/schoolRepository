package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.BusRouteName;
import com.school.app.repository.BusRouteNameRepository;

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
 * Integration tests for the {@link BusRouteNameResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class BusRouteNameResourceIT {

    private static final String DEFAULT_ROUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_NAME = "BBBBBBBBBB";

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
        BusRouteName busRouteName = new BusRouteName()
            .routeName(DEFAULT_ROUTE_NAME);
        return busRouteName;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusRouteName createUpdatedEntity(EntityManager em) {
        BusRouteName busRouteName = new BusRouteName()
            .routeName(UPDATED_ROUTE_NAME);
        return busRouteName;
    }

    @BeforeEach
    public void initTest() {
        busRouteName = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusRouteName() throws Exception {
        int databaseSizeBeforeCreate = busRouteNameRepository.findAll().size();

        // Create the BusRouteName
        restBusRouteNameMockMvc.perform(post("/api/bus-route-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isCreated());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeCreate + 1);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(DEFAULT_ROUTE_NAME);
    }

    @Test
    @Transactional
    public void createBusRouteNameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = busRouteNameRepository.findAll().size();

        // Create the BusRouteName with an existing ID
        busRouteName.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusRouteNameMockMvc.perform(post("/api/bus-route-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBusRouteNames() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        // Get all the busRouteNameList
        restBusRouteNameMockMvc.perform(get("/api/bus-route-names?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busRouteName.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeName").value(hasItem(DEFAULT_ROUTE_NAME)));
    }
    
    @Test
    @Transactional
    public void getBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        // Get the busRouteName
        restBusRouteNameMockMvc.perform(get("/api/bus-route-names/{id}", busRouteName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busRouteName.getId().intValue()))
            .andExpect(jsonPath("$.routeName").value(DEFAULT_ROUTE_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingBusRouteName() throws Exception {
        // Get the busRouteName
        restBusRouteNameMockMvc.perform(get("/api/bus-route-names/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();

        // Update the busRouteName
        BusRouteName updatedBusRouteName = busRouteNameRepository.findById(busRouteName.getId()).get();
        // Disconnect from session so that the updates on updatedBusRouteName are not directly saved in db
        em.detach(updatedBusRouteName);
        updatedBusRouteName
            .routeName(UPDATED_ROUTE_NAME);

        restBusRouteNameMockMvc.perform(put("/api/bus-route-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusRouteName)))
            .andExpect(status().isOk());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
        BusRouteName testBusRouteName = busRouteNameList.get(busRouteNameList.size() - 1);
        assertThat(testBusRouteName.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBusRouteName() throws Exception {
        int databaseSizeBeforeUpdate = busRouteNameRepository.findAll().size();

        // Create the BusRouteName

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusRouteNameMockMvc.perform(put("/api/bus-route-names")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busRouteName)))
            .andExpect(status().isBadRequest());

        // Validate the BusRouteName in the database
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBusRouteName() throws Exception {
        // Initialize the database
        busRouteNameRepository.saveAndFlush(busRouteName);

        int databaseSizeBeforeDelete = busRouteNameRepository.findAll().size();

        // Delete the busRouteName
        restBusRouteNameMockMvc.perform(delete("/api/bus-route-names/{id}", busRouteName.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusRouteName> busRouteNameList = busRouteNameRepository.findAll();
        assertThat(busRouteNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
