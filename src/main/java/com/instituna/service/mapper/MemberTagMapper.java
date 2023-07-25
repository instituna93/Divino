package com.instituna.service.mapper;

import com.instituna.domain.Member;
import com.instituna.domain.MemberTag;
import com.instituna.domain.Tag;
import com.instituna.service.dto.MemberDTO;
import com.instituna.service.dto.MemberTagDTO;
import com.instituna.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberTag} and its DTO {@link MemberTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberTagMapper extends EntityMapper<MemberTagDTO, MemberTag> {
    @Mapping(target = "tag", source = "tag", qualifiedByName = "tagDescription")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberNickname")
    MemberTagDTO toDto(MemberTag s);

    @Named("tagDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    TagDTO toDtoTagDescription(Tag tag);

    @Named("memberNickname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nickname", source = "nickname")
    MemberDTO toDtoMemberNickname(Member member);
}
