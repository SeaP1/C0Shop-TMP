package pojo;

import exception.InvalidItems;

import java.util.Set;

public class MenuItem {
    private String id;
    private String describe;
    private double cost;
    private String category;

    private static final Set<String> VALID_CATEGORIES =
            Set.of("beverage", "food", "other");

    public MenuItem(String id, String describe, double cost, String category) throws InvalidItems {
        if (id == null || !id.matches("[A-Z]+-\\d{3}")) {
            throw new InvalidItems("Invalid item id: " + id);
        }

        if (describe == null || describe.trim().isEmpty()) {
            throw new InvalidItems("Description cannot be empty");
        }

        if (cost <= 0) {
            throw new InvalidItems("Cost must be greater than 0");
        }

        if (category == null || !VALID_CATEGORIES.contains(category.toLowerCase())) {
            throw new InvalidItems("Invalid category: " + category);
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