package main.java.service;
import main.java.pojo.MenuItem;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RpService {
    public String printReport(Map<String, MenuItem> menu, List<MenuItem> allOrderedItems) {
        StringBuilder s = new StringBuilder("Report\n");
        Map<String, Integer> counts = new HashMap<>();
        double res = 0;
        for (MenuItem item : allOrderedItems) {
            counts.put(item.getId(), counts.getOrDefault(item.getId(), 0) + 1);
            res += item.getCost();
        }
        for (MenuItem item : menu.values()) {
            int count = counts.getOrDefault(item.getId(), 0);
            s.append(String.format("Item: %s times: %d\n", item.getDescribe(), count));
        }
        s.append(String.format("Total incomes: £%.2f\n", res));
        return s.toString();
    }
}
