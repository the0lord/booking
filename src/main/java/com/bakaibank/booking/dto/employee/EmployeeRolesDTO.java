package com.bakaibank.booking.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeRolesDTO {
    @NotNull(message = "Укажите список ролей")
    private Set<String> roles;
}
