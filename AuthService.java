import java.io.*;
import java.util.*;

public class AuthService {

    private final String FILE_NAME = "users.txt";

    // Register
    public String register(String name, String email, String password) {

        if (!validate(email, password)) {
            return "Invalid input data";
        }

        List<User> users = loadUsers();

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return "Email already exists";
            }
        }

        String id = String.valueOf(users.size() + 1);

        User newUser = new User(id, name, email, password);
        saveUser(newUser);

        return "Registration successful";
    }
    
    // Login
    public String login(String email, String password) {

        if (!validate(email, password)) {
            return "Invalid input data";
        }

        List<User> users = loadUsers();

        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return "Login successful";
            }
        }

        return "Invalid email or password";
    }

    // Validation
    private boolean validate(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email is required");
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Invalid email format");
            return false;
        }

        if (password == null || password.length() < 8) {
            System.out.println("Password must be at least 8 characters");
            return false;
        }

        return true;
    }

    // Save
    private void saveUser(User user) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);
            writer.write(user.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving user");
        }
    }

    // Load
    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                users.add(new User(
                        data[0],
                        data[1],
                        data[2],
                        data[3]
                ));
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("No users file yet");
        }

        return users;
    }
}