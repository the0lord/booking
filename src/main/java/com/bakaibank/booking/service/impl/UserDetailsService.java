package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.BookingUserDetailsImpl;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public UserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));

        return new BookingUserDetailsImpl(
                employee.getId(),
                employee.getUsername(),
                employee.getPassword(),
                mapRolesToGrantedAuthorities(employee.getRoles())
        );
    }

    private Set<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
