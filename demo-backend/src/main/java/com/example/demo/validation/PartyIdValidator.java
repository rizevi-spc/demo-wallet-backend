package com.example.demo.validation;

import com.example.demo.dto.transaction.HasParty;
import com.example.demo.validation.annotation.ValidPartyId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for checking if a party ID is valid based on the party type.
 */
public class PartyIdValidator implements ConstraintValidator<ValidPartyId, HasParty> {

    @Override
    public boolean isValid(HasParty dto, ConstraintValidatorContext context) {
        if (dto.partyType() == null || dto.partyId() == null) {
            return true;
        }
        return dto.partyType().isValidDestination(dto.partyId());
    }
}