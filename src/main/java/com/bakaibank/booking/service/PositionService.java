package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.position.CreatePositionDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.position.UpdatePositionDTO;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    List<PositionDTO> findAll();
    Optional<PositionDTO> findById(Long id);
    PositionDTO save(@Valid CreatePositionDTO createPositionDTO);
    PositionDTO update(Long positionId, @Valid UpdatePositionDTO updatePositionDTO);
    void deleteById(Long id) throws RelatedEntityExistsException;
}
