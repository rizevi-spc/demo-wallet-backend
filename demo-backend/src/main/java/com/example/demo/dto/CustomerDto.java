package com.example.demo.dto;

import jakarta.validation.Valid;
import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for Customer.
 */
public record CustomerDto(
    Long id,
    String name,
    String surname,
    /**
     * is related with user
     */
    @Valid
    UserDto user
){ }