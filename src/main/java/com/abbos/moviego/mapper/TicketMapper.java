package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.TicketResponseDto;
import com.abbos.moviego.entity.Ticket;
import com.abbos.moviego.mapper.base.CollectionMapper;
import com.abbos.moviego.mapper.base.EntityConverter;
import com.abbos.moviego.mapper.base.UtilMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {EventMapper.class, UserMapper.class, UtilMapper.class})
public interface TicketMapper
        extends EntityConverter<Ticket, TicketResponseDto>, CollectionMapper<Ticket, TicketResponseDto> {

    @Override
    @Mapping(target = "price", source = "price", qualifiedByName = "bigDecimalToFormattedString")
    TicketResponseDto toDto(Ticket ticket);
}
