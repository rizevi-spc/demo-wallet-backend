package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * user dto
 */
public record UserDto(
    @NotNull(message = "{valid.null.error}")
    @Email(message = "{valid.email.error}")
    String username,
    
    /**
     * to prevent user from read but be ablo to send
     */
    @NotNull(message = "{valid.null.error}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password,
    
    List<RoleDto> roles
) { }