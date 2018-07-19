package pl.com.rst.books.Discount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UtilsTest {

    private float expected;
    private int percent;

    public UtilsTest(int percent, float expected) {
        this.percent = percent;
        this.expected = expected;
    }

    @Parameterized.Parameters(name= "{index}: percent: {0}, float: {1}")
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][] {
                { 10, 0.10f },
                { 25, 0.25f },
                { 99, 0.99f },
                { 1, 0.01f },
                { 5, 0.05f }
        });
    }

    @Test
    public void testUtil() throws Exception {
        float result = Utils.percentToFloat(percent);
        assertEquals(expected, result, 1);
    }
}