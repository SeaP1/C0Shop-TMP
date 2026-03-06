package controller;

import pojo.MenuItem;
import pojo.Orders;
import service.DiscService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopController {
    private Map<String, MenuItem> menu;
    private List<MenuItem> cur;
    private List<MenuItem> allOrderedItems;

    public ShopController(Map<String, MenuItem> menu, List<Orders> existingOrders) {
        this.menu = menu;
        this.cur = new ArrayList<>();
        this.allOrderedItems = new ArrayList<>();

        for (Orders order : existingOrders) {
            MenuItem item = menu.get(order.getItemId());
            if (item != null) {
                allOrderedItems.add(item);
            }
        }
    }

    public void addItem(MenuItem item) {
        if (item != null) {
            cur.add(item);
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < cur.size()) {
            cur.remove(index);
        }
    }

    public List<MenuItem> getCurrentItems() {
        return cur;
    }

    public double checkout() {
        double finalBill = DiscService.calDisTl(cur);
        allOrderedItems.addAll(cur);
        cur.clear();
        return finalBill;
    }

    public String getBillText() {
        StringBuilder sb = new StringBuilder();

        if (cur.isEmpty()) {
            sb.append("No items in cart.");
            return sb.toString();
        }

        sb.append("Items in current order:\n");
        for (MenuItem item : cur) {
            sb.append("- ")
                    .append(item.getDescribe())
                    .append(" (£")
                    .append(String.format("%.2f", item.getCost()))
                    .append(")\n");
        }

        double subtotal = DiscService.calculateSubtotal(cur);
        double finalTotal = DiscService.calDisTl(cur);
        String discountInfo = DiscService.getDiscountDescription(cur);

        sb.append("\nSubtotal: £").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("Discount: ").append(discountInfo).append("\n");
        sb.append("Final total: £").append(String.format("%.2f", finalTotal)).append("\n");

        return sb.toString();
    }

    public Map<String, MenuItem> getMenu() {
        return menu;
    }

    public List<MenuItem> getAllOrderedItems() {
        return allOrderedItems;
    }
}