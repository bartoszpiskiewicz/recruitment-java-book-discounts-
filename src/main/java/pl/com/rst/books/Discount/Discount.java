package pl.com.rst.books.Discount;

public class Discount {

    private final int discount;
    private final DiscountType type;

    public Discount(int discount, DiscountType type) {
        this.discount = discount;
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public DiscountType getType() {
        return type;
    }
}
