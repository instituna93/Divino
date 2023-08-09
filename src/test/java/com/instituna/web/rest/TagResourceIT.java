package com.instituna.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.instituna.IntegrationTest;
import com.instituna.domain.Tag;
import com.instituna.domain.TagType;
import com.instituna.repository.EntityManager;
import com.instituna.repository.TagRepository;
import com.instituna.service.TagService;
import com.instituna.service.dto.TagDTO;
import com.instituna.service.mapper.TagMapper;
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
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TagResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagRepository tagRepository;

    @Mock
    private TagRepository tagRepositoryMock;

    @Autowired
    private TagMapper tagMapper;

    @Mock
    private TagService tagServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        TagType tagType;
        tagType = em.insert(TagTypeResourceIT.createEntity(em)).block();
        tag.setTagType(tagType);
        return tag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        TagType tagType;
        tagType = em.insert(TagTypeResourceIT.createUpdatedEntity(em)).block();
        tag.setTagType(tagType);
        return tag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Tag.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TagTypeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tag = createEntity(em);
    }

    @Test
    void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().collectList().block().size();
        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTag.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTag.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTag.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testTag.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testTag.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testTag.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        int databaseSizeBeforeCreate = tagRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().collectList().block().size();
        // set the field null
        tag.setDescription(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTags() {
        // Initialize the database
        tagRepository.save(tag).block();

        // Get all the tagList
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
            .value(hasItem(tag.getId().intValue()))
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
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTagsWithEagerRelationshipsIsEnabled() {
        when(tagServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTagsWithEagerRelationshipsIsNotEnabled() {
        when(tagServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(tagRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTag() {
        // Initialize the database
        tagRepository.save(tag).block();

        // Get the tag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tag.getId().intValue()))
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
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingTag() {
        // Get the tag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTag() throws Exception {
        // Initialize the database
        tagRepository.save(tag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).block();
        updatedTag
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTag.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTag.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTag.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTag.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.save(tag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTag.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTag.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTag.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTag.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.save(tag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTag.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTag.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTag.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTag.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTag.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTag() {
        // Initialize the database
        tagRepository.save(tag).block();

        int databaseSizeBeforeDelete = tagRepository.findAll().collectList().block().size();

        // Delete the tag
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
