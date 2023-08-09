package com.instituna.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.instituna.IntegrationTest;
import com.instituna.domain.Member;
import com.instituna.repository.EntityManager;
import com.instituna.repository.MemberRepository;
import com.instituna.service.MemberService;
import com.instituna.service.dto.MemberDTO;
import com.instituna.service.mapper.MemberMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MemberResourceIT {

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

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Autowired
    private MemberMapper memberMapper;

    @Mock
    private MemberService memberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Member member;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON)
            .nickname(DEFAULT_NICKNAME)
            .birthday(DEFAULT_BIRTHDAY);
        return member;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .nickname(UPDATED_NICKNAME)
            .birthday(UPDATED_BIRTHDAY);
        return member;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Member.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        member = createEntity(em);
    }

    @Test
    void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().collectList().block().size();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMember.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testMember.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMember.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testMember.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMember.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testMember.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testMember.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        int databaseSizeBeforeCreate = memberRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMembers() {
        // Initialize the database
        memberRepository.save(member).block();

        // Get all the memberList
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
            .value(hasItem(member.getId().intValue()))
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
            .jsonPath("$.[*].nickname")
            .value(hasItem(DEFAULT_NICKNAME))
            .jsonPath("$.[*].birthday")
            .value(hasItem(DEFAULT_BIRTHDAY.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsEnabled() {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(memberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsNotEnabled() {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(memberRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMember() {
        // Initialize the database
        memberRepository.save(member).block();

        // Get the member
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, member.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(member.getId().intValue()))
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
            .jsonPath("$.nickname")
            .value(is(DEFAULT_NICKNAME))
            .jsonPath("$.birthday")
            .value(is(DEFAULT_BIRTHDAY.toString()));
    }

    @Test
    void getNonExistingMember() {
        // Get the member
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMember() throws Exception {
        // Initialize the database
        memberRepository.save(member).block();

        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).block();
        updatedMember
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .nickname(UPDATED_NICKNAME)
            .birthday(UPDATED_BIRTHDAY);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memberDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMember.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testMember.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMember.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMember.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMember.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testMember.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testMember.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    void putNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memberDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.save(member).block();

        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember.createdBy(UPDATED_CREATED_BY).updatedOn(UPDATED_UPDATED_ON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMember.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMember.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testMember.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMember.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMember.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMember.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testMember.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testMember.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.save(member).block();

        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .nickname(UPDATED_NICKNAME)
            .birthday(UPDATED_BIRTHDAY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMember.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMember.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testMember.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMember.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testMember.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMember.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testMember.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testMember.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    void patchNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, memberDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().collectList().block().size();
        member.setId(count.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(memberDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMember() {
        // Initialize the database
        memberRepository.save(member).block();

        int databaseSizeBeforeDelete = memberRepository.findAll().collectList().block().size();

        // Delete the member
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, member.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll().collectList().block();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
