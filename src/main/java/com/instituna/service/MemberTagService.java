package com.instituna.service;

import com.instituna.service.dto.MemberTagDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.instituna.domain.MemberTag}.
 */
public interface MemberTagService {
    /**
     * Save a memberTag.
     *
     * @param memberTagDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MemberTagDTO> save(MemberTagDTO memberTagDTO);

    /**
     * Updates a memberTag.
     *
     * @param memberTagDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MemberTagDTO> update(MemberTagDTO memberTagDTO);

    /**
     * Partially updates a memberTag.
     *
     * @param memberTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MemberTagDTO> partialUpdate(MemberTagDTO memberTagDTO);

    /**
     * Get all the memberTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MemberTagDTO> findAll(Pageable pageable);

    /**
     * Get all the memberTags with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MemberTagDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of memberTags available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" memberTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MemberTagDTO> findOne(Long id);

    /**
     * Delete the "id" memberTag.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
