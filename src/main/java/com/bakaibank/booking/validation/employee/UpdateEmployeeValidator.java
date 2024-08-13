package com.bakaibank.booking.validation.employee;

import com.bakaibank.booking.dto.employee.UpdateEmployeeDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Setter
public class UpdateEmployeeValidator extends AbstractEmployeeValidator implements Validator {
    private Employee updatableEmployee;

    @Autowired
    public UpdateEmployeeValidator(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        super(employeeRepository, roleRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateEmployeeDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (updatableEmployee == null) throw new RuntimeException("updatableEmployee не может быть NULL");
        UpdateEmployeeDTO dto = (UpdateEmployeeDTO) target;
        super.validate(dto, errors);
    }

    @Override
    protected boolean validateUsername(String username, Errors errors) {
        //Проверяем на уникальность, только если имя пользователя изменилось
        if(updatableEmployee.getUsername().equalsIgnoreCase(username))
            return true;

        return checkUsernameIsUnique(username, errors);
    }

    @Override
    protected boolean validateEmail(String email, Errors errors) {
        //Проверяем на уникальность, только если почта изменилась
        if(updatableEmployee.getEmail().equalsIgnoreCase(email))
            return true;

        return checkEmailIsUnique(email, errors);
    }
}
