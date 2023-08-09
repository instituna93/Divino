package com.instituna.repository.rowmapper;

import com.instituna.domain.InstrumentRequest;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InstrumentRequest}, with proper type conversions.
 */
@Service
public class InstrumentRequestRowMapper implements BiFunction<Row, String, InstrumentRequest> {

    private final ColumnConverter converter;

    public InstrumentRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InstrumentRequest} stored in the database.
     */
    @Override
    public InstrumentRequest apply(Row row, String prefix) {
        InstrumentRequest entity = new InstrumentRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setIsReturned(converter.fromRow(row, prefix + "_is_returned", Boolean.class));
        entity.setInstrumentId(converter.fromRow(row, prefix + "_instrument_id", Long.class));
        entity.setMemberId(converter.fromRow(row, prefix + "_member_id", Long.class));
        return entity;
    }
}
