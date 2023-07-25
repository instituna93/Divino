package com.instituna.service.mapper;

import com.instituna.domain.Member;
import com.instituna.domain.User;
import com.instituna.service.dto.MemberDTO;
import com.instituna.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    MemberDTO toDto(Member s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
