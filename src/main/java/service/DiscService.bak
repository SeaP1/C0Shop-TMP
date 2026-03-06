package main.java.service;
import main.java.pojo.MenuItem;
import java.util.List;

public class DiscService {
    public double calDisTl(List<MenuItem> items) {
        long cntbever = items.stream().filter(i -> i.getCategory().equalsIgnoreCase("BEVERAGES")).count();
        long cntfood = items.stream().filter(i -> i.getCategory().equalsIgnoreCase("FOOD")).count();
        long cntother = items.stream().filter(i -> i.getCategory().equalsIgnoreCase("OTHER")).count();
        double total = items.stream().mapToDouble(MenuItem::getCost).sum();
        double disc = 0;
        // Test
        // 先假设折扣是这样的, 之后改
        if (cntbever >= 1 && cntfood >= 2) {
            total = total * 0.8;
        }
        if (total >= 20 && cntother >= 1) {
            disc += 2;
        }
        return total - disc;
    }
}
