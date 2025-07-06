package com.example.demo.repository;

import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.entity.Wallet;
import org.springframework.data.domain.Page;

/**
 * Repository interface for dynamic wallet search operations.
 */
public interface DynamicWalletRepository {
    Page<Wallet> searchWallets(WalletSearchRequest name);
}
