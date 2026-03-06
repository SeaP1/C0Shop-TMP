package service;
import pojo.MenuItem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RpService {
//    public String printReport(Map<String, MenuItem> menu, List<MenuItem> allOrderedItems) {
//        StringBuilder s = new StringBuilder("Report\n");
//        Map<String, Integer> counts = new HashMap<>();
//        double res = 0;
//        for (MenuItem item : allOrderedItems) {
//            counts.put(item.getId(), counts.getOrDefault(item.getId(), 0) + 1);
//            res += item.getCost();
//        }
//        for (MenuItem item : menu.values()) {
//            int count = counts.getOrDefault(item.getId(), 0);
//            s.append(String.format("Item: %s times: %d\n", item.getDescribe(), count));
//        }
//        s.append(String.format("Total incomes: £%.2f\n", res));
//        return s.toString();
//    }

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

        sb.append("\nTotal cost of all orders: £").append(totalCost).append("\n");

        return sb.toString();
    }

    public static void writeReportToFile(String report, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
