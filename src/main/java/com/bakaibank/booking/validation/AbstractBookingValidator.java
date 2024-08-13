package com.bakaibank.booking.validation;

import com.bakaibank.booking.entity.Weekend;
import com.bakaibank.booking.repository.WeekendRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
@NoArgsConstructor
public abstract class AbstractBookingValidator {
    protected WeekendRepository weekendRepository;

    @Value("${booking.open.time}")
    protected String bookingOpenTimePattern;

    @Value("${booking.close.time}")
    protected String bookingCloseTimePattern;

    public AbstractBookingValidator(WeekendRepository weekendRepository) {
        this.weekendRepository = weekendRepository;
    }

    protected boolean validateBookingOpenCloseTime(Errors errors) {
        LocalTime bookingOpenTime = parseTimeFromPattern(this.bookingOpenTimePattern);
        LocalTime bookingCloseTime = parseTimeFromPattern(this.bookingCloseTimePattern);

        if(LocalTime.now().isBefore(bookingOpenTime) || LocalTime.now().isAfter(bookingCloseTime)) {
            errors.reject("invalidBookingTime", "Бронирование доступно только в период с "
                    + bookingOpenTimePattern + " до " + bookingCloseTimePattern);
            return false;
        }

        return true;
    }

    protected LocalTime parseTimeFromPattern(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }

    protected boolean validateBookingDate(LocalDate bookingDate, int allowedDaysAhead, Errors errors) {
        LocalDate now = LocalDate.now();

        if(bookingDate.isBefore(now) || bookingDate.equals(now)) {
            errors.reject("invalidBookingDate", "Нельзя создать бронь на прошедшую или текущую дату");
            return false;
        }

        if(isWeekend(bookingDate)) {
            errors.reject("dateIsWeekend", "Нельзя создать бронь на выходной день");
            return false;
        }

        if(ChronoUnit.DAYS.between(now, bookingDate) > allowedDaysAhead) {
            LocalDate nextWorkingDate = getNextWorkingDate(now);

            if(!bookingDate.equals(nextWorkingDate)) {
                errors.reject("tooMuchDaysAhead", "Вы можете создать бронь максимум на "
                        + allowedDaysAhead + " дней вперед, либо на следующий календарный рабочий день: " + nextWorkingDate);
                return false;
            }

        }

        return true;
    }

    protected boolean isWeekend(LocalDate date) {
        return weekendRepository.existsByDate(date);
    }

    protected LocalDate getNextWorkingDate(LocalDate currentDate) {
        List<LocalDate> weekends = weekendRepository.findAllByDateIsGreaterThanEqualOrderByDateAsc(currentDate).stream()
                .map(Weekend::getDate)
                .toList();

        do
            currentDate = currentDate.plusDays(1);
        while (Collections.binarySearch(weekends, currentDate) >= 0);

        return currentDate;
    }
}
