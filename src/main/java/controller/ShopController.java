package controller;

import pojo.*;
import service.DiscService;
import java.time.LocalDateTime;
import java.util.*;

public class ShopController {
    private Map<String, MenuItem> m;
    private List<MenuItem> c = new ArrayList<>(), all = new ArrayList<>();
    private List<Orders> nos = new ArrayList<>();
    private int nxt = 1;
    public ShopController(Map<String, MenuItem> menu, List<Orders> eos) {
        this.m = menu;
        int max = 0;
        for (Orders o : eos) {
            MenuItem i = m.get(o.getItemId());
            if (i != null) all.add(i);
            String u = o.getCUserId();
            // substring and re
            if (u != null && u.startsWith("CUST")) {
                try {
                    int v = Integer.parseInt(u.substring(4));
                    if (v > max) max = v;
                } catch (Exception e) {}
            }
        }
        this.nxt = max + 1;
    }

    public void add(MenuItem i) { if (i != null) c.add(i); }
    public void rem(int idx) { if (idx >= 0 && idx < c.size()) c.remove(idx); }
    public double chk() { // <- checkout
        double res = DiscService.calDisTl(c);
        all.addAll(c);
        String cid = String.format("CUST%03d", nxt++);
        LocalDateTime now = LocalDateTime.now();
        for (MenuItem i : c) nos.add(new Orders(cid, now, i.getId()));
        c.clear();
        return res;
    }

    public String getBill() {
        if (c.isEmpty()) return "No items in cart.";
        StringBuilder sb = new StringBuilder("Items in current order:\n");
        for (MenuItem i : c) sb.append("- ").append(i.getDescribe()).append(" (£").append(String.format("%.2f", i.getCost())).append(")\n");
        double s = DiscService.calculateSubtotal(c);
        double f = DiscService.calDisTl(c);
        return sb.append("\nSubtotal: £").append(String.format("%.2f", s))
                .append("\nDiscount: ").append(DiscService.getDiscountDescription(c))
                .append("\nFinal total: £").append(String.format("%.2f", f)).append("\n").toString();
    }

    public Map<String, MenuItem> getM() { return m; }
    public List<MenuItem> getC() { return c; }
    public List<MenuItem> getA() { return all; }
    public List<Orders> getN() { return nos; }
}