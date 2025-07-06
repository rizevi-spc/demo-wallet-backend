package com.example.demo.mapper;

import com.example.demo.dto.transaction.*;
import com.example.demo.entity.DepositTransaction;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.WithdrawTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

/**
 * Mapper interface for converting between Transaction entities and their corresponding DTOs.
 */
@Mapper(componentModel = "spring", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface TransactionMapper{
    
    @SubclassMapping(source = DepositTransaction.class, target = DepositDto.class)
    @SubclassMapping(source = WithdrawTransaction.class, target = WithdrawDto.class)
    @Mapping(target = "walletId", source = "wallet.id")
    TransactionDto toDto(Transaction transaction);

    @Mapping(target = "sourceType", source = "oppositePartyType")
    @Mapping(target = "sourceId", source = "oppositePartyId")
    @Mapping(target = "walletId", source = "wallet.id")
    DepositDto depositToDto(DepositTransaction depositDto);

    @Mapping(target = "destinationType", source = "oppositePartyType")
    @Mapping(target = "destinationId", source = "oppositePartyId")
    @Mapping(target = "walletId", source = "wallet.id")
    WithdrawDto withdrawToDto(WithdrawTransaction withdrawDto);

    @Mapping(source = "sourceType", target = "oppositePartyType")
    @Mapping(source = "sourceId", target = "oppositePartyId")
    @Mapping(source = "walletId", target = "wallet.id")
    DepositTransaction createDepositDtoToEntity(CreateDepositDto createDepositDto);

    @Mapping(source = "destinationType", target = "oppositePartyType")
    @Mapping(source = "destinationId", target = "oppositePartyId")
    @Mapping(source = "walletId", target = "wallet.id")
    WithdrawTransaction createWithdrawDtoToEntity(CreateWithdrawDto createWithdrawDto);

}