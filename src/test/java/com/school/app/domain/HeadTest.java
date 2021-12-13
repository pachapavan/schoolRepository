package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Head.class);
        Head head1 = new Head();
        head1.setId(1L);
        Head head2 = new Head();
        head2.setId(head1.getId());
        assertThat(head1).isEqualTo(head2);
        head2.setId(2L);
        assertThat(head1).isNotEqualTo(head2);
        head1.setId(null);
        assertThat(head1).isNotEqualTo(head2);
    }
}
