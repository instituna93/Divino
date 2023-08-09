package com.instituna.repository.rowmapper;

import com.instituna.domain.Instrument;
import com.instituna.domain.enumeration.InstrumentType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Instrument}, with proper type conversions.
 */
@Service
public class InstrumentRowMapper implements BiFunction<Row, String, Instrument> {

    private final ColumnConverter converter;

    public InstrumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Instrument} stored in the database.
     */
    @Override
    public Instrument apply(Row row, String prefix) {
        Instrument entity = new Instrument();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", Long.class));
        entity.setCreatedOn(converter.fromRow(row, prefix + "_created_on", Instant.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", Long.class));
        entity.setUpdatedOn(converter.fromRow(row, prefix + "_updated_on", Instant.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", Long.class));
        entity.setDeletedOn(converter.fromRow(row, prefix + "_deleted_on", Instant.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", InstrumentType.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setBoughtDate(converter.fromRow(row, prefix + "_bought_date", LocalDate.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        return entity;
    }
}
