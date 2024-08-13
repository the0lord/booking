package com.bakaibank.booking.core.security.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MeetingRoomBookingSecurityService {
    private final MeetingRoomBookingRepository meetingRoomBookingRepository;

    public MeetingRoomBookingSecurityService(MeetingRoomBookingRepository meetingRoomBookingRepository) {
        this.meetingRoomBookingRepository = meetingRoomBookingRepository;
    }

    public boolean canUpdateMeetingRoomBooking(Long id, BookingUserDetails bookingUserDetails) {
        if(bookingUserDetails == null || id == null) return false;

        String meetingRoomBookingOwner = meetingRoomBookingRepository
                .findBookingCreatorByBookingId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return meetingRoomBookingOwner.equalsIgnoreCase(bookingUserDetails.getUsername());
    }

    public boolean canDeleteMeetingRoomBooking(Long id, BookingUserDetails bookingUserDetails) {
        return canUpdateMeetingRoomBooking(id, bookingUserDetails);
    }
}
