package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.weekend.CreateWeekendDTO;
import com.bakaibank.booking.dto.weekend.WeekendDTO;
import com.bakaibank.booking.service.WeekendService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weekends")
public class WeekendController {
    private final WeekendService weekendService;

    public WeekendController(WeekendService weekendService) {
        this.weekendService = weekendService;
    }

    @GetMapping
    public List<WeekendDTO> findAllWeekends() {
        return  weekendService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public List<WeekendDTO> createWeekends(@RequestBody CreateWeekendDTO createWeekendDTO) {
        return weekendService.saveAll(createWeekendDTO);
    }

    @DeleteMapping("/{date}")
    @AdminRoleRequired
    public ResponseEntity<?> deleteWeekend(@PathVariable
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               LocalDate date) {
        return weekendService.findByDate(date)
                .map(weekend -> {
                    weekendService.deleteByDate(date);
                    return ResponseEntity.ok(weekend);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
