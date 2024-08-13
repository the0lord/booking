package com.bakaibank.booking.dto.placelock;

import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPlaceLockDTO {
    @NotNull(message = "Укажите место")
    @ForeignKey(message = "Место с таким ID не найдено", entity = Place.class)
    protected Long placeId;

    @NotNull(message = "Укажите дату начала блокировки")
    protected LocalDate lockStartDate;

    @NotNull(message = "Укажите дату конца блокировки")
    protected LocalDate lockEndDate;

    protected String reason;

    @ForeignKey(message = "Сотрудник с таким ID не найден", entity = Employee.class)
    protected Long assignedEmployeeId;
}
