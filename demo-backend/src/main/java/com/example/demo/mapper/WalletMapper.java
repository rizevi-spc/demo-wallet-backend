package com.example.demo.mapper;


import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Wallet entity and Wallet DTOs.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface WalletMapper {
    @Mapping(target = "customerId", source = "customer.id")
    WalletDto toDTO(Wallet wallet);
    @Mapping(source = "customerId", target = "customer.id")
    Wallet toEntity(CreateWalletDto walletDto);
}