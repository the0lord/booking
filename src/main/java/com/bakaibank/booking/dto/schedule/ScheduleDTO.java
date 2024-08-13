package com.bakaibank.booking.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScheduleDTO {
    private Long id;
    private String placeCode;
    private LocalDate date;
    private List<String> teams; //название команд
}
