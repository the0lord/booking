package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.position.CreatePositionDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.position.UpdatePositionDTO;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionController {
    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public List<PositionDTO> findAllPositions() {
        return positionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDTO> findPositionById(@PathVariable Long id) {
        return positionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public PositionDTO createPosition(@RequestBody CreatePositionDTO createPositionDTO) {
        return positionService.save(createPositionDTO);
    }

    @PutMapping("/{id}")
    @AdminRoleRequired
    public PositionDTO updatePositionById(@PathVariable Long id, @RequestBody UpdatePositionDTO updatePositionDTO) {
        return positionService.update(id, updatePositionDTO);
    }

    @DeleteMapping("/{id}")
    @AdminRoleRequired
    public ResponseEntity<?> deletePositionById(@PathVariable Long id) throws RelatedEntityExistsException {
        return positionService.findById(id)
                .map(position -> {
                    positionService.deleteById(id);
                    return ResponseEntity.ok(position);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
