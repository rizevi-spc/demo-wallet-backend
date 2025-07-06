package com.example.demo.controller;

import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.dto.PageRequestInfo;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * Controller for customer operations.
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Inserts a new customer.
     */
    @PostMapping("add")
    public ResponseEntity<CustomerDto> insertCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return Optional.ofNullable(customerService.add(customerDto))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);

    }
}
