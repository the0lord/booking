package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.dto.meetingroom.CreateMeetingRoomDTO;
import com.bakaibank.booking.dto.meetingroom.MeetingRoomDTO;
import com.bakaibank.booking.service.MeetingRoomBookingService;
import com.bakaibank.booking.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {
    private final MeetingRoomService meetingRoomService;

    private final MeetingRoomBookingService meetingRoomBookingService;

    @Autowired
    public MeetingRoomController(MeetingRoomService meetingRoomService,
                                 MeetingRoomBookingService meetingRoomBookingService) {
        this.meetingRoomService = meetingRoomService;
        this.meetingRoomBookingService = meetingRoomBookingService;
    }

    @GetMapping
    public List<MeetingRoomDTO> findAllMeetingRooms() {
        return meetingRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoomDTO> findMeetingRoomById(@PathVariable Long id) {
        return meetingRoomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<MeetingRoomBookingDTO>> findBookingsByIdAndDate(@PathVariable Long id,
                                                               @RequestParam("date")
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate date) {
        return meetingRoomService.findById(id)
                .map(meetingRoom -> ResponseEntity.ok(meetingRoomBookingService.findAllByMeetingRoomIdAndDate(id, date)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public MeetingRoomDTO createMeetingRoom(@RequestBody CreateMeetingRoomDTO createMeetingRoomDTO) {
        return meetingRoomService.save(createMeetingRoomDTO);
    }

    @DeleteMapping("/{id}")
    @AdminRoleRequired
    public ResponseEntity<?> deleteMeetingRoomById(@PathVariable Long id) {
        return meetingRoomService.findById(id)
                .map(meetingRoom -> {
                    meetingRoomService.deleteById(id);
                    return ResponseEntity.ok(meetingRoom);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
