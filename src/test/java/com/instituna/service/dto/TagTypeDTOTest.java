package com.instituna.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.instituna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagTypeDTO.class);
        TagTypeDTO tagTypeDTO1 = new TagTypeDTO();
        tagTypeDTO1.setId(1L);
        TagTypeDTO tagTypeDTO2 = new TagTypeDTO();
        assertThat(tagTypeDTO1).isNotEqualTo(tagTypeDTO2);
        tagTypeDTO2.setId(tagTypeDTO1.getId());
        assertThat(tagTypeDTO1).isEqualTo(tagTypeDTO2);
        tagTypeDTO2.setId(2L);
        assertThat(tagTypeDTO1).isNotEqualTo(tagTypeDTO2);
        tagTypeDTO1.setId(null);
        assertThat(tagTypeDTO1).isNotEqualTo(tagTypeDTO2);
    }
}
