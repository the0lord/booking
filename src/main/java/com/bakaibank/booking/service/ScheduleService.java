package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.schedule.CreateScheduleDTO;
import com.bakaibank.booking.dto.schedule.ScheduleDTO;
import com.bakaibank.booking.dto.schedule.UpdateScheduleDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    List<ScheduleDTO> findAllByDate(LocalDate date);
    Optional<ScheduleDTO> findById(Long id);
    ScheduleDTO save(@Valid CreateScheduleDTO createScheduleDTO);
    ScheduleDTO update(Long scheduleId, @Valid UpdateScheduleDTO updateScheduleDTO);
    void deleteById(Long id);
    boolean existsById(Long id);
}
