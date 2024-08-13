package com.bakaibank.booking.dto.booking.rooms;

import com.bakaibank.booking.dto.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomBookingDTO {
    private Long id;
    private String meetingRoom;
    private EmployeeDTO employee;
    private String topic;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<ParticipantDTO> participants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantDTO {
        private String username;
        private String firstName;
        private String lastName;
        private String middleName;
    }
}
