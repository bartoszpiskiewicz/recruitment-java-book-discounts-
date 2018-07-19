package pl.com.rst.books.Discount;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.rst.books.Book.Book;
import pl.com.rst.books.Book.BookNotFoundException;
import pl.com.rst.books.Book.BookRepository;

import java.util.ArrayList;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

public class DiscountServiceTest {

    BookRepository bookRepository;

    Book book;

    DiscountService discountService;

    @Before
    public void init() {
        bookRepository = Mockito.mock(BookRepository.class);
        discountService = new DiscountService(bookRepository);
        book = Mockito.mock(Book.class);
        Mockito.when(bookRepository.getBook(anyLong())).thenReturn(Optional.of(book));
        Mockito.when(book.getPrice()).thenReturn(100f);
    }

    @Test
    public void shouldReturnNoDiscountWhenNoCodeIsPassed() throws Exception {
        //given

        //when
        DiscountService.DiscountResult result = discountService.getDiscount(5, 10, "Abc", new String[]{});

        //then
        assertEquals(0, result.getDiscount());
    }

    @Test
    public void test() throws Exception {
        //given
        Discount value = new Discount(10, DiscountType.MONEY);
        Mockito.when(book.useDiscountCode(any())).thenReturn(value);
        Mockito.when(book.isCodeNotUsed(any())).thenReturn(true);

        //when
        DiscountService.DiscountResult result = discountService.getDiscount(5, 20, "Abc", new String[]{"code", "large-order"});

        //then
        assertEquals(10, result.getDiscount());
    }

    @Test
    public void shouldReturnDiscountForLargeOrder() {
        //given
        Discount value = new Discount(20, DiscountType.MONEY);
        Mockito.when(book.getLargeOrderDiscount()).thenReturn(value);

        //when
        DiscountService.DiscountResult result = discountService.getDiscount(5, 3000, "Abc", new String[]{"large-order"});

        //then
        assertEquals(20, result.getDiscount());
    }

    @Test
    public void shouldReturnZeroDiscountForLargeOrderAndLowPrice() throws Exception {
        //given

        //when
        DiscountService.DiscountResult result = discountService.getDiscount(5, 100, "Abc", new String[]{"large-order"});

        //then
        assertEquals(0, result.getDiscount());
    }

    @Test
    public void shouldNotDiscountCodeTwice() throws Exception {
        //given
        final Book book = new Book(new ArrayList<>(), 20, null, new Discount(10, DiscountType.MONEY));
        Mockito.when(bookRepository.getBook(anyLong())).thenReturn(Optional.of(book));

        DiscountService.DiscountResult result = discountService.getDiscount(5, 20, "Abc", new String[]{"code"});
        assertEquals(10, result.getDiscount());

        //when
        result = discountService.getDiscount(5, 20, "Abc", new String[]{"code"});

        //then
        assertEquals(0, result.getDiscount());
    }

    @Test(expected = BookNotFoundException.class)
    public void shouldThrowExceptionWhenBookDoesNotExist() throws Exception {
        //given
        Mockito.when(bookRepository.getBook(anyLong())).thenReturn(Optional.empty());

        //when
        discountService.getDiscount(5, 100, "Abc", new String[]{"large-order"});

        //then exception should be thrown
    }

}