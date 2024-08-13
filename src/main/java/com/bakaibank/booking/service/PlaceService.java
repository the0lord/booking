package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.place.CreatePlaceDTO;
import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.place.PlaceWithBookingDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceService {
    List<PlaceDTO> findAll(LocalDate date);
    Optional<PlaceDTO> findById(Long id);
    Optional<PlaceWithBookingDTO> findByIdAndDateWithBookingInfo(Long id, LocalDate date);
    Optional<PlaceLockDTO> findNearestPlaceLock(Long placeId, LocalDate date);
    PlaceDTO save(@Valid CreatePlaceDTO createPlaceDTO);
    void deleteById(Long id);
}
