package com.bakaibank.booking.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.places.BookingDTO;
import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.dto.booking.places.PlaceBookingsStatsRequest;
import com.bakaibank.booking.dto.booking.places.UpdateBookingDTO;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BookingService {
    List<BookingDTO> findAllByDate(LocalDate date);
    Optional<BookingDTO> findById(Long id);
    BookingDTO save(@Valid CreateBookingDTO createBookingDTO, BookingUserDetails userDetails);
    BookingDTO update(Long bookingId, @Valid UpdateBookingDTO updateBookingDTO) throws ResponseStatusException;
    void deleteById(Long id);
    List<BookingDTO> getStats(@Valid PlaceBookingsStatsRequest statsRequest);
}
