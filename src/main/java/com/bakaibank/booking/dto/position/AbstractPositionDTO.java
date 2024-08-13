package com.bakaibank.booking.dto.position;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractPositionDTO {
    @NotBlank(message = "Укажите название должности")
    private String name;
}
