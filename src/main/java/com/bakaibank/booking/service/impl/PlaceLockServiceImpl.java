package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.placelock.CreatePlaceLockDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import com.bakaibank.booking.dto.placelock.UpdatePlaceLockDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.entity.PlaceLock;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.PlaceRepository;
import com.bakaibank.booking.service.PlaceLockService;
import com.bakaibank.booking.validation.CreatePlaceLockValidator;
import com.bakaibank.booking.validation.UpdatePlaceLockValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class PlaceLockServiceImpl implements PlaceLockService {
    private final PlaceLockRepository placeLockRepository;
    private final ModelMapper modelMapper;
    private final CreatePlaceLockValidator createPlaceLockValidator;
    private final UpdatePlaceLockValidator updatePlaceLockValidator;
    private final PlaceRepository placeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<PlaceLockDTO> findAll() {
        List<PlaceLock> placeLocks = placeLockRepository.findAllWithPlaceAndEmployeeInfo();

        return placeLocks.stream()
                .map(placeLock -> modelMapper.map(placeLock, PlaceLockDTO.class))
                .toList();
    }

    @Override
    public Optional<PlaceLockDTO> findById(Long id) {
        return placeLockRepository.findByIdWithPlaceAndEmployeeInfo(id)
                .map(placeLock -> modelMapper.map(placeLock, PlaceLockDTO.class));
    }

    @Override
    public PlaceLockDTO save(@Valid CreatePlaceLockDTO createPlaceLockDTO) {
        Errors errors = new BeanPropertyBindingResult(createPlaceLockDTO, "createPlaceLockErrors");
        createPlaceLockValidator.validate(createPlaceLockDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        PlaceLock placeLock = placeLockRepository.save(modelMapper.map(createPlaceLockDTO, PlaceLock.class));
        return modelMapper.map(placeLock, PlaceLockDTO.class);
    }

    @Override
    public PlaceLockDTO updateById(Long id, @Valid UpdatePlaceLockDTO updatePlaceLockDTO)
            throws ResponseStatusException, ValidationException {
        PlaceLock updatablePlaceLock = placeLockRepository.findByIdWithPlaceAndEmployeeInfo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Errors errors = new BeanPropertyBindingResult(updatePlaceLockDTO, "updatePlaceLockErrors");
        updatePlaceLockValidator.setUpdatablePlaceLock(updatablePlaceLock);
        updatePlaceLockValidator.validate(updatePlaceLockDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        // Запрос к базе происходит, только если место изменилось
        if(!updatablePlaceLock.getPlace().getId().equals(updatePlaceLockDTO.getPlaceId())) {
            Place place = placeRepository.findById(updatePlaceLockDTO.getPlaceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Место с таким ID не найдено"));
            updatablePlaceLock.setPlace(place);
        }

        updatablePlaceLock.setLockStartDate(updatePlaceLockDTO.getLockStartDate());
        updatablePlaceLock.setLockEndDate(updatePlaceLockDTO.getLockEndDate());
        updatablePlaceLock.setReason(updatePlaceLockDTO.getReason());

        Employee employee = null;

        if(updatePlaceLockDTO.getAssignedEmployeeId() != null) {
            employee = employeeRepository.findByIdWithPositionAndTeam(updatePlaceLockDTO.getAssignedEmployeeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));
        }

        updatablePlaceLock.setAssignedEmployee(employee);

        return modelMapper.map(placeLockRepository.save(updatablePlaceLock), PlaceLockDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        placeLockRepository.deleteById(id);
    }
}
