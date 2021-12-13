package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ButtonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Button.class);
        Button button1 = new Button();
        button1.setId(1L);
        Button button2 = new Button();
        button2.setId(button1.getId());
        assertThat(button1).isEqualTo(button2);
        button2.setId(2L);
        assertThat(button1).isNotEqualTo(button2);
        button1.setId(null);
        assertThat(button1).isNotEqualTo(button2);
    }
}
