package com.example.demo.service;


import com.example.demo.dto.transaction.CreateWithdrawDto;
import com.example.demo.dto.transaction.WithdrawDto;

/**
 * Service interface for handling withdrawal transactions.
 */
public interface WithdrawService {
    /**
     * Processes a withdrawal transaction based on the provided CreateWithdrawDto.
     *
     * @param createWithdrawDto the DTO containing details for the withdrawal transaction
     * @return a WithdrawDto containing the details of the processed withdrawal transaction
     */
    WithdrawDto processWithdraw(CreateWithdrawDto createWithdrawDto);
}
