package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.Logging;
import com.example.demo.dto.PageRequestInfo;
import com.example.demo.dto.transaction.StatusUpdateRequest;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.entity.DepositTransaction;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Wallet;
import com.example.demo.entity.WithdrawTransaction;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the TransactionService interface for managing transactions.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;


    @Override
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CUSTOMER') and" +
            " @customerSecurityComponent.isCustomerWalletOwner(#walletId, authentication.principal.claims['user_id']))")
    @Logging
    public Page<TransactionDto> getTransactions(Long walletId, @Valid PageRequestInfo pageable) {
        walletRepository.findById(walletId)
                .orElseThrow(() -> new CustomValidationException("valid.wallet.not.exist", walletId));

        return transactionRepository.findTransactionByWallet_Id(walletId, pageable.getPageRequest()).map(transactionMapper::toDto);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Logging
    public TransactionDto updateTransactionStatus(StatusUpdateRequest statusUpdateRequest, Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> processTransaction(transaction, statusUpdateRequest)).map(transactionMapper::toDto)
                .orElseThrow(() -> new CustomValidationException("valid.transaction.not.exist", transactionId));
    }

    private Transaction processTransaction(Transaction transaction, StatusUpdateRequest statusUpdateRequest) {
        final TransactionStatus status = statusUpdateRequest.transactionStatus();
        transaction.setStatus(status);
        final Wallet wallet = transaction.getWallet();
        if (TransactionStatus.APPROVED.equals(status) && transaction instanceof DepositTransaction) {
            wallet.deposit(transaction.getAmount());
        } else if (TransactionStatus.APPROVED.equals(status) && transaction instanceof WithdrawTransaction withdrawTransaction) {
           wallet.withdraw(withdrawTransaction.getAmount());
        }
        return transactionRepository.save(transaction);
    }
}