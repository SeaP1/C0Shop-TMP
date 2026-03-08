package service;
import pojo.MenuItem;
import pojo.Orders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RpService {

    public static String generateReport(Map<String, MenuItem> menu, List<MenuItem> allOrderedItems) {
        StringBuilder sb = new StringBuilder();
        double totalCost = 0.0;

        sb.append("=== Coffee Shop Summary Report ===\n");

        for (String id : menu.keySet()) {
            MenuItem menuItem = menu.get(id);

            int count = 0;
            for (MenuItem orderedItem : allOrderedItems) {
                if (orderedItem.getId().equals(id)) {
                    count++;
                    totalCost += orderedItem.getCost();
                }
            }

            sb.append("Item ID: ").append(menuItem.getId())
                    .append(", Description: ").append(menuItem.getDescribe())
                    .append(", Ordered: ").append(count).append(" times\n");
        }

        sb.append(String.format("%nTotal cost of all orders: £%.2f%n", totalCost));

        return sb.toString();
    }

    public static void writeReportToFile(String report, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将新产生的订单追加写入 orders.txt（资源目录中的文件）
     * 格式：timestamp,customerId,itemId
     */
    public static void appendOrdersToFile(List<Orders> newOrders, String filePath) {
        if (newOrders == null || newOrders.isEmpty()) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Orders order : newOrders) {
                writer.write(order.getTimestamp() + "," + order.getCUserId() + "," + order.getItemId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
