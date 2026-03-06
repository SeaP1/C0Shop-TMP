package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pojo.MenuItem;
import pojo.Orders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void testLdMenu_SkipsInvalidLines() throws IOException {
        Path menuFile = tempDir.resolve("menu.txt");
        Files.writeString(menuFile,
                "BEV-001,beverage,Tea,3.0\n" +          // valid
                        "BAD_ID,beverage,Coffee,4.0\n" +        // invalid id
                        "FOOD-001,food,Burger,8.0\n" +          // valid
                        "OTHER-001,other,Napkin,abc\n" +        // invalid cost
                        "FOOD-002,wrongcat,Pizza,6.0\n" +       // invalid category
                        "FOOD-003,food,,5.0\n"                  // empty description
        );

        Map<String, MenuItem> menu = LoadUtils.ldMenuFromFile(menuFile.toString());

        assertEquals(2, menu.size());
        assertTrue(menu.containsKey("BEV-001"));
        assertTrue(menu.containsKey("FOOD-001"));
    }

    @Test
    void testLdOrders_SkipsInvalidLines() throws IOException {
        Path menuFile = tempDir.resolve("menu.txt");
        Files.writeString(menuFile,
                "BEV-001,beverage,Tea,3.0\n" +
                        "FOOD-001,food,Burger,8.0\n" +
                        "OTHER-001,other,Napkin,2.0\n"
        );

        Map<String, MenuItem> menu = LoadUtils.ldMenuFromFile(menuFile.toString());

        Path ordersFile = tempDir.resolve("orders.txt");
        Files.writeString(ordersFile,
                "2025-01-01T10:00:00,C001,BEV-001\n" +   // valid
                        "bad-time,C002,FOOD-001\n" +             // invalid timestamp
                        "2025-01-01T10:01:00,C003,BAD-XYZ\n" +   // invalid format
                        "2025-01-01T10:02:00,C004,FOOD-999\n" +  // not in menu
                        "2025-01-01T10:03:00,C005,OTHER-001\n"   // valid
        );

        List<Orders> orders = LoadUtils.ldOrdersFromFile(ordersFile.toString(), menu);

        assertEquals(2, orders.size());
        assertEquals("BEV-001", orders.get(0).getItemId());
        assertEquals("OTHER-001", orders.get(1).getItemId());
    }

    @Test
    void testLdOrders_SkipsWrongFieldCount() throws IOException {
        Path menuFile = tempDir.resolve("menu.txt");
        Files.writeString(menuFile,
                "BEV-001,beverage,Tea,3.0\n"
        );

        Map<String, MenuItem> menu = LoadUtils.ldMenuFromFile(menuFile.toString());

        Path ordersFile = tempDir.resolve("orders.txt");
        Files.writeString(ordersFile,
                "2025-01-01T10:00:00,C001,BEV-001\n" +   // valid
                        "2025-01-01T10:01:00,C002\n" +           // too few fields
                        "2025-01-01T10:02:00,C003,BEV-001,EXTRA\n" // too many fields
        );

        List<Orders> orders = LoadUtils.ldOrdersFromFile(ordersFile.toString(), menu);

        assertEquals(1, orders.size());
        assertEquals("BEV-001", orders.get(0).getItemId());
    }

    @Test
    void testLdOrders_EmptyFile() throws IOException {
        Path menuFile = tempDir.resolve("menu.txt");
        Files.writeString(menuFile,
                "BEV-001,beverage,Tea,3.0\n"
        );

        Map<String, MenuItem> menu = LoadUtils.ldMenuFromFile(menuFile.toString());

        Path ordersFile = tempDir.resolve("orders.txt");
        Files.writeString(ordersFile, "");

        List<Orders> orders = LoadUtils.ldOrdersFromFile(ordersFile.toString(), menu);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testLdMenu_EmptyFile() throws IOException {
        Path menuFile = tempDir.resolve("menu.txt");
        Files.writeString(menuFile, "");

        Map<String, MenuItem> menu = LoadUtils.ldMenuFromFile(menuFile.toString());

        assertTrue(menu.isEmpty());
    }
}