package exception;

import pojo.MenuItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class LoadDataValidator {

    public static void validateMenuLine(String[] parts)
            throws InvalidMenuLineException {
        if (parts.length != 4) {
            throw new InvalidMenuLineException("wrong number of fields");
        }
    }

    public static LocalDateTime validateOrderLine(String[] parts, Map<String, MenuItem> menu)
            throws InvalidOrderLineException {
        if (parts.length != 3) {
            throw new InvalidOrderLineException("wrong number of fields");
        }

        String timestampStr = parts[0].trim();
        String itemId = parts[2].trim();

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timestampStr);
        } catch (DateTimeParseException e) {
            throw new InvalidOrderLineException("invalid timestamp");
        }

        if (!itemId.matches("[A-Z]+-\\d{3}")) {
            throw new InvalidOrderLineException("invalid item ID format");
        }

        if (!menu.containsKey(itemId)) {
            throw new InvalidOrderLineException("item ID not found in menu");
        }

        return timestamp;
    }
}