package com.instituna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstrumentRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstrumentRequest.class);
        InstrumentRequest instrumentRequest1 = new InstrumentRequest();
        instrumentRequest1.setId(1L);
        InstrumentRequest instrumentRequest2 = new InstrumentRequest();
        instrumentRequest2.setId(instrumentRequest1.getId());
        assertThat(instrumentRequest1).isEqualTo(instrumentRequest2);
        instrumentRequest2.setId(2L);
        assertThat(instrumentRequest1).isNotEqualTo(instrumentRequest2);
        instrumentRequest1.setId(null);
        assertThat(instrumentRequest1).isNotEqualTo(instrumentRequest2);
    }
}
