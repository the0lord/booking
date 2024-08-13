package com.bakaibank.booking.dto.employee.converters;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Role;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeConvertersManager {

    public static Converter<Employee, EmployeeDTO> toEmployeeDTO = new Converter<>() {
        @Override
        public EmployeeDTO convert(MappingContext<Employee, EmployeeDTO> mappingContext) {
            return mapEmployeeToEmployeeDTO(mappingContext.getSource());
        }
    };

    public static Converter<Employee, EmployeeRolesDTO> toEmployeeRolesDTO = new Converter<>() {
        @Override
        public EmployeeRolesDTO convert(MappingContext<Employee, EmployeeRolesDTO> mappingContext) {
            Employee employee = mappingContext.getSource();

            return new EmployeeRolesDTO(
                    employee.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet())
            );
        }
    };

    public static Converter<CreateEmployeeDTO, Employee> toEmployee = new Converter<>() {
        @Override
        public Employee convert(MappingContext<CreateEmployeeDTO, Employee> mappingContext) {
            CreateEmployeeDTO employeeDTO = mappingContext.getSource();

            return new Employee(
                    employeeDTO.getUsername(),
                    employeeDTO.getEmail(),
                    employeeDTO.getPassword(),
                    employeeDTO.getFirstName(),
                    employeeDTO.getLastName(),
                    employeeDTO.getMiddleName()
            );
        }
    };

    public static EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getUsername(),
                employee.getEmail(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getMiddleName(),
                Optional.ofNullable(employee.getPosition())
                        .map(position -> new PositionDTO(position.getId(), position.getName()))
                        .orElse(null),
                Optional.ofNullable(employee.getTeam())
                        .map(team -> new TeamDTO(team.getId(), team.getName()))
                        .orElse(null)
        );
    }
}
