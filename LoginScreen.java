package com.mycompany.a2.us6and7;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private AuthService auth = new AuthService();

    public LoginScreen() {

        // Frame
        setTitle("Login");
        setSize(500, 500);
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
        card.setPreferredSize(new Dimension(300, 320));
        card.setLayout(null);

        // Title
        JLabel title = new JLabel("LOGIN");
        title.setForeground(purpleGlow);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBounds(110, 20, 150, 40);
        card.add(title);

        // Email Label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(textColor);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setBounds(30, 80, 100, 25);
        card.add(emailLabel);

        // Email Field
        emailField = new JTextField();
        emailField.setBounds(30, 105, 240, 35);
        emailField.setBackground(new Color(40, 40, 45));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setBorder(BorderFactory.createLineBorder(purple, 2));
        card.add(emailField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 155, 100, 25);
        card.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 180, 240, 35);
        passwordField.setBackground(new Color(40, 40, 45));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(purple, 2));
        card.add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(30, 240, 110, 40);
        loginButton.setBackground(purple);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(loginButton);

        // Sign Up Button
        signupButton = new JButton("Sign Up");
        signupButton.setBounds(160, 240, 110, 40);
        signupButton.setBackground(purpleGlow);
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(signupButton);

        panel.add(card);
        add(panel);

        setVisible(true);

        // Action Listeners
        signupButton.addActionListener(e -> {
            new RegisterScreen();
            dispose();
        });

        // Login Button Action
loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            String result = auth.login(email, password);

            if (result.equals("Login successful")) {
                User loggedInUser = auth.getUserByEmail(email); 
                JOptionPane.showMessageDialog(this, "Welcome Back, " + loggedInUser.getName());
                new DashboardScreen(loggedInUser).setVisible(true); 
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public static void main(String[] args) {
        new LoginScreen();
    }
}