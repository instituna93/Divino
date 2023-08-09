package com.instituna.web.rest;

import com.instituna.repository.InstrumentRepository;
import com.instituna.service.InstrumentService;
import com.instituna.service.dto.InstrumentDTO;
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
 * REST controller for managing {@link com.instituna.domain.Instrument}.
 */
@RestController
@RequestMapping("/api")
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);

    private static final String ENTITY_NAME = "instrument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentService instrumentService;

    private final InstrumentRepository instrumentRepository;

    public InstrumentResource(InstrumentService instrumentService, InstrumentRepository instrumentRepository) {
        this.instrumentService = instrumentService;
        this.instrumentRepository = instrumentRepository;
    }

    /**
     * {@code POST  /instruments} : Create a new instrument.
     *
     * @param instrumentDTO the instrumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrumentDTO, or with status {@code 400 (Bad Request)} if the instrument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instruments")
    public Mono<ResponseEntity<InstrumentDTO>> createInstrument(@Valid @RequestBody InstrumentDTO instrumentDTO) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrumentDTO);
        if (instrumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new instrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return instrumentService
            .save(instrumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/instruments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /instruments/:id} : Updates an existing instrument.
     *
     * @param id the id of the instrumentDTO to save.
     * @param instrumentDTO the instrumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrumentDTO,
     * or with status {@code 400 (Bad Request)} if the instrumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instrumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instruments/{id}")
    public Mono<ResponseEntity<InstrumentDTO>> updateInstrument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstrumentDTO instrumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}, {}", id, instrumentDTO);
        if (instrumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instrumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return instrumentService
                    .update(instrumentDTO)
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
     * {@code PATCH  /instruments/:id} : Partial updates given fields of an existing instrument, field will ignore if it is null
     *
     * @param id the id of the instrumentDTO to save.
     * @param instrumentDTO the instrumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrumentDTO,
     * or with status {@code 400 (Bad Request)} if the instrumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the instrumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the instrumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instruments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InstrumentDTO>> partialUpdateInstrument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstrumentDTO instrumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Instrument partially : {}, {}", id, instrumentDTO);
        if (instrumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instrumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InstrumentDTO> result = instrumentService.partialUpdate(instrumentDTO);

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
     * {@code GET  /instruments} : get all the instruments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instruments in body.
     */
    @GetMapping(value = "/instruments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InstrumentDTO>>> getAllInstruments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Instruments");
        return instrumentService
            .countAll()
            .zipWith(instrumentService.findAll(pageable).collectList())
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
     * {@code GET  /instruments/:id} : get the "id" instrument.
     *
     * @param id the id of the instrumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instruments/{id}")
    public Mono<ResponseEntity<InstrumentDTO>> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Mono<InstrumentDTO> instrumentDTO = instrumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instrumentDTO);
    }

    /**
     * {@code DELETE  /instruments/:id} : delete the "id" instrument.
     *
     * @param id the id of the instrumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instruments/{id}")
    public Mono<ResponseEntity<Void>> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        return instrumentService
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
