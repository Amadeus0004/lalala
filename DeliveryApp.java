import managers.AuthenticationManager;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeliveryApp {
    //private static List<DeliveryStaff> deliveryStaffList = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Customer> registeredUsers = new ArrayList<>();
    private static List<KitchenStaff> registeredKitchenStaff = new ArrayList<>();
    private static List<DeliveryStaff> registeredDeliveryStaff = new ArrayList<>();
    private static List<FoodManager> registeredFoodManagers = new ArrayList<>();
    private static AuthenticationManager authenticationManager = new AuthenticationManager();
    private static User currentUser  = null;
    private static FileManager fileManager = new FileManager();
    private static FoodManager foodManager = new FoodManager("System food manager", "FM001");

    public static void main(String[] args) {
        foodManager.loadMenu();
        registeredDeliveryStaff = fileManager.loadDeliveryStaff();
        orders = fileManager.loadOrders();
        registeredUsers = fileManager.loadCustomers();
        registeredKitchenStaff = fileManager.loadKitchenStaff();
        registeredFoodManagers = fileManager.loadFoodManagers();
        //System.out.println(deliveryStaffList);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Delivery App!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser (scanner);
                    break;
                case 2:
                    if (loginUser (scanner)) {
                        userMenu(scanner);
                    }
                    break;
                case 3:
                    saveDataAndExit(scanner);
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static String generateUniqueId(String userType) {
        long timestamp = System.currentTimeMillis();
        int randomSuffix = (int) (Math.random() * 1000);
        return userType.substring(0, 3).toUpperCase() + timestamp + randomSuffix;
    }

    private static void registerUser (Scanner scanner) {
        System.out.println("Select your position:");
        System.out.println("1. Customer");
        System.out.println("2. Kitchen Staff");
        System.out.println("3. Delivery Staff");
        System.out.println("4. Food Manager");
        System.out.print("Enter your choice: ");

        int positionChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        String id;
        switch (positionChoice) {
            case 1:
                id = generateUniqueId("customer");
                Customer customer = new Customer(name, id);
                registeredUsers.add(customer);
                System.out.println("Registered successfully as Customer. Your ID is: " + id);
                break;
            case 2:
                id = generateUniqueId("kitchenstaff");
                KitchenStaff kitchenStaff = new KitchenStaff(name, id);
                registeredKitchenStaff.add(kitchenStaff);
                System.out.println("Registered successfully as Kitchen Staff. Your ID is: " + id);
                break;
            case 3:
                id = generateUniqueId("deliverystaff");
                DeliveryStaff deliveryStaff = new DeliveryStaff(name, id);
                registeredDeliveryStaff.add(deliveryStaff);
                //deliveryStaffList.add(deliveryStaff);
                System.out.println("Registered successfully as Delivery Staff. Your ID is: " + id);
                break;
            case 4:
                id = generateUniqueId("foodmanager");
                FoodManager foodManager = new FoodManager(name, id);
                registeredFoodManagers.add(foodManager);
                System.out.println("Registered successfully as Food Manager. Your ID is: " + id);
                break;
            default:
                System.out.println("Invalid choice. Registration failed.");
                return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (authenticationManager.registerUser (id, password)) {
            System.out.println("Please remember your ID for future logins: " + id);
        }
    }

    private static boolean loginUser (Scanner scanner) {
        System.out.print("Enter your ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(registeredUsers);
        allUsers.addAll(registeredKitchenStaff);
        allUsers.addAll(registeredDeliveryStaff);
        allUsers.addAll(registeredFoodManagers);

        User matchingUser  = allUsers.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (matchingUser  != null && authenticationManager.loginUser (id, password)) {
            currentUser  = matchingUser ;

            System.out.println("Login successful. Welcome " + matchingUser .getName());
            return true;
        }
        return false;
    }

    private static void userMenu(Scanner scanner) {
        if (currentUser  instanceof Customer) {
            customerMenu(scanner);
        } else if (currentUser  instanceof KitchenStaff) {
            kitchenStaffMenu(scanner);
        } else if (currentUser  instanceof DeliveryStaff) {
            deliveryStaffMenu(scanner);
        } else if (currentUser  instanceof FoodManager) {
            foodManagerMenu(scanner);
        }
    }

    private static void customerMenu(Scanner scanner) {
        Customer customer = (Customer) currentUser ;
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Check Order Status");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    System.out.print("Enter the food item name: ");
                    String foodName = scanner.nextLine();
                    customer.placeOrder(foodName, foodManager, orders);
                    break;
                case 3:
                    customer.checkOrderStatus(orders);
                    break;
                case 4:
                    loggedIn = false;
                    currentUser  = null;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void kitchenStaffMenu(Scanner scanner) {
        KitchenStaff kitchenStaff = (KitchenStaff) currentUser ;
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Kitchen Staff Menu ===");
            System.out.println("1. View Orders");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    kitchenStaff.viewOrders(orders);
                    break;
                case 2:
                    loggedIn = false;
                    currentUser  = null;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void deliveryStaffMenu(Scanner scanner) {
        DeliveryStaff deliveryStaff = (DeliveryStaff) currentUser ;
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Delivery Staff Menu ===");
            System.out.println("1. Show Available Orders");
            System.out.println("2. Start delivery");
            System.out.println("3. Logout");
            System.out.print("Enter your choice:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showAvailableOrders();
                    break;
                case 2:
                    Order orderToDeliver = orders.stream()
                        .filter(order -> !order.isCompleted() && order.getAssignedStaff() == null)
                        .findFirst().orElse(null);

                    if (orderToDeliver == null){
                        System.out.println("No orders to deliver.");
                        break;
                    }
                    orderToDeliver.setAssignedStaff(deliveryStaff);
                    deliveryStaff.startDelivery(orderToDeliver, scanner);

                    List<String> commands = deliveryStaff.generateDeliveryCommands(orderToDeliver.getRemainingTicks());
                    deliveryStaff.performDelivery(commands, scanner);
                    break;
                case 3:
                    loggedIn = false;
                    currentUser  = null;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

//    private static void startDelivery(DeliveryStaff deliveryStaff, Scanner scanner) {
//        Order orderToDeliver = orders.stream()
//                .filter(order -> !order.isCompleted() && order.getAssignedStaff() == null)
//                .findFirst()
//                .orElse(null);
//        if (orderToDeliver == null) {
//            System.out.println("No available orders to deliver.");
//            return;
//        }
//        orderToDeliver.setAssignedStaff(deliveryStaff);
//        deliveryStaff.startDelivery(orderToDeliver, scanner);
//    }

    private static void foodManagerMenu(Scanner scanner) {
        FoodManager currentFoodManager = (FoodManager) currentUser ;
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Food Manager Menu ===");
            System.out.println("1. View Menu");
            System.out.println("2. Add Food Item");
            System.out.println("3. Save Menu");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    currentFoodManager.viewMenu();
                    break;
                case 2:
                    System.out.print("Enter food item name: ");
                    String foodName = scanner.nextLine();
                    System.out.print("Enter delivery time (in ticks): ");
                    int deliveryTime = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    currentFoodManager.addFoodItem(foodName, deliveryTime);
                    break;
                case 3:
                    currentFoodManager.saveMenu();
                    break;
                case 4:
                    loggedIn = false;
                    currentUser  = null;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewMenu() {
        System.out.println("\n=== Menu ===");
        for (FoodItem item : foodManager.getFoodItems()) {
            System.out.println("Food: " + item.getName() + ", Delivery Time: " + item.getDeliveryTime() + " ticks");
        }
    }

    private static void showAvailableOrders() {
        System.out.println("\n=== Available Orders ===");
        for (Order order : orders) {
            if (!order.isCompleted()) {
                System.out.println("Customer: " + order.getCustomerName() +
                        ", Food: " + order.getPizzaType() +
                        ", Remaining Ticks: " + order.getRemainingTicks());
            }
        }
    }

    private static void saveDataAndExit(Scanner scanner) {
        for(DeliveryStaff deliveryStaff : registeredDeliveryStaff){
            deliveryStaff.setAvailable(true);
        }
        fileManager.saveDeliveryStaff(registeredDeliveryStaff);
        fileManager.saveOrders(orders);
        fileManager.saveCustomers(registeredUsers);
        fileManager.saveFoodManagers(registeredFoodManagers);
        System.out.println("Goodbye!");
    }
}