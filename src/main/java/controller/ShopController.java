package main.java.controller;
import main.java.pojo.MenuItem;
import main.java.service.DiscService;
import java.util.ArrayList;
import java.util.List;

public class ShopController {
    private DiscService discService = new DiscService();
    private List<MenuItem> cur = new ArrayList<>();
    public void addItem(MenuItem item) {
        cur.add(item);
    }

    public double checkout() {
        return discService.calDisTl(cur);
    }
}
