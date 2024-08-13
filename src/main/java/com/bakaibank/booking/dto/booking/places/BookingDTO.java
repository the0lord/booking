package com.bakaibank.booking.dto.booking.places;

import com.bakaibank.booking.dto.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private EmployeeDTO employee;
    private String placeCode;
    private LocalDate bookingDate;
}
