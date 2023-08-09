package com.instituna.repository;

import com.instituna.domain.InstrumentRequest;
import com.instituna.repository.rowmapper.InstrumentRequestRowMapper;
import com.instituna.repository.rowmapper.InstrumentRowMapper;
import com.instituna.repository.rowmapper.MemberRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the InstrumentRequest entity.
 */
@SuppressWarnings("unused")
class InstrumentRequestRepositoryInternalImpl
    extends SimpleR2dbcRepository<InstrumentRequest, Long>
    implements InstrumentRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InstrumentRowMapper instrumentMapper;
    private final MemberRowMapper memberMapper;
    private final InstrumentRequestRowMapper instrumentrequestMapper;

    private static final Table entityTable = Table.aliased("instrument_request", EntityManager.ENTITY_ALIAS);
    private static final Table instrumentTable = Table.aliased("instrument", "instrument");
    private static final Table memberTable = Table.aliased("member", "e_member");

    public InstrumentRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InstrumentRowMapper instrumentMapper,
        MemberRowMapper memberMapper,
        InstrumentRequestRowMapper instrumentrequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InstrumentRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.instrumentMapper = instrumentMapper;
        this.memberMapper = memberMapper;
        this.instrumentrequestMapper = instrumentrequestMapper;
    }

    @Override
    public Flux<InstrumentRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InstrumentRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InstrumentRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InstrumentSqlHelper.getColumns(instrumentTable, "instrument"));
        columns.addAll(MemberSqlHelper.getColumns(memberTable, "member"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(instrumentTable)
            .on(Column.create("instrument_id", entityTable))
            .equals(Column.create("id", instrumentTable))
            .leftOuterJoin(memberTable)
            .on(Column.create("member_id", entityTable))
            .equals(Column.create("id", memberTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InstrumentRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InstrumentRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InstrumentRequest> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<InstrumentRequest> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<InstrumentRequest> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<InstrumentRequest> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private InstrumentRequest process(Row row, RowMetadata metadata) {
        InstrumentRequest entity = instrumentrequestMapper.apply(row, "e");
        entity.setInstrument(instrumentMapper.apply(row, "instrument"));
        entity.setMember(memberMapper.apply(row, "member"));
        return entity;
    }

    @Override
    public <S extends InstrumentRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
