package main.java;

import main.java.Guis.ShopGuis;
import main.java.pojo.MenuItem;
import main.java.utils.LoadUtils;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // test.txt 如果有
        String menuFilePath = "menu.txt";
        // System.out.println("Start");
        Map<String, MenuItem> menu = LoadUtils.ldMenu(menuFilePath);
        if (menu.isEmpty()) {
            System.err.println("No files");
            return;
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ShopGuis(menu);
        });
        System.out.println("Go");
    }
}