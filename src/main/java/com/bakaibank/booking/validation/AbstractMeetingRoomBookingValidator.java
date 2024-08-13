package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.rooms.AbstractMeetingRoomBookingDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public abstract class AbstractMeetingRoomBookingValidator extends AbstractBookingValidator {
    protected MeetingRoomBookingRepository meetingRoomBookingRepository;
    protected EmployeeRepository employeeRepository;

    @Value("${booking.rooms.allowed.days-ahead}")
    protected int roomBookingAllowedDaysAhead;

    @Value("${booking.rooms.allowed.start-time}")
    protected String bookingStartTimePattern;

    @Value("${booking.rooms.allowed.end-time}")
    protected String bookingEndTimePattern;

    @Value("${booking.rooms.allowed.min-participants}")
    protected int minParticipantsCount;

    @Value("${booking.rooms.allowed.max-participants}")
    protected int maxParticipantsCount;

    @Value("${booking.rooms.allowed.min-meeting-duration-minutes}")
    protected int minMeetingDurationMinutes;

    public AbstractMeetingRoomBookingValidator(WeekendRepository weekendRepository,
                                               MeetingRoomBookingRepository meetingRoomBookingRepository,
                                               EmployeeRepository employeeRepository) {
        super(weekendRepository);
        this.meetingRoomBookingRepository = meetingRoomBookingRepository;
        this.employeeRepository = employeeRepository;
    }

    protected abstract boolean existConflictingBookingsByDateTime(AbstractMeetingRoomBookingDTO dto, Errors errors);
    protected abstract boolean existConflictingBookingsByParticipants(AbstractMeetingRoomBookingDTO dto, Errors errors);

    protected void validate(AbstractMeetingRoomBookingDTO dto, Errors errors) {
        if(!validateBookingDate(dto.getDate(), roomBookingAllowedDaysAhead, errors)) return;

        if(!validateMeetingTime(dto.getStartTime(), dto.getEndTime(), errors)) return;

        if(!validateParticipantsList(dto.getParticipants(), errors)) return;

        if(existConflictingBookingsByDateTime(dto, errors)) return;

        if(existConflictingBookingsByParticipants(dto, errors)) return;
    }

    protected boolean validateMeetingTime(LocalTime startTime, LocalTime endTime, Errors errors) {
        LocalTime bookingStartTime = parseTimeFromPattern(this.bookingStartTimePattern);
        LocalTime bookingEndTime = parseTimeFromPattern(this.bookingEndTimePattern);

        if(startTime.isBefore(bookingStartTime)) {
            errors.reject("invalidRoomBookingTime",
                    "Встреча в переговорной комнате должна начинаться с " + bookingStartTime);
            return false;
        }

        if(endTime.isAfter(bookingEndTime)) {
            errors.reject("invalidRoomBookingTime",
                    "Встреча в переговорной комнате должна заканчивается в " + bookingEndTime);

            return false;
        }

        if(ChronoUnit.MINUTES.between(startTime, endTime) < minMeetingDurationMinutes) {
            errors.reject("meetingIsTooShort",
                    "Минимальное время встречи (в минутах): " + minMeetingDurationMinutes);
            return false;
        }

        return true;
    }

    protected boolean validateParticipantsList(Collection<String> participants, Errors errors) {
        if(participants.size() < minParticipantsCount) {
            errors.reject("notEnoughParticipants",
                    "Минимальное количество участников встречи: " + minParticipantsCount);
            return false;
        }

        if(participants.size() > maxParticipantsCount) {
            errors.reject("tooMuchParticipants",
                    "Максимальное количество участников встречи: " + maxParticipantsCount);
            return false;
        }

        List<String> existingUsernames = employeeRepository.findExistingUsernames(participants);

        for (String username : participants) {
            if (!existingUsernames.contains(username)) {
                errors.reject("invalidParticipant", "Сотрудника с ником " + username + " не существует");
                return false;
            }
        }

        return true;
    }
}
