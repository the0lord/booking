package com.bakaibank.booking.dto.weekend;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateWeekendDTO {
    private Set<LocalDate> weekends;
}
