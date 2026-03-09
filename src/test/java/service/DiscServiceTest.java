package service;

import exception.InvalidItemsException;
import org.junit.jupiter.api.Test;
import pojo.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiscServiceTest {

    private MenuItem item(String id, String desc, double cost, String category) throws InvalidItemsException {
        return new MenuItem(id, desc, cost, category);
    }

    // --- calculateSubtotal ---

    @Test
    public void subtotalOfEmptyListIsZero() {
        assertEquals(0.0, DiscService.calculateSubtotal(new ArrayList<>()), 0.001);
    }

    @Test
    public void subtotalSumsAllItems() throws InvalidItemsException {
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food")
        );
        assertEquals(6.30, DiscService.calculateSubtotal(items), 0.001);
    }

    // --- calDisTl: no discount ---

    @Test
    public void noDiscountWhenNeitherRuleApplies() throws InvalidItemsException {
        // 1 beverage, no food -> Rule 1 not met; subtotal < 20 -> Rule 2 not met
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage")
        );
        assertEquals(3.50, DiscService.calDisTl(items), 0.001);
    }

    @Test
    public void noDiscountWhenOnlyOneFoodWithBeverage() throws InvalidItemsException {
        // 1 beverage + 1 food is not enough for Rule 1 (needs 2 food)
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food")
        );
        assertEquals(6.30, DiscService.calDisTl(items), 0.001);
    }

    @Test
    public void noDiscountWhenSubtotalBelow20WithOtherItem() throws InvalidItemsException {
        // other item present but subtotal < 20 -> Rule 2 not met
        List<MenuItem> items = List.of(
                item("OTH-001", "Coffee Mug", 8.00, "other"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food")
        );
        // subtotal = 10.80 < 20 -> no discount
        assertEquals(10.80, DiscService.calDisTl(items), 0.001);
    }

    // --- calDisTl: Rule 1 only (20% off) ---

    @Test
    public void rule1AppliesWhenOneBeverageAndTwoFood() throws InvalidItemsException {
        // 1 beverage + 2 food, subtotal < 20 -> only Rule 1
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food")
        );
        // subtotal = 11.80, 20% off -> 11.80 * 0.8 = 9.44
        assertEquals(9.44, DiscService.calDisTl(items), 0.001);
    }

    @Test
    public void rule1AppliesWithMultipleBeveragesAndTwoFood() throws InvalidItemsException {
        // Rule 1 requires >= 1 beverage; more beverages still qualify
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("BEV-002", "Latte", 4.20, "beverage"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food")
        );
        // subtotal = 16.00, 20% off -> 16.00 * 0.8 = 12.80
        assertEquals(12.80, DiscService.calDisTl(items), 0.001);
    }

    // --- calDisTl: Rule 2 only (£2 off) ---

    @Test
    public void rule2AppliesWhenSubtotalExactly20WithOtherItem_boundary() throws InvalidItemsException {
        // Boundary: subtotal exactly £20.00 should still trigger Rule 2
        List<MenuItem> items = List.of(
                item("FOOD-001", "Burger", 10.0, "food"),
                item("OTH-001", "Napkin", 10.0, "other")
        );
        // subtotal = 20.00, Rule 2: 20.00 - 2.00 = 18.00
        assertEquals(18.00, DiscService.calDisTl(items), 0.001);
    }

    @Test
    public void rule2AppliesWhenSubtotalAbove20WithOtherItem() throws InvalidItemsException {
        // No beverage -> Rule 1 not met; subtotal > 20 + other -> Rule 2
        List<MenuItem> items = List.of(
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food"),
                item("OTH-001", "Coffee Mug", 8.00, "other")
        );
        // subtotal = 20.30, Rule 2: 20.30 - 2.00 = 18.30
        assertEquals(18.30, DiscService.calDisTl(items), 0.001);
    }

    @Test
    public void rule2DoesNotApplyWithoutOtherItem() throws InvalidItemsException {
        // subtotal >= 20 but no other item -> Rule 2 not met
        List<MenuItem> items = List.of(
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food")
        );
        // subtotal = 21.80, no other item -> no discount
        assertEquals(21.80, DiscService.calDisTl(items), 0.001);
    }

    // --- calDisTl: Both rules apply simultaneously ---

    @Test
    public void bothRulesApplySimultaneously() throws InvalidItemsException {
        // 1 beverage + 2 food + 1 other, subtotal >= 20 -> both rules apply
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food"),
                item("OTH-001", "Coffee Mug", 8.00, "other")
        );
        // subtotal = 21.00
        // Rule 1: 21.00 * 0.8 = 16.80
        // Rule 2: original subtotal(21.00) >= 20 -> 16.80 - 2.00 = 14.80
        assertEquals(14.80, DiscService.calDisTl(items), 0.001);
    }

    // --- getDiscountDescription ---

    @Test
    public void descriptionNoDiscount() throws InvalidItemsException {
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage")
        );
        assertEquals("No discount applied", DiscService.getDiscountDescription(items));
    }

    @Test
    public void descriptionRule1Only() throws InvalidItemsException {
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food")
        );
        assertEquals("20% off for 1 beverage + 2 food items",
                DiscService.getDiscountDescription(items));
    }

    @Test
    public void descriptionRule2Only() throws InvalidItemsException {
        List<MenuItem> items = List.of(
                item("FOOD-001", "Blueberry Muffin", 2.80, "food"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food"),
                item("OTH-001", "Coffee Mug", 8.00, "other")
        );
        assertEquals("£2 off for subtotal >= £20 with 1 other item",
                DiscService.getDiscountDescription(items));
    }

    @Test
    public void descriptionBothRules() throws InvalidItemsException {
        List<MenuItem> items = List.of(
                item("BEV-001", "Americano", 3.50, "beverage"),
                item("FOOD-002", "Ham Sandwich", 5.50, "food"),
                item("FOOD-003", "Chocolate Cake", 4.00, "food"),
                item("OTH-001", "Coffee Mug", 8.00, "other")
        );
        assertEquals("20% off for 1 beverage + 2 food items; £2 off for subtotal >= £20 with 1 other item",
                DiscService.getDiscountDescription(items));
    }
}
