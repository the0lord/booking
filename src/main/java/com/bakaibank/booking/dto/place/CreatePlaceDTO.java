package com.bakaibank.booking.dto.place;

import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.validation.annotations.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaceDTO {
    @NotBlank(message = "Код места не может быть пустым или содержать значение NULL")
    @Unique(message = "Место с таким кодовым номером уже существует", field = "code", entity = Place.class)
    private String code;
}
