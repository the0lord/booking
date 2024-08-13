package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.placelock.CreatePlaceLockDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import com.bakaibank.booking.dto.placelock.UpdatePlaceLockDTO;
import com.bakaibank.booking.service.PlaceLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place-locks")
@AdminRoleRequired
public class PlaceLockController {
    private final PlaceLockService placeLockService;

    @Autowired
    public PlaceLockController(PlaceLockService placeLockService) {
        this.placeLockService = placeLockService;
    }

    @GetMapping
    public List<PlaceLockDTO> findAllPlaceLocks() {
        return placeLockService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceLockDTO> findPlaceLockById(@PathVariable Long id) {
        return placeLockService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PlaceLockDTO createPlaceLock(@RequestBody CreatePlaceLockDTO createPlaceLockDTO) {
        return placeLockService.save(createPlaceLockDTO);
    }

    @PutMapping("/{id}")
    public PlaceLockDTO updatePlaceLockById(@PathVariable Long id, @RequestBody UpdatePlaceLockDTO updatePlaceLockDTO) {
        return placeLockService.updateById(id, updatePlaceLockDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaceLockById(@PathVariable Long id) {
        return placeLockService.findById(id)
                .map(placeLock -> {
                    placeLockService.deleteById(id);
                    return ResponseEntity.ok(placeLock);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
