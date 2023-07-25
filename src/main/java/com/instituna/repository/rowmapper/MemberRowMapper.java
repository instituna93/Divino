package com.instituna.repository.rowmapper;

import com.instituna.domain.Member;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Member}, with proper type conversions.
 */
@Service
public class MemberRowMapper implements BiFunction<Row, String, Member> {

    private final ColumnConverter converter;

    public MemberRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Member} stored in the database.
     */
    @Override
    public Member apply(Row row, String prefix) {
        Member entity = new Member();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setNickname(converter.fromRow(row, prefix + "_nickname", String.class));
        entity.setBirthday(converter.fromRow(row, prefix + "_birthday", LocalDate.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
