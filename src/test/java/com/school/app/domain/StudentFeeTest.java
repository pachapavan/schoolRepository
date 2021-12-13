package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentFeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentFee.class);
        StudentFee studentFee1 = new StudentFee();
        studentFee1.setId(1L);
        StudentFee studentFee2 = new StudentFee();
        studentFee2.setId(studentFee1.getId());
        assertThat(studentFee1).isEqualTo(studentFee2);
        studentFee2.setId(2L);
        assertThat(studentFee1).isNotEqualTo(studentFee2);
        studentFee1.setId(null);
        assertThat(studentFee1).isNotEqualTo(studentFee2);
    }
}
