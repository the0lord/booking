package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.UpdateMeetingRoomBookingDTO;
import com.bakaibank.booking.service.MeetingRoomBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/rooms")
public class MeetingRoomBookingController {
    private final MeetingRoomBookingService meetingRoomBookingService;

    @Autowired
    public MeetingRoomBookingController(MeetingRoomBookingService meetingRoomBookingService) {
        this.meetingRoomBookingService = meetingRoomBookingService;
    }

    @GetMapping
    public List<MeetingRoomBookingDTO> findAllRoomBookings(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return meetingRoomBookingService.findAllByDate(date != null ? date : LocalDate.now());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoomBookingDTO> findRoomBookingById(@PathVariable Long id) {
        return meetingRoomBookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeetingRoomBookingDTO createRoomBooking(@RequestBody CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO,
                                                   @AuthenticationPrincipal BookingUserDetails userDetails) {
        return meetingRoomBookingService.save(createMeetingRoomBookingDTO, userDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole(@authoritiesNameProvider.roleAdmin()) or @meetingRoomBookingSecurityService.canUpdateMeetingRoomBooking(#id, principal)")
    public MeetingRoomBookingDTO updateMeetingRoomBookingById(@PathVariable Long id,
                                                              @RequestBody UpdateMeetingRoomBookingDTO updateMeetingRoomBookingDTO) {
        return meetingRoomBookingService.update(id, updateMeetingRoomBookingDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(@authoritiesNameProvider.roleAdmin()) or @meetingRoomBookingSecurityService.canDeleteMeetingRoomBooking(#id, principal)")
    public ResponseEntity<?> deleteRoomBookingById(@PathVariable Long id) {
        return meetingRoomBookingService.findById(id)
                .map(meetingRoomBooking -> {
                    meetingRoomBookingService.deleteById(id);
                    return ResponseEntity.ok(meetingRoomBooking);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
