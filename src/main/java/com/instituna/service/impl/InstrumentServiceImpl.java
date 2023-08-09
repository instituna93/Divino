package com.instituna.service.impl;

import com.instituna.domain.Instrument;
import com.instituna.repository.InstrumentRepository;
import com.instituna.service.InstrumentService;
import com.instituna.service.UserService;
import com.instituna.service.dto.InstrumentDTO;
import com.instituna.service.mapper.InstrumentMapper;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Instrument}.
 */
@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

    private final Logger log = LoggerFactory.getLogger(InstrumentServiceImpl.class);

    private final InstrumentRepository instrumentRepository;

    private final InstrumentMapper instrumentMapper;

    private final UserService userService;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper, UserService userService) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
        this.userService = userService;
    }

    @Override
    public Mono<InstrumentDTO> save(InstrumentDTO instrumentDTO) {
        log.debug("Request to save Instrument : {}", instrumentDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentDTO.setCreatedBy(user.getId());
                instrumentDTO.setCreatedOn(timeStamp);
                instrumentDTO.setUpdatedBy(user.getId());
                instrumentDTO.setUpdatedOn(timeStamp);

                return instrumentRepository.save(instrumentMapper.toEntity(instrumentDTO)).map(instrumentMapper::toDto);
            });
    }

    @Override
    public Mono<InstrumentDTO> update(InstrumentDTO instrumentDTO) {
        log.debug("Request to update Instrument : {}", instrumentDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentDTO.setUpdatedBy(user.getId());
                instrumentDTO.setUpdatedOn(timeStamp);

                return instrumentRepository.save(instrumentMapper.toEntity(instrumentDTO)).map(instrumentMapper::toDto);
            });
    }

    @Override
    public Mono<InstrumentDTO> partialUpdate(InstrumentDTO instrumentDTO) {
        log.debug("Request to partially update Instrument : {}", instrumentDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentDTO.setUpdatedBy(user.getId());
                instrumentDTO.setUpdatedOn(timeStamp);

                return instrumentRepository
                    .findById(instrumentDTO.getId())
                    .map(existingInstrument -> {
                        instrumentMapper.partialUpdate(existingInstrument, instrumentDTO);

                        return existingInstrument;
                    })
                    .flatMap(instrumentRepository::save)
                    .map(instrumentMapper::toDto);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InstrumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Instruments");
        return instrumentRepository.findAllBy(pageable).map(instrumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return instrumentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InstrumentDTO> findOne(Long id) {
        log.debug("Request to get Instrument : {}", id);
        return instrumentRepository.findById(id).map(instrumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Instrument : {}", id);
        return instrumentRepository.deleteById(id);
    }
}
