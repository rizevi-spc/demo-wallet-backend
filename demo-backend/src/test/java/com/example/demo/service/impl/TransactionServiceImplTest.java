package com.example.demo.service.impl;

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
import com.example.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void getTransactions_whenWalletNotFound_shouldThrowException() {
        Long walletId = 1L;
        PageRequestInfo pageRequestInfo = new PageRequestInfo(0, 10);

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> transactionService.getTransactions(walletId, pageRequestInfo));

        assertEquals("valid.wallet.not.exist", ex.getMessage());
    }

    @Test
    void getTransactions_whenSuccess_shouldReturnPageOfTransactionDto() {
        Long walletId = 1L;
        PageRequestInfo pageRequestInfo = new PageRequestInfo(0, 10);

        Wallet wallet = new Wallet();
        wallet.setId(walletId);

        Transaction transaction = mock(Transaction.class);
        TransactionDto transactionDto = mock(TransactionDto.class);

        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findTransactionByWallet_Id(eq(walletId), any(Pageable.class))).thenReturn(transactionPage);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        Page<TransactionDto> result = transactionService.getTransactions(walletId, pageRequestInfo);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(transactionDto, result.getContent().get(0));
    }

    @Test
    void updateTransactionStatus_whenTransactionNotFound_shouldThrowException() {
        Long transactionId = 1L;
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest(TransactionStatus.APPROVED);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> transactionService.updateTransactionStatus(statusUpdateRequest, transactionId));

        assertEquals("valid.transaction.not.exist", ex.getMessage());
    }

    @Test
    void updateTransactionStatus_whenDepositTransactionAndApproved_shouldDepositToWallet() {
        Long transactionId = 1L;
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest(TransactionStatus.APPROVED);

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setId(1L);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(BigDecimal.valueOf(200));
        depositTransaction.setWallet(wallet);

        TransactionDto transactionDto = mock(TransactionDto.class);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(depositTransaction));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionMapper.toDto(depositTransaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.updateTransactionStatus(statusUpdateRequest, transactionId);

        assertNotNull(result);
        assertEquals(transactionDto, result);
        assertEquals(BigDecimal.valueOf(1200), wallet.getBalance());  // 1000 + 200
    }

    @Test
    void updateTransactionStatus_whenWithdrawTransactionAndApproved_shouldWithdrawFromWallet() {
        Long transactionId = 1L;
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest(TransactionStatus.APPROVED);

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setId(1L);
        wallet.setActiveForWithdraw(Boolean.TRUE);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setAmount(BigDecimal.valueOf(200));
        withdrawTransaction.setWallet(wallet);

        TransactionDto transactionDto = mock(TransactionDto.class);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(withdrawTransaction));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionMapper.toDto(withdrawTransaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.updateTransactionStatus(statusUpdateRequest, transactionId);

        assertNotNull(result);
        assertEquals(transactionDto, result);
        assertEquals(BigDecimal.valueOf(800), wallet.getBalance());  // 1000 - 200
    }

    @Test
    void updateTransactionStatus_whenStatusNotApproved_shouldNotChangeWalletBalance() {
        Long transactionId = 1L;
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest(TransactionStatus.PENDING);

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setId(1L);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setAmount(BigDecimal.valueOf(200));
        depositTransaction.setWallet(wallet);

        TransactionDto transactionDto = mock(TransactionDto.class);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(depositTransaction));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionMapper.toDto(depositTransaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.updateTransactionStatus(statusUpdateRequest, transactionId);

        assertNotNull(result);
        assertEquals(transactionDto, result);
        assertEquals(BigDecimal.valueOf(1000), wallet.getBalance());  // unchanged
    }
}
