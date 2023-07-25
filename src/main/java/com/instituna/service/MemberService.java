package com.instituna.service;

import com.instituna.service.dto.MemberDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.instituna.domain.Member}.
 */
public interface MemberService {
    /**
     * Save a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MemberDTO> save(MemberDTO memberDTO);

    /**
     * Updates a member.
     *
     * @param memberDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MemberDTO> update(MemberDTO memberDTO);

    /**
     * Partially updates a member.
     *
     * @param memberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MemberDTO> partialUpdate(MemberDTO memberDTO);

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MemberDTO> findAll(Pageable pageable);

    /**
     * Returns the number of members available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" member.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MemberDTO> findOne(Long id);

    /**
     * Delete the "id" member.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
