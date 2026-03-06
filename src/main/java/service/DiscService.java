package service;

import pojo.MenuItem;

import java.util.List;

public class DiscService {
    public static double calculateSubtotal(List<MenuItem> items) {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getCost();
        }
        return total;
    }

    public static double calDisTl(List<MenuItem> items) {
        double subtotal = calculateSubtotal(items);

        int beverageCount = 0;
        int foodCount = 0;
        int otherCount = 0;

        for (MenuItem item : items) {
            String category = item.getCategory().toLowerCase();
            if (category.equals("beverage")) {
                beverageCount++;
            } else if (category.equals("food")) {
                foodCount++;
            } else if (category.equals("other")) {
                otherCount++;
            }
        }

        double finalTotal = subtotal;

        if (beverageCount >= 1 && foodCount >= 2) {
            finalTotal = finalTotal * 0.8;
        }

        if (subtotal >= 20 && otherCount >= 1) {
            finalTotal = finalTotal - 2.0;
        }

        return finalTotal;
    }

    public static String getDiscountDescription(List<MenuItem> items) {
        double subtotal = calculateSubtotal(items);

        int beverageCount = 0;
        int foodCount = 0;
        int otherCount = 0;

        for (MenuItem item : items) {
            String category = item.getCategory().toLowerCase();
            if (category.equals("beverage")) {
                beverageCount++;
            } else if (category.equals("food")) {
                foodCount++;
            } else if (category.equals("other")) {
                otherCount++;
            }
        }

        StringBuilder sb = new StringBuilder();
        boolean hasDiscount = false;

        if (beverageCount >= 1 && foodCount >= 2) {
            sb.append("20% off for 1 beverage + 2 food items");
            hasDiscount = true;
        }

        if (subtotal >= 20 && otherCount >= 1) {
            if (hasDiscount) {
                sb.append("; ");
            }
            sb.append("£2 off for subtotal >= £20 with 1 other item");
            hasDiscount = true;
        }

        if (!hasDiscount) {
            return "No discount applied";
        }

        return sb.toString();
    }
}