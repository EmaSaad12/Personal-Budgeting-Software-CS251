import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton loginButton;

    private AuthService auth = new AuthService();


    public RegisterScreen() {

        // Frame
        setTitle("Register");
        setSize(500, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Colors
        Color bg = new Color(20, 20, 25);
        Color cardBg = new Color(30, 30, 35);
        Color purple = new Color(90, 40, 130);
        Color purpleGlow = new Color(130, 70, 180);
        Color textColor = new Color(230, 230, 230);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setLayout(new GridBagLayout());

        // Card Panel
        JPanel card = new JPanel();
        card.setBackground(cardBg);
        card.setPreferredSize(new Dimension(320, 390));
        card.setLayout(null);

        // Title
        JLabel title = new JLabel("SIGN UP");
        title.setForeground(purpleGlow);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBounds(100, 20, 150, 40);
        card.add(title);

        // Name Label
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(textColor);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setBounds(30, 80, 100, 25);
        card.add(nameLabel);

        // Name Field
        nameField = new JTextField();
        nameField.setBounds(30, 105, 250, 35);
        nameField.setBackground(new Color(40, 40, 45));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(purple, 2));
        card.add(nameField);

        // Email Label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(textColor);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setBounds(30, 155, 100, 25);
        card.add(emailLabel);

        // Email Field
        emailField = new JTextField();
        emailField.setBounds(30, 180, 250, 35);
        emailField.setBackground(new Color(40, 40, 45));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setBorder(BorderFactory.createLineBorder(purple, 2));
        card.add(emailField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 230, 100, 25);
        card.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 255, 250, 35);
        passwordField.setBackground(new Color(40, 40, 45));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(purple, 2));
        card.add(passwordField);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(30, 320, 110, 40);
        registerButton.setBackground(purple);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(registerButton);

        // Login Button
        loginButton = new JButton("Back to Login");
        loginButton.setBounds(155, 320, 125, 40);
        loginButton.setBackground(purpleGlow);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(loginButton);

        panel.add(card);
        add(panel);

        setVisible(true);

        // Action Listeners
        registerButton.addActionListener(e -> {

        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        String result = auth.register(name, email, password);

        if (result.equals("Registration successful")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new LoginScreen();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });
    // Login Button Action Listener
    loginButton.addActionListener(e -> {
        new LoginScreen();
        dispose();
    });
    }

    public static void main(String[] args) {
        new RegisterScreen();
    }
}