package utils;

import exception.InvaildItems;
import pojo.MenuItem;
import pojo.Orders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadUtils {

    private static BufferedReader getResourceReader(String resourceName) {
        InputStream is = LoadUtils.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            throw new RuntimeException("Resource not found: " + resourceName);
        }
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public static Map<String, MenuItem> ldMenu(String resourceName) {
        try (BufferedReader br = getResourceReader(resourceName)) {
            return readMenu(br);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read menu resource: " + resourceName, e);
        }
    }

    public static List<Orders> ldOrders(String resourceName, Map<String, MenuItem> menu) {
        try (BufferedReader br = getResourceReader(resourceName)) {
            return readOrders(br, menu);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read orders resource: " + resourceName, e);
        }
    }

    public static Map<String, MenuItem> ldMenuFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return readMenu(br);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read menu file: " + filePath, e);
        }
    }

    public static List<Orders> ldOrdersFromFile(String filePath, Map<String, MenuItem> menu) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return readOrders(br, menu);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read orders file: " + filePath, e);
        }
    }

    private static Map<String, MenuItem> readMenu(BufferedReader br) throws IOException {
        Map<String, MenuItem> menu = new HashMap<>();
        String line;
        int lineNo = 0;

        while ((line = br.readLine()) != null) {
            lineNo++;
            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.println("Skipping invalid menu line " + lineNo + ": wrong number of fields");
                    continue;
                }

                String id = parts[0].trim();
                String category = parts[1].trim();
                String description = parts[2].trim();
                double cost = Double.parseDouble(parts[3].trim());

                MenuItem item = new MenuItem(id, description, cost, category);
                menu.put(id, item);

            } catch (InvaildItems | NumberFormatException e) {
                System.out.println("Skipping invalid menu line " + lineNo + ": " + e.getMessage());
            }
        }

        return menu;
    }

    private static List<Orders> readOrders(BufferedReader br, Map<String, MenuItem> menu) throws IOException {
        List<Orders> orders = new ArrayList<>();
        String line;
        int lineNo = 0;

        while ((line = br.readLine()) != null) {
            lineNo++;
            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.out.println("Skipping invalid order line " + lineNo + ": wrong number of fields");
                    continue;
                }

                String timestampStr = parts[0].trim();
                String customerId = parts[1].trim();
                String itemId = parts[2].trim();

                LocalDateTime timestamp = LocalDateTime.parse(timestampStr);

                if (!itemId.matches("[A-Z]+-\\d{3}")) {
                    System.out.println("Skipping invalid order line " + lineNo + ": invalid item ID format");
                    continue;
                }

                if (!menu.containsKey(itemId)) {
                    System.out.println("Skipping invalid order line " + lineNo + ": item ID not found in menu");
                    continue;
                }

                Orders order = new Orders(customerId, timestamp, itemId);
                orders.add(order);

            } catch (DateTimeParseException e) {
                System.out.println("Skipping invalid order line " + lineNo + ": invalid timestamp");
            } catch (Exception e) {
                System.out.println("Skipping invalid order line " + lineNo + ": " + e.getMessage());
            }
        }

        return orders;
    }
}