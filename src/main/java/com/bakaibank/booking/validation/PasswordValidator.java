package com.bakaibank.booking.validation;

import com.bakaibank.booking.validation.annotations.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Value("${booking.security.auth.password.min.length}")
    private int passwordMinLength;

    @Value("${booking.security.auth.password.min.letters-count}")
    private int passwordMinLettersCount;

    @Value("${booking.security.auth.password.min.upper-letters-count}")
    private int passwordMinUpperLettersCount;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) return true; //Должно быть обработано другой аннотацией

        boolean passwordValid = true;
        int lettersCount = 0, upperLettersCount = 0;
        StringBuilder errorMessage = new StringBuilder();

        if (password.length() < passwordMinLength) {
            errorMessage.append(String.format("Пароль должен быть как минимум %s симолов в длину. ", passwordMinLength));
            passwordValid = false;
        }

        for(int i = 0; i < password.length(); i++) {
            if(Character.isLetter(password.charAt(i)))
                lettersCount++;

            if(Character.isUpperCase(password.charAt(i)))
                upperLettersCount++;
        }

        if(lettersCount < passwordMinLettersCount) {
            errorMessage.append(String.format("Пароль должен содержать как минимум %s строчных букв. ", passwordMinLettersCount));
            passwordValid = false;
        }

        if(upperLettersCount < passwordMinUpperLettersCount) {
            errorMessage.append(String.format("Пароль должен содержать как минимум %s заглавных букв. ", passwordMinUpperLettersCount));
            passwordValid = false;
        }

        if(!passwordValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(errorMessage.toString())
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
