package com.school.app.web.rest;

import com.school.app.JhipsterSampleApplicationApp;
import com.school.app.domain.BusStops;
import com.school.app.repository.BusStopsRepository;

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
 * Integration tests for the {@link BusStopsResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class BusStopsResourceIT {

    private static final String DEFAULT_ROUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_STOPS = "AAAAAAAAAA";
    private static final String UPDATED_BUS_STOPS = "BBBBBBBBBB";

    @Autowired
    private BusStopsRepository busStopsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusStopsMockMvc;

    private BusStops busStops;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusStops createEntity(EntityManager em) {
        BusStops busStops = new BusStops()
            .routeName(DEFAULT_ROUTE_NAME)
            .busStops(DEFAULT_BUS_STOPS);
        return busStops;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusStops createUpdatedEntity(EntityManager em) {
        BusStops busStops = new BusStops()
            .routeName(UPDATED_ROUTE_NAME)
            .busStops(UPDATED_BUS_STOPS);
        return busStops;
    }

    @BeforeEach
    public void initTest() {
        busStops = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusStops() throws Exception {
        int databaseSizeBeforeCreate = busStopsRepository.findAll().size();

        // Create the BusStops
        restBusStopsMockMvc.perform(post("/api/bus-stops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busStops)))
            .andExpect(status().isCreated());

        // Validate the BusStops in the database
        List<BusStops> busStopsList = busStopsRepository.findAll();
        assertThat(busStopsList).hasSize(databaseSizeBeforeCreate + 1);
        BusStops testBusStops = busStopsList.get(busStopsList.size() - 1);
        assertThat(testBusStops.getRouteName()).isEqualTo(DEFAULT_ROUTE_NAME);
        assertThat(testBusStops.getBusStops()).isEqualTo(DEFAULT_BUS_STOPS);
    }

    @Test
    @Transactional
    public void createBusStopsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = busStopsRepository.findAll().size();

        // Create the BusStops with an existing ID
        busStops.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusStopsMockMvc.perform(post("/api/bus-stops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busStops)))
            .andExpect(status().isBadRequest());

        // Validate the BusStops in the database
        List<BusStops> busStopsList = busStopsRepository.findAll();
        assertThat(busStopsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBusStops() throws Exception {
        // Initialize the database
        busStopsRepository.saveAndFlush(busStops);

        // Get all the busStopsList
        restBusStopsMockMvc.perform(get("/api/bus-stops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busStops.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeName").value(hasItem(DEFAULT_ROUTE_NAME)))
            .andExpect(jsonPath("$.[*].busStops").value(hasItem(DEFAULT_BUS_STOPS)));
    }
    
    @Test
    @Transactional
    public void getBusStops() throws Exception {
        // Initialize the database
        busStopsRepository.saveAndFlush(busStops);

        // Get the busStops
        restBusStopsMockMvc.perform(get("/api/bus-stops/{id}", busStops.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busStops.getId().intValue()))
            .andExpect(jsonPath("$.routeName").value(DEFAULT_ROUTE_NAME))
            .andExpect(jsonPath("$.busStops").value(DEFAULT_BUS_STOPS));
    }

    @Test
    @Transactional
    public void getNonExistingBusStops() throws Exception {
        // Get the busStops
        restBusStopsMockMvc.perform(get("/api/bus-stops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusStops() throws Exception {
        // Initialize the database
        busStopsRepository.saveAndFlush(busStops);

        int databaseSizeBeforeUpdate = busStopsRepository.findAll().size();

        // Update the busStops
        BusStops updatedBusStops = busStopsRepository.findById(busStops.getId()).get();
        // Disconnect from session so that the updates on updatedBusStops are not directly saved in db
        em.detach(updatedBusStops);
        updatedBusStops
            .routeName(UPDATED_ROUTE_NAME)
            .busStops(UPDATED_BUS_STOPS);

        restBusStopsMockMvc.perform(put("/api/bus-stops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusStops)))
            .andExpect(status().isOk());

        // Validate the BusStops in the database
        List<BusStops> busStopsList = busStopsRepository.findAll();
        assertThat(busStopsList).hasSize(databaseSizeBeforeUpdate);
        BusStops testBusStops = busStopsList.get(busStopsList.size() - 1);
        assertThat(testBusStops.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
        assertThat(testBusStops.getBusStops()).isEqualTo(UPDATED_BUS_STOPS);
    }

    @Test
    @Transactional
    public void updateNonExistingBusStops() throws Exception {
        int databaseSizeBeforeUpdate = busStopsRepository.findAll().size();

        // Create the BusStops

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusStopsMockMvc.perform(put("/api/bus-stops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(busStops)))
            .andExpect(status().isBadRequest());

        // Validate the BusStops in the database
        List<BusStops> busStopsList = busStopsRepository.findAll();
        assertThat(busStopsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBusStops() throws Exception {
        // Initialize the database
        busStopsRepository.saveAndFlush(busStops);

        int databaseSizeBeforeDelete = busStopsRepository.findAll().size();

        // Delete the busStops
        restBusStopsMockMvc.perform(delete("/api/bus-stops/{id}", busStops.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusStops> busStopsList = busStopsRepository.findAll();
        assertThat(busStopsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
