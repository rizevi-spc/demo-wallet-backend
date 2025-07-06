package com.example.demo.service;

import com.example.demo.dto.PageRequestInfo;
import com.example.demo.dto.transaction.StatusUpdateRequest;
import com.example.demo.dto.transaction.TransactionDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing transactions.
 */
public interface TransactionService {
    /**
     * Retrieves a paginated list of transactions for a specific wallet.
     *
     * @param walletId wallet identifier
     * @param pageable pagination information
     * @return a page of transactions
     */
    Page<TransactionDto> getTransactions(Long walletId, @Valid PageRequestInfo pageable);

    /**
     * Updates the status of a transaction.
     *
     * @param statusUpdateRequest status update request containing the new status
     * @param transactionId transaction identifier
     * @return updated transaction DTO
     */
    TransactionDto updateTransactionStatus(StatusUpdateRequest statusUpdateRequest, Long transactionId);
}
