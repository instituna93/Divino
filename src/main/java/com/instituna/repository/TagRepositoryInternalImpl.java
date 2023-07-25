package com.instituna.repository;

import com.instituna.domain.Tag;
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
 * Spring Data R2DBC custom repository implementation for the Tag entity.
 */
@SuppressWarnings("unused")
class TagRepositoryInternalImpl extends SimpleR2dbcRepository<Tag, Long> implements TagRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TagTypeRowMapper tagtypeMapper;
    private final TagRowMapper tagMapper;

    private static final Table entityTable = Table.aliased("tag", EntityManager.ENTITY_ALIAS);
    private static final Table tagTypeTable = Table.aliased("tag_type", "tagType");

    public TagRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TagTypeRowMapper tagtypeMapper,
        TagRowMapper tagMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Tag.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tagtypeMapper = tagtypeMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public Flux<Tag> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Tag> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TagSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TagTypeSqlHelper.getColumns(tagTypeTable, "tagType"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tagTypeTable)
            .on(Column.create("tag_type_id", entityTable))
            .equals(Column.create("id", tagTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Tag.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Tag> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Tag> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Tag> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Tag> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Tag> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Tag process(Row row, RowMetadata metadata) {
        Tag entity = tagMapper.apply(row, "e");
        entity.setTagType(tagtypeMapper.apply(row, "tagType"));
        return entity;
    }

    @Override
    public <S extends Tag> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
