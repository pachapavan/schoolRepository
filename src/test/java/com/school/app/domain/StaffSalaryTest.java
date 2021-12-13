package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaffSalaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffSalary.class);
        StaffSalary staffSalary1 = new StaffSalary();
        staffSalary1.setId(1L);
        StaffSalary staffSalary2 = new StaffSalary();
        staffSalary2.setId(staffSalary1.getId());
        assertThat(staffSalary1).isEqualTo(staffSalary2);
        staffSalary2.setId(2L);
        assertThat(staffSalary1).isNotEqualTo(staffSalary2);
        staffSalary1.setId(null);
        assertThat(staffSalary1).isNotEqualTo(staffSalary2);
    }
}
