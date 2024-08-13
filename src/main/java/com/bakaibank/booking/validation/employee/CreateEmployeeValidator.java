package com.bakaibank.booking.validation.employee;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateEmployeeValidator extends AbstractEmployeeValidator implements Validator  {

    @Autowired
    public CreateEmployeeValidator(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        super(employeeRepository, roleRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateEmployeeDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateEmployeeDTO dto = (CreateEmployeeDTO) target;

        if(!super.validateAllRolesExists(dto.getRoles(), errors)) return;
        super.validate(dto, errors);
    }

    @Override
    protected boolean validateUsername(String username, Errors errors) {
        return super.checkUsernameIsUnique(username, errors);
    }

    @Override
    protected boolean validateEmail(String email, Errors errors) {
        return super.checkEmailIsUnique(email, errors);
    }
}
