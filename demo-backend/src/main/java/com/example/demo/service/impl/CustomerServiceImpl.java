package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.Logging;
import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Customer;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.mapper.WalletMapper;
import com.example.demo.repository.WalletRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Implementation of the CustomerService interface for managing customer-related operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserService userService;

    @Override
    @Logging
    public CustomerDto add(CustomerDto customerDto) {
        final Customer customer = customerMapper.toEntity(customerDto);
        customer.setUserId(userService.saveCustomerUser(customerDto.user()));
        return customerMapper.toDTO(customerRepository.save(customer));
    }
}
