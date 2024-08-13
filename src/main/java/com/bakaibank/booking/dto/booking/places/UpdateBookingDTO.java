package com.bakaibank.booking.dto.booking.places;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingDTO {
    @NotBlank(message = "Укажите код места")
    private String placeCode;
}
