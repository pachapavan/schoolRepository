package com.school.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.school.app.web.rest.TestUtil;

public class ClassNameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassName.class);
        ClassName className1 = new ClassName();
        className1.setId(1L);
        ClassName className2 = new ClassName();
        className2.setId(className1.getId());
        assertThat(className1).isEqualTo(className2);
        className2.setId(2L);
        assertThat(className1).isNotEqualTo(className2);
        className1.setId(null);
        assertThat(className1).isNotEqualTo(className2);
    }
}
