package pl.com.rst.books.Discount;

import pl.com.rst.books.Book.Book;
import pl.com.rst.books.Book.BookNotFoundException;
import pl.com.rst.books.Book.BookRepository;

import java.util.Arrays;

public class DiscountService {

    private static final DiscountResult ZERO_DISCOUNT = new DiscountResult(0);

    private final BookRepository bookRepository;

    public DiscountService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public DiscountResult getDiscount(long bookId, float orderPrice, String discountCode, String[] availableDiscounts) {
        if (availableDiscounts == null || availableDiscounts.length == 0) {
            return ZERO_DISCOUNT;
        }

        Book book = bookRepository.getBook(bookId).orElseThrow(BookNotFoundException::new);

        /**
         * As I read the code in loop I wonder if this part of business logic has sense.
         * discountAlreadyCalculated is set even if code is used
         * order of the codes changes discount
         *
         * I'll change the logic, but I without speaking to business I'm unable to understand it.
        */

        double totalDiscount = Arrays.stream(availableDiscounts)
                .filter("code"::equals)
                .mapToDouble(code -> toDiscount(book, discountCode))
                .sum();

        if (totalDiscount == 0 && orderPrice > 300) {
            totalDiscount += Arrays.stream(availableDiscounts)
                    .filter("large-order"::equals)
                    .mapToDouble(code -> computeLargeOrderDiscount(book))
                    .sum();
        }

        return new DiscountResult((long)totalDiscount);
    }

    private double computeLargeOrderDiscount(Book book) {
        Discount largeOrderDiscount = book.getLargeOrderDiscount();
        return book.getPrice() * Utils.percentToFloat(largeOrderDiscount.getDiscount());
    }

    private float toDiscount(Book book, String discountCode) {
        if (book.isCodeNotUsed(discountCode)) {
            Discount codeDiscount = book.useDiscountCode(discountCode);
            return codeDiscount.getType() == DiscountType.PERCENT ?
                    book.getPrice() * Utils.percentToFloat(codeDiscount.getDiscount())
                    : codeDiscount.getDiscount();
        }
        return 0;
    }

    public static class DiscountResult {
        private final long discount;

        public DiscountResult(long discount) {
            this.discount = discount;
        }

        public long getDiscount() {
            return discount;
        }

    }
}
