package exception;

import pojo.MenuItem;
import java.time.LocalDateTime;
import java.util.Map;

public class LoadValidator {
    public static void vM(String[] p) throws InvalidMenuLineException {
        if (p.length != 4) throw new InvalidMenuLineException("wrong fields");
    }
    public static LocalDateTime vO(String[] p, Map<String, MenuItem> m) throws InvalidOrderLineException {
        if (p.length != 3) throw new InvalidOrderLineException("wrong fields");
        String s = p[0].trim(), id = p[2].trim();
        try {
            if (!id.matches("[A-Z]+-\\d{3}")) throw new Exception("bad ID fmt");
            if (!m.containsKey(id)) throw new Exception("ID not in menu");
            return LocalDateTime.parse(s);
        } catch (Exception e) {
            String msg = e.getMessage();
            throw new InvalidOrderLineException(msg.contains("Text") ? "invalid timestamp" : msg);
        }
    }
}