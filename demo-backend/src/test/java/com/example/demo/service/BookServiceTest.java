package com.example.demo.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
//    @InjectMocks
//    private BookServiceImpl bookService;
//    @Mock
//    private BookRepository bookRepository;
//    @Spy
//    private BookMapper bookMapper = new BookMapperImpl();
//
//    @Test
//    void test_add_then_exception() {
//        when(bookRepository.save(any()))
//                .thenReturn(new BookStock());
//        mockDto();
//        assertNotNull(bookService.add(new BookInsert()));
//    }
//
//    @Test
//    void test_update_then_exception() {
//        assertThrows(CustomValidationException.class, () -> bookService.update(new BookDto()));
//    }
//
//    @Test
//    void test_update_then_ok() {
//        when(bookRepository.findById(any()))
//                .thenReturn(Optional.of(new BookStock()));
//        when(bookRepository.save(any()))
//                .thenReturn(new BookStock());
//        mockDto();
//
//        assertNotNull(bookService.update(new BookDto()));
//    }
//
//    private void mockDto() {
////        when(bookMapper.toDTO(any(BookStock.class)))
////                .thenReturn(new BookDto());
//    }
//
//    @Test
//    void test_get_all_then_exception() {
//        when(bookRepository.findAll(any(Pageable.class)))
//                .thenReturn(getSingletonPageOf(BookStock.class));
//        mockDto();
//        assertFalse(bookService.getAllBooks(PageRequestInfo.of(0, 5)).isEmpty());
//    }
//
//    @SneakyThrows
//    static <T> Page<T> getSingletonPageOf(Class<T> cls) {
//        final List<T> stockList = Collections.singletonList(cls.getDeclaredConstructor().newInstance());
//        Page<T> bookStocks = new PageImpl<>(stockList, Pageable.unpaged(), stockList.size());
//        return bookStocks;
//    }
}
