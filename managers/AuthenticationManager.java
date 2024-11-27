package managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private static Map<String, String> userCredentials = new HashMap<>();

    public boolean registerUser(String id, String password) {
        // Validate input
        if (id == null || id.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Invalid ID or password.");
            return false;
        }

        // Check if user already exists
        if (userCredentials.containsKey(id)) {
            System.out.println("User ID already exists. Please choose a different ID.");
            return false;
        }

        // Add user with trimmed credentials
        userCredentials.put(id, password);
        return true;
    }

    public boolean loginUser(String id, String password) {
        // Debug print statements
        System.out.println("Login attempt - ID: " + id);
        System.out.println("Stored credentials: " + userCredentials);

        // Validate input
        if (id == null || password == null) {
            System.out.println("Invalid login credentials. ");
            return false;
        }

        // Check credentials
        String storedPassword = userCredentials.get(id.trim());
        System.out.println("Stored password for " + id + ": " + storedPassword);

        if (storedPassword != null && storedPassword.equals(password.trim())) {
            return true;
        }

        System.out.println("Invalid ID or password. ");
        return false;
    }

    public static String getUserPassword(String id) {
        return userCredentials.get(id);
    }

    // Additional security method to change password
    public boolean changePassword(String id, String oldPassword, String newPassword) {
        if (loginUser(id, oldPassword)) {
            userCredentials.put(id, newPassword);
            return true;
        }
        return false;
    }
}