package com.example.demo.service.impl;

import com.example.demo.dto.transaction.CreateWithdrawDto;
import com.example.demo.dto.transaction.WithdrawDto;
import com.example.demo.entity.WithdrawTransaction;
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
class WithdrawServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawServiceImpl withdrawService;

    @Test
    void processWithdraw_walletNotFound_shouldThrowException() {
        CreateWithdrawDto createWithdrawDto = new CreateWithdrawDto(1L, BigDecimal.TEN, OppositePartyType.WALLET, "0123456789");

        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> withdrawService.processWithdraw(createWithdrawDto));
        assertEquals("valid.wallet.not.exist", ex.getMessage());
    }

    @Test
    void processWithdraw_amountLessThanMax_shouldApproveAndWithdraw() {
        CreateWithdrawDto createWithdrawDto = new CreateWithdrawDto(1L, BigDecimal.valueOf(50), OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY); // currency enum with maxApprovableAmount
        wallet.setActiveForWithdraw(Boolean.TRUE);
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setAmount(createWithdrawDto.amount());

        WithdrawDto withdrawDto = WithdrawDto.builder()
                .id(1L)
                .walletId(1L)
                .amount(createWithdrawDto.amount())
                .status(TransactionStatus.APPROVED)
                .destinationType(OppositePartyType.WALLET)
                .destinationId("0123456789")
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createWithdrawDtoToEntity(createWithdrawDto)).thenReturn(withdrawTransaction);
        when(transactionRepository.save(withdrawTransaction)).thenReturn(withdrawTransaction);
        when(transactionMapper.withdrawToDto(withdrawTransaction)).thenReturn(withdrawDto);

        WithdrawDto result = withdrawService.processWithdraw(createWithdrawDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), wallet.getBalance()); // 100 + 50
        assertEquals(TransactionStatus.APPROVED, withdrawTransaction.getStatus());
    }

    @Test
    void processWithdraw_amountGreaterThanMax_shouldSetPendingStatus() {
        BigDecimal maxAmount = BigDecimal.valueOf(Currency.TRY.getMaxApprovableAmount());

        CreateWithdrawDto createWithdrawDto = new CreateWithdrawDto(1L, maxAmount.add(BigDecimal.ONE), OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY);
        wallet.setActiveForWithdraw(Boolean.TRUE);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setAmount(createWithdrawDto.amount());

        WithdrawDto withdrawDto = WithdrawDto.builder()
                .id(1L)
                .walletId(1L)
                .amount(createWithdrawDto.amount())
                .status(TransactionStatus.PENDING)
                .destinationType(OppositePartyType.WALLET)
                .destinationId("0123456789")
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createWithdrawDtoToEntity(createWithdrawDto)).thenReturn(withdrawTransaction);
        when(transactionRepository.save(withdrawTransaction)).thenReturn(withdrawTransaction);
        when(transactionMapper.withdrawToDto(withdrawTransaction)).thenReturn(withdrawDto);

        WithdrawDto result = withdrawService.processWithdraw(createWithdrawDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), wallet.getBalance()); // balance unchanged
        assertEquals(TransactionStatus.PENDING, withdrawTransaction.getStatus());
    }

    @Test
    void processWithdraw_walletInactiveForWithdraw_shouldThrowException() {
        CreateWithdrawDto createWithdrawDto = new CreateWithdrawDto(1L, BigDecimal.TEN, OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY);
        wallet.setActiveForWithdraw(Boolean.FALSE);
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setAmount(createWithdrawDto.amount());

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createWithdrawDtoToEntity(createWithdrawDto)).thenReturn(withdrawTransaction);

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> withdrawService.processWithdraw(createWithdrawDto));
        assertEquals("valid.wallet.not.active", ex.getMessage());
    }

    @Test
    void processWithdraw_walletInsufficientBalance_shouldThrowException() {
        CreateWithdrawDto createWithdrawDto = new CreateWithdrawDto(1L, BigDecimal.valueOf(200), OppositePartyType.WALLET, "0123456789");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setCurrency(Currency.TRY);
        wallet.setActiveForWithdraw(Boolean.TRUE);
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setAmount(createWithdrawDto.amount());

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionMapper.createWithdrawDtoToEntity(createWithdrawDto)).thenReturn(withdrawTransaction);

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> withdrawService.processWithdraw(createWithdrawDto));
        assertEquals("valid.wallet.insufficient.balance", ex.getMessage());
    }
}
