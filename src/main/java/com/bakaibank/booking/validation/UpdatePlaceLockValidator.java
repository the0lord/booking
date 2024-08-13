package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.placelock.UpdatePlaceLockDTO;
import com.bakaibank.booking.entity.PlaceLock;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
@Setter
public class UpdatePlaceLockValidator extends AbstractPlaceLockValidator implements Validator {
    private PlaceLock updatablePlaceLock;

    @Autowired
    public UpdatePlaceLockValidator(PlaceLockRepository placeLockRepository,
                                    EmployeeRepository employeeRepository,
                                    BookingRepository bookingRepository,
                                    ScheduleRepository scheduleRepository) {
        super(placeLockRepository, employeeRepository, bookingRepository, scheduleRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdatePlaceLockDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(updatablePlaceLock == null) throw new RuntimeException("updatablePlaceLock не может быть NULL");

        UpdatePlaceLockDTO dto = (UpdatePlaceLockDTO) target;
        super.validate(dto, errors);
    }

    @Override
    protected boolean isPlaceAlreadyLocked(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate, Errors errors) {
        if(placeLockRepository.isUpdatablePlaceLockIntersectsWithOtherPlaceLock(updatablePlaceLock.getId(), placeId, lockStartDate, lockEndDate)) {
            errors.reject("placeLockIntersectsWithAnotherPlaceLock",
                    "Блокировка с указанными датами пересекается с другой блокировкой этого места");
            return true;
        }

        return false;
    }
}
