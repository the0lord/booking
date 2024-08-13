package com.bakaibank.booking.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthoritiesNameProvider {
    @Value("${booking.security.authorities.roles.admin-role-name}")
    private String adminRoleName;

    @Value("${booking.security.authorities.roles.user-role-name}")
    private String userRoleName;

    public String roleAdmin() {
        return adminRoleName;
    }

    public String roleUser() {
        return userRoleName;
    }

    public List<String> getRolesNames() {
        return List.of(roleAdmin(), roleUser());
    }

    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(String.format("%s > %s",
                roleAdmin(),
                roleUser())
        );
    }
}
