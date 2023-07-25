package com.instituna.repository.rowmapper;

import com.instituna.domain.TagType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TagType}, with proper type conversions.
 */
@Service
public class TagTypeRowMapper implements BiFunction<Row, String, TagType> {

    private final ColumnConverter converter;

    public TagTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TagType} stored in the database.
     */
    @Override
    public TagType apply(Row row, String prefix) {
        TagType entity = new TagType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setRestricted(converter.fromRow(row, prefix + "_restricted", Boolean.class));
        entity.setDefaultTagId(converter.fromRow(row, prefix + "_default_tag_id", Long.class));
        return entity;
    }
}
