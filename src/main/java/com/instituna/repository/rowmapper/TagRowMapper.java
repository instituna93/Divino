package com.instituna.repository.rowmapper;

import com.instituna.domain.Tag;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Tag}, with proper type conversions.
 */
@Service
public class TagRowMapper implements BiFunction<Row, String, Tag> {

    private final ColumnConverter converter;

    public TagRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Tag} stored in the database.
     */
    @Override
    public Tag apply(Row row, String prefix) {
        Tag entity = new Tag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setTagTypeId(converter.fromRow(row, prefix + "_tag_type_id", Long.class));
        return entity;
    }
}
