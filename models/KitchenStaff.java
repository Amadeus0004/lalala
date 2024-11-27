package models;

import java.util.List;

public class KitchenStaff implements User {
    private String name;
    private String id;

    public KitchenStaff(String name, String id) {
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

    public void viewOrders(List<Order> orders) {
        for (Order order : orders) {
            System.out.println("Order for " + order.getCustomerName() + ": " + order.getPizzaType() +
                    ", Remaining Ticks: " + order.getRemainingTicks() +
                    ", Completed: " + order.isCompleted());
        }
    }
}