package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.placelock.CreatePlaceLockDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import com.bakaibank.booking.dto.placelock.UpdatePlaceLockDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface PlaceLockService {
    List<PlaceLockDTO> findAll();
    Optional<PlaceLockDTO> findById(Long id);
    PlaceLockDTO save(@Valid CreatePlaceLockDTO createPlaceLockDTO);
    PlaceLockDTO updateById(Long id, @Valid UpdatePlaceLockDTO updatePlaceLockDTO);
    void deleteById(Long id);
}
