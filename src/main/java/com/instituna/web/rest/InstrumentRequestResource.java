package com.instituna.web.rest;

import com.instituna.repository.InstrumentRequestRepository;
import com.instituna.service.InstrumentRequestService;
import com.instituna.service.dto.InstrumentRequestDTO;
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
 * REST controller for managing {@link com.instituna.domain.InstrumentRequest}.
 */
@RestController
@RequestMapping("/api")
public class InstrumentRequestResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentRequestResource.class);

    private static final String ENTITY_NAME = "instrumentRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentRequestService instrumentRequestService;

    private final InstrumentRequestRepository instrumentRequestRepository;

    public InstrumentRequestResource(
        InstrumentRequestService instrumentRequestService,
        InstrumentRequestRepository instrumentRequestRepository
    ) {
        this.instrumentRequestService = instrumentRequestService;
        this.instrumentRequestRepository = instrumentRequestRepository;
    }

    /**
     * {@code POST  /instrument-requests} : Create a new instrumentRequest.
     *
     * @param instrumentRequestDTO the instrumentRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrumentRequestDTO, or with status {@code 400 (Bad Request)} if the instrumentRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instrument-requests")
    public Mono<ResponseEntity<InstrumentRequestDTO>> createInstrumentRequest(
        @Valid @RequestBody InstrumentRequestDTO instrumentRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InstrumentRequest : {}", instrumentRequestDTO);
        if (instrumentRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new instrumentRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return instrumentRequestService
            .save(instrumentRequestDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/instrument-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /instrument-requests/:id} : Updates an existing instrumentRequest.
     *
     * @param id the id of the instrumentRequestDTO to save.
     * @param instrumentRequestDTO the instrumentRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrumentRequestDTO,
     * or with status {@code 400 (Bad Request)} if the instrumentRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instrumentRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instrument-requests/{id}")
    public Mono<ResponseEntity<InstrumentRequestDTO>> updateInstrumentRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstrumentRequestDTO instrumentRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InstrumentRequest : {}, {}", id, instrumentRequestDTO);
        if (instrumentRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrumentRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instrumentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return instrumentRequestService
                    .update(instrumentRequestDTO)
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
     * {@code PATCH  /instrument-requests/:id} : Partial updates given fields of an existing instrumentRequest, field will ignore if it is null
     *
     * @param id the id of the instrumentRequestDTO to save.
     * @param instrumentRequestDTO the instrumentRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrumentRequestDTO,
     * or with status {@code 400 (Bad Request)} if the instrumentRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the instrumentRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the instrumentRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instrument-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InstrumentRequestDTO>> partialUpdateInstrumentRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstrumentRequestDTO instrumentRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InstrumentRequest partially : {}, {}", id, instrumentRequestDTO);
        if (instrumentRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrumentRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instrumentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InstrumentRequestDTO> result = instrumentRequestService.partialUpdate(instrumentRequestDTO);

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
     * {@code GET  /instrument-requests} : get all the instrumentRequests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instrumentRequests in body.
     */
    @GetMapping(value = "/instrument-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InstrumentRequestDTO>>> getAllInstrumentRequests(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of InstrumentRequests");
        return instrumentRequestService
            .countAll()
            .zipWith(instrumentRequestService.findAll(pageable).collectList())
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
     * {@code GET  /instrument-requests/:id} : get the "id" instrumentRequest.
     *
     * @param id the id of the instrumentRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrumentRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instrument-requests/{id}")
    public Mono<ResponseEntity<InstrumentRequestDTO>> getInstrumentRequest(@PathVariable Long id) {
        log.debug("REST request to get InstrumentRequest : {}", id);
        Mono<InstrumentRequestDTO> instrumentRequestDTO = instrumentRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instrumentRequestDTO);
    }

    /**
     * {@code DELETE  /instrument-requests/:id} : delete the "id" instrumentRequest.
     *
     * @param id the id of the instrumentRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instrument-requests/{id}")
    public Mono<ResponseEntity<Void>> deleteInstrumentRequest(@PathVariable Long id) {
        log.debug("REST request to delete InstrumentRequest : {}", id);
        return instrumentRequestService
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
