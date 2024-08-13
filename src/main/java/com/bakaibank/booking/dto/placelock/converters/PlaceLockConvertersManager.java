package com.bakaibank.booking.dto.placelock.converters;

import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.dto.placelock.CreatePlaceLockDTO;
import com.bakaibank.booking.dto.placelock.PlaceLockDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.entity.PlaceLock;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlaceLockConvertersManager {
    private final PlaceRepository placeRepository;
    private final EmployeeRepository employeeRepository;

    public static Converter<PlaceLock, PlaceLockDTO> placeLockToPlaceLockDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceLockDTO convert(MappingContext<PlaceLock, PlaceLockDTO> mappingContext) {
                PlaceLock placeLock = mappingContext.getSource();

                return new PlaceLockDTO(
                        placeLock.getId(),
                        placeLock.getPlace().getCode(),
                        placeLock.getLockStartDate(),
                        placeLock.getLockEndDate(),
                        placeLock.getReason(),
                        Optional.ofNullable(placeLock.getAssignedEmployee())
                                .map(EmployeeConvertersManager::mapEmployeeToEmployeeDTO)
                                .orElse(null)
                );
            }
        };
    }

    public Converter<CreatePlaceLockDTO, PlaceLock> createPlaceLockDTOToPlaceLockConverter() {
        return new Converter<>() {
            @Override
            public PlaceLock convert(MappingContext<CreatePlaceLockDTO, PlaceLock> mappingContext) {
                CreatePlaceLockDTO source = mappingContext.getSource();

                Place place = placeRepository.findById(source.getPlaceId()).orElse(null);
                Employee employee = employeeRepository.findByIdWithPositionAndTeam(source.getAssignedEmployeeId()).orElse(null);

                return new PlaceLock(
                        place,
                        source.getLockStartDate(),
                        source.getLockEndDate(),
                        source.getReason(),
                        employee
                );
            }
        };
    }
}
