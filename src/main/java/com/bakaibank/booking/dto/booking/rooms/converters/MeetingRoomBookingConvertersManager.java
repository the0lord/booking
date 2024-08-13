package com.bakaibank.booking.dto.booking.rooms.converters;

import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.MeetingRoom;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeetingRoomBookingConvertersManager {
    private final EmployeeRepository employeeRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    public static Converter<MeetingRoomBooking, MeetingRoomBookingDTO> meetingRoomBookingToMeetingRoomBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public MeetingRoomBookingDTO convert(MappingContext<MeetingRoomBooking, MeetingRoomBookingDTO> mappingContext) {
                MeetingRoomBooking meetingRoomBooking = mappingContext.getSource();
                return new MeetingRoomBookingDTO(
                        meetingRoomBooking.getId(),
                        meetingRoomBooking.getMeetingRoom().getCode(),
                        EmployeeConvertersManager.mapEmployeeToEmployeeDTO(meetingRoomBooking.getEmployee()),
                        meetingRoomBooking.getTopic(),
                        meetingRoomBooking.getDate(),
                        meetingRoomBooking.getStartTime(),
                        meetingRoomBooking.getEndTime(),
                        meetingRoomBooking.getParticipants().stream()
                                .map(participant -> new MeetingRoomBookingDTO.ParticipantDTO(
                                        participant.getUsername(),
                                        participant.getFirstName(),
                                        participant.getLastName(),
                                        participant.getMiddleName()
                                ))
                                .toList()
                );
            }
        };
    }

    public Converter<CreateMeetingRoomBookingDTO, MeetingRoomBooking> createMeetingRoomBookingDTOToMeetingRoomBookingConverter() {
        return new Converter<>() {
            @Override
            public MeetingRoomBooking convert(MappingContext<CreateMeetingRoomBookingDTO, MeetingRoomBooking> mappingContext) {
                CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO = mappingContext.getSource();

                Employee employee = employeeRepository
                        .findByIdWithPositionAndTeam(createMeetingRoomBookingDTO.getEmployeeId())
                        .orElse(null);
                MeetingRoom meetingRoom = meetingRoomRepository
                        .findById(createMeetingRoomBookingDTO.getMeetingRoomId())
                        .orElse(null);
                List<Employee> participants = employeeRepository.findAllByUsernameIn(createMeetingRoomBookingDTO.getParticipants());

                return new MeetingRoomBooking(
                        meetingRoom,
                        employee,
                        createMeetingRoomBookingDTO.getTopic(),
                        createMeetingRoomBookingDTO.getDate(),
                        createMeetingRoomBookingDTO.getStartTime(),
                        createMeetingRoomBookingDTO.getEndTime(),
                        participants
                );
            }
        };
    }
}
