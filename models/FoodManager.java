package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FoodManager implements User {
    private String name;
    private String id;
    private List<FoodItem> foodItems = new ArrayList<>();
    private static final String MENU_FILE = "menu.txt";

    public FoodManager(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void loadMenu() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    FoodItem foodItem = new FoodItem(parts[0], Integer.parseInt(parts[1]));
                    foodItems.add(foodItem);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Menu file not found. Starting with an empty menu.");
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }
    }

    public void saveMenu() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (FoodItem foodItem : foodItems) {
                writer.write(String.format("%s,%d\n", foodItem.getName(), foodItem.getDeliveryTime()));
            }
            System.out.println("Menu saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving menu: " + e.getMessage());
        }
    }

    public void addFoodItem(String name, int deliveryTime) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty");
        }
        if (deliveryTime <= 0) {
            throw new IllegalArgumentException("Delivery time must be positive");
        }
        FoodItem foodItem = new FoodItem(name, deliveryTime);
        foodItems.add(foodItem);
        System.out.println(name + " added successfully.");
    }

    public void viewMenu() {
        System.out.println("\n=== Menu ===");
        for (FoodItem item : foodItems) {
            System.out.println("Food: " + item.getName() + ", Delivery Time: " + item.getDeliveryTime() + " ticks");
        }
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }
}