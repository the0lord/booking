package com.bakaibank.booking.dto.booking.places;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBookingsStatsRequest {
    @NotNull(message = "Укажите дату начала выборки")
    private LocalDate from;

    @NotNull(message = "Укажите дату конца выборки")
    private LocalDate to;

    private Long employeeId;
}
