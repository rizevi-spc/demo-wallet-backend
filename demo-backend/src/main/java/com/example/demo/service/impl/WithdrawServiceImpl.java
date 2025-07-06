package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.Logging;
import com.example.demo.dto.transaction.CreateWithdrawDto;
import com.example.demo.dto.transaction.WithdrawDto;
import com.example.demo.entity.Wallet;
import com.example.demo.entity.WithdrawTransaction;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of the WithdrawService interface for processing withdrawal transactions.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawServiceImpl implements WithdrawService {
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    @Logging
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CUSTOMER') and" +
            " @customerSecurityComponent.isCustomerWalletOwner(#createWithdrawDto.walletId(), authentication.principal.claims['user_id']))")
    public WithdrawDto processWithdraw(CreateWithdrawDto createWithdrawDto) {
        Wallet wallet = walletRepository.findById(createWithdrawDto.walletId())
                .orElseThrow(() -> new CustomValidationException("valid.wallet.not.exist", createWithdrawDto.walletId()));



        WithdrawTransaction withdrawTransaction = transactionMapper.createWithdrawDtoToEntity(createWithdrawDto);
        withdrawTransaction.setWallet(wallet);
        final TransactionStatus status = getStatus(withdrawTransaction, wallet);
        withdrawTransaction.setStatus(status);

        if (TransactionStatus.APPROVED.equals(status)) {
            wallet.withdraw(withdrawTransaction.getAmount());
        }

        walletRepository.save(wallet);

        return transactionMapper.withdrawToDto(transactionRepository.save(withdrawTransaction));
    }

    private TransactionStatus getStatus(WithdrawTransaction withdrawTransaction, Wallet wallet) {
        return withdrawTransaction.getAmount().compareTo(BigDecimal.valueOf(wallet.getCurrency().getMaxApprovableAmount())) > 0
                ? TransactionStatus.PENDING : TransactionStatus.APPROVED;
    }
}