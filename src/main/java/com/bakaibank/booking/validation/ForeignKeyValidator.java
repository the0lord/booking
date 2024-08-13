package com.bakaibank.booking.validation;

import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class ForeignKeyValidator implements ConstraintValidator<ForeignKey, Long> {
    private final EntityManager entityManager;
    private Class<?> entity;

    @Autowired
    public ForeignKeyValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(ForeignKey constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) return true;

        return entityManager.find(entity, id) != null;
    }
}
