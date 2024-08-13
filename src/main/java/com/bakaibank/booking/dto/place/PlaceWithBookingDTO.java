package com.bakaibank.booking.dto.place;

import com.bakaibank.booking.dto.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlaceWithBookingDTO {
    private Long id;
    private String code;
    private NestedBookingDTO booking;

    public PlaceWithBookingDTO(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestedBookingDTO {
        private LocalDate bookingDate;
        private EmployeeDTO employee;
    }
}
