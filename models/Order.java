package models;

public class Order {
    private String customerName;
    private String pizzaType;
    private int remainingTicks;
    private boolean completed;
    private DeliveryStaff assignedStaff;
    private static final int MAX_ORDER_TICKS = 100;

    public Order(String customerName, String pizzaType, int deliveryTime, boolean completed) {
        this.customerName = customerName;
        this.pizzaType = pizzaType;
        this.remainingTicks = deliveryTime;
        this.completed = completed;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public DeliveryStaff getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(DeliveryStaff assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public void incrementTicks() {
        remainingTicks--;
        if (remainingTicks <= 0) {
            cancelOrder();
        }
    }

    private void cancelOrder() {
        this.completed = true;
        System.out.println("Order for " + customerName + " has been cancelled due to timeout.");
    }
}
