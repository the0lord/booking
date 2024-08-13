package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.role.CreateRoleDTO;
import com.bakaibank.booking.dto.role.RoleDTO;
import com.bakaibank.booking.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AdminRoleRequired
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO> findAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody CreateRoleDTO createRoleDTO) {
        return roleService.save(createRoleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(roleDTO -> {
                    roleService.delete(roleDTO);
                    return ResponseEntity.ok(roleDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
