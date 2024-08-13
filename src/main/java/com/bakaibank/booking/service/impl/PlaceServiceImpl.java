package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.place.CreatePlaceDTO;
import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.place.PlaceWithBookingDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.entity.PlaceLock;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.PlaceRepository;
import com.bakaibank.booking.service.PlaceService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;
    private final PlaceLockRepository placeLockRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository,
                            ModelMapper modelMapper,
                            BookingRepository bookingRepository,
                            PlaceLockRepository placeLockRepository) {
        this.placeRepository = placeRepository;
        this.modelMapper = modelMapper;
        this.bookingRepository = bookingRepository;
        this.placeLockRepository = placeLockRepository;
    }
    @Override
    public List<PlaceDTO> findAll(LocalDate date) {
        List<Object[]> places = placeRepository.findAllWithBookingAndLockByDate(date);
        return places.stream()
                .map(result -> {
                    PlaceDTO dto = modelMapper.map(result[0], PlaceDTO.class);
                    dto.setHasBooking((Boolean) result[1]);
                    dto.setLocked((Boolean) result[2]);
                    return dto;
                })
                .toList();
    }

    @Override
    public Optional<PlaceDTO> findById(Long id) {
        return placeRepository.findById(id)
                .map(place -> modelMapper.map(place, PlaceDTO.class));
    }

    @Override
    public Optional<PlaceWithBookingDTO> findByIdAndDateWithBookingInfo(Long id, LocalDate date) {
        Optional<Booking> placeBooking = bookingRepository.findByPlace_IdAndBookingDate(id, date);

        return placeRepository.findById(id).map(place -> {
            PlaceWithBookingDTO dto = modelMapper.map(place, PlaceWithBookingDTO.class);
            dto.setBooking(modelMapper.map(placeBooking, PlaceWithBookingDTO.NestedBookingDTO.class));

            return dto;
        });
    }

    @Override
    public Optional<PlaceLockDTO> findNearestPlaceLock(Long placeId, LocalDate date) {
        return placeLockRepository.findNearestPlaceLockByPlaceIdAndDate(placeId, date)
                .map(placeLock -> modelMapper.map(placeLock, PlaceLockDTO.class));
    }

    @Override
    public PlaceDTO save(@Valid CreatePlaceDTO createPlaceDTO) {
        Place place = placeRepository.save(modelMapper.map(createPlaceDTO, Place.class));
        return modelMapper.map(place, PlaceDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        placeRepository.deleteById(id);
    }
}
