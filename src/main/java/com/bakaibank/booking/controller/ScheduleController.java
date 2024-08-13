package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.schedule.CreateScheduleDTO;
import com.bakaibank.booking.dto.schedule.ScheduleDTO;
import com.bakaibank.booking.dto.schedule.UpdateScheduleDTO;
import com.bakaibank.booking.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@AdminRoleRequired
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<ScheduleDTO> findSchedulesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.findAllByDate(date);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> findScheduleById(@PathVariable Long id) {
        return scheduleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody CreateScheduleDTO createScheduleDTO) {
        return scheduleService.save(createScheduleDTO);
    }

    @PutMapping("/{id}")
    public ScheduleDTO updateSchedule(@PathVariable Long id, @RequestBody UpdateScheduleDTO updateScheduleDTO) {
        return scheduleService.update(id, updateScheduleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScheduleById(@PathVariable Long id) {
        if(!scheduleService.existsById(id))
            return ResponseEntity.notFound().build();

        scheduleService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
