package pojo;

import exception.InvalidItems;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {

    @Test
    public void validMenuItemShouldBeCreated() {
        assertDoesNotThrow(() -> {
            MenuItem item = new MenuItem("BEV-001", "Americano", 3.50, "beverage");
            assertEquals("BEV-001", item.getId());
            assertEquals("Americano", item.getDescribe());
            assertEquals(3.50, item.getCost());
            assertEquals("beverage", item.getCategory());
        });
    }

    @Test
    public void invalidIdShouldThrowException() {
        assertThrows(InvalidItems.class, () ->
                new MenuItem("Ababa", "Americano", 3.50, "beverage"));
    }

    @Test
    public void invalidCategoryShouldThrowException() {
        assertThrows(InvalidItems.class, () ->
                new MenuItem("BEV-001", "Americano", 3.50, "drink"));
    }

    @Test
    public void emptyDescriptionShouldThrowException() {
        assertThrows(InvalidItems.class, () ->
                new MenuItem("BEV-001", "   ", 3.50, "beverage"));
    }

    @Test
    public void nonPositiveCostShouldThrowException() {
        assertThrows(InvalidItems.class, () ->
                new MenuItem("BEV-001", "Americano", 0, "beverage"));
    }
}