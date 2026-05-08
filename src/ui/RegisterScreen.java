package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import service.AuthService;

public class RegisterScreen extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton loginButton;

    private AuthService auth =
            new AuthService();

    // COLORS
    Color bg =
            new Color(8, 8, 12);

    Color cardBg =
            new Color(15, 15, 22);

    Color bankBlue =
            new Color(120, 200, 255);

    Color purple =
            new Color(70, 20, 120);

    Color hover =
            new Color(95, 35, 155);

    Color textColor =
            Color.WHITE;

    public RegisterScreen() {

        // FRAME
        setTitle("Register");

        setSize(850, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(bg);

        // CARD
        JPanel card =
                new JPanel();

        card.setBackground(cardBg);

        card.setPreferredSize(
                new Dimension(420, 520)
        );

        card.setLayout(null);

        // LOGO
        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        45
                )
        );

        logo.setForeground(bankBlue);

        logo.setBounds(
                170,
                10,
                100,
                50
        );

        card.add(logo);

        // TITLE
        JLabel title =
                new JLabel("SIGN UP");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setBounds(
                135,
                60,
                200,
                40
        );

        card.add(title);

        // NAME
        JLabel nameLabel =
                new JLabel("Name");

        nameLabel.setForeground(textColor);

        nameLabel.setBounds(
                30,
                120,
                100,
                20
        );

        card.add(nameLabel);

        nameField =
                createInput();

        nameField.setBounds(
                30,
                145,
                320,
                40
        );

        addPlaceholder(
                nameField,
                "Enter your name"
        );

        card.add(nameField);

        // EMAIL
        JLabel emailLabel =
                new JLabel("Email");

        emailLabel.setForeground(textColor);

        emailLabel.setBounds(
                30,
                200,
                100,
                20
        );

        card.add(emailLabel);

        emailField =
                createInput();

        emailField.setBounds(
                30,
                225,
                320,
                40
        );

        addPlaceholder(
                emailField,
                "Enter your email"
        );

        card.add(emailField);

        // PASSWORD
        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setForeground(textColor);

        passwordLabel.setBounds(
                30,
                280,
                100,
                20
        );

        card.add(passwordLabel);

        passwordField =
                createPasswordInput();

        passwordField.setBounds(
                30,
                305,
                320,
                40
        );

        addPasswordPlaceholder(
                passwordField,
                "Enter your password"
        );

        card.add(passwordField);

        // ENTER
        nameField.addActionListener(
                e -> emailField.requestFocus()
        );

        emailField.addActionListener(
                e -> passwordField.requestFocus()
        );

        passwordField.addActionListener(
                e -> registerButton.doClick()
        );

        // REGISTER BUTTON
        registerButton =
                new RoundedButton(
                        "Register",
                        purple,
                        hover
                );

        registerButton.setBounds(
                30,
                380,
                150,
                45
        );

        card.add(registerButton);

        // LOGIN BUTTON
        loginButton =
                new RoundedButton(
                        "Back to Login",
                        bankBlue,
                        new Color(150, 220, 255)
                );

        loginButton.setForeground(Color.BLACK);

        loginButton.setBounds(
                200,
                380,
                150,
                45
        );

        card.add(loginButton);

        panel.add(card);

        add(panel);

        setVisible(true);

        // ACTIONS
        registerButton.addActionListener(
                e -> {

                    String name =
                            nameField.getText();

                    String email =
                            emailField.getText();

                    String password =
                            new String(
                                    passwordField
                                            .getPassword()
                            );

                    String result =
                            auth.register(
                                    name,
                                    email,
                                    password
                            );

                    if (result.equals(
                            "Registration successful"
                    )) {

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
                }
        );

        loginButton.addActionListener(
                e -> {

                    new LoginScreen();

                    dispose();
                }
        );
    }

    // INPUT
    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setBackground(
                new Color(25, 25, 35)
        );

        f.setForeground(Color.WHITE);

        f.setCaretColor(Color.WHITE);

        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        // نفس لون اللوجو
        f.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        return f;
    }

    // PASSWORD INPUT
    private JPasswordField createPasswordInput() {

        JPasswordField f =
                new JPasswordField();

        f.setBackground(
                new Color(25, 25, 35)
        );

        f.setForeground(Color.WHITE);

        f.setCaretColor(Color.WHITE);

        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        // نفس لون اللوجو
        f.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        return f;
    }

    // PLACEHOLDER
    private void addPlaceholder(
            JTextField field,
            String text
    ) {

        field.setText(text);

        field.setForeground(Color.GRAY);

        field.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e
                    ) {

                        if (field.getText().equals(text)) {

                            field.setText("");

                            field.setForeground(
                                    Color.WHITE
                            );
                        }
                    }

                    public void focusLost(
                            FocusEvent e
                    ) {

                        if (field.getText().isEmpty()) {

                            field.setText(text);

                            field.setForeground(
                                    Color.GRAY
                            );
                        }
                    }
                }
        );
    }

    // PASSWORD PLACEHOLDER
    private void addPasswordPlaceholder(
            JPasswordField field,
            String text
    ) {

        field.setText(text);

        field.setEchoChar((char) 0);

        field.setForeground(Color.GRAY);

        field.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e
                    ) {

                        String pass =
                                new String(
                                        field.getPassword()
                                );

                        if (pass.equals(text)) {

                            field.setText("");

                            field.setEchoChar('•');

                            field.setForeground(
                                    Color.WHITE
                            );
                        }
                    }

                    public void focusLost(
                            FocusEvent e
                    ) {

                        String pass =
                                new String(
                                        field.getPassword()
                                );

                        if (pass.isEmpty()) {

                            field.setText(text);

                            field.setEchoChar((char) 0);

                            field.setForeground(
                                    Color.GRAY
                            );
                        }
                    }
                }
        );
    }

    // BUTTON
    class RoundedButton extends JButton {

        private Color base;

        private Color hover;

        public RoundedButton(
                String text,
                Color base,
                Color hover
        ) {

            super(text);

            this.base = base;

            this.hover = hover;

            setFocusPainted(false);

            setContentAreaFilled(false);

            setBorderPainted(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setBackground(base);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            setForeground(Color.WHITE);

            addMouseListener(
                    new java.awt.event.MouseAdapter() {

                        public void mouseEntered(
                                java.awt.event.MouseEvent e
                        ) {

                            setBackground(hover);
                        }

                        public void mouseExited(
                                java.awt.event.MouseEvent e
                        ) {

                            setBackground(base);
                        }
                    }
            );
        }

        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    25,
                    25
            );

            super.paintComponent(g);
        }
    }

    // BORDER
    class RoundedBorder
            extends AbstractBorder {

        private Color color;

        private int radius;

        public RoundedBorder(
                Color color,
                int radius
        ) {

            this.color = color;

            this.radius = radius;
        }

        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );
        }

        public Insets getBorderInsets(
                Component c
        ) {

            return new Insets(
                    10,
                    15,
                    10,
                    15
            );
        }
    }
}