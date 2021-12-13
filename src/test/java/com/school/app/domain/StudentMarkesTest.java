package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentMarkesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentMarkes.class);
        StudentMarkes studentMarkes1 = new StudentMarkes();
        studentMarkes1.setId(1L);
        StudentMarkes studentMarkes2 = new StudentMarkes();
        studentMarkes2.setId(studentMarkes1.getId());
        assertThat(studentMarkes1).isEqualTo(studentMarkes2);
        studentMarkes2.setId(2L);
        assertThat(studentMarkes1).isNotEqualTo(studentMarkes2);
        studentMarkes1.setId(null);
        assertThat(studentMarkes1).isNotEqualTo(studentMarkes2);
    }
}
