package com.instituna.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberTagDTO.class);
        MemberTagDTO memberTagDTO1 = new MemberTagDTO();
        memberTagDTO1.setId(1L);
        MemberTagDTO memberTagDTO2 = new MemberTagDTO();
        assertThat(memberTagDTO1).isNotEqualTo(memberTagDTO2);
        memberTagDTO2.setId(memberTagDTO1.getId());
        assertThat(memberTagDTO1).isEqualTo(memberTagDTO2);
        memberTagDTO2.setId(2L);
        assertThat(memberTagDTO1).isNotEqualTo(memberTagDTO2);
        memberTagDTO1.setId(null);
        assertThat(memberTagDTO1).isNotEqualTo(memberTagDTO2);
    }
}
