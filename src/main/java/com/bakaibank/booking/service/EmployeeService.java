package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.dto.employee.UpdateEmployeeDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    Optional<EmployeeDTO> findById(Long id);
    EmployeeDTO save(@Valid CreateEmployeeDTO createEmployeeDTO);
    EmployeeDTO update(Long employeeId, @Valid UpdateEmployeeDTO updateEmployeeDTO);
    void deleteById(Long id);
    EmployeeRolesDTO findRolesById(Long employeeId);
    EmployeeRolesDTO updateRoles(Long employeeId, @Valid EmployeeRolesDTO roles);
}
