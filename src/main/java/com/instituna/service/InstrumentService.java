package com.instituna.service;

import com.instituna.service.dto.InstrumentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.instituna.domain.Instrument}.
 */
public interface InstrumentService {
    /**
     * Save a instrument.
     *
     * @param instrumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InstrumentDTO> save(InstrumentDTO instrumentDTO);

    /**
     * Updates a instrument.
     *
     * @param instrumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InstrumentDTO> update(InstrumentDTO instrumentDTO);

    /**
     * Partially updates a instrument.
     *
     * @param instrumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InstrumentDTO> partialUpdate(InstrumentDTO instrumentDTO);

    /**
     * Get all the instruments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InstrumentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of instruments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" instrument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InstrumentDTO> findOne(Long id);

    /**
     * Delete the "id" instrument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
