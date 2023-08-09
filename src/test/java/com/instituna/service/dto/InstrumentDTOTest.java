package com.instituna.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstrumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstrumentDTO.class);
        InstrumentDTO instrumentDTO1 = new InstrumentDTO();
        instrumentDTO1.setId(1L);
        InstrumentDTO instrumentDTO2 = new InstrumentDTO();
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
        instrumentDTO2.setId(instrumentDTO1.getId());
        assertThat(instrumentDTO1).isEqualTo(instrumentDTO2);
        instrumentDTO2.setId(2L);
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
        instrumentDTO1.setId(null);
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
    }
}
