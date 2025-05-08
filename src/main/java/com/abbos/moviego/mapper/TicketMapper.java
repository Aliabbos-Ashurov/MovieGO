package com.abbos.moviego.mapper;

import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.TicketResponseDto;
import com.abbos.moviego.entity.Event;
import com.abbos.moviego.entity.Ticket;
import com.abbos.moviego.entity.User;
import com.abbos.moviego.mapper.base.CollectionMapper;
import com.abbos.moviego.mapper.base.EntityConverter;
import com.abbos.moviego.mapper.base.UtilMapper;
import org.mapstruct.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {EventMapper.class, UserMapper.class, UtilMapper.class})
public interface TicketMapper
        extends EntityConverter<Ticket, TicketResponseDto>, CollectionMapper<Ticket, TicketResponseDto> {

    @Override
    @Mapping(target = "price", source = "price", qualifiedByName = "bigDecimalToFormattedString")
    TicketResponseDto toDto(Ticket ticket);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "event", source = "event"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "price", source = "event.price")
    })
    Ticket createFrom(TicketCreateDto dto, User user, Event event);
}
