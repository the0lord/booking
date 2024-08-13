package com.bakaibank.booking.validation;

import com.bakaibank.booking.validation.annotations.BakaiEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BakaiEmailValidator implements ConstraintValidator<BakaiEmail, String> {

    @Value("${booking.security.auth.email.bakai-domain-only-registration}")
    private boolean bakaiDomainOnlyRegistration;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email == null) return true; //Должно быть обработано другой аннотацией

        String errorMessage;
        Pattern emailPattern;

        if(bakaiDomainOnlyRegistration) {
            emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@bakai\\.kg$");
            errorMessage = "Регистрация доступна только с домена @bakai.kg";
        }
        else {
            emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            errorMessage = "Электронная почта должна быть корректной";
        }

        boolean isEmailValid = emailPattern.matcher(email).matches();

        if(!isEmailValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
