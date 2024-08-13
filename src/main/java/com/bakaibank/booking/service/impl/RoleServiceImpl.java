package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.role.CreateRoleDTO;
import com.bakaibank.booking.dto.role.RoleDTO;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import com.bakaibank.booking.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();
    }

    @Override
    public Optional<RoleDTO> findById(Long id) {
        return roleRepository.findById(id)
                .map(role -> modelMapper.map(role, RoleDTO.class));
    }

    @Override
    public RoleDTO save(@Valid CreateRoleDTO createRoleDTO) {
        Role role = roleRepository.save(modelMapper.map(createRoleDTO, Role.class));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public void delete(RoleDTO roleDTO) throws RelatedEntityExistsException {
        Role role = modelMapper.map(roleDTO, Role.class);

        if(employeeRepository.existsByRoles(List.of(role))) {
            throw new RelatedEntityExistsException("Невозможно удалить эту роль, т.к. есть как минимум 1 сотрудник с этой ролью");
        }

        roleRepository.delete(role);
    }
}
