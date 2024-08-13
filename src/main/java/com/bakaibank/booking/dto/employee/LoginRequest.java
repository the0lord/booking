package com.bakaibank.booking.dto.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    @NotBlank(message = "Укажите имя пользователя")
    private String username;

    @NotBlank(message = "Укажите пароль")
    private String password;
}
