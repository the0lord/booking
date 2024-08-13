package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.places.BookingDTO;
import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.dto.booking.places.PlaceBookingsStatsRequest;
import com.bakaibank.booking.dto.booking.places.UpdateBookingDTO;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.PlaceRepository;
import com.bakaibank.booking.service.BookingService;
import com.bakaibank.booking.specification.PlaceBookingSpecification;
import com.bakaibank.booking.validation.PlaceBookingValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final PlaceRepository placeRepository;

    private final ModelMapper modelMapper;

    private final PlaceBookingValidator placeBookingValidator;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              PlaceRepository placeRepository,
                              ModelMapper modelMapper,
                              PlaceBookingValidator placeBookingValidator) {
        this.bookingRepository = bookingRepository;
        this.placeRepository = placeRepository;
        this.modelMapper = modelMapper;
        this.placeBookingValidator = placeBookingValidator;
    }
    @Override
    public List<BookingDTO> findAllByDate(LocalDate date) {
        List<Booking> bookings = bookingRepository.findAllByBookingDate(date);
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }

    @Override
    public Optional<BookingDTO> findById(Long id) {
        return bookingRepository.findByIdWithFullEmployeeInfo(id)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    public BookingDTO save(@Valid CreateBookingDTO createBookingDTO, BookingUserDetails userDetails) {
        createBookingDTO.setEmployeeId(userDetails.getId());

        Errors errors = new BeanPropertyBindingResult(createBookingDTO, "bookingErrors");
        placeBookingValidator.validate(createBookingDTO, errors);

        if (errors.hasErrors())
            throw new ValidationException(errors);

        Booking booking = bookingRepository.save(modelMapper.map(createBookingDTO, Booking.class));
        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    public BookingDTO update(Long bookingId, @Valid UpdateBookingDTO updateBookingDTO) throws ResponseStatusException {
        Booking booking = bookingRepository.findByIdWithFullEmployeeInfo(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Place place = placeRepository.findByCodeIgnoreCase(updateBookingDTO.getPlaceCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Errors errors = new BeanPropertyBindingResult(updateBookingDTO, "updateBookingErrors");
        placeBookingValidator.validatePlaceIsAvailable(place.getId(), booking.getBookingDate(),
                booking.getEmployee().getId(), errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        booking.setPlace(place);
        return modelMapper.map(bookingRepository.save(booking), BookingDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingDTO> getStats(@Valid PlaceBookingsStatsRequest statsRequest) {
        PlaceBookingSpecification placeBookingSpecification = new PlaceBookingSpecification(statsRequest);

        return bookingRepository.findAll(placeBookingSpecification)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }
}
