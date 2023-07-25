package com.instituna.web.rest;

import com.instituna.repository.MemberTagRepository;
import com.instituna.service.MemberTagService;
import com.instituna.service.dto.MemberTagDTO;
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
 * REST controller for managing {@link com.instituna.domain.MemberTag}.
 */
@RestController
@RequestMapping("/api")
public class MemberTagResource {

    private final Logger log = LoggerFactory.getLogger(MemberTagResource.class);

    private static final String ENTITY_NAME = "memberTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberTagService memberTagService;

    private final MemberTagRepository memberTagRepository;

    public MemberTagResource(MemberTagService memberTagService, MemberTagRepository memberTagRepository) {
        this.memberTagService = memberTagService;
        this.memberTagRepository = memberTagRepository;
    }

    /**
     * {@code POST  /member-tags} : Create a new memberTag.
     *
     * @param memberTagDTO the memberTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberTagDTO, or with status {@code 400 (Bad Request)} if the memberTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-tags")
    public Mono<ResponseEntity<MemberTagDTO>> createMemberTag(@Valid @RequestBody MemberTagDTO memberTagDTO) throws URISyntaxException {
        log.debug("REST request to save MemberTag : {}", memberTagDTO);
        if (memberTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return memberTagService
            .save(memberTagDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/member-tags/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /member-tags/:id} : Updates an existing memberTag.
     *
     * @param id the id of the memberTagDTO to save.
     * @param memberTagDTO the memberTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberTagDTO,
     * or with status {@code 400 (Bad Request)} if the memberTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-tags/{id}")
    public Mono<ResponseEntity<MemberTagDTO>> updateMemberTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberTagDTO memberTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MemberTag : {}, {}", id, memberTagDTO);
        if (memberTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return memberTagRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return memberTagService
                    .update(memberTagDTO)
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
     * {@code PATCH  /member-tags/:id} : Partial updates given fields of an existing memberTag, field will ignore if it is null
     *
     * @param id the id of the memberTagDTO to save.
     * @param memberTagDTO the memberTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberTagDTO,
     * or with status {@code 400 (Bad Request)} if the memberTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/member-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MemberTagDTO>> partialUpdateMemberTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberTagDTO memberTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MemberTag partially : {}, {}", id, memberTagDTO);
        if (memberTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return memberTagRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MemberTagDTO> result = memberTagService.partialUpdate(memberTagDTO);

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
     * {@code GET  /member-tags} : get all the memberTags.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberTags in body.
     */
    @GetMapping(value = "/member-tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MemberTagDTO>>> getAllMemberTags(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of MemberTags");
        return memberTagService
            .countAll()
            .zipWith(memberTagService.findAll(pageable).collectList())
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
     * {@code GET  /member-tags/:id} : get the "id" memberTag.
     *
     * @param id the id of the memberTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-tags/{id}")
    public Mono<ResponseEntity<MemberTagDTO>> getMemberTag(@PathVariable Long id) {
        log.debug("REST request to get MemberTag : {}", id);
        Mono<MemberTagDTO> memberTagDTO = memberTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberTagDTO);
    }

    /**
     * {@code DELETE  /member-tags/:id} : delete the "id" memberTag.
     *
     * @param id the id of the memberTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-tags/{id}")
    public Mono<ResponseEntity<Void>> deleteMemberTag(@PathVariable Long id) {
        log.debug("REST request to delete MemberTag : {}", id);
        return memberTagService
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
