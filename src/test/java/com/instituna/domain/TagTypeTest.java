package com.instituna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagType.class);
        TagType tagType1 = new TagType();
        tagType1.setId(1L);
        TagType tagType2 = new TagType();
        tagType2.setId(tagType1.getId());
        assertThat(tagType1).isEqualTo(tagType2);
        tagType2.setId(2L);
        assertThat(tagType1).isNotEqualTo(tagType2);
        tagType1.setId(null);
        assertThat(tagType1).isNotEqualTo(tagType2);
    }
}
