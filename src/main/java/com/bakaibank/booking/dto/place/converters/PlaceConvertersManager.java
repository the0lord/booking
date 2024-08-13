package com.bakaibank.booking.dto.place.converters;

import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.place.PlaceWithBookingDTO;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.entity.Place;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;


public class PlaceConvertersManager {
    public static Converter<Place, PlaceDTO> placeToPlaceDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceDTO convert(MappingContext<Place, PlaceDTO> mappingContext) {
                Place place = mappingContext.getSource();

                return new PlaceDTO(
                        place.getId(),
                        place.getCode()
                );
            }
        };
    }

    public static Converter<Place, PlaceWithBookingDTO> placeToPlaceWithBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceWithBookingDTO convert(MappingContext<Place, PlaceWithBookingDTO> mappingContext) {
                Place place = mappingContext.getSource();

                return new PlaceWithBookingDTO(
                        place.getId(),
                        place.getCode()
                );
            }
        };
    }

    public static Converter<Booking, PlaceWithBookingDTO.NestedBookingDTO> bookingToNestedBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceWithBookingDTO.NestedBookingDTO convert(MappingContext<Booking, PlaceWithBookingDTO.NestedBookingDTO> mappingContext) {
                Booking booking = mappingContext.getSource();

                return new PlaceWithBookingDTO.NestedBookingDTO(
                        booking.getBookingDate(),
                        EmployeeConvertersManager.mapEmployeeToEmployeeDTO(booking.getEmployee())
                );
            }
        };
    }
}
