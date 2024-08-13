package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.role.CreateRoleDTO;
import com.bakaibank.booking.dto.role.RoleDTO;
import com.bakaibank.booking.entity.Role;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDTO> findAll();
    Optional<RoleDTO> findById(Long id);
    RoleDTO save(@Valid CreateRoleDTO createRoleDTO);
    void delete(RoleDTO roleDTO);
}
