package com.school.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.school.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TabelValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TabelValues.class);
        TabelValues tabelValues1 = new TabelValues();
        tabelValues1.setId(1L);
        TabelValues tabelValues2 = new TabelValues();
        tabelValues2.setId(tabelValues1.getId());
        assertThat(tabelValues1).isEqualTo(tabelValues2);
        tabelValues2.setId(2L);
        assertThat(tabelValues1).isNotEqualTo(tabelValues2);
        tabelValues1.setId(null);
        assertThat(tabelValues1).isNotEqualTo(tabelValues2);
    }
}
