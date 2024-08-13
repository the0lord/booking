package com.bakaibank.booking.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.UpdateMeetingRoomBookingDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MeetingRoomBookingService {
    List<MeetingRoomBookingDTO> findAllByDate(LocalDate date);

    List<MeetingRoomBookingDTO> findAllByMeetingRoomIdAndDate(Long meetingRoomId, LocalDate date);
    Optional<MeetingRoomBookingDTO> findById(Long id);
    MeetingRoomBookingDTO save(@Valid CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO,
                               BookingUserDetails userDetails);
    MeetingRoomBookingDTO update(Long meetingRoomBookingId,
                                 @Valid UpdateMeetingRoomBookingDTO updateMeetingRoomBookingDTO)
            throws ResponseStatusException, ValidationException;
    void deleteById(Long id);

    List<Employee> getEmployeesFromParticipantsUsernamesList(Collection<String> participantUsernames);
}
