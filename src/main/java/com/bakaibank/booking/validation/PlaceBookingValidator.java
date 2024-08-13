package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Schedule;
import com.bakaibank.booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class PlaceBookingValidator extends AbstractBookingValidator implements Validator {
    private final BookingRepository bookingRepository;
    private final PlaceLockRepository placeLockRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${booking.places.allowed.days-ahead}")
    private int placeBookingAllowedDaysAhead;

    @Autowired
    public PlaceBookingValidator(BookingRepository bookingRepository,
                                 WeekendRepository weekendRepository,
                                 PlaceLockRepository placeLockRepository,
                                 ScheduleRepository scheduleRepository,
                                 EmployeeRepository employeeRepository) {
        super(weekendRepository);
        this.bookingRepository = bookingRepository;
        this.placeLockRepository = placeLockRepository;
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateBookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateBookingDTO createBookingDTO = (CreateBookingDTO) target;
        LocalDate bookingDate = createBookingDTO.getBookingDate();

        if(!validateBookingOpenCloseTime(errors)) return;

        if(!validateBookingDate(bookingDate, placeBookingAllowedDaysAhead, errors)) return;

        if(!validatePlaceIsAvailable(createBookingDTO.getPlaceId(), bookingDate, createBookingDTO.getEmployeeId(), errors)) return;

        if(bookingRepository.existsByEmployee_IdAndBookingDate(createBookingDTO.getEmployeeId(), bookingDate)) {
            errors.reject("alreadyBookedAnotherPlace", "Вы уже забронировали место на выбранную дату");
        }
    }

    public boolean validatePlaceIsAvailable(Long placeId, LocalDate bookingDate, Long bookingOwnerId, Errors errors) {
        if(bookingRepository.existsByPlace_IdAndBookingDate(placeId, bookingDate)) {
            errors.reject("placeIsAlreadyTaken", "Выбранное место уже забронировано на эту дату");
            return false;
        }

        if(placeLockRepository.findNearestPlaceLockByPlaceIdAndDate(placeId, bookingDate).isPresent()) {
            errors.reject("placeIsLocked", "Выбранное место недоступно для бронирования в эту дату");
            return false;
        }

        Schedule schedule = scheduleRepository.findByPlace_IdAndDate(placeId, bookingDate).orElse(null);

        if(schedule != null) {
            Employee employee = employeeRepository.findByIdWithPositionAndTeam(bookingOwnerId).orElse(null);

            if(employee != null && employee.getTeam() != null && schedule.getTeams().stream().noneMatch(team -> team.equals(employee.getTeam()))) {
                errors.reject("placeScheduledForAnotherTeam",
                        "Данное место недоступно для бронирование в эту дату для вашей команды");
                return false;
            }
        }

        return true;
    }
}
