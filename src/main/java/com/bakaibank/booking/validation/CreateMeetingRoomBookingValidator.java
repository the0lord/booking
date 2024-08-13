package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.rooms.AbstractMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;

@Component
public class CreateMeetingRoomBookingValidator extends AbstractMeetingRoomBookingValidator implements Validator {
    @Autowired
    public CreateMeetingRoomBookingValidator(WeekendRepository weekendRepository,
                                             MeetingRoomBookingRepository meetingRoomBookingRepository,
                                             EmployeeRepository employeeRepository) {
        super(weekendRepository, meetingRoomBookingRepository, employeeRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateMeetingRoomBookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateMeetingRoomBookingDTO dto = (CreateMeetingRoomBookingDTO) target;

        if(!validateBookingOpenCloseTime(errors)) return;

        super.validate(dto, errors);
    }

    @Override
    protected boolean existConflictingBookingsByDateTime(AbstractMeetingRoomBookingDTO dto, Errors errors) {
        if(meetingRoomBookingRepository.isMeetingIntersectsWithOtherMeetings(
                dto.getMeetingRoomId(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime())) {
            errors.reject("meetingIntersectsWithOtherMeetings", "Встреча пересекается с другими встречами");
            return true;
        }

        return false;
    }

    @Override
    protected boolean existConflictingBookingsByParticipants(AbstractMeetingRoomBookingDTO dto, Errors errors) {
        List<String> participantsWithConflictingBookings = meetingRoomBookingRepository.findEmployeesWithConflictBooking(
                dto.getParticipants(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        if(!participantsWithConflictingBookings.isEmpty()) {
            errors.reject("oneOfParticipantsHasConflictBookings", "Следующие сотрудники: " +
                    String.join(", ", participantsWithConflictingBookings) +
                    " уже участвуют во встрече, которая пересекается по времени с планируемой встречей");
            return true;
        }

        return false;
    }
}
