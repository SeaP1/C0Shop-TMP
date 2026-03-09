package service;
import pojo.*;
import java.io.*;
import java.util.*;

public class RpService {

    public static String generateReport(Map<String, MenuItem> m, List<MenuItem> os) {
        StringBuilder sb = new StringBuilder("=== Coffee Shop Summary Report ===\n");
        double total = 0;
        // Map预处理 降低复杂度 O(N*M) -> O(N+M)
        Map<String, Integer> cnt = new HashMap<>();
        for (MenuItem i : os) {
            cnt.put(i.getId(), cnt.getOrDefault(i.getId(), 0) + 1);
            total += i.getCost();
        }

        for (MenuItem mi : m.values()) {
            int n = cnt.getOrDefault(mi.getId(), 0);
            sb.append("Item ID: ").append(mi.getId())
                    .append(", Description: ").append(mi.getDescribe())
                    .append(", Ordered: ").append(n).append(" times\n");
        }
        return sb.append(String.format("\nTotal cost: £%.2f\n", total)).toString();
    }

    public static void wR(String r, String f) {
        try (FileWriter w = new FileWriter(f)) { w.write(r); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void aO(List<Orders> os, String f) {
        if (os == null || os.isEmpty()) return;
        // 使用 PrintWriter 可以直接 println，代码最减
        try (PrintWriter p = new PrintWriter(new FileWriter(f, true))) {
            for (Orders o : os)
                p.println(o.getTimestamp() + "," + o.getCUserId() + "," + o.getItemId());
        } catch (Exception e) { e.printStackTrace(); }
    }
}