package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.rooms.AbstractMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.UpdateMeetingRoomBookingDTO;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;


@Component
@Setter
public class UpdateMeetingRoomBookingValidator extends AbstractMeetingRoomBookingValidator implements Validator {
    private MeetingRoomBooking targetMeetingRoomBooking;

    @Autowired
    public UpdateMeetingRoomBookingValidator(WeekendRepository weekendRepository,
                                             MeetingRoomBookingRepository meetingRoomBookingRepository,
                                             EmployeeRepository employeeRepository) {
        super(weekendRepository, meetingRoomBookingRepository, employeeRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateMeetingRoomBookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (targetMeetingRoomBooking == null) throw new RuntimeException("targetMeetingRoomBooking не может быть NULL");
        UpdateMeetingRoomBookingDTO dto = (UpdateMeetingRoomBookingDTO) target;

        if(!validateUpdatableBookingIsNotOver(dto, errors)) return;

        super.validate(dto, errors);
    }

    @Override
    protected boolean existConflictingBookingsByDateTime(AbstractMeetingRoomBookingDTO dto, Errors errors) {
        if(meetingRoomBookingRepository.isUpdatableMeetingIntersectsWithOtherMeetings(
                targetMeetingRoomBooking.getId(),
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
        List<String> participantsWithConflictingBookings = meetingRoomBookingRepository.findEmployeesWithConflictBookingExceptUpdatableBooking(
                dto.getParticipants(),
                targetMeetingRoomBooking.getId(),
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

    private boolean validateUpdatableBookingIsNotOver(UpdateMeetingRoomBookingDTO dto, Errors errors) {
        if(dto.getDate().isBefore(LocalDate.now())) {
            errors.reject("meetingIsOver", "Вы не можете редактировать это собрание, т.к. оно уже завершилось");
            return false;
        }

        return true;
    }
}
