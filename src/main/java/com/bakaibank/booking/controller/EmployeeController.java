package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.dto.employee.UpdateEmployeeDTO;
import com.bakaibank.booking.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> findAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public EmployeeDTO getAuthenticatedEmployeeInfo(@AuthenticationPrincipal BookingUserDetails userDetails) {
        return employeeService.findById(userDetails.getId()).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public EmployeeDTO createEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO) {
        return employeeService.save(createEmployeeDTO);
    }

    @PutMapping("/{id}")
    @AdminRoleRequired
    public EmployeeDTO updateEmployeeById(@PathVariable Long id, @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {
        return employeeService.update(id, updateEmployeeDTO);
    }

    @DeleteMapping("/{id}")
    @AdminRoleRequired
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(employee -> {
                    employeeService.deleteById(id);
                    return ResponseEntity.ok(employee);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/roles")
    @AdminRoleRequired
    public EmployeeRolesDTO findEmployeeRoles(@PathVariable Long id) {
        return employeeService.findRolesById(id);
    }

    @PutMapping("/{id}/roles")
    @AdminRoleRequired
    public EmployeeRolesDTO updateEmployeeRoles(@PathVariable Long id, @RequestBody EmployeeRolesDTO roles) {
        return employeeService.updateRoles(id, roles);
    }
}
