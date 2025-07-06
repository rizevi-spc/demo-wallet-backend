package com.example.demo.controller;

import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller for wallet operations.
 */
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    /**
     * Adds a new wallet.
     *
     * @param walletDto wallet create request
     * @return created wallet or 204 if failed
     */
    @PostMapping("add")
    public ResponseEntity<WalletDto> addWallet(@Valid @RequestBody CreateWalletDto walletDto) {
        return Optional.ofNullable(walletService.add(walletDto))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    /**
     * Searches wallets by criteria.
     *
     * @param searchRequest search request
     * @return list of wallets or 204 if empty
     */
    @PostMapping("search")
    public ResponseEntity<Page<WalletDto>> searchOrder(@Valid @RequestBody WalletSearchRequest searchRequest) {
        return Optional.ofNullable(walletService.getWallets(searchRequest))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }
}