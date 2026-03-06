package main.java.utils;
import main.java.pojo.MenuItem;
import main.java.exception.InvaildItems;
import java.io.*;
import java.util.*;

public class LoadUtils {
    public static Map<String, MenuItem> ldMenu(String path) {
        Map<String, MenuItem> menuMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 4) continue;
                    String id = parts[0].trim();
                    String category = parts[1].trim();
                    String describe = parts[2].trim();
                    double cost = Double.parseDouble(parts[3].trim());
                    MenuItem item = new MenuItem(id, describe, cost, category);
                    menuMap.put(id, item);
                } catch (InvaildItems | NumberFormatException e) { //debugs
                    System.err.println("0Ops Lines: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuMap;
    }
}
