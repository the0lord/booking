package com.bakaibank.booking.dto.position.converters;

import com.bakaibank.booking.dto.position.CreatePositionDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.entity.Position;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class PositionConvertersManager {
    public static Converter<Position, PositionDTO> toPositionDTO = new Converter<>() {
        @Override
        public PositionDTO convert(MappingContext<Position, PositionDTO> mappingContext) {
            Position source = mappingContext.getSource();

            return new PositionDTO(
                    source.getId(),
                    source.getName()
            );
        }
    };

    public static Converter<CreatePositionDTO, Position> toPosition = new Converter<>() {
        @Override
        public Position convert(MappingContext<CreatePositionDTO, Position> mappingContext) {
            CreatePositionDTO source = mappingContext.getSource();

            return new Position(
                    source.getName()
            );
        }
    };

}
