package com.instituna.service.mapper;

import com.instituna.domain.Tag;
import com.instituna.domain.TagType;
import com.instituna.service.dto.TagDTO;
import com.instituna.service.dto.TagTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagType} and its DTO {@link TagTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagTypeMapper extends EntityMapper<TagTypeDTO, TagType> {
    @Mapping(target = "defaultTag", source = "defaultTag", qualifiedByName = "tagDescription")
    TagTypeDTO toDto(TagType s);

    @Named("tagDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    TagDTO toDtoTagDescription(Tag tag);
}
