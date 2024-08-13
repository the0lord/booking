package com.bakaibank.booking.dto.role;

import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.validation.annotations.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateRoleDTO {
    @NotBlank(message = "Укажите название роли")
    @Unique(message = "Роль с таким названием уже существует", field = "name", entity = Role.class)
    private String name;
}
