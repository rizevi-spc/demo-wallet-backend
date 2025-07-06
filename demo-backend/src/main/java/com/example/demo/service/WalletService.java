package com.example.demo.service;


import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.dto.PageRequestInfo;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing wallets.
 */
public interface WalletService {
    /**
     * Adds a new wallet.
     *
     * @param walletDto the wallet data transfer object containing wallet details
     * @return the created wallet data transfer object
     */
    WalletDto add(CreateWalletDto walletDto);


    /**
     * Retrieves a wallet by its ID and user ID.
     *
     * @param searchRequest the request containing the wallet ID and user ID
     * @return the wallet data transfer object if found, or null if not found
     */
    Page<WalletDto> getWallets(WalletSearchRequest searchRequest);

}
