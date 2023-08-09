package com.instituna.repository;

import com.instituna.domain.Instrument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Instrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRepository extends ReactiveCrudRepository<Instrument, Long>, InstrumentRepositoryInternal {
    Flux<Instrument> findAllBy(Pageable pageable);

    @Override
    <S extends Instrument> Mono<S> save(S entity);

    @Override
    Flux<Instrument> findAll();

    @Override
    Mono<Instrument> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InstrumentRepositoryInternal {
    <S extends Instrument> Mono<S> save(S entity);

    Flux<Instrument> findAllBy(Pageable pageable);

    Flux<Instrument> findAll();

    Mono<Instrument> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Instrument> findAllBy(Pageable pageable, Criteria criteria);
}
