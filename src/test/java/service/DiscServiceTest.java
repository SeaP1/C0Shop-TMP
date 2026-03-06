package service;

import exception.InvaildItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojo.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscServiceTest {

    private DiscService discService;

    @BeforeEach
    void setUp() {
        discService = new DiscService();
    }

    private MenuItem item(String id, String desc, double cost, String category) throws InvaildItems {
        return new MenuItem(id, desc, cost, category);
    }

    @Test
    void testCalculateSubtotal() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 3.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 8.0, "food"));
        items.add(item("OTHER-001", "Napkin", 1.0, "other"));

        double subtotal = discService.calculateSubtotal(items);

        assertEquals(12.0, subtotal, 0.0001);
    }

    @Test
    void testNoDiscount() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 3.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 8.0, "food"));

        double total = discService.calDisTl(items);

        assertEquals(11.0, total, 0.0001);
    }

    @Test
    void testOnlyTwentyPercentDiscount() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 4.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 8.0, "food"));
        items.add(item("FOOD-002", "Pizza", 8.0, "food"));

        double total = discService.calDisTl(items);

        assertEquals(16.0, total, 0.0001);
    }

    @Test
    void testOnlyTwoPoundsDiscount() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("FOOD-001", "Burger", 10.0, "food"));
        items.add(item("FOOD-002", "Pizza", 10.0, "food"));
        items.add(item("OTHER-001", "Napkin", 1.0, "other"));

        double total = discService.calDisTl(items);

        assertEquals(19.0, total, 0.0001);
    }

    @Test
    void testBothDiscountsApplied() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 4.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 10.0, "food"));
        items.add(item("FOOD-002", "Pizza", 10.0, "food"));
        items.add(item("OTHER-001", "Napkin", 2.0, "other"));

        double total = discService.calDisTl(items);

        assertEquals(18.8, total, 0.0001);
    }

    @Test
    void testBoundarySubtotalExactlyTwenty_WithOther() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("FOOD-001", "Burger", 10.0, "food"));
        items.add(item("OTHER-001", "Napkin", 10.0, "other"));

        double total = discService.calDisTl(items);

        assertEquals(18.0, total, 0.0001);
    }

    @Test
    void testBoundaryExactlyTwoFoodAndOneBeverage() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 2.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 5.0, "food"));
        items.add(item("FOOD-002", "Pizza", 5.0, "food"));

        double total = discService.calDisTl(items);

        assertEquals(9.6, total, 0.0001);
    }

    @Test
    void testEmptyOrder() {
        List<MenuItem> items = new ArrayList<>();

        double subtotal = discService.calculateSubtotal(items);
        double total = discService.calDisTl(items);

        assertEquals(0.0, subtotal, 0.0001);
        assertEquals(0.0, total, 0.0001);
    }

    @Test
    void testDiscountDescription_NoDiscount() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("FOOD-001", "Burger", 8.0, "food"));

        String desc = discService.getDiscountDescription(items);

        assertEquals("No discount applied", desc);
    }

    @Test
    void testDiscountDescription_BothDiscounts() throws InvaildItems {
        List<MenuItem> items = new ArrayList<>();
        items.add(item("BEV-001", "Tea", 4.0, "beverage"));
        items.add(item("FOOD-001", "Burger", 10.0, "food"));
        items.add(item("FOOD-002", "Pizza", 10.0, "food"));
        items.add(item("OTHER-001", "Napkin", 2.0, "other"));

        String desc = discService.getDiscountDescription(items);

        assertEquals("20% off + £2 off", desc);
    }
}