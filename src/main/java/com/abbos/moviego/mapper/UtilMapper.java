package com.abbos.moviego.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.math.BigDecimal;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.NULL)
public interface UtilMapper {

    @Named("bigDecimalToFormattedString")
    default String bigDecimalToFormattedString(BigDecimal price) {
        return price != null ? String.format("%.2f", price) : "";
    }

    @Named("enumToString")
    default String enumToString(Enum<?> e) {
        return e != null ? e.name() : null;
    }
}
