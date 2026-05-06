public class Main {
    public static void main(String[] args) {

        AuthService auth = new AuthService();

        // Test Register
        String registered = auth.register("Nour", "nour@gmail.com", "12ppppp345");

        System.out.println("Register result: " + registered);

        // Test Login
        String login = auth.login("nour@gmail.com", "12345678");

        System.out.println("Login result: " + login);
    }
}