package com.instituna.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.instituna.IntegrationTest;
import com.instituna.domain.Instrument;
import com.instituna.domain.InstrumentRequest;
import com.instituna.domain.Member;
import com.instituna.repository.EntityManager;
import com.instituna.repository.InstrumentRequestRepository;
import com.instituna.service.InstrumentRequestService;
import com.instituna.service.dto.InstrumentRequestDTO;
import com.instituna.service.mapper.InstrumentRequestMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link InstrumentRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstrumentRequestResourceIT {

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_DELETED_BY = 1L;
    private static final Long UPDATED_DELETED_BY = 2L;

    private static final Instant DEFAULT_DELETED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RETURNED = false;
    private static final Boolean UPDATED_IS_RETURNED = true;

    private static final String ENTITY_API_URL = "/api/instrument-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstrumentRequestRepository instrumentRequestRepository;

    @Mock
    private InstrumentRequestRepository instrumentRequestRepositoryMock;

    @Autowired
    private InstrumentRequestMapper instrumentRequestMapper;

    @Mock
    private InstrumentRequestService instrumentRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private InstrumentRequest instrumentRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstrumentRequest createEntity(EntityManager em) {
        InstrumentRequest instrumentRequest = new InstrumentRequest()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON)
            .description(DEFAULT_DESCRIPTION)
            .isReturned(DEFAULT_IS_RETURNED);
        // Add required entity
        Instrument instrument;
        instrument = em.insert(InstrumentResourceIT.createEntity(em)).block();
        instrumentRequest.setInstrument(instrument);
        // Add required entity
        Member member;
        member = em.insert(MemberResourceIT.createEntity(em)).block();
        instrumentRequest.setMember(member);
        return instrumentRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstrumentRequest createUpdatedEntity(EntityManager em) {
        InstrumentRequest instrumentRequest = new InstrumentRequest()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .isReturned(UPDATED_IS_RETURNED);
        // Add required entity
        Instrument instrument;
        instrument = em.insert(InstrumentResourceIT.createUpdatedEntity(em)).block();
        instrumentRequest.setInstrument(instrument);
        // Add required entity
        Member member;
        member = em.insert(MemberResourceIT.createUpdatedEntity(em)).block();
        instrumentRequest.setMember(member);
        return instrumentRequest;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(InstrumentRequest.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        InstrumentResourceIT.deleteEntities(em);
        MemberResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        instrumentRequest = createEntity(em);
    }

    @Test
    void createInstrumentRequest() throws Exception {
        int databaseSizeBeforeCreate = instrumentRequestRepository.findAll().collectList().block().size();
        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeCreate + 1);
        InstrumentRequest testInstrumentRequest = instrumentRequestList.get(instrumentRequestList.size() - 1);
        assertThat(testInstrumentRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInstrumentRequest.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInstrumentRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInstrumentRequest.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInstrumentRequest.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testInstrumentRequest.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testInstrumentRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstrumentRequest.getIsReturned()).isEqualTo(DEFAULT_IS_RETURNED);
    }

    @Test
    void createInstrumentRequestWithExistingId() throws Exception {
        // Create the InstrumentRequest with an existing ID
        instrumentRequest.setId(1L);
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        int databaseSizeBeforeCreate = instrumentRequestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInstrumentRequests() {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        // Get all the instrumentRequestList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(instrumentRequest.getId().intValue()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY.intValue()))
            .jsonPath("$.[*].createdOn")
            .value(hasItem(DEFAULT_CREATED_ON.toString()))
            .jsonPath("$.[*].updatedBy")
            .value(hasItem(DEFAULT_UPDATED_BY.intValue()))
            .jsonPath("$.[*].updatedOn")
            .value(hasItem(DEFAULT_UPDATED_ON.toString()))
            .jsonPath("$.[*].deletedBy")
            .value(hasItem(DEFAULT_DELETED_BY.intValue()))
            .jsonPath("$.[*].deletedOn")
            .value(hasItem(DEFAULT_DELETED_ON.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].isReturned")
            .value(hasItem(DEFAULT_IS_RETURNED.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInstrumentRequestsWithEagerRelationshipsIsEnabled() {
        when(instrumentRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(instrumentRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInstrumentRequestsWithEagerRelationshipsIsNotEnabled() {
        when(instrumentRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(instrumentRequestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getInstrumentRequest() {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        // Get the instrumentRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, instrumentRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(instrumentRequest.getId().intValue()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY.intValue()))
            .jsonPath("$.createdOn")
            .value(is(DEFAULT_CREATED_ON.toString()))
            .jsonPath("$.updatedBy")
            .value(is(DEFAULT_UPDATED_BY.intValue()))
            .jsonPath("$.updatedOn")
            .value(is(DEFAULT_UPDATED_ON.toString()))
            .jsonPath("$.deletedBy")
            .value(is(DEFAULT_DELETED_BY.intValue()))
            .jsonPath("$.deletedOn")
            .value(is(DEFAULT_DELETED_ON.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.isReturned")
            .value(is(DEFAULT_IS_RETURNED.booleanValue()));
    }

    @Test
    void getNonExistingInstrumentRequest() {
        // Get the instrumentRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstrumentRequest() throws Exception {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();

        // Update the instrumentRequest
        InstrumentRequest updatedInstrumentRequest = instrumentRequestRepository.findById(instrumentRequest.getId()).block();
        updatedInstrumentRequest
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .isReturned(UPDATED_IS_RETURNED);
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(updatedInstrumentRequest);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instrumentRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
        InstrumentRequest testInstrumentRequest = instrumentRequestList.get(instrumentRequestList.size() - 1);
        assertThat(testInstrumentRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInstrumentRequest.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInstrumentRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInstrumentRequest.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInstrumentRequest.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testInstrumentRequest.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testInstrumentRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstrumentRequest.getIsReturned()).isEqualTo(UPDATED_IS_RETURNED);
    }

    @Test
    void putNonExistingInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instrumentRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstrumentRequestWithPatch() throws Exception {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();

        // Update the instrumentRequest using partial update
        InstrumentRequest partialUpdatedInstrumentRequest = new InstrumentRequest();
        partialUpdatedInstrumentRequest.setId(instrumentRequest.getId());

        partialUpdatedInstrumentRequest.createdOn(UPDATED_CREATED_ON).updatedBy(UPDATED_UPDATED_BY).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstrumentRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrumentRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
        InstrumentRequest testInstrumentRequest = instrumentRequestList.get(instrumentRequestList.size() - 1);
        assertThat(testInstrumentRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInstrumentRequest.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInstrumentRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInstrumentRequest.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInstrumentRequest.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testInstrumentRequest.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testInstrumentRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstrumentRequest.getIsReturned()).isEqualTo(DEFAULT_IS_RETURNED);
    }

    @Test
    void fullUpdateInstrumentRequestWithPatch() throws Exception {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();

        // Update the instrumentRequest using partial update
        InstrumentRequest partialUpdatedInstrumentRequest = new InstrumentRequest();
        partialUpdatedInstrumentRequest.setId(instrumentRequest.getId());

        partialUpdatedInstrumentRequest
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .isReturned(UPDATED_IS_RETURNED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstrumentRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrumentRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
        InstrumentRequest testInstrumentRequest = instrumentRequestList.get(instrumentRequestList.size() - 1);
        assertThat(testInstrumentRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInstrumentRequest.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInstrumentRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInstrumentRequest.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInstrumentRequest.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testInstrumentRequest.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testInstrumentRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstrumentRequest.getIsReturned()).isEqualTo(UPDATED_IS_RETURNED);
    }

    @Test
    void patchNonExistingInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, instrumentRequestDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstrumentRequest() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRequestRepository.findAll().collectList().block().size();
        instrumentRequest.setId(count.incrementAndGet());

        // Create the InstrumentRequest
        InstrumentRequestDTO instrumentRequestDTO = instrumentRequestMapper.toDto(instrumentRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentRequestDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InstrumentRequest in the database
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstrumentRequest() {
        // Initialize the database
        instrumentRequestRepository.save(instrumentRequest).block();

        int databaseSizeBeforeDelete = instrumentRequestRepository.findAll().collectList().block().size();

        // Delete the instrumentRequest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, instrumentRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InstrumentRequest> instrumentRequestList = instrumentRequestRepository.findAll().collectList().block();
        assertThat(instrumentRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
