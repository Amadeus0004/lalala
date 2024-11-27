package models;

public class FoodItem {
    private String name;
    private int deliveryTime; // Delivery time in ticks

    public FoodItem(String name, int deliveryTime) {
        this.name = name;
        this.deliveryTime = deliveryTime;
    }

    public String getName() {
        return name;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}