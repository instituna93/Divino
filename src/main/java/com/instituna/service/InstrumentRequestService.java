package com.instituna.service;

import com.instituna.service.dto.InstrumentRequestDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.instituna.domain.InstrumentRequest}.
 */
public interface InstrumentRequestService {
    /**
     * Save a instrumentRequest.
     *
     * @param instrumentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InstrumentRequestDTO> save(InstrumentRequestDTO instrumentRequestDTO);

    /**
     * Updates a instrumentRequest.
     *
     * @param instrumentRequestDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InstrumentRequestDTO> update(InstrumentRequestDTO instrumentRequestDTO);

    /**
     * Partially updates a instrumentRequest.
     *
     * @param instrumentRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InstrumentRequestDTO> partialUpdate(InstrumentRequestDTO instrumentRequestDTO);

    /**
     * Get all the instrumentRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InstrumentRequestDTO> findAll(Pageable pageable);

    /**
     * Get all the instrumentRequests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InstrumentRequestDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of instrumentRequests available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" instrumentRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InstrumentRequestDTO> findOne(Long id);

    /**
     * Delete the "id" instrumentRequest.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
