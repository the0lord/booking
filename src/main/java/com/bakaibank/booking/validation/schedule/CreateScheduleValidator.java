package com.bakaibank.booking.validation.schedule;

import com.bakaibank.booking.dto.schedule.AbstractScheduleDTO;
import com.bakaibank.booking.dto.schedule.CreateScheduleDTO;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import com.bakaibank.booking.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateScheduleValidator extends AbstractScheduleValidator implements Validator {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public CreateScheduleValidator(TeamRepository teamRepository,
                                   PlaceLockRepository placeLockRepository,
                                   BookingRepository bookingRepository,
                                   ScheduleRepository scheduleRepository) {
        super(teamRepository, placeLockRepository, bookingRepository);
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateScheduleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateScheduleDTO dto = (CreateScheduleDTO) target;
        super.validate(dto, errors);
    }

    @Override
    protected boolean isScheduleAlreadyExists(AbstractScheduleDTO dto) {
        return scheduleRepository.existsByPlace_IdAndDate(dto.getPlaceId(), dto.getDate());
    }
}
