package com.school.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.school.app.web.rest.TestUtil;

public class BusStopsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusStops.class);
        BusStops busStops1 = new BusStops();
        busStops1.setId(1L);
        BusStops busStops2 = new BusStops();
        busStops2.setId(busStops1.getId());
        assertThat(busStops1).isEqualTo(busStops2);
        busStops2.setId(2L);
        assertThat(busStops1).isNotEqualTo(busStops2);
        busStops1.setId(null);
        assertThat(busStops1).isNotEqualTo(busStops2);
    }
}
