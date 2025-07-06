package com.example.demo.service.impl;

import com.example.demo.dto.transaction.CreateDepositDto;
import com.example.demo.dto.transaction.DepositDto;
import com.example.demo.entity.DepositTransaction;
import com.example.demo.entity.Wallet;
import com.example.demo.enumeration.Currency;
import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    @Test
    void processDeposit_walletNotFound_shouldThrowException() {
        CreateDepositDto createDepositDto = new CreateDepositDto(1L, BigDecimal.TEN, OppositePartyType.WALLET, "0123456789");

        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> depositService.processDeposit(createDepositDto));
        assertEquals("valid.wallet.not.exist", ex.getMessage());
    }

    @Test
    void processDeposit_amountLessThanMax_shouldApproveAndDeposit() {
        CreateDepositDto createDepositDto = new CreateDepositDto(1L, BigDecimal.valueOf(50), OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY); // currency enum with maxApprovableAmount

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(createDepositDto.amount());

        DepositDto depositDto = DepositDto.builder()
                .id(1L)
                .walletId(1L)
                .amount(createDepositDto.amount())
                .status(TransactionStatus.APPROVED)
                .sourceType(OppositePartyType.WALLET)
                .sourceId("0123456789")
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createDepositDtoToEntity(createDepositDto)).thenReturn(depositTransaction);
        when(transactionRepository.save(depositTransaction)).thenReturn(depositTransaction);
        when(transactionMapper.depositToDto(depositTransaction)).thenReturn(depositDto);

        DepositDto result = depositService.processDeposit(createDepositDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), wallet.getBalance()); // 100 + 50
        assertEquals(TransactionStatus.APPROVED, depositTransaction.getStatus());
    }

    @Test
    void processDeposit_amountGreaterThanMax_shouldSetPendingStatus() {
        BigDecimal maxAmount = BigDecimal.valueOf(Currency.TRY.getMaxApprovableAmount());

        CreateDepositDto createDepositDto = new CreateDepositDto(1L, maxAmount.add(BigDecimal.ONE), OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(createDepositDto.amount());

        DepositDto depositDto = DepositDto.builder()
                .id(1L)
                .walletId(1L)
                .amount(createDepositDto.amount())
                .status(TransactionStatus.PENDING)
                .sourceType(OppositePartyType.WALLET)
                .sourceId("0123456789")
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createDepositDtoToEntity(createDepositDto)).thenReturn(depositTransaction);
        when(transactionRepository.save(depositTransaction)).thenReturn(depositTransaction);
        when(transactionMapper.depositToDto(depositTransaction)).thenReturn(depositDto);

        DepositDto result = depositService.processDeposit(createDepositDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), wallet.getBalance()); // balance unchanged
        assertEquals(TransactionStatus.PENDING, depositTransaction.getStatus());
    }
}
