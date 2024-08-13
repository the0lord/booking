package com.bakaibank.booking.dto.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceDTO {
    private Long id;
    private String code;
    private boolean hasBooking;
    private boolean isLocked;

    public PlaceDTO(Long id, String code) {
        this.id = id;
        this.code = code;
    }
}
