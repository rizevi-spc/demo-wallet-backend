package com.example.demo.validation.annotation;

import com.example.demo.validation.PartyIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation for checking the validity of a party ID.
 */
@Documented
@Constraint(validatedBy = PartyIdValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPartyId {
    String message() default "{validation.partyId.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}