package com.example.demo.service.impl;

import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.RoleDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Customer;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void add_shouldSaveUserAndCustomer_thenReturnDto() {
        // RoleDto listesi eklenebilir
        List<RoleDto> roles = List.of(new RoleDto("1","ROLE_EMPLOYEE"));

        UserDto userDto = new UserDto(
                "user@example.com",          // username (email)
                "securePassword123",         // password (write-only)
                roles                       // roller
        );

        CustomerDto inputDto = new CustomerDto(null, "Ali", "Uzun", userDto);
        Customer entity = new Customer();
        entity.setId(1L);
        entity.setName("Ali");
        entity.setSurname("Uzun");

        when(customerMapper.toEntity(inputDto)).thenReturn(entity);
        when(userService.saveCustomerUser(userDto)).thenReturn(10L);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });
        when(customerMapper.toDTO(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            return new CustomerDto(c.getId(), c.getName(), c.getSurname(), userDto);
        });

        CustomerDto result = customerService.add(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Ali", result.name());
        assertEquals("Uzun", result.surname());
        assertEquals(userDto, result.user());

        verify(userService).saveCustomerUser(userDto);
        verify(customerRepository).save(entity);
    }
}
