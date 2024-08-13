package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.meetingroom.CreateMeetingRoomDTO;
import com.bakaibank.booking.dto.meetingroom.MeetingRoomDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface MeetingRoomService {
    List<MeetingRoomDTO> findAll();
    Optional<MeetingRoomDTO> findById(Long id);
    MeetingRoomDTO save(@Valid CreateMeetingRoomDTO createMeetingRoomDTO);
    void deleteById(Long id);
}
