package com.bakaibank.booking.dto.employee;

import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private PositionDTO position;
    private TeamDTO team;
}
