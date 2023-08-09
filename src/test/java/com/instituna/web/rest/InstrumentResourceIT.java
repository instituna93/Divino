package com.instituna.web.rest;

import static com.instituna.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.instituna.IntegrationTest;
import com.instituna.domain.Instrument;
import com.instituna.domain.enumeration.InstrumentType;
import com.instituna.repository.EntityManager;
import com.instituna.repository.InstrumentRepository;
import com.instituna.service.dto.InstrumentDTO;
import com.instituna.service.mapper.InstrumentMapper;
import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link InstrumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstrumentResourceIT {

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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final InstrumentType DEFAULT_TYPE = InstrumentType.Cordas;
    private static final InstrumentType UPDATED_TYPE = InstrumentType.Sopro;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_BOUGHT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BOUGHT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/instruments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentMapper instrumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Instrument instrument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .deletedBy(DEFAULT_DELETED_BY)
            .deletedOn(DEFAULT_DELETED_ON)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .boughtDate(DEFAULT_BOUGHT_DATE)
            .price(DEFAULT_PRICE);
        return instrument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createUpdatedEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .boughtDate(UPDATED_BOUGHT_DATE)
            .price(UPDATED_PRICE);
        return instrument;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Instrument.class).block();
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
        instrument = createEntity(em);
    }

    @Test
    void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().collectList().block().size();
        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInstrument.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInstrument.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInstrument.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInstrument.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testInstrument.getDeletedOn()).isEqualTo(DEFAULT_DELETED_ON);
        assertThat(testInstrument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstrument.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testInstrument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstrument.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testInstrument.getBoughtDate()).isEqualTo(DEFAULT_BOUGHT_DATE);
        assertThat(testInstrument.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    void createInstrumentWithExistingId() throws Exception {
        // Create the Instrument with an existing ID
        instrument.setId(1L);
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        int databaseSizeBeforeCreate = instrumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().collectList().block().size();
        // set the field null
        instrument.setName(null);

        // Create the Instrument, which fails.
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().collectList().block().size();
        // set the field null
        instrument.setType(null);

        // Create the Instrument, which fails.
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().collectList().block().size();
        // set the field null
        instrument.setIsActive(null);

        // Create the Instrument, which fails.
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInstruments() {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        // Get all the instrumentList
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
            .value(hasItem(instrument.getId().intValue()))
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
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].boughtDate")
            .value(hasItem(DEFAULT_BOUGHT_DATE.toString()))
            .jsonPath("$.[*].price")
            .value(hasItem(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getInstrument() {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        // Get the instrument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, instrument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(instrument.getId().intValue()))
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
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.boughtDate")
            .value(is(DEFAULT_BOUGHT_DATE.toString()))
            .jsonPath("$.price")
            .value(is(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getNonExistingInstrument() {
        // Get the instrument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();

        // Update the instrument
        Instrument updatedInstrument = instrumentRepository.findById(instrument.getId()).block();
        updatedInstrument
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .boughtDate(UPDATED_BOUGHT_DATE)
            .price(UPDATED_PRICE);
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(updatedInstrument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instrumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInstrument.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInstrument.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInstrument.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInstrument.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testInstrument.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testInstrument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstrument.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInstrument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstrument.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testInstrument.getBoughtDate()).isEqualTo(UPDATED_BOUGHT_DATE);
        assertThat(testInstrument.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void putNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instrumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstrumentWithPatch() throws Exception {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();

        // Update the instrument using partial update
        Instrument partialUpdatedInstrument = new Instrument();
        partialUpdatedInstrument.setId(instrument.getId());

        partialUpdatedInstrument
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .price(UPDATED_PRICE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstrument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInstrument.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInstrument.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInstrument.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInstrument.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testInstrument.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testInstrument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstrument.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testInstrument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstrument.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testInstrument.getBoughtDate()).isEqualTo(DEFAULT_BOUGHT_DATE);
        assertThat(testInstrument.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void fullUpdateInstrumentWithPatch() throws Exception {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();

        // Update the instrument using partial update
        Instrument partialUpdatedInstrument = new Instrument();
        partialUpdatedInstrument.setId(instrument.getId());

        partialUpdatedInstrument
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .deletedBy(UPDATED_DELETED_BY)
            .deletedOn(UPDATED_DELETED_ON)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .boughtDate(UPDATED_BOUGHT_DATE)
            .price(UPDATED_PRICE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstrument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInstrument.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInstrument.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInstrument.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInstrument.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testInstrument.getDeletedOn()).isEqualTo(UPDATED_DELETED_ON);
        assertThat(testInstrument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstrument.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInstrument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstrument.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testInstrument.getBoughtDate()).isEqualTo(UPDATED_BOUGHT_DATE);
        assertThat(testInstrument.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void patchNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, instrumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().collectList().block().size();
        instrument.setId(count.incrementAndGet());

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instrumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstrument() {
        // Initialize the database
        instrumentRepository.save(instrument).block();

        int databaseSizeBeforeDelete = instrumentRepository.findAll().collectList().block().size();

        // Delete the instrument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, instrument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Instrument> instrumentList = instrumentRepository.findAll().collectList().block();
        assertThat(instrumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
