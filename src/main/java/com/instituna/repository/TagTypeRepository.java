package com.instituna.repository;

import com.instituna.domain.TagType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TagType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagTypeRepository extends ReactiveCrudRepository<TagType, Long>, TagTypeRepositoryInternal {
    Flux<TagType> findAllBy(Pageable pageable);

    @Override
    Mono<TagType> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TagType> findAllWithEagerRelationships();

    @Override
    Flux<TagType> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM tag_type entity WHERE entity.default_tag_id = :id")
    Flux<TagType> findByDefaultTag(Long id);

    @Query("SELECT * FROM tag_type entity WHERE entity.default_tag_id IS NULL")
    Flux<TagType> findAllWhereDefaultTagIsNull();

    @Override
    <S extends TagType> Mono<S> save(S entity);

    @Override
    Flux<TagType> findAll();

    @Override
    Mono<TagType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TagTypeRepositoryInternal {
    <S extends TagType> Mono<S> save(S entity);

    Flux<TagType> findAllBy(Pageable pageable);

    Flux<TagType> findAll();

    Mono<TagType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TagType> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TagType> findOneWithEagerRelationships(Long id);

    Flux<TagType> findAllWithEagerRelationships();

    Flux<TagType> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
