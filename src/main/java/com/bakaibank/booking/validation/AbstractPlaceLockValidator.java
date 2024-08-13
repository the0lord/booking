package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.placelock.AbstractPlaceLockDTO;
import com.bakaibank.booking.entity.Schedule;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@NoArgsConstructor
public abstract class AbstractPlaceLockValidator {
    protected PlaceLockRepository placeLockRepository;
    protected EmployeeRepository employeeRepository;
    protected BookingRepository bookingRepository;
    protected ScheduleRepository scheduleRepository;

    @Value("${booking.places.locks.max.days}")
    protected Integer maxLockDaysAllowed;

    public AbstractPlaceLockValidator(PlaceLockRepository placeLockRepository,
                                      EmployeeRepository employeeRepository,
                                      BookingRepository bookingRepository,
                                      ScheduleRepository scheduleRepository) {
        this.placeLockRepository = placeLockRepository;
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.scheduleRepository = scheduleRepository;
    }


    public void validate(AbstractPlaceLockDTO dto, Errors errors) {
        if(!validateLockDates(dto.getLockStartDate(), dto.getLockEndDate(), errors)) return;

        if(isPlaceBooked(dto.getPlaceId(), dto.getLockStartDate(), dto.getLockEndDate(), errors)) return;

        if(isPlaceScheduled(dto.getPlaceId(), dto.getLockStartDate(), dto.getLockEndDate(), errors)) return;

        if(isPlaceAlreadyLocked(dto.getPlaceId(), dto.getLockStartDate(), dto.getLockEndDate(), errors)) return;

        if(dto.getAssignedEmployeeId() != null) {
            if (isEmployeeAlreadyAssignedToAnotherPlace(
                    dto.getPlaceId(),
                    dto.getAssignedEmployeeId(),
                    dto.getLockStartDate(),
                    dto.getLockStartDate(), errors)
            ) return;
        }
    }


    protected boolean validateLockDates(LocalDate lockStartDate, LocalDate lockEndDate, Errors errors) {
        if (lockEndDate.isBefore(lockStartDate)) {
            errors.reject("invalidDates", "Дата конца блокировки места не может быть " +
                    "меньше даты начала блокировки");
            return false;
        }

        if(ChronoUnit.DAYS.between(lockStartDate, lockEndDate) > this.maxLockDaysAllowed) {
            errors.reject("tooMuchDaysLock",
                    "Максимальное время блокировки места (в днях): " + this.maxLockDaysAllowed);
            return false;
        }

        return true;
    }

    /**
     * Абстрактный метод для проверки того, заблокировано ли уже место с ID placeId. Для операций создания и обновления
     * эта проверка выполняется по-разному
     * @param placeId ID места
     * @param lockStartDate Дата начала блокировки
     * @param lockEndDate Дата конца блокировки
     * @param errors Список ошибок валидации
     */
    protected abstract boolean isPlaceAlreadyLocked(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate, Errors errors);


    /**
     * Проверка, что блокировка места в эти даты не пересекается с бронью этого места
     * @param placeId ID места
     * @param lockStartDate Дата начала блокировки
     * @param lockEndDate Дата конца блокировки
     * @param errors Список ошибок валидации
     */
    protected boolean isPlaceBooked(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate, Errors errors) {
        if(bookingRepository.existsByPlace_IdAndBookingDateBetween(placeId, lockStartDate, lockEndDate)) {
            errors.reject("placeHasBooking", "Блокировка пересекается с бронью места");
            return true;
        }

        return false;
    }


    /**
     * Проверка, существует ли для этого места в заданные даты расписание
     * @param placeId ID места
     * @param lockStartDate Дата начала блокировки
     * @param lockEndDate Дата конца блокировки
     * @param errors Список ошибок валидации
     */
    protected boolean isPlaceScheduled(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate, Errors errors) {
        Schedule schedule = scheduleRepository
                .findFirstByPlace_IdAndDateBetweenOrderByDate(placeId, lockStartDate, lockEndDate)
                .orElse(null);

        if(schedule != null) {
            errors.reject("placeHasSchedule", "Невозможно заблокировать это место на " +
                    "этот период, т.к. для него существует расписание на дату " + schedule.getDate());
            return true;
        }

        return false;
    }


    protected boolean isEmployeeAlreadyAssignedToAnotherPlace(Long placeId, Long employeeId, LocalDate lockStartDate,
                                                              LocalDate lockEndDate, Errors errors) {
        if(placeLockRepository.isEmployeeAlreadyAssignedToAnotherPlace(placeId, employeeId, lockStartDate, lockEndDate)) {
            errors.reject("employeeAlreadyAssignedToAnotherPlace",
                    "Данный сотрудник уже закреплен за другим местом в эти даты");
            return true;
        }

        return false;
    }

}
