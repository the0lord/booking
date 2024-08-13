package com.bakaibank.booking.validation;

import com.bakaibank.booking.validation.annotations.Unique;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValidator implements ConstraintValidator<Unique, String> {
    private final EntityManager entityManager;

    private String field;

    private Class<?> entity;

    private boolean ignoreCase;

    @Autowired
    public UniqueValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.field = constraintAnnotation.field();
        this.entity = constraintAnnotation.entity();
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return true;

        return isFieldUnique(entity, field, value);
    }

    private boolean isFieldUnique(Class<?> entity, String field, String value) {
        String queryString;

        if(this.ignoreCase)
            queryString = "SELECT CASE WHEN COUNT(*) > 0 THEN false ELSE true END FROM "
                    + entity.getSimpleName() + " WHERE lower( " + field + ") = lower(:value)";
        else
            queryString = "SELECT CASE WHEN COUNT(*) > 0 THEN false ELSE true END FROM "
                    + entity.getSimpleName() + " WHERE  " + field + " = :value";


        Query query = entityManager
                .createQuery(queryString)
                .setParameter("value", value);

        return (Boolean) query.getSingleResult();
    }
}
