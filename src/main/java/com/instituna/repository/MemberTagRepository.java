package com.instituna.repository;

import com.instituna.domain.MemberTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MemberTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberTagRepository extends ReactiveCrudRepository<MemberTag, Long>, MemberTagRepositoryInternal {
    Flux<MemberTag> findAllBy(Pageable pageable);

    @Override
    Mono<MemberTag> findOneWithEagerRelationships(Long id);

    @Override
    Flux<MemberTag> findAllWithEagerRelationships();

    @Override
    Flux<MemberTag> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM member_tag entity WHERE entity.tag_id = :id")
    Flux<MemberTag> findByTag(Long id);

    @Query("SELECT * FROM member_tag entity WHERE entity.tag_id IS NULL")
    Flux<MemberTag> findAllWhereTagIsNull();

    @Query("SELECT * FROM member_tag entity WHERE entity.member_id = :id")
    Flux<MemberTag> findByMember(Long id);

    @Query("SELECT * FROM member_tag entity WHERE entity.member_id IS NULL")
    Flux<MemberTag> findAllWhereMemberIsNull();

    @Override
    <S extends MemberTag> Mono<S> save(S entity);

    @Override
    Flux<MemberTag> findAll();

    @Override
    Mono<MemberTag> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MemberTagRepositoryInternal {
    <S extends MemberTag> Mono<S> save(S entity);

    Flux<MemberTag> findAllBy(Pageable pageable);

    Flux<MemberTag> findAll();

    Mono<MemberTag> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MemberTag> findAllBy(Pageable pageable, Criteria criteria);

    Mono<MemberTag> findOneWithEagerRelationships(Long id);

    Flux<MemberTag> findAllWithEagerRelationships();

    Flux<MemberTag> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
