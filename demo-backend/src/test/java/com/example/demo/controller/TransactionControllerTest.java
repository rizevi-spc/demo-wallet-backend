package com.example.demo.controller;


import com.example.demo.dto.transaction.CreateDepositDto;
import com.example.demo.dto.transaction.CreateWithdrawDto;
import com.example.demo.dto.transaction.StatusUpdateRequest;
import com.example.demo.entity.DepositTransaction;
import com.example.demo.entity.Wallet;
import com.example.demo.enumeration.Currency;
import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.enumeration.TransactionType;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanUp() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void saveDeposit_whenValid_returns200() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.ZERO);
        wallet = walletRepository.save(wallet);

        CreateDepositDto req = new CreateDepositDto(
                wallet.getId(),
                BigDecimal.valueOf(100),
                OppositePartyType.IBAN,
                "TR00ABC"
        );

        mockMvc.perform(post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(wallet.getId()))
                .andExpect(jsonPath("$.type").value(TransactionType.DEPOSIT.name()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void saveDeposit_whenInvalid_returns400() throws Exception {
        CreateDepositDto bad = new CreateDepositDto(null, BigDecimal.ZERO, null, "");
        mockMvc.perform(post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveWithdraw_whenValid_returns200() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setActiveForWithdraw(Boolean.TRUE);
        wallet = walletRepository.save(wallet);
        CreateWithdrawDto req = new CreateWithdrawDto(
                wallet.getId(),
                BigDecimal.valueOf(50),
                OppositePartyType.WALLET,
                "1234567890"
        );

        mockMvc.perform(post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(wallet.getId()))
                .andExpect(jsonPath("$.type").value(TransactionType.WITHDRAW.name()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void saveWithdraw_whenInvalid_returns400() throws Exception {
        CreateWithdrawDto bad = new CreateWithdrawDto(null, BigDecimal.ZERO, null, null);
        mockMvc.perform(post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_whenValid_returns200() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet = walletRepository.save(wallet);

        DepositTransaction tx = new DepositTransaction();
        tx.setWallet(wallet);
        tx.setAmount(BigDecimal.valueOf(30));
        tx.setStatus(TransactionStatus.APPROVED);
        tx = transactionRepository.save(tx);

        StatusUpdateRequest req = new StatusUpdateRequest(TransactionStatus.PENDING);

        mockMvc.perform(patch("/transaction/updateStatus/{id}", tx.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(TransactionStatus.PENDING.name()));
    }

    @Test
    void updateStatus_whenInvalid_returns400() throws Exception {
        StatusUpdateRequest bad = new StatusUpdateRequest(null);
        mockMvc.perform(patch("/transaction/updateStatus/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTransactions_whenEmpty_returns204() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.ZERO);
        wallet = walletRepository.save(wallet);

        mockMvc.perform(get("/transaction/{walletId}", wallet.getId())
                        .param("page", "0").param("size", "5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTransactions_whenNonEmpty_returns200() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.ZERO);
        wallet = walletRepository.save(wallet);

        DepositTransaction tx = new DepositTransaction();
        tx.setWallet(wallet);
        tx.setAmount(BigDecimal.valueOf(20));
        tx.setStatus(TransactionStatus.APPROVED);
        transactionRepository.save(tx);

        mockMvc.perform(get("/transaction/{walletId}", wallet.getId())
                        .param("page", "0").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].walletId").value(wallet.getId()))
                .andExpect(jsonPath("$.content[0].amount").value(20));
    }
}
