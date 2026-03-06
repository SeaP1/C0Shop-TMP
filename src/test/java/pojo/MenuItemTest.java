package test.java.pojo;
import main.java.exception.InvaildItems;
import main.java.pojo.MenuItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// 用的是Junit5, 但我不确定有没有指定版本
public class MenuItemTest {
    @Test
    void testValidMenu() {
        assertDoesNotThrow(() -> {
            new MenuItem("Ababa", "Coffee", 3.50, "BEVERAGES");
        });
    }
    @Test
    void testInvalidId() {
        assertThrows(InvaildItems.class, () -> {
            new MenuItem("Invvvvvvalid", "Tea", 2.00, "BEVERAGES");
        });
    }
}
