package com.bakaibank.booking.core.security.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlaceBookingSecurityService {
    private final BookingRepository bookingRepository;

    public PlaceBookingSecurityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean canUpdateBooking(Long bookingId, BookingUserDetails bookingUserDetails) {
        if(bookingUserDetails == null || bookingId == null) return false;

        String bookingCreator = bookingRepository
                .findBookingCreatorByBookingId(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return bookingCreator.equalsIgnoreCase(bookingUserDetails.getUsername());
    }

    public boolean canDeleteBooking(Long bookingId, BookingUserDetails bookingUserDetails) {
        return canUpdateBooking(bookingId, bookingUserDetails);
    }

}
