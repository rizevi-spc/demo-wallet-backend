package com.example.demo.controller;


import com.example.demo.repository.CustomerRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
@ActiveProfiles("test")
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void insert_then_ok() throws Exception {

        mockMvc.perform(post("/customer/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name" : "ali",
                                  "surname" : "veli",
                                  "user" : {
                                    "username" : "ali@veli.com",
                                    "password":"test"
                                  }
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void insert_then_bad_request() throws Exception {
        mockMvc.perform(post("/customer/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name" : "ali",
                                  "surname" : "veli",
                                  "user" : {
                                    "username" : "ali"
                                  }
                                }"""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
