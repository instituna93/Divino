package com.instituna.repository;

import com.instituna.domain.InstrumentRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InstrumentRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRequestRepository extends ReactiveCrudRepository<InstrumentRequest, Long>, InstrumentRequestRepositoryInternal {
    Flux<InstrumentRequest> findAllBy(Pageable pageable);

    @Override
    Mono<InstrumentRequest> findOneWithEagerRelationships(Long id);

    @Override
    Flux<InstrumentRequest> findAllWithEagerRelationships();

    @Override
    Flux<InstrumentRequest> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM instrument_request entity WHERE entity.instrument_id = :id")
    Flux<InstrumentRequest> findByInstrument(Long id);

    @Query("SELECT * FROM instrument_request entity WHERE entity.instrument_id IS NULL")
    Flux<InstrumentRequest> findAllWhereInstrumentIsNull();

    @Query("SELECT * FROM instrument_request entity WHERE entity.member_id = :id")
    Flux<InstrumentRequest> findByMember(Long id);

    @Query("SELECT * FROM instrument_request entity WHERE entity.member_id IS NULL")
    Flux<InstrumentRequest> findAllWhereMemberIsNull();

    @Override
    <S extends InstrumentRequest> Mono<S> save(S entity);

    @Override
    Flux<InstrumentRequest> findAll();

    @Override
    Mono<InstrumentRequest> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InstrumentRequestRepositoryInternal {
    <S extends InstrumentRequest> Mono<S> save(S entity);

    Flux<InstrumentRequest> findAllBy(Pageable pageable);

    Flux<InstrumentRequest> findAll();

    Mono<InstrumentRequest> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InstrumentRequest> findAllBy(Pageable pageable, Criteria criteria);

    Mono<InstrumentRequest> findOneWithEagerRelationships(Long id);

    Flux<InstrumentRequest> findAllWithEagerRelationships();

    Flux<InstrumentRequest> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
