package com.bakaibank.booking.dto.employee;

import com.bakaibank.booking.validation.annotations.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateEmployeeDTO extends AbstractEmployeeDTO {
    @NotBlank(message = "Пароль должен быть указан")
    @Password
    private String password;

    @NotNull(message = "Укажите список ролей")
    protected Set<String> roles;

    public CreateEmployeeDTO(String username, String email, String password, String firstName, String lastName, Set<String> roles) {
        super(username, email, firstName, lastName);
        this.password = password;
        this.roles = roles;
    }
}
