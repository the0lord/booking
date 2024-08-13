package com.bakaibank.booking.dto.booking.places;

import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDTO {
    @Null
    private Long employeeId;

    @NotNull(message = "ID места не может быть NULL")
    @ForeignKey(message = "Место с таким ID не найдено", entity = Place.class)
    private Long placeId;

    @NotNull(message = "Дата бронирования не может быть NULL")
    private LocalDate bookingDate;
}
