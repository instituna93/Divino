package com.instituna.web.rest;

import com.instituna.repository.TagTypeRepository;
import com.instituna.service.TagTypeService;
import com.instituna.service.dto.TagTypeDTO;
import com.instituna.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.instituna.domain.TagType}.
 */
@RestController
@RequestMapping("/api")
public class TagTypeResource {

    private final Logger log = LoggerFactory.getLogger(TagTypeResource.class);

    private static final String ENTITY_NAME = "tagType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagTypeService tagTypeService;

    private final TagTypeRepository tagTypeRepository;

    public TagTypeResource(TagTypeService tagTypeService, TagTypeRepository tagTypeRepository) {
        this.tagTypeService = tagTypeService;
        this.tagTypeRepository = tagTypeRepository;
    }

    /**
     * {@code POST  /tag-types} : Create a new tagType.
     *
     * @param tagTypeDTO the tagTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagTypeDTO, or with status {@code 400 (Bad Request)} if the tagType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tag-types")
    public Mono<ResponseEntity<TagTypeDTO>> createTagType(@Valid @RequestBody TagTypeDTO tagTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TagType : {}", tagTypeDTO);
        if (tagTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tagTypeService
            .save(tagTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tag-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tag-types/:id} : Updates an existing tagType.
     *
     * @param id the id of the tagTypeDTO to save.
     * @param tagTypeDTO the tagTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagTypeDTO,
     * or with status {@code 400 (Bad Request)} if the tagTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tag-types/{id}")
    public Mono<ResponseEntity<TagTypeDTO>> updateTagType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TagTypeDTO tagTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TagType : {}, {}", id, tagTypeDTO);
        if (tagTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tagTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tagTypeService
                    .update(tagTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /tag-types/:id} : Partial updates given fields of an existing tagType, field will ignore if it is null
     *
     * @param id the id of the tagTypeDTO to save.
     * @param tagTypeDTO the tagTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagTypeDTO,
     * or with status {@code 400 (Bad Request)} if the tagTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tag-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TagTypeDTO>> partialUpdateTagType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TagTypeDTO tagTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TagType partially : {}, {}", id, tagTypeDTO);
        if (tagTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tagTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TagTypeDTO> result = tagTypeService.partialUpdate(tagTypeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /tag-types} : get all the tagTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagTypes in body.
     */
    @GetMapping(value = "/tag-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TagTypeDTO>>> getAllTagTypes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of TagTypes");
        return tagTypeService
            .countAll()
            .zipWith(tagTypeService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /tag-types/:id} : get the "id" tagType.
     *
     * @param id the id of the tagTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tag-types/{id}")
    public Mono<ResponseEntity<TagTypeDTO>> getTagType(@PathVariable Long id) {
        log.debug("REST request to get TagType : {}", id);
        Mono<TagTypeDTO> tagTypeDTO = tagTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagTypeDTO);
    }

    /**
     * {@code DELETE  /tag-types/:id} : delete the "id" tagType.
     *
     * @param id the id of the tagTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tag-types/{id}")
    public Mono<ResponseEntity<Void>> deleteTagType(@PathVariable Long id) {
        log.debug("REST request to delete TagType : {}", id);
        return tagTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
