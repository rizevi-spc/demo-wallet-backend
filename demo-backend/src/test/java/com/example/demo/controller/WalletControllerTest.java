package com.example.demo.controller;

import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.enumeration.Currency;
import com.example.demo.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
@ActiveProfiles("test")
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addWallet_whenValidRequest_shouldReturnOk() throws Exception {
        CreateWalletDto dto = new CreateWalletDto(
                "My Wallet",
                Currency.USD,
                true,
                true,
                1L
        );

        mockMvc.perform(post("/wallet/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Wallet"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    void addWallet_whenInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Missing required 'name' and 'customerId'
        CreateWalletDto dto = new CreateWalletDto(
                null,
                Currency.EUR,
                true,
                true,
                null
        );

        mockMvc.perform(post("/wallet/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchWallets_whenValidRequest_shouldReturnPage() throws Exception {
        WalletSearchRequest searchRequest = new WalletSearchRequest(
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                new com.example.demo.dto.PageRequestInfo(0, 10)
        );

        mockMvc.perform(post("/wallet/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
