package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusRouteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusRoute.class);
        BusRoute busRoute1 = new BusRoute();
        busRoute1.setId(1L);
        BusRoute busRoute2 = new BusRoute();
        busRoute2.setId(busRoute1.getId());
        assertThat(busRoute1).isEqualTo(busRoute2);
        busRoute2.setId(2L);
        assertThat(busRoute1).isNotEqualTo(busRoute2);
        busRoute1.setId(null);
        assertThat(busRoute1).isNotEqualTo(busRoute2);
    }
}
