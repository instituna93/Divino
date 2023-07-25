package com.instituna.repository;

import com.instituna.domain.MemberTag;
import com.instituna.repository.rowmapper.MemberRowMapper;
import com.instituna.repository.rowmapper.MemberTagRowMapper;
import com.instituna.repository.rowmapper.TagRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MemberTag entity.
 */
@SuppressWarnings("unused")
class MemberTagRepositoryInternalImpl extends SimpleR2dbcRepository<MemberTag, Long> implements MemberTagRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TagRowMapper tagMapper;
    private final MemberRowMapper memberMapper;
    private final MemberTagRowMapper membertagMapper;

    private static final Table entityTable = Table.aliased("member_tag", EntityManager.ENTITY_ALIAS);
    private static final Table tagTable = Table.aliased("tag", "tag");
    private static final Table memberTable = Table.aliased("member", "e_member");

    public MemberTagRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TagRowMapper tagMapper,
        MemberRowMapper memberMapper,
        MemberTagRowMapper membertagMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MemberTag.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tagMapper = tagMapper;
        this.memberMapper = memberMapper;
        this.membertagMapper = membertagMapper;
    }

    @Override
    public Flux<MemberTag> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MemberTag> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MemberTagSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TagSqlHelper.getColumns(tagTable, "tag"));
        columns.addAll(MemberSqlHelper.getColumns(memberTable, "member"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tagTable)
            .on(Column.create("tag_id", entityTable))
            .equals(Column.create("id", tagTable))
            .leftOuterJoin(memberTable)
            .on(Column.create("member_id", entityTable))
            .equals(Column.create("id", memberTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MemberTag.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MemberTag> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MemberTag> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<MemberTag> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<MemberTag> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<MemberTag> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private MemberTag process(Row row, RowMetadata metadata) {
        MemberTag entity = membertagMapper.apply(row, "e");
        entity.setTag(tagMapper.apply(row, "tag"));
        entity.setMember(memberMapper.apply(row, "member"));
        return entity;
    }

    @Override
    public <S extends MemberTag> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
