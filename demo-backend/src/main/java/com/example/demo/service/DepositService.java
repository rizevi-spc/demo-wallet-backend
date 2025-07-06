package com.example.demo.service;


import com.example.demo.dto.transaction.CreateDepositDto;
import com.example.demo.dto.transaction.DepositDto;

/**
 * Service interface for handling deposit transactions.
 */
public interface DepositService {
    /**
     * Processes a deposit transaction.
     *
     * @param createDepositDto dto containing the details of the deposit to be processed.
     * @return DepositDto containing the details of the processed deposit transaction.
     */
    DepositDto processDeposit(CreateDepositDto createDepositDto);
}
