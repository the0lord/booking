package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.position.CreatePositionDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.position.UpdatePositionDTO;
import com.bakaibank.booking.entity.Position;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PositionRepository;
import com.bakaibank.booking.service.PositionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository,
                               EmployeeRepository employeeRepository,
                               ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PositionDTO> findAll() {
        List<Position> positions = positionRepository.findAll();

        return positions.stream()
                .map(position -> modelMapper.map(position, PositionDTO.class))
                .toList();
    }

    @Override
    public Optional<PositionDTO> findById(Long id) {
        return positionRepository.findById(id)
                .map(position -> modelMapper.map(position, PositionDTO.class));
    }

    @Override
    public PositionDTO save(@Valid CreatePositionDTO createPositionDTO) {
        Position position = positionRepository.save(modelMapper.map(createPositionDTO, Position.class));
        return modelMapper.map(position, PositionDTO.class);
    }

    @Override
    public PositionDTO update(Long positionId, UpdatePositionDTO updatePositionDTO) {
        Position position = positionRepository
                .findById(positionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        position.setName(updatePositionDTO.getName());
        return modelMapper.map(positionRepository.save(position), PositionDTO.class);
    }

    @Override
    public void deleteById(Long id) throws RelatedEntityExistsException {
        if(employeeRepository.existsByPosition_Id(id))
            throw new RelatedEntityExistsException("Невозможно удалить эту должность," +
                    " т.к. существует как минимум 1 сотрудник, ссылающийся на нее");

        positionRepository.deleteById(id);
    }
}
