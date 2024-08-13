package com.bakaibank.booking.dto.booking.places.converters;

import com.bakaibank.booking.dto.booking.places.BookingDTO;
import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceRepository;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BookingConvertersManager {
    private final EmployeeRepository employeeRepository;

    private final PlaceRepository placeRepository;

    @Autowired
    public BookingConvertersManager(EmployeeRepository employeeRepository,
                                    PlaceRepository placeRepository) {
        this.employeeRepository = employeeRepository;
        this.placeRepository = placeRepository;
    }

    public static Converter<Booking, BookingDTO> bookingToBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public BookingDTO convert(MappingContext<Booking, BookingDTO> mappingContext) {
                Booking booking = mappingContext.getSource();

                return new BookingDTO(
                        booking.getId(),
                        EmployeeConvertersManager.mapEmployeeToEmployeeDTO(booking.getEmployee()),
                        booking.getPlace().getCode(),
                        booking.getBookingDate()
                );
            }
        };
    }

    public Converter<CreateBookingDTO, Booking> createBookingDTOToBookingConverter() {
        return new Converter<>() {
            @Override
            public Booking convert(MappingContext<CreateBookingDTO, Booking> mappingContext) {
                CreateBookingDTO createBookingDTO = mappingContext.getSource();

                Employee employee = employeeRepository.findById(createBookingDTO.getEmployeeId()).orElse(null);
                Place place = placeRepository.findById(createBookingDTO.getPlaceId()).orElse(null);

                return new Booking(employee, place, createBookingDTO.getBookingDate());
            }
        };
    }
}
