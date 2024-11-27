package models;

import java.util.*;

public class DeliveryStaff implements User {
    private final String name;
    private final String id;
    private boolean isAvailable;
    private Order currentOrder;
    private static final Random RANDOM = new Random();

    public DeliveryStaff(String name, String id) {
        this.name = name;
        this.id = id;
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void startDelivery(Order order, Scanner scanner) {
        this.currentOrder = order;
        this.isAvailable = false;

        if (isAvailable == false) {
            displayMessage("You are already on a delivery.");
            return;
        }


        List<String> deliveryCommands = generateDeliveryCommands(order.getRemainingTicks());
        displayMessage("Delivery started for order: " + order.getPizzaType());
        displayMessage("Follow the delivery instructions carefully!");

        if (performDelivery(deliveryCommands, scanner)) {
            completeDelivery();
        } else {
            displayMessage("Delivery failed. Returning to shop.");
            resetDeliveryState();
        }
    }

    public List<String> generateDeliveryCommands(int ticks) {
        List<String> directions = Arrays.asList("LEFT", "RIGHT", "STRAIGHT");
        List<String> commands = new ArrayList<>();
        for (int i = 0; i < ticks; i++) {
            commands.add(directions.get(RANDOM.nextInt(directions.size())));
        }
        commands.add("STOP");
        return commands;
    }
    public boolean performDelivery(List<String> commands, Scanner scanner) {
        displayMessage("Delivery Instructions: ");
        int retries = 3; // Max retries per command
        int leftTicks = currentOrder.getRemainingTicks();
        for (String command : commands) {
            while (retries > 0) {
                displayMessage("Enter the next command (Hint: " + command + "): ");
                String userInput = scanner.nextLine().toUpperCase();

                if (userInput.equals(command)) {
                    leftTicks--;
                    if (leftTicks == 0){
                        currentOrder.setCompleted(true);
                        System.out.println("The order is completed");
                        break;
                    }
                    break; // Correct command
                } else if (userInput.equals("HELP")) {
                    displayMessage("Possible commands are: LEFT, RIGHT, STRAIGHT, STOP");
                } else {
                    retries--;
                    if (retries > 0) {
                        displayMessage("Incorrect command. You have " + retries + " retries left.");
                    } else {
                        displayMessage("Delivery failed due to too many incorrect commands.");
                        return false;
                    }
                }
            }
            retries = 3; // Reset retries for the next command
        }
        return true;
    }

    private void completeDelivery() {
        if (currentOrder != null) {
            currentOrder.setCompleted(true);
            displayMessage("Delivery completed successfully!");
            resetDeliveryState();
        }
    }

    private void resetDeliveryState() {
        this.isAvailable = true;
        this.currentOrder = null;
    }

    private void displayMessage(String message) {
        System.out.println(message);
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
