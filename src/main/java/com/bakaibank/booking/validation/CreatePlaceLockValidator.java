package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.placelock.CreatePlaceLockDTO;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class CreatePlaceLockValidator extends AbstractPlaceLockValidator implements Validator {

    @Autowired
    public CreatePlaceLockValidator(PlaceLockRepository placeLockRepository,
                                    EmployeeRepository employeeRepository,
                                    BookingRepository bookingRepository,
                                    ScheduleRepository scheduleRepository) {
        super(placeLockRepository, employeeRepository, bookingRepository, scheduleRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreatePlaceLockDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreatePlaceLockDTO dto = (CreatePlaceLockDTO) target;
        super.validate(dto, errors);
    }

    @Override
    protected boolean isPlaceAlreadyLocked(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate, Errors errors) {
        if (placeLockRepository.isPlaceAlreadyLocked(placeId, lockStartDate, lockEndDate)) {
            errors.reject("placeIsAlreadyLocked", "Данное место уже имеет блокировку," +
                    " пересекающуюся по датам с создаваемой блокировкой");
            return true;
        }

        return false;
    }
}
