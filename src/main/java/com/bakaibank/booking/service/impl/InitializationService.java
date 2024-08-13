package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.security.AuthoritiesNameProvider;
import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import com.bakaibank.booking.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Отвечает за инициализацию ролей и учетной записи администратора при первом запуске приложения
 */
@Service
@RequiredArgsConstructor
public class InitializationService {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final AuthoritiesNameProvider authoritiesNameProvider;

    @Value("${booking.security.admin.username}")
    private String adminUsername;

    @Value("${booking.security.admin.email}")
    private String adminEmail;

    @Value("${booking.security.admin.password}")
    private String adminPassword;

    @Value("${booking.security.admin.first-name}")
    private String adminFirstName;

    @Value("${booking.security.admin.last-name}")
    private String adminLastName;

    @Transactional
    public void initialize() {
        if(isEmployeesTableEmpty() && isRolesTableEmpty()) {
            List<String> rolesNames = authoritiesNameProvider.getRolesNames();

            roleRepository.saveAll(rolesNames.stream()
                    .map(Role::new)
                    .toList()
            );

            CreateEmployeeDTO dto = new CreateEmployeeDTO(
                    adminUsername,
                    adminEmail,
                    adminPassword,
                    adminFirstName,
                    adminLastName,
                    Set.of(authoritiesNameProvider.roleAdmin())
            );

            employeeService.save(dto);
        }
    }

    public boolean isEmployeesTableEmpty() {
        return !employeeRepository.existsAny();
    }

    public boolean isRolesTableEmpty() {
        return !roleRepository.existsAny();
    }
}
