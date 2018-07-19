package pl.com.rst.books.Book;

import pl.com.rst.books.Discount.Discount;
import pl.com.rst.books.Discount.DiscountType;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private List<String> usedCodes = new ArrayList<>();
    private float price = 12;

    private Discount largeOrderDiscount = new Discount(10, DiscountType.PERCENT);
    private Discount codeDiscount = new Discount(25, DiscountType.MONEY);

    public Book(List<String> usedCodes, float price, Discount largeOrderDiscount, Discount codeDiscount) {
        this.usedCodes = usedCodes;
        this.price = price;
        this.largeOrderDiscount = largeOrderDiscount;
        this.codeDiscount = codeDiscount;
    }

    public Book() {
    }

    public float getPrice() {
        return price;
    }

    public Discount getLargeOrderDiscount() {
        return largeOrderDiscount;
    }

    public Discount useDiscountCode(String code) {
        usedCodes.add(code);
        return codeDiscount;
    }

    public boolean isCodeNotUsed(String code) {
        return !usedCodes.contains(code);
    }

}
