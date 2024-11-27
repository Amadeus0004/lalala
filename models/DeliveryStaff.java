package models;

import java.util.*;

public class DeliveryStaff implements User {
    private String name;
    private String id;
    private boolean available;
    private Order currentOrder;

    public DeliveryStaff(String name, String id) {
        this.name = name;
        this.id = id;
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void startDelivery(Order order, Scanner scanner) {
        if (!available) {
            System.out.println("You are already on a delivery.");
            return;
        }
        this.currentOrder = order;
        this.available = false;

        List<String> deliveryCommands = generateDeliveryCommands(order.getRemainingTicks());

        System.out.println("Delivery started for order: " + order.getPizzaType());
        System.out.println("Follow the delivery instructions carefully!");

        boolean deliverySuccessful = performDelivery(deliveryCommands, scanner);

        if (deliverySuccessful) {
            completeDelivery();
        } else {
            System.out.println("Delivery failed. Returning to shop.");
            this.available = true;
            this.currentOrder = null;
        }
    }

    private List<String> generateDeliveryCommands(int ticks) {
        List<String> directions = Arrays.asList("LEFT", "RIGHT", "STRAIGHT");
        List<String> commands = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < ticks; i++) {
            String direction = directions.get(random.nextInt(directions.size()));
            commands.add(direction);
        }
        commands.add("STOP");
        return commands;
    }

    private boolean performDelivery(List<String> commands, Scanner scanner) {
        System.out.println("Delivery Instructions: ");
        for (String command : commands) {
            while (true) {
                System.out.println("Enter the next command (Hint: " + command + "): ");
                String userInput = scanner.nextLine().toUpperCase();

                if (userInput.equals(command)) {
                    break; // Correct command
                } else if (userInput.equals("HELP")) {
                    System.out.println("Possible commands are: LEFT, RIGHT, STRAIGHT, STOP");
                } else {
                    System.out.println("Incorrect command. Try again or type HELP.");
                }
            }
        }
        return true;
    }

    private void completeDelivery() {
        if (currentOrder != null) {
            currentOrder.setCompleted(true);
            System.out.println("Delivery completed successfully!");
            this.available = true;
            this.currentOrder = null;
        }
    }
}