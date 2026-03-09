package service;

import pojo.MenuItem;
import java.util.List;

public class DiscService {
    public static double calculateSubtotal(List<MenuItem> items) {
        double s = 0;
        for (MenuItem i : items) s += i.getCost();
        return s;
    }

    private static int[] getC(List<MenuItem> items) {
        int[] c = new int[3];
        for (MenuItem i : items) {
            String t = i.getCategory().toLowerCase();
            if (t.equals("beverage")) c[0]++;
            else if (t.equals("food")) c[1]++;
            else if (t.equals("other")) c[2]++;
        }
        return c;
    }

    public static double calDisTl(List<MenuItem> items) {
        double s = calculateSubtotal(items), res = s;
        int[] c = getC(items);

        if (c[0] >= 1 && c[1] >= 2) res *= 0.8;
        if (s >= 20 && c[2] >= 1) res -= 2.0;
        return res;
    }

    public static String getDiscountDescription(List<MenuItem> items) {
        double s = calculateSubtotal(items);
        int[] c = getC(items);
        String r = "";
        if (c[0] >= 1 && c[1] >= 2) r = "20% off for 1 beverage + 2 food items";
        if (s >= 20 && c[2] >= 1) r += (r.isEmpty() ? "" : "; ") + "£2 off for subtotal >= £20 with 1 other item";
        return r.isEmpty() ? "No discount applied" : r;
    }
}