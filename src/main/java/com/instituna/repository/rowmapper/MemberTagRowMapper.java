package com.instituna.repository.rowmapper;

import com.instituna.domain.MemberTag;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MemberTag}, with proper type conversions.
 */
@Service
public class MemberTagRowMapper implements BiFunction<Row, String, MemberTag> {

    private final ColumnConverter converter;

    public MemberTagRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MemberTag} stored in the database.
     */
    @Override
    public MemberTag apply(Row row, String prefix) {
        MemberTag entity = new MemberTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setTagId(converter.fromRow(row, prefix + "_tag_id", Long.class));
        entity.setMemberId(converter.fromRow(row, prefix + "_member_id", Long.class));
        return entity;
    }
}
