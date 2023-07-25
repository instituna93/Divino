package com.instituna.service.mapper;

import com.instituna.domain.Tag;
import com.instituna.domain.TagType;
import com.instituna.service.dto.TagDTO;
import com.instituna.service.dto.TagTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "tagType", source = "tagType", qualifiedByName = "tagTypeDescription")
    TagDTO toDto(Tag s);

    @Named("tagTypeDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    TagTypeDTO toDtoTagTypeDescription(TagType tagType);
}
