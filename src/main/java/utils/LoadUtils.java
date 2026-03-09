package utils;

import exception.*;
import pojo.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class LoadUtils {
    private static BufferedReader getBR(String s, boolean f) throws Exception {
        // The decision is whether to read the file stream from the physical path or the resource stream from the classpath.
        InputStream is = f ? new FileInputStream(s) : LoadUtils.class.getClassLoader().getResourceAsStream(s);
        if (is == null) throw new RuntimeException("Missing: " + s);
        // UTF8
        return new BufferedReader(new InputStreamReader(is, "UTF-8"));
    }
    // menu and orders
    public static Map<String, MenuItem> ldMenu(String s) { return rM(s, false); }
    public static Map<String, MenuItem> ldMenuFromFile(String s) { return rM(s, true); }
    public static List<Orders> ldOrders(String s, Map<String, MenuItem> m) { return rO(s, m, false); }
    public static List<Orders> ldOrdersFromFile(String s, Map<String, MenuItem> m) { return rO(s, m, true); }
    // read menu
    private static Map<String, MenuItem> rM(String s, boolean f) {
        Map<String, MenuItem> m = new HashMap<>();
        try (BufferedReader br = getBR(s, f)) {
            String l; int n = 0;
            while ((l = br.readLine()) != null) {
                n++; if (l.trim().isEmpty()) continue; // skip empty line
                try {
                    String[] p = l.split(",");
                    LoadValidator.vM(p); // check up
                    m.put(p[0].trim(), new MenuItem(p[0].trim(), p[2].trim(), Double.parseDouble(p[3].trim()), p[1].trim()));
                } catch (InvalidMenuLineException | InvalidItemsException | NumberFormatException e) {
                    // If error happen
                    LogUtils.add("M_Err L" + n + ": " + e.getMessage());
                }
            }
        } catch (Exception e) { throw new RuntimeException(e); }
        return m;
    }

    // read order
    // same as above
    private static List<Orders> rO(String s, Map<String, MenuItem> m, boolean f) {
        List<Orders> os = new ArrayList<>();
        try (BufferedReader br = getBR(s, f)) {
            String l; int n = 0;
            while ((l = br.readLine()) != null) {
                n++; if (l.trim().isEmpty()) continue;
                try {
                    String[] p = l.split(",");
                    LocalDateTime t = LoadValidator.vO(p, m);
                    os.add(new Orders(p[1].trim(), t, p[2].trim()));
                } catch (InvalidOrderLineException e) {
                    LogUtils.add("O_Err L" + n + ": " + e.getMessage());
                } catch (Exception e) {
                    LogUtils.add("O_SysErr L" + n + ": " + e.getMessage());
                }
            }
        } catch (Exception e) { throw new RuntimeException(e); }
        return os;
    }
}