package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjectContainingStringTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObjectContainingString.class);
        ObjectContainingString objectContainingString1 = new ObjectContainingString();
        objectContainingString1.setId(1L);
        ObjectContainingString objectContainingString2 = new ObjectContainingString();
        objectContainingString2.setId(objectContainingString1.getId());
        assertThat(objectContainingString1).isEqualTo(objectContainingString2);
        objectContainingString2.setId(2L);
        assertThat(objectContainingString1).isNotEqualTo(objectContainingString2);
        objectContainingString1.setId(null);
        assertThat(objectContainingString1).isNotEqualTo(objectContainingString2);
    }
}
