package com.instituna.service.mapper;

import com.instituna.domain.Instrument;
import com.instituna.service.dto.InstrumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Instrument} and its DTO {@link InstrumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstrumentMapper extends EntityMapper<InstrumentDTO, Instrument> {}
