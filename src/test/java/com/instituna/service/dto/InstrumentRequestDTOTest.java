package com.instituna.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstrumentRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstrumentRequestDTO.class);
        InstrumentRequestDTO instrumentRequestDTO1 = new InstrumentRequestDTO();
        instrumentRequestDTO1.setId(1L);
        InstrumentRequestDTO instrumentRequestDTO2 = new InstrumentRequestDTO();
        assertThat(instrumentRequestDTO1).isNotEqualTo(instrumentRequestDTO2);
        instrumentRequestDTO2.setId(instrumentRequestDTO1.getId());
        assertThat(instrumentRequestDTO1).isEqualTo(instrumentRequestDTO2);
        instrumentRequestDTO2.setId(2L);
        assertThat(instrumentRequestDTO1).isNotEqualTo(instrumentRequestDTO2);
        instrumentRequestDTO1.setId(null);
        assertThat(instrumentRequestDTO1).isNotEqualTo(instrumentRequestDTO2);
    }
}
