/*package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import service.AuthService;

public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton signupButton;

    private AuthService auth =
            new AuthService();

    // COLORS
    Color bg =
            new Color(8, 8, 12);

    Color cardBg =
            new Color(15, 15, 22);

    // ✨ نفس لون signup
    Color bankBlue =
            new Color(120, 200, 255);

    Color purple =
            new Color(70, 20, 120);

    Color hover =
            new Color(95, 35, 155);

    public LoginScreen() {

        // FRAME
        setTitle("Login");

        setSize(850, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel main =
                new JPanel(
                        new GridBagLayout()
                );

        main.setBackground(bg);

        // CARD
        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(cardBg);

        card.setPreferredSize(
                new Dimension(420, 560)
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.gridx = 0;

        // LOGO
        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        50
                )
        );

        logo.setForeground(bankBlue);

        // TITLE
        JLabel title =
                new JLabel("LOGIN");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        // SUBTITLE
        JLabel sub =
                new JLabel(
                        "Welcome Back"
                );

        sub.setForeground(
                Color.LIGHT_GRAY
        );

        // INPUTS
        emailField =
                createInput();

        passwordField =
                createPasswordInput();

        addPlaceholder(
                emailField,
                "Enter Email"
        );

        addPasswordPlaceholder(
                passwordField,
                "Enter Password"
        );

        // ENTER
        emailField.addActionListener(
                e -> passwordField.requestFocus()
        );

        passwordField.addActionListener(
                e -> loginButton.doClick()
        );

        // LABELS
        JLabel emailLabel =
                label("Email");

        JLabel passwordLabel =
                label("Password");

        // BUTTONS
        loginButton =
                new RoundedButton(
                        "Login",
                        purple,
                        hover
                );

        signupButton =
                new RoundedButton(
                        "Sign Up",
                        bankBlue,
                        new Color(150, 220, 255)
                );

        signupButton.setForeground(
                Color.BLACK
        );

        Dimension btnSize =
                new Dimension(250, 45);

        loginButton.setPreferredSize(
                btnSize
        );

        signupButton.setPreferredSize(
                btnSize
        );

        int y = 0;

        c.insets =
                new Insets(10, 0, 5, 0);

        c.gridy = y++;
        card.add(logo, c);

        c.gridy = y++;
        card.add(title, c);

        c.insets =
                new Insets(0, 0, 20, 0);

        c.gridy = y++;
        card.add(sub, c);

        // EMAIL
        c.anchor =
                GridBagConstraints.WEST;

        c.insets =
                new Insets(10, 35, 5, 0);

        c.gridy = y++;
        card.add(emailLabel, c);

        c.insets =
                new Insets(0, 35, 15, 35);

        c.gridy = y++;
        card.add(emailField, c);

        // PASSWORD
        c.insets =
                new Insets(5, 35, 5, 0);

        c.gridy = y++;
        card.add(passwordLabel, c);

        c.insets =
                new Insets(0, 35, 20, 35);

        c.gridy = y++;
        card.add(passwordField, c);

        // BUTTONS
        c.anchor =
                GridBagConstraints.CENTER;

        c.insets =
                new Insets(10, 0, 0, 0);

        c.gridy = y++;
        card.add(loginButton, c);

        c.gridy = y++;
        card.add(signupButton, c);

        main.add(card);

        add(main);

        setVisible(true);

        // SIGNUP
        signupButton.addActionListener(
                e -> {

                    new RegisterScreen();

                    dispose();
                }
        );

        // LOGIN
        loginButton.addActionListener(
                e -> {

                    String email =
                            emailField
                                    .getText();

                    String password =
                            new String(
                                    passwordField
                                            .getPassword()
                            );

                    String result =
                            auth.login(
                                    email,
                                    password
                            );

                    if (result.equals(
                            "Login successful"
                    )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Login Successful!\nWelcome back.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        new DashboardScreen(user).setVisible(true);

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
    }

    // LABEL
    private JLabel label(String text) {

        JLabel l =
                new JLabel(text);

        l.setForeground(Color.WHITE);

        l.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        return l;
    }

    // TEXT INPUT
    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setPreferredSize(
                new Dimension(300, 42)
        );

        // نفس خلفية signup
        f.setBackground(
                new Color(25, 25, 35)
        );

        // لون الكتابة
        f.setForeground(Color.WHITE);

        // لون العلامة
        f.setCaretColor(Color.WHITE);

        // الخط
        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        // ✨ نفس حواف signup
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

        f.setPreferredSize(
                new Dimension(300, 42)
        );

        // نفس الخلفية
        f.setBackground(
                new Color(25, 25, 35)
        );

        // لون الكتابة
        f.setForeground(Color.WHITE);

        // لون العلامة
        f.setCaretColor(Color.WHITE);

        // الخط
        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        // ✨ نفس حواف signup
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

            setForeground(Color.WHITE);

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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LoginScreen::new
        );
    }
}*/
package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import service.AuthService;

public class LoginScreen extends JFrame {


    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton signupButton;

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

    public LoginScreen() {

        // FRAME
        setTitle("Login");

        setSize(850, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel main =
                new JPanel(
                        new GridBagLayout()
                );

        main.setBackground(bg);

        // CARD
        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(cardBg);

        card.setPreferredSize(
                new Dimension(420, 560)
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.gridx = 0;

        // LOGO
        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        50
                )
        );

        logo.setForeground(bankBlue);

        // TITLE
        JLabel title =
                new JLabel("LOGIN");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        // SUBTITLE
        JLabel sub =
                new JLabel(
                        "Welcome Back"
                );

        sub.setForeground(
                Color.LIGHT_GRAY
        );

        // INPUTS
        emailField =
                createInput();

        passwordField =
                createPasswordInput();

        addPlaceholder(
                emailField,
                "Enter Email"
        );

        addPasswordPlaceholder(
                passwordField,
                "Enter Password"
        );

        // ENTER
        emailField.addActionListener(
                e -> passwordField.requestFocus()
        );

        passwordField.addActionListener(
                e -> loginButton.doClick()
        );

        // LABELS
        JLabel emailLabel =
                label("Email");

        JLabel passwordLabel =
                label("Password");

        // BUTTONS
        loginButton =
                new RoundedButton(
                        "Login",
                        purple,
                        hover
                );

        signupButton =
                new RoundedButton(
                        "Sign Up",
                        bankBlue,
                        new Color(150, 220, 255)
                );

        signupButton.setForeground(
                Color.BLACK
        );

        Dimension btnSize =
                new Dimension(250, 45);

        loginButton.setPreferredSize(
                btnSize
        );

        signupButton.setPreferredSize(
                btnSize
        );

        int y = 0;

        c.insets =
                new Insets(10, 0, 5, 0);

        c.gridy = y++;
        card.add(logo, c);

        c.gridy = y++;
        card.add(title, c);

        c.insets =
                new Insets(0, 0, 20, 0);

        c.gridy = y++;
        card.add(sub, c);

        // EMAIL
        c.anchor =
                GridBagConstraints.WEST;

        c.insets =
                new Insets(10, 35, 5, 0);

        c.gridy = y++;
        card.add(emailLabel, c);

        c.insets =
                new Insets(0, 35, 15, 35);

        c.gridy = y++;
        card.add(emailField, c);

        // PASSWORD
        c.insets =
                new Insets(5, 35, 5, 0);

        c.gridy = y++;
        card.add(passwordLabel, c);

        c.insets =
                new Insets(0, 35, 20, 35);

        c.gridy = y++;
        card.add(passwordField, c);

        // BUTTONS
        c.anchor =
                GridBagConstraints.CENTER;

        c.insets =
                new Insets(10, 0, 0, 0);

        c.gridy = y++;
        card.add(loginButton, c);

        c.gridy = y++;
        card.add(signupButton, c);

        main.add(card);

        add(main);

        setVisible(true);

        // SIGNUP
        signupButton.addActionListener(
                e -> {

                    new RegisterScreen();

                    dispose();
                }
        );

        // LOGIN
        loginButton.addActionListener(
                e -> {

                    String email =
                            emailField
                                    .getText();

                    String password =
                            new String(
                                    passwordField
                                            .getPassword()
                            );

                    String result =
                            auth.login(
                                    email,
                                    password
                            );

                    if (result.equals(
                            "Login successful"
                    )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Login Successful!\nWelcome back.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        new DashboardScreen()
                                .setVisible(true);

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
    }

    // LABEL
    private JLabel label(String text) {

        JLabel l =
                new JLabel(text);

        l.setForeground(Color.WHITE);

        l.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        return l;
    }

    // TEXT INPUT
    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setPreferredSize(
                new Dimension(300, 42)
        );

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

        f.setPreferredSize(
                new Dimension(300, 42)
        );

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

            setForeground(Color.WHITE);

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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LoginScreen::new
        );
    }


}
