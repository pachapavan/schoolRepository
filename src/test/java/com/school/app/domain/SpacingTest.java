package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpacingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spacing.class);
        Spacing spacing1 = new Spacing();
        spacing1.setId(1L);
        Spacing spacing2 = new Spacing();
        spacing2.setId(spacing1.getId());
        assertThat(spacing1).isEqualTo(spacing2);
        spacing2.setId(2L);
        assertThat(spacing1).isNotEqualTo(spacing2);
        spacing1.setId(null);
        assertThat(spacing1).isNotEqualTo(spacing2);
    }
}
