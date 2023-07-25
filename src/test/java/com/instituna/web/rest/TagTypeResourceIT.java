package com.instituna.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.instituna.IntegrationTest;
import com.instituna.domain.TagType;
import com.instituna.repository.EntityManager;
import com.instituna.repository.TagTypeRepository;
import com.instituna.service.TagTypeService;
import com.instituna.service.dto.TagTypeDTO;
import com.instituna.service.mapper.TagTypeMapper;
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
 * Integration tests for the {@link TagTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TagTypeResourceIT {

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

    private static final Boolean DEFAULT_RESTRICTED = false;
    private static final Boolean UPDATED_RESTRICTED = true;

    private static final String ENTITY_API_URL = "/api/tag-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagTypeRepository tagTypeRepository;

    @Mock
    private TagTypeRepository tagTypeRepositoryMock;

    @Autowired
    private TagTypeMapper tagTypeMapper;

    @Mock
    private TagTypeService tagTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TagType tagType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagType createEntity(EntityManager em) {
        TagType tagType = new TagType()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON)
            .description(DEFAULT_DESCRIPTION)
            .restricted(DEFAULT_RESTRICTED);
        return tagType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagType createUpdatedEntity(EntityManager em) {
        TagType tagType = new TagType()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .restricted(UPDATED_RESTRICTED);
        return tagType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TagType.class).block();
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
        tagType = createEntity(em);
    }

    @Test
    void createTagType() throws Exception {
        int databaseSizeBeforeCreate = tagTypeRepository.findAll().collectList().block().size();
        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TagType testTagType = tagTypeList.get(tagTypeList.size() - 1);
        assertThat(testTagType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTagType.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTagType.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTagType.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testTagType.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testTagType.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testTagType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTagType.getRestricted()).isEqualTo(DEFAULT_RESTRICTED);
    }

    @Test
    void createTagTypeWithExistingId() throws Exception {
        // Create the TagType with an existing ID
        tagType.setId(1L);
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        int databaseSizeBeforeCreate = tagTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagTypeRepository.findAll().collectList().block().size();
        // set the field null
        tagType.setDescription(null);

        // Create the TagType, which fails.
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTagTypes() {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        // Get all the tagTypeList
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
            .value(hasItem(tagType.getId().intValue()))
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
            .jsonPath("$.[*].restricted")
            .value(hasItem(DEFAULT_RESTRICTED.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTagTypesWithEagerRelationshipsIsEnabled() {
        when(tagTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tagTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTagTypesWithEagerRelationshipsIsNotEnabled() {
        when(tagTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(tagTypeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTagType() {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        // Get the tagType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tagType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tagType.getId().intValue()))
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
            .jsonPath("$.restricted")
            .value(is(DEFAULT_RESTRICTED.booleanValue()));
    }

    @Test
    void getNonExistingTagType() {
        // Get the tagType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTagType() throws Exception {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();

        // Update the tagType
        TagType updatedTagType = tagTypeRepository.findById(tagType.getId()).block();
        updatedTagType
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .restricted(UPDATED_RESTRICTED);
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(updatedTagType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
        TagType testTagType = tagTypeList.get(tagTypeList.size() - 1);
        assertThat(testTagType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTagType.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTagType.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTagType.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTagType.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTagType.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testTagType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTagType.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
    }

    @Test
    void putNonExistingTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTagTypeWithPatch() throws Exception {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();

        // Update the tagType using partial update
        TagType partialUpdatedTagType = new TagType();
        partialUpdatedTagType.setId(tagType.getId());

        partialUpdatedTagType
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTagType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTagType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
        TagType testTagType = tagTypeList.get(tagTypeList.size() - 1);
        assertThat(testTagType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTagType.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTagType.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTagType.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTagType.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTagType.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testTagType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTagType.getRestricted()).isEqualTo(DEFAULT_RESTRICTED);
    }

    @Test
    void fullUpdateTagTypeWithPatch() throws Exception {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();

        // Update the tagType using partial update
        TagType partialUpdatedTagType = new TagType();
        partialUpdatedTagType.setId(tagType.getId());

        partialUpdatedTagType
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .description(UPDATED_DESCRIPTION)
            .restricted(UPDATED_RESTRICTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTagType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTagType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
        TagType testTagType = tagTypeList.get(tagTypeList.size() - 1);
        assertThat(testTagType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTagType.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTagType.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTagType.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testTagType.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testTagType.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testTagType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTagType.getRestricted()).isEqualTo(UPDATED_RESTRICTED);
    }

    @Test
    void patchNonExistingTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tagTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTagType() throws Exception {
        int databaseSizeBeforeUpdate = tagTypeRepository.findAll().collectList().block().size();
        tagType.setId(count.incrementAndGet());

        // Create the TagType
        TagTypeDTO tagTypeDTO = tagTypeMapper.toDto(tagType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TagType in the database
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTagType() {
        // Initialize the database
        tagTypeRepository.save(tagType).block();

        int databaseSizeBeforeDelete = tagTypeRepository.findAll().collectList().block().size();

        // Delete the tagType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tagType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TagType> tagTypeList = tagTypeRepository.findAll().collectList().block();
        assertThat(tagTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
