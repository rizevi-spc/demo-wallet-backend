package com.example.demo.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import java.io.Serializable;

/**
 * Type for returning page request
 */
public record PageRequestInfo(
    @PositiveOrZero(message = "{valid.negative.error}") int page,
    @Positive(message = "{valid.negative.zero.error}") int size
) implements Serializable {

    public PageRequest getPageRequest() {
        return PageRequest.of(page, size);
    }
}