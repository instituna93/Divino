package com.instituna.service.impl;

import com.instituna.domain.InstrumentRequest;
import com.instituna.repository.InstrumentRequestRepository;
import com.instituna.service.InstrumentRequestService;
import com.instituna.service.UserService;
import com.instituna.service.dto.InstrumentRequestDTO;
import com.instituna.service.mapper.InstrumentRequestMapper;
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
 * Service Implementation for managing {@link InstrumentRequest}.
 */
@Service
@Transactional
public class InstrumentRequestServiceImpl implements InstrumentRequestService {

    private final Logger log = LoggerFactory.getLogger(InstrumentRequestServiceImpl.class);

    private final InstrumentRequestRepository instrumentRequestRepository;

    private final InstrumentRequestMapper instrumentRequestMapper;

    private final UserService userService;

    public InstrumentRequestServiceImpl(
        InstrumentRequestRepository instrumentRequestRepository,
        InstrumentRequestMapper instrumentRequestMapper,
        UserService userService
    ) {
        this.instrumentRequestRepository = instrumentRequestRepository;
        this.instrumentRequestMapper = instrumentRequestMapper;
        this.userService = userService;
    }

    @Override
    public Mono<InstrumentRequestDTO> save(InstrumentRequestDTO instrumentRequestDTO) {
        log.debug("Request to save InstrumentRequest : {}", instrumentRequestDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentRequestDTO.setCreatedBy(user.getId());
                instrumentRequestDTO.setCreatedOn(timeStamp);
                instrumentRequestDTO.setUpdatedBy(user.getId());
                instrumentRequestDTO.setUpdatedOn(timeStamp);

                return instrumentRequestRepository
                    .save(instrumentRequestMapper.toEntity(instrumentRequestDTO))
                    .map(instrumentRequestMapper::toDto);
            });
    }

    @Override
    public Mono<InstrumentRequestDTO> update(InstrumentRequestDTO instrumentRequestDTO) {
        log.debug("Request to update InstrumentRequest : {}", instrumentRequestDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentRequestDTO.setUpdatedBy(user.getId());
                instrumentRequestDTO.setUpdatedOn(timeStamp);

                return instrumentRequestRepository
                    .save(instrumentRequestMapper.toEntity(instrumentRequestDTO))
                    .map(instrumentRequestMapper::toDto);
            });
    }

    @Override
    public Mono<InstrumentRequestDTO> partialUpdate(InstrumentRequestDTO instrumentRequestDTO) {
        log.debug("Request to partially update InstrumentRequest : {}", instrumentRequestDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                instrumentRequestDTO.setUpdatedBy(user.getId());
                instrumentRequestDTO.setUpdatedOn(timeStamp);

                return instrumentRequestRepository
                    .findById(instrumentRequestDTO.getId())
                    .map(existingInstrumentRequest -> {
                        instrumentRequestMapper.partialUpdate(existingInstrumentRequest, instrumentRequestDTO);

                        return existingInstrumentRequest;
                    })
                    .flatMap(instrumentRequestRepository::save)
                    .map(instrumentRequestMapper::toDto);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InstrumentRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstrumentRequests");
        return instrumentRequestRepository.findAllBy(pageable).map(instrumentRequestMapper::toDto);
    }

    public Flux<InstrumentRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return instrumentRequestRepository.findAllWithEagerRelationships(pageable).map(instrumentRequestMapper::toDto);
    }

    public Mono<Long> countAll() {
        return instrumentRequestRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InstrumentRequestDTO> findOne(Long id) {
        log.debug("Request to get InstrumentRequest : {}", id);
        return instrumentRequestRepository.findOneWithEagerRelationships(id).map(instrumentRequestMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete InstrumentRequest : {}", id);
        return instrumentRequestRepository.deleteById(id);
    }
}
