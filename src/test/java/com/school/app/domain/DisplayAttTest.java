package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisplayAttTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisplayAtt.class);
        DisplayAtt displayAtt1 = new DisplayAtt();
        displayAtt1.setId(1L);
        DisplayAtt displayAtt2 = new DisplayAtt();
        displayAtt2.setId(displayAtt1.getId());
        assertThat(displayAtt1).isEqualTo(displayAtt2);
        displayAtt2.setId(2L);
        assertThat(displayAtt1).isNotEqualTo(displayAtt2);
        displayAtt1.setId(null);
        assertThat(displayAtt1).isNotEqualTo(displayAtt2);
    }
}
