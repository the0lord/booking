package com.bakaibank.booking.dto.placelock;

import com.bakaibank.booking.dto.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceLockDTO {
    private Long id;
    private String placeCode;
    private LocalDate lockStartDate;
    private LocalDate lockEndDate;
    private String reason;
    private EmployeeDTO assignedEmployee;
}
