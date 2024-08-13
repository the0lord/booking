package com.bakaibank.booking.validation.schedule;

import com.bakaibank.booking.dto.schedule.AbstractScheduleDTO;
import com.bakaibank.booking.dto.schedule.UpdateScheduleDTO;
import com.bakaibank.booking.entity.Schedule;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import com.bakaibank.booking.repository.TeamRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Setter
public class UpdateScheduleValidator extends AbstractScheduleValidator implements Validator {
    private final ScheduleRepository scheduleRepository;
    private Schedule updatableSchedule;

    @Autowired
    public UpdateScheduleValidator(TeamRepository teamRepository,
                                   PlaceLockRepository placeLockRepository,
                                   BookingRepository bookingRepository,
                                   ScheduleRepository scheduleRepository) {
        super(teamRepository, placeLockRepository, bookingRepository);
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateScheduleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (updatableSchedule == null) throw new RuntimeException("updatableSchedule не может быть NULL");

        UpdateScheduleDTO dto = (UpdateScheduleDTO) target;
        super.validate(dto, errors);
    }

    @Override
    protected boolean isScheduleAlreadyExists(AbstractScheduleDTO dto) {
        return scheduleRepository.existsByPlace_IdAndDateAndIdNot(dto.getPlaceId(), dto.getDate(), updatableSchedule.getId());
    }
}
