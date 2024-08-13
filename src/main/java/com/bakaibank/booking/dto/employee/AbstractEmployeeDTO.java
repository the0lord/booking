package com.bakaibank.booking.dto.employee;

import com.bakaibank.booking.entity.Position;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.validation.annotations.BakaiEmail;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@ToString
public abstract class AbstractEmployeeDTO {
    @NotBlank(message = "Имя пользователя должно быть указано")
    protected String username;

    @NotBlank(message = "Почта должна быть указана")
    @BakaiEmail
    protected String email;

    @NotBlank(message = "Укажите имя сотрудника")
    protected String firstName;

    @NotBlank(message = "Укажите фамилию сотрудника")
    protected String lastName;

    protected String middleName;

    @ForeignKey(message = "Должность с таким ID не найдена", entity = Position.class)
    protected Long positionId;

    @ForeignKey(message = "Команда с таким ID не найдена", entity = Team.class)
    protected Long teamId;

    public AbstractEmployeeDTO(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
