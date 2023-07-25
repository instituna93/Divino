package com.instituna.repository;

import com.instituna.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Member entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long>, MemberRepositoryInternal {
    Flux<Member> findAllBy(Pageable pageable);

    @Query("SELECT * FROM member entity WHERE entity.user_id = :id")
    Flux<Member> findByUser(Long id);

    @Query("SELECT * FROM member entity WHERE entity.user_id IS NULL")
    Flux<Member> findAllWhereUserIsNull();

    @Override
    <S extends Member> Mono<S> save(S entity);

    @Override
    Flux<Member> findAll();

    @Override
    Mono<Member> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MemberRepositoryInternal {
    <S extends Member> Mono<S> save(S entity);

    Flux<Member> findAllBy(Pageable pageable);

    Flux<Member> findAll();

    Mono<Member> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Member> findAllBy(Pageable pageable, Criteria criteria);
}
