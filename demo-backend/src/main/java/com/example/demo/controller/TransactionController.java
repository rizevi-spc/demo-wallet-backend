package com.example.demo.controller;

import com.example.demo.dto.PageRequestInfo;
import com.example.demo.dto.transaction.*;
import com.example.demo.service.DepositService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.WithdrawService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * Controller for transaction operations.
 */
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final DepositService depositService;
    private final WithdrawService withdrawService;

    /**
     * Returns all transactions for a wallet.
     *
     * @param walletId wallet id
     * @param pageRequestInfo pagination info
     * @return list of transactions or 204 if empty
     */
    @GetMapping("{walletId}")
    public ResponseEntity<Page<TransactionDto>> getAllTransactions(@PathVariable("walletId") Long walletId, @Valid PageRequestInfo pageRequestInfo) {
        return Optional.ofNullable(transactionService.getTransactions(walletId, pageRequestInfo))
                .filter(not(Streamable::isEmpty))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    /**
     * Creates a deposit transaction.
     *
     * @param createDepositDto deposit request
     * @return deposit info or 204 if failed
     */
    @PostMapping("deposit")
    public ResponseEntity<DepositDto> saveDeposit(@Valid @RequestBody CreateDepositDto createDepositDto) {
        return Optional.ofNullable(depositService.processDeposit(createDepositDto))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    /**
     * Creates a withdraw transaction.
     *
     * @param createWithdrawDto withdraw request
     * @return withdraw info or 204 if failed
     */
    @PostMapping("withdraw")
    public ResponseEntity<WithdrawDto> saveWithdraw(@Valid @RequestBody CreateWithdrawDto createWithdrawDto) {
        return Optional.ofNullable(withdrawService.processWithdraw(createWithdrawDto))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    /**
     * Updates the status of a transaction.
     *
     * @param transactionId transaction id
     * @param updateRequest status update request
     * @return updated transaction or 204 if failed
     */
    @PatchMapping("updateStatus/{transactionId}")
    public ResponseEntity<TransactionDto> updateStatus(@PathVariable("transactionId") Long transactionId, @Valid @RequestBody StatusUpdateRequest updateRequest) {
        return Optional.ofNullable(transactionService.updateTransactionStatus(updateRequest, transactionId))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }
}