package pl.com.rst.books.Discount;

import pl.com.rst.books.Book.Book;
import pl.com.rst.books.Book.BookNotFoundException;
import pl.com.rst.books.Book.BookRepository;

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

        boolean discountAlreadyCalculated = false;
        float totalDiscount = 0;
        Book book = bookRepository.getBook(bookId).orElseThrow(BookNotFoundException::new);

        for (String discount : availableDiscounts) {
            switch (discount) {
                case "large-order":
                    if (!discountAlreadyCalculated && orderPrice > 300) {
                        Discount largeOrderDiscount = book.getLargeOrderDiscount();
                        totalDiscount += book.getPrice() * Utils.percentToFloat(largeOrderDiscount.getDiscount());
                    }

                    break;
                case "code":

                    if (book.isCodeNotUsed(discountCode)) {
                        Discount codeDiscount = book.useDiscountCode(discountCode);

                        totalDiscount += codeDiscount.getType() == DiscountType.PERCENT ?
                                book.getPrice() * Utils.percentToFloat(codeDiscount.getDiscount())
                                : codeDiscount.getDiscount();
                    }

                    discountAlreadyCalculated = true;
                    break;
            }
        }

        return new DiscountResult((long)totalDiscount);
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
