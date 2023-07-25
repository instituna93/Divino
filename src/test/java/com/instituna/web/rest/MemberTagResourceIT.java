package com.instituna.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.instituna.IntegrationTest;
import com.instituna.domain.Member;
import com.instituna.domain.MemberTag;
import com.instituna.domain.Tag;
import com.instituna.repository.EntityManager;
import com.instituna.repository.MemberTagRepository;
import com.instituna.service.MemberTagService;
import com.instituna.service.dto.MemberTagDTO;
import com.instituna.service.mapper.MemberTagMapper;
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
 * Integration tests for the {@link MemberTagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MemberTagResourceIT {

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

    private static final String ENTITY_API_URL = "/api/member-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberTagRepository memberTagRepository;

    @Mock
    private MemberTagRepository memberTagRepositoryMock;

    @Autowired
    private MemberTagMapper memberTagMapper;

    @Mock
    private MemberTagService memberTagServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MemberTag memberTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberTag createEntity(EntityManager em) {
        MemberTag memberTag = new MemberTag()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON);
        // Add required entity
        Tag tag;
        tag = em.insert(TagResourceIT.createEntity(em)).block();
        memberTag.setTag(tag);
        // Add required entity
        Member member;
        member = em.insert(MemberResourceIT.createEntity(em)).block();
        memberTag.setMember(member);
        return memberTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberTag createUpdatedEntity(EntityManager em) {
        MemberTag memberTag = new MemberTag()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON);
        // Add required entity
        Tag tag;
        tag = em.insert(TagResourceIT.createUpdatedEntity(em)).block();
        memberTag.setTag(tag);
        // Add required entity
        Member member;
        member = em.insert(MemberResourceIT.createUpdatedEntity(em)).block();
        memberTag.setMember(member);
        return memberTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MemberTag.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TagResourceIT.deleteEntities(em);
        MemberResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        memberTag = createEntity(em);
    }

    @Test
    void createMemberTag() throws Exception {
        int databaseSizeBeforeCreate = memberTagRepository.findAll().collectList().block().size();
        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeCreate + 1);
        MemberTag testMemberTag = memberTagList.get(memberTagList.size() - 1);
        assertThat(testMemberTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMemberTag.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testMemberTag.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMemberTag.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testMemberTag.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMemberTag.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
    }

    @Test
    void createMemberTagWithExistingId() throws Exception {
        // Create the MemberTag with an existing ID
        memberTag.setId(1L);
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        int databaseSizeBeforeCreate = memberTagRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMemberTags() {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        // Get all the memberTagList
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
            .value(hasItem(memberTag.getId().intValue()))
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
            .value(hasItem(DEFAULT_DELETED_ON.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberTagsWithEagerRelationshipsIsEnabled() {
        when(memberTagServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(memberTagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberTagsWithEagerRelationshipsIsNotEnabled() {
        when(memberTagServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(memberTagRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMemberTag() {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        // Get the memberTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, memberTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(memberTag.getId().intValue()))
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
            .value(is(DEFAULT_DELETED_ON.toString()));
    }

    @Test
    void getNonExistingMemberTag() {
        // Get the memberTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMemberTag() throws Exception {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();

        // Update the memberTag
        MemberTag updatedMemberTag = memberTagRepository.findById(memberTag.getId()).block();
        updatedMemberTag
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON);
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(updatedMemberTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memberTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
        MemberTag testMemberTag = memberTagList.get(memberTagList.size() - 1);
        assertThat(testMemberTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMemberTag.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testMemberTag.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMemberTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMemberTag.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMemberTag.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
    }

    @Test
    void putNonExistingMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memberTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMemberTagWithPatch() throws Exception {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();

        // Update the memberTag using partial update
        MemberTag partialUpdatedMemberTag = new MemberTag();
        partialUpdatedMemberTag.setId(memberTag.getId());

        partialUpdatedMemberTag.createdBy(UPDATED_CREATED_BY).updatedOn(UPDATED_UPDATED_ON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMemberTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
        MemberTag testMemberTag = memberTagList.get(memberTagList.size() - 1);
        assertThat(testMemberTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMemberTag.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testMemberTag.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMemberTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMemberTag.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMemberTag.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
    }

    @Test
    void fullUpdateMemberTagWithPatch() throws Exception {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();

        // Update the memberTag using partial update
        MemberTag partialUpdatedMemberTag = new MemberTag();
        partialUpdatedMemberTag.setId(memberTag.getId());

        partialUpdatedMemberTag
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMemberTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
        MemberTag testMemberTag = memberTagList.get(memberTagList.size() - 1);
        assertThat(testMemberTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMemberTag.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testMemberTag.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMemberTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMemberTag.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMemberTag.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
    }

    @Test
    void patchNonExistingMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, memberTagDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMemberTag() throws Exception {
        int databaseSizeBeforeUpdate = memberTagRepository.findAll().collectList().block().size();
        memberTag.setId(count.incrementAndGet());

        // Create the MemberTag
        MemberTagDTO memberTagDTO = memberTagMapper.toDto(memberTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MemberTag in the database
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMemberTag() {
        // Initialize the database
        memberTagRepository.save(memberTag).block();

        int databaseSizeBeforeDelete = memberTagRepository.findAll().collectList().block().size();

        // Delete the memberTag
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, memberTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MemberTag> memberTagList = memberTagRepository.findAll().collectList().block();
        assertThat(memberTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
