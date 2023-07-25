package com.instituna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberTag.class);
        MemberTag memberTag1 = new MemberTag();
        memberTag1.setId(1L);
        MemberTag memberTag2 = new MemberTag();
        memberTag2.setId(memberTag1.getId());
        assertThat(memberTag1).isEqualTo(memberTag2);
        memberTag2.setId(2L);
        assertThat(memberTag1).isNotEqualTo(memberTag2);
        memberTag1.setId(null);
        assertThat(memberTag1).isNotEqualTo(memberTag2);
    }
}
