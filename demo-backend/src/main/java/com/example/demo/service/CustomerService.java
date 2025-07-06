package com.example.demo.service;

import com.example.demo.dto.CustomerDto;

/**
 * Service interface for managing customers.
 */
public interface CustomerService {

    /**
     * Adds a new customer to the system.
     *
     * @param customerDto the DTO containing customer details
     */
    CustomerDto add(CustomerDto customerDto);

}
