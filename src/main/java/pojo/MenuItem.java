package pojo;

import exception.InvaildItems;

import java.util.Set;

public class MenuItem {
    private String id;
    private String describe;
    private double cost;
    private String category;

    private static final Set<String> VALID_CATEGORIES =
            Set.of("beverage", "food", "other");

    public MenuItem(String id, String describe, double cost, String category) throws InvaildItems {
        if (id == null || !id.matches("[A-Z]+-\\d{3}")) {
            throw new InvaildItems("Invalid item id: " + id);
        }

        if (describe == null || describe.trim().isEmpty()) {
            throw new InvaildItems("Description cannot be empty");
        }

        if (cost <= 0) {
            throw new InvaildItems("Cost must be greater than 0");
        }

        if (category == null || !VALID_CATEGORIES.contains(category.toLowerCase())) {
            throw new InvaildItems("Invalid category: " + category);
        }

        this.id = id;
        this.describe = describe;
        this.cost = cost;
        this.category = category.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public String getDescribe() {
        return describe;
    }

    public double getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return id + " - " + describe + " (£" + String.format("%.2f", cost) + ")";
    }
}