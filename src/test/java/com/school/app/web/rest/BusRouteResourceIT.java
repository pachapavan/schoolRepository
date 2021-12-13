package com.school.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.school.app.IntegrationTest;
import com.school.app.domain.BusRoute;
import com.school.app.repository.BusRouteRepository;
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
 * Integration tests for the {@link BusRouteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusRouteResourceIT {

    private static final String DEFAULT_ROUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROUTE_DRIVER = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_DRIVER = "BBBBBBBBBB";

    private static final Long DEFAULT_BUS_NUMBER = 1L;
    private static final Long UPDATED_BUS_NUMBER = 2L;

    private static final Long DEFAULT_YEAR = 1L;
    private static final Long UPDATED_YEAR = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bus-routes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusRouteMockMvc;

    private BusRoute busRoute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusRoute createEntity(EntityManager em) {
        BusRoute busRoute = new BusRoute()
            .routeName(DEFAULT_ROUTE_NAME)
            .routeDriver(DEFAULT_ROUTE_DRIVER)
            .busNumber(DEFAULT_BUS_NUMBER)
            .year(DEFAULT_YEAR)
            .status(DEFAULT_STATUS)
            .comments(DEFAULT_COMMENTS);
        return busRoute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusRoute createUpdatedEntity(EntityManager em) {
        BusRoute busRoute = new BusRoute()
            .routeName(UPDATED_ROUTE_NAME)
            .routeDriver(UPDATED_ROUTE_DRIVER)
            .busNumber(UPDATED_BUS_NUMBER)
            .year(UPDATED_YEAR)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);
        return busRoute;
    }

    @BeforeEach
    public void initTest() {
        busRoute = createEntity(em);
    }

    @Test
    @Transactional
    void createBusRoute() throws Exception {
        int databaseSizeBeforeCreate = busRouteRepository.findAll().size();
        // Create the BusRoute
        restBusRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isCreated());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeCreate + 1);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteName()).isEqualTo(DEFAULT_ROUTE_NAME);
        assertThat(testBusRoute.getRouteDriver()).isEqualTo(DEFAULT_ROUTE_DRIVER);
        assertThat(testBusRoute.getBusNumber()).isEqualTo(DEFAULT_BUS_NUMBER);
        assertThat(testBusRoute.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBusRoute.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBusRoute.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createBusRouteWithExistingId() throws Exception {
        // Create the BusRoute with an existing ID
        busRoute.setId(1L);

        int databaseSizeBeforeCreate = busRouteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusRoutes() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        // Get all the busRouteList
        restBusRouteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeName").value(hasItem(DEFAULT_ROUTE_NAME)))
            .andExpect(jsonPath("$.[*].routeDriver").value(hasItem(DEFAULT_ROUTE_DRIVER)))
            .andExpect(jsonPath("$.[*].busNumber").value(hasItem(DEFAULT_BUS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        // Get the busRoute
        restBusRouteMockMvc
            .perform(get(ENTITY_API_URL_ID, busRoute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busRoute.getId().intValue()))
            .andExpect(jsonPath("$.routeName").value(DEFAULT_ROUTE_NAME))
            .andExpect(jsonPath("$.routeDriver").value(DEFAULT_ROUTE_DRIVER))
            .andExpect(jsonPath("$.busNumber").value(DEFAULT_BUS_NUMBER.intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingBusRoute() throws Exception {
        // Get the busRoute
        restBusRouteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();

        // Update the busRoute
        BusRoute updatedBusRoute = busRouteRepository.findById(busRoute.getId()).get();
        // Disconnect from session so that the updates on updatedBusRoute are not directly saved in db
        em.detach(updatedBusRoute);
        updatedBusRoute
            .routeName(UPDATED_ROUTE_NAME)
            .routeDriver(UPDATED_ROUTE_DRIVER)
            .busNumber(UPDATED_BUS_NUMBER)
            .year(UPDATED_YEAR)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);

        restBusRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusRoute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusRoute))
            )
            .andExpect(status().isOk());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
        assertThat(testBusRoute.getRouteDriver()).isEqualTo(UPDATED_ROUTE_DRIVER);
        assertThat(testBusRoute.getBusNumber()).isEqualTo(UPDATED_BUS_NUMBER);
        assertThat(testBusRoute.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBusRoute.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusRoute.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busRoute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusRouteWithPatch() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();

        // Update the busRoute using partial update
        BusRoute partialUpdatedBusRoute = new BusRoute();
        partialUpdatedBusRoute.setId(busRoute.getId());

        partialUpdatedBusRoute
            .routeName(UPDATED_ROUTE_NAME)
            .routeDriver(UPDATED_ROUTE_DRIVER)
            .busNumber(UPDATED_BUS_NUMBER)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);

        restBusRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusRoute))
            )
            .andExpect(status().isOk());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
        assertThat(testBusRoute.getRouteDriver()).isEqualTo(UPDATED_ROUTE_DRIVER);
        assertThat(testBusRoute.getBusNumber()).isEqualTo(UPDATED_BUS_NUMBER);
        assertThat(testBusRoute.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBusRoute.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusRoute.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateBusRouteWithPatch() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();

        // Update the busRoute using partial update
        BusRoute partialUpdatedBusRoute = new BusRoute();
        partialUpdatedBusRoute.setId(busRoute.getId());

        partialUpdatedBusRoute
            .routeName(UPDATED_ROUTE_NAME)
            .routeDriver(UPDATED_ROUTE_DRIVER)
            .busNumber(UPDATED_BUS_NUMBER)
            .year(UPDATED_YEAR)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);

        restBusRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusRoute))
            )
            .andExpect(status().isOk());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
        assertThat(testBusRoute.getRouteDriver()).isEqualTo(UPDATED_ROUTE_DRIVER);
        assertThat(testBusRoute.getBusNumber()).isEqualTo(UPDATED_BUS_NUMBER);
        assertThat(testBusRoute.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBusRoute.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusRoute.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();
        busRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusRouteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        int databaseSizeBeforeDelete = busRouteRepository.findAll().size();

        // Delete the busRoute
        restBusRouteMockMvc
            .perform(delete(ENTITY_API_URL_ID, busRoute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
