package com.instituna.service;

import com.instituna.service.dto.TagTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.instituna.domain.TagType}.
 */
public interface TagTypeService {
    /**
     * Save a tagType.
     *
     * @param tagTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TagTypeDTO> save(TagTypeDTO tagTypeDTO);

    /**
     * Updates a tagType.
     *
     * @param tagTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TagTypeDTO> update(TagTypeDTO tagTypeDTO);

    /**
     * Partially updates a tagType.
     *
     * @param tagTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TagTypeDTO> partialUpdate(TagTypeDTO tagTypeDTO);

    /**
     * Get all the tagTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TagTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the tagTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TagTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of tagTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tagType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TagTypeDTO> findOne(Long id);

    /**
     * Delete the "id" tagType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
