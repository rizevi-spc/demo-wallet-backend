package com.example.demo.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
 class WalletServiceTest {
//    @InjectMocks
//    private WalletServiceImpl orderService;
//    @Mock
//    private WalletRepository orderRepository;
//    @Mock
//    private CustomerRepository customerRepository;
//    @Mock
//    private BookRepository bookRepository;
//    @Mock
//    private WalletMapper walletMapper;
//    @Mock
//    private BookOrderMapper bookOrderMapper;
//
//    @Test
//     void test_add_then_book_order_exception() {
//        final CustomValidationException ex = assertThrows(CustomValidationException.class,
//                () -> orderService.add(new WalletDto()));
//        assertEquals("valid.book.orders.empty", ex.getMessage());
//    }
//
//    @Test
//     void test_add_then_customer_exception() {
//        final WalletDto orderDto = new WalletDto();
//        orderDto.setBookOrders(Collections.singletonList(new BookOrderDto()));
//        final CustomValidationException ex = assertThrows(CustomValidationException.class,
//                () -> orderService.add(orderDto));
//        assertEquals("valid.customer.not.exist", ex.getMessage());
//    }
//
//    @Test
//     void test_add_then_stock_exception() {
//        final BookStock book = new BookStock();
//        book.setQuantity(0L);
//        book.setId(1L);
//        final WalletDto orderDto = getCustomerOrderDto(book);
//        final CustomValidationException ex = assertThrows(CustomValidationException.class,
//                () -> orderService.add(orderDto));
//        assertEquals("valid.book.stock.not.exist", ex.getMessage());
//    }
//
//    @Test
//     void test_add_then_stock_ok() {
//        final BookStock book = new BookStock();
//        book.setQuantity(5L);
//        book.setPrice(10D);
//        book.setId(1L);
//        final WalletDto orderDto = getCustomerOrderDto(book);
//        when(walletMapper.toDTO(any()))
//                .thenReturn(orderDto);
//
//        assertNotNull(orderService.add(orderDto));
//    }
//
//    @Test
//     void test_get_then_ok() {
//        when(orderRepository.findById(anyLong()))
//                .thenReturn(Optional.of(new Wallet()));
//        when(walletMapper.toDTO(any()))
//                .thenReturn(new WalletDto());
//        assertNotNull(orderService.getOrderById(1L));
//    }
//
//    @Test
//     void test_search_then_exception() {
//        final WalletSearchRequest searchRequest = new WalletSearchRequest();
//        searchRequest.setStartDate(LocalDateTime.now());
//        searchRequest.setEndDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
//        assertThrows(CustomValidationException.class,
//                () -> orderService.getOrdersBetweenDates(searchRequest));
//    }
//
//    @Test
//     void test_search_then_ok() {
//        when(walletMapper.toDTO(any()))
//                .thenReturn(new WalletDto());
//        when(orderRepository.findCustomerOrderByCreateDateTimeBetween(any(), any(), any()))
//                .thenReturn(getSingletonPageOf(Wallet.class));
//        final WalletSearchRequest searchRequest = new WalletSearchRequest();
//        searchRequest.setPageRequestInfo(PageRequestInfo.of(0, 5));
//        searchRequest.setStartDate(LocalDateTime.now());
//        searchRequest.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
//        assertNotNull(orderService.getOrdersBetweenDates(searchRequest));
//    }
//
//    @Test
//     void test_statistics_then_ok() {
//        when(orderRepository.findOrderStatistics())
//                .thenReturn(Collections.singletonList(new OrderStatistics()));
//        assertFalse(orderService.getStatistics().isEmpty());
//    }
//
//    private WalletDto getCustomerOrderDto(BookStock book) {
//        final WalletDto orderDto = new WalletDto();
//        orderDto.setCustomerId(1L);
//        orderDto.setBookOrders(Collections.singletonList(new BookOrderDto()));
//        final Customer customer = new Customer();
//        final Wallet order = new Wallet();
//        order.setCustomer(customer);
//        final BookOrder bookOrder = new BookOrder();
//        bookOrder.setBook(book);
//        bookOrder.setQuantity(1L);
//        order.setBookOrders(Collections.singletonList(bookOrder));
//        when(customerRepository.findById(anyLong()))
//                .thenReturn(Optional.of(customer));
//        when(bookRepository.findById(anyLong()))
//                .thenReturn(Optional.of(book));
//        when(walletMapper.toEntity(any()))
//                .thenReturn(order);
//        return orderDto;
//    }
}
