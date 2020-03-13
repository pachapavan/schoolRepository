package com.school.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.school.app.web.rest.TestUtil;

public class BusRouteNameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusRouteName.class);
        BusRouteName busRouteName1 = new BusRouteName();
        busRouteName1.setId(1L);
        BusRouteName busRouteName2 = new BusRouteName();
        busRouteName2.setId(busRouteName1.getId());
        assertThat(busRouteName1).isEqualTo(busRouteName2);
        busRouteName2.setId(2L);
        assertThat(busRouteName1).isNotEqualTo(busRouteName2);
        busRouteName1.setId(null);
        assertThat(busRouteName1).isNotEqualTo(busRouteName2);
    }
}
