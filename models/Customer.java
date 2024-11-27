package models;

import java.util.List;

public class Customer implements User {
    private String name;
    private String id;

    public Customer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public void placeOrder(String foodName, FoodManager foodManager, List<Order> orders) {
        FoodItem foodItem = foodManager.getFoodItems().stream()
                .filter(item -> item.getName().equalsIgnoreCase(foodName))
                .findFirst()
                .orElse(null);

        if (foodItem == null) {
            System.out.println("Food item not found.");
            return;
        }

        Order order = new Order(name, foodItem.getName(), foodItem.getDeliveryTime(), false);
        orders.add(order);
        System.out.println("The order has been added to the list");

//        DeliveryStaff availableStaff = deliveryStaffList.stream()
//                .filter(DeliveryStaff::isAvailable)
//                .findFirst()
//                .orElse(null);
//
//        if (availableStaff != null) {
//            Order order = new Order(name, foodItem.getName(), foodItem.getDeliveryTime(), false);
//            orders.add(order);
//            availableStaff.setAvailable(false);
//            order.setAssignedStaff(availableStaff);
//            System.out.println("Order placed successfully and assigned to " + availableStaff.getName() + ".");
//        } else {
//            System.out.println("No available delivery staff.");
//        }
    }

    public void checkOrderStatus(List<Order> orders) {
        orders.stream()
                .filter(order -> order.getCustomerName().equals(this.name))
                .forEach(order -> {
                    String staffName = order.getAssignedStaff() != null ? order.getAssignedStaff().getName() : "Unassigned";
                    System.out.println("Order for " + order.getCustomerName() + ": " + order.getPizzaType() +
                            ", Remaining Ticks: " + order.getRemainingTicks() +
                            ", Completed: " + order.isCompleted() +
                            ", Assigned to: " + staffName);
                });
    }
}