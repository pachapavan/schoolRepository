package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexBoxTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexBox.class);
        FlexBox flexBox1 = new FlexBox();
        flexBox1.setId(1L);
        FlexBox flexBox2 = new FlexBox();
        flexBox2.setId(flexBox1.getId());
        assertThat(flexBox1).isEqualTo(flexBox2);
        flexBox2.setId(2L);
        assertThat(flexBox1).isNotEqualTo(flexBox2);
        flexBox1.setId(null);
        assertThat(flexBox1).isNotEqualTo(flexBox2);
    }
}
