package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.weekend.CreateWeekendDTO;
import com.bakaibank.booking.dto.weekend.WeekendDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekendService {
    List<WeekendDTO> findAll();

    Optional<WeekendDTO> findByDate(LocalDate date);

    List<WeekendDTO> saveAll(CreateWeekendDTO createWeekendDTO);

    void deleteByDate(LocalDate date);
}
