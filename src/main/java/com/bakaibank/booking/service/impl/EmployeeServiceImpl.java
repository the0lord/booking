package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.employee.*;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.PositionRepository;
import com.bakaibank.booking.repository.RoleRepository;
import com.bakaibank.booking.repository.TeamRepository;
import com.bakaibank.booking.service.EmployeeService;
import com.bakaibank.booking.validation.employee.CreateEmployeeValidator;
import com.bakaibank.booking.validation.employee.UpdateEmployeeValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CreateEmployeeValidator createEmployeeValidator;
    private final UpdateEmployeeValidator updateEmployeeValidator;
    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;

    @Override
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAllWithPositionAndTeam();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
    }

    @Override
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findByIdWithPositionAndTeam(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public EmployeeDTO save(@Valid CreateEmployeeDTO createEmployeeDTO) throws ValidationException {
        Errors errors = new BeanPropertyBindingResult(createEmployeeDTO, "createEmployeeErrors");
        createEmployeeValidator.validate(createEmployeeDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        createEmployeeDTO.setPassword(passwordEncoder.encode(createEmployeeDTO.getPassword()));
        Employee employee = modelMapper.map(createEmployeeDTO, Employee.class);

        this.setEmployeePositionAndTeam(employee, createEmployeeDTO);
        employee.setRoles(roleRepository.findByNameIn(createEmployeeDTO.getRoles()));

        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO update(Long employeeId, @Valid UpdateEmployeeDTO updateEmployeeDTO)
            throws ResponseStatusException, ValidationException {
        Employee employee = employeeRepository.findByIdWithPositionAndTeam(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));

        Errors errors = new BeanPropertyBindingResult(updateEmployeeDTO, "updateEmployeeErrors");
        updateEmployeeValidator.setUpdatableEmployee(employee);
        updateEmployeeValidator.validate(updateEmployeeDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        employee.setUsername(updateEmployeeDTO.getUsername());
        employee.setEmail(updateEmployeeDTO.getEmail());
        employee.setFirstName(updateEmployeeDTO.getFirstName());
        employee.setLastName(updateEmployeeDTO.getLastName());
        employee.setMiddleName(updateEmployeeDTO.getMiddleName());

        this.setEmployeePositionAndTeam(employee, updateEmployeeDTO);

        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeRolesDTO findRolesById(Long employeeId) throws ResponseStatusException {
        Employee employee = employeeRepository.findByIdWithRoles(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));

        return modelMapper.map(employee, EmployeeRolesDTO.class);
    }

    @Override
    public EmployeeRolesDTO updateRoles(Long employeeId, @Valid EmployeeRolesDTO roles) {
        Employee employee = employeeRepository.findByIdWithRoles(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));

        Errors errors = new BeanPropertyBindingResult(roles, "updateEmployeeRolesErrors");
        createEmployeeValidator.validateAllRolesExists(roles.getRoles(), errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        employee.setRoles(roleRepository.findByNameIn(roles.getRoles()));
        return modelMapper.map(employeeRepository.save(employee), EmployeeRolesDTO.class);
    }

    private void setEmployeePositionAndTeam(Employee employee, AbstractEmployeeDTO dto) {
        employee.setTeam(
                Optional.ofNullable(dto.getTeamId())
                        .flatMap(teamRepository::findById)
                        .orElse(null)
        );

        employee.setPosition(
                Optional.ofNullable(dto.getPositionId())
                        .flatMap(positionRepository::findById)
                        .orElse(null)
        );
    }
}
