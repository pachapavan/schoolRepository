package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Text.class);
        Text text1 = new Text();
        text1.setId(1L);
        Text text2 = new Text();
        text2.setId(text1.getId());
        assertThat(text1).isEqualTo(text2);
        text2.setId(2L);
        assertThat(text1).isNotEqualTo(text2);
        text1.setId(null);
        assertThat(text1).isNotEqualTo(text2);
    }
}
