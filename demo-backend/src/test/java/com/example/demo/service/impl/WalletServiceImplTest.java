package com.example.demo.service.impl;

import com.example.demo.dto.PageRequestInfo;
import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Wallet;
import com.example.demo.enumeration.Currency;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.WalletMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.security.CustomerSecurityComponent;
import com.example.demo.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private CustomerSecurityComponent customerSecurityComponent;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void addWallet_whenCustomerNotFound_shouldThrowException() {
        CreateWalletDto createWalletDto = new CreateWalletDto(
                "MyWallet",
                Currency.TRY,
                true,
                true,
                999L
        );

        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> walletService.add(createWalletDto));
        assertEquals("valid.customer.not.exist", ex.getMessage());
    }

    @Test
    void addWallet_whenSuccess_shouldReturnWalletDto() {
        CreateWalletDto createWalletDto = new CreateWalletDto(
                "MyWallet",
                Currency.TRY,
                true,
                true,
                1L
        );

        Customer customer = new Customer();
        customer.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setBalance(BigDecimal.ZERO);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(walletMapper.toEntity(any())).thenReturn(wallet);
        when(walletRepository.save(any())).thenReturn(wallet);
        when(walletMapper.toDTO(any())).thenReturn(new WalletDto(
                1L, "MyWallet", Currency.TRY, true, true, BigDecimal.ZERO, 1L
        ));

        WalletDto result = walletService.add(createWalletDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("MyWallet", result.name());
    }

    @Test
    void getWallets_whenEmpty_shouldReturnEmptyPage() {
        WalletSearchRequest searchRequest = new WalletSearchRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new PageRequestInfo(0, 10)
        );

        when(walletRepository.searchWallets(any())).thenReturn(Page.empty());

        Page<WalletDto> result = walletService.getWallets(searchRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void getWallets_whenSuccess_shouldReturnPage() {
        WalletSearchRequest searchRequest = new WalletSearchRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new PageRequestInfo(0, 10)
        );

        Wallet wallet = new Wallet();
        WalletDto walletDto = new WalletDto(
                1L,
                "MyWallet",
                Currency.TRY,
                true,
                true,
                BigDecimal.TEN,
                1L
        );

        when(walletRepository.searchWallets(any())).thenReturn(new PageImpl<>(Collections.singletonList(wallet)));
        when(walletMapper.toDTO(any())).thenReturn(walletDto);

        Page<WalletDto> result = walletService.getWallets(searchRequest);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("MyWallet", result.getContent().get(0).name());
    }
}
