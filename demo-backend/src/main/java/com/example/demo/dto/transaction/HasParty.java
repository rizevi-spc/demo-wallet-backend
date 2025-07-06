package com.example.demo.dto.transaction;

import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.validation.annotation.ValidPartyId;

@ValidPartyId
/**
 * Interface for objects that have a party type and party ID.
 */
public interface HasParty {
    OppositePartyType partyType();
    String partyId();
}