package com.instituna.repository;

import com.instituna.domain.TagType;
import com.instituna.repository.rowmapper.TagRowMapper;
import com.instituna.repository.rowmapper.TagTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the TagType entity.
 */
@SuppressWarnings("unused")
class TagTypeRepositoryInternalImpl extends SimpleR2dbcRepository<TagType, Long> implements TagTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TagRowMapper tagMapper;
    private final TagTypeRowMapper tagtypeMapper;

    private static final Table entityTable = Table.aliased("tag_type", EntityManager.ENTITY_ALIAS);
    private static final Table defaultTagTable = Table.aliased("tag", "defaultTag");

    public TagTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TagRowMapper tagMapper,
        TagTypeRowMapper tagtypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TagType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tagMapper = tagMapper;
        this.tagtypeMapper = tagtypeMapper;
    }

    @Override
    public Flux<TagType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TagType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TagTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TagSqlHelper.getColumns(defaultTagTable, "defaultTag"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(defaultTagTable)
            .on(Column.create("default_tag_id", entityTable))
            .equals(Column.create("id", defaultTagTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TagType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TagType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TagType> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TagType> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TagType> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TagType> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TagType process(Row row, RowMetadata metadata) {
        TagType entity = tagtypeMapper.apply(row, "e");
        entity.setDefaultTag(tagMapper.apply(row, "defaultTag"));
        return entity;
    }

    @Override
    public <S extends TagType> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
