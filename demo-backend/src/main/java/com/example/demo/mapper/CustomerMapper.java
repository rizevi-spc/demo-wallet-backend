package com.example.demo.mapper;

import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Customer;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Customer entity and CustomerDto.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDTO(Customer customer);

    Customer toEntity(CustomerDto customerDto);
}