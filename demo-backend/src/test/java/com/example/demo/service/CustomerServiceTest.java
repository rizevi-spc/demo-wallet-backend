package com.example.demo.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
//    @InjectMocks
//    private CustomerServiceImpl customerService;
//    @Mock
//    private CustomerRepository customerRepository;
//    @Mock
//    private WalletRepository orderRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private RoleRepository roleRepository;
//    @InjectMocks
//    private CustomerMapper customerMapper = spy(new CustomerMapperImpl());
//    @Spy
//    private UserMapper userMapper = new UserMapperImpl();
//    @Mock
//    private WalletMapper orderMapper;
//    @Mock
//    private PasswordEncoder encoder;
//
//    @Test
//    void test_add_then_ok() {
//        when(userRepository.save(any()))
//                .thenReturn(new User());
//        when(roleRepository.findRoleByName(any()))
//                .thenReturn(Optional.of(new Role()));
//        when(customerRepository.save(any()))
//                .thenReturn(new Customer());
//        final CustomerDto customerDto = new CustomerDto();
//        customerDto.setUser(new UserDto());
//        assertNotNull(customerService.add(customerDto));
//    }
//
//    @Test
//    void test_get_orders_then_ok() {
//        when(orderRepository.findCustomerOrderByCustomer_Id(anyLong(), any(Pageable.class)))
//                .thenReturn(getSingletonPageOf(Wallet.class));
//        when(orderMapper.toDTO(any()))
//                .thenReturn(new WalletDto());
//        assertFalse(customerService.getOrdersOfCustomer(1L, PageRequestInfo.of(0, 5))
//                .isEmpty());
//    }
}
