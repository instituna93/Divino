package com.instituna.service.mapper;

import com.instituna.domain.Instrument;
import com.instituna.domain.InstrumentRequest;
import com.instituna.domain.Member;
import com.instituna.service.dto.InstrumentDTO;
import com.instituna.service.dto.InstrumentRequestDTO;
import com.instituna.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstrumentRequest} and its DTO {@link InstrumentRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstrumentRequestMapper extends EntityMapper<InstrumentRequestDTO, InstrumentRequest> {
    @Mapping(target = "instrument", source = "instrument", qualifiedByName = "instrumentName")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberNickname")
    InstrumentRequestDTO toDto(InstrumentRequest s);

    @Named("instrumentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    InstrumentDTO toDtoInstrumentName(Instrument instrument);

    @Named("memberNickname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nickname", source = "nickname")
    MemberDTO toDtoMemberNickname(Member member);
}
