package com.bakaibank.booking.dto.schedule;

import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Data
@ToString
public abstract class AbstractScheduleDTO {
    @NotNull(message = "Укажите код места")
    @ForeignKey(message = "Место с таким ID не найдено", entity = Place.class)
    private Long placeId;

    @NotNull(message = "Укажите дату")
    private LocalDate date;

    /*
    ID команд. SET гарантирует выполнение ограничения на уникальность полей (schedule_id, team_id).
    Т.е. что каждая команда может быть включена в каждую запись расписания только 1 раз
     */
    @NotNull(message = "Выберите команды")
    @Size(min = 1, message = "Выберите как минимум одну команду")
    private Set<Long> teams;
}
