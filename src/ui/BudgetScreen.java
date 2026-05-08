package ui;

import service.BudgetService;
import model.Transaction;
import repository.TransactionRepository;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import model.User;

public class BudgetScreen extends JFrame {

    private BudgetService service;
    private JComboBox<String> categoryBox;
    private JTextField amountField;
    private ArrayList<Transaction> transactions;
    private User currentUser;

    public BudgetScreen(User user) {
        this.currentUser = user;
        service = new BudgetService();

        transactions =
                new TransactionRepository()
                        .loadTransactions();

        setTitle("Budget Bank");

        setSize(850, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        Color red =
                new Color(190, 50, 50);

        String[] categories = {"Food", "Rent", "Transport", "Entertainment", "Health", "Shopping", "Others"};
        categoryBox = new JComboBox<>(categories);


        categoryBox.setBackground(new Color(25, 25, 35));
        categoryBox.setForeground(Color.WHITE);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 15));
        categoryBox.setBorder(new RoundedBorder(new Color(80, 120, 200), 18));
        categoryBox.setPreferredSize(new Dimension(300, 42));

        // MAIN
        JPanel main =
                new JPanel(new GridBagLayout());

        main.setBackground(bg);

        // CARD
        JPanel card =
                new JPanel(new GridBagLayout());

        card.setBackground(cardBg);

        card.setPreferredSize(
                new Dimension(430, 620)
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
                new JLabel("Budget Bank");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        // SUBTITLE
        JLabel sub =
                new JLabel(
                        "Welcome to Budget Bank"
                );

        sub.setForeground(Color.LIGHT_GRAY);

        // INPUTS


        amountField = createInput();


        addPlaceholder(
                amountField,
                "Enter amount"
        );

        // ENTER


        amountField.addActionListener(
                e -> createBudget()
        );

        // BUTTONS
        RoundedButton createBtn =
                new RoundedButton(
                        "Create Budget",
                        purple,
                        hover
                );

        RoundedButton editBtn =
                new RoundedButton(
                        "Edit Budget",
                        purple,
                        hover
                );

        RoundedButton alertBtn =
                new RoundedButton(
                        "Check Alert",
                        purple,
                        hover
                );

        // DASHBOARD BUTTON
        RoundedButton dashBtn =
                new RoundedButton(
                        "Dashboard",
                        bankBlue,
                        new Color(150, 220, 255)
                );
        dashBtn.setForeground(Color.BLACK);

        // CLOSE BUTTON
        RoundedButton closeBtn =
                new RoundedButton(
                        "Close",
                        red,
                        new Color(255, 80, 80)
                );

        Dimension btnSize =
                new Dimension(250, 45);

        createBtn.setPreferredSize(btnSize);
        editBtn.setPreferredSize(btnSize);
        alertBtn.setPreferredSize(btnSize);


        dashBtn.setPreferredSize(
                new Dimension(115, 40)
        );

        closeBtn.setPreferredSize(
                new Dimension(115, 40)
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

        // CATEGORY
        c.anchor =
                GridBagConstraints.WEST;

        c.insets =
                new Insets(10, 35, 5, 0);

        c.gridy = y++;
        card.add(label("Category"), c);

        c.insets =
                new Insets(0, 35, 15, 35);

        c.gridy = y++;
        card.add(categoryBox, c);

        // AMOUNT
        c.insets =
                new Insets(5, 35, 5, 0);

        c.gridy = y++;
        card.add(label("Amount"), c);

        c.insets =
                new Insets(0, 35, 20, 35);

        c.gridy = y++;
        card.add(amountField, c);

        // BIG BUTTONS
        c.anchor =
                GridBagConstraints.CENTER;

        c.insets =
                new Insets(10, 0, 0, 0);

        c.gridy = y++;
        card.add(createBtn, c);

        c.gridy = y++;
        card.add(editBtn, c);

        c.gridy = y++;
        card.add(alertBtn, c);

        // SMALL BUTTONS PANEL
        JPanel bottomButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                0
                        )
                );

        bottomButtons.setOpaque(false);

        bottomButtons.add(dashBtn);

        bottomButtons.add(closeBtn);

        c.gridy = y++;

        c.insets =
                new Insets(20, 0, 0, 0);

        card.add(bottomButtons, c);

        main.add(card);

        add(main);

        // ACTIONS
        createBtn.addActionListener(
                e -> createBudget()
        );

        editBtn.addActionListener(e -> {

            try {

                service.editBudget(
                        Double.parseDouble(
                                amountField.getText()
                        ),
                        categoryBox.getSelectedItem().toString()
                );

                showToast(
                        "Budget Updated Successfully",
                        bankBlue
                );

            } catch (Exception ex) {

                showToast(
                        "Invalid Amount",
                        bankBlue
                );
            }
        });

        alertBtn.addActionListener(e -> {

            String result =
                    service.CheckAlert(
                            categoryBox.getSelectedItem().toString(),
                            transactions
                    );

            showToast(result, bankBlue);
        });

        // CLOSE
        closeBtn.addActionListener(
                e -> dispose()
        );

        // DASHBOARD
        dashBtn.addActionListener(e -> {

            showToast(
                    "Opening Dashboard...",
                    bankBlue
            );


            new DashboardScreen(currentUser);

            dispose();
        });

        setVisible(true);
    }

    // CREATE BUDGET

    private void createBudget() {

        Color bankBlue = new Color(120, 200, 255);

        try {

            String category = categoryBox.getSelectedItem().toString();


            String amountText = amountField.getText().trim();


            if (category.equals("Select Category") || amountText.isEmpty() || amountText.equals("Enter amount")) {
                showToast("Please select a category and enter an amount", bankBlue);
                return;
            }


            double amount = Double.parseDouble(amountText);


            service.createBudget(amount, category,currentUser.getId());


            showToast("Budget Plan Created Successfully", bankBlue);
            amountField.setText("");

            amountField.setForeground(Color.GRAY);
            amountField.setText("Enter amount");

        } catch (NumberFormatException ex) {

            showToast("Invalid Amount: Please enter numbers only", bankBlue);
        } catch (Exception ex) {

            showToast("An error occurred: " + ex.getMessage(), bankBlue);
        }
    }
    // LABEL
    private JLabel label(String t) {

        JLabel l =
                new JLabel(t);

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

    // INPUT
    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setPreferredSize(
                new Dimension(300, 42)
        );

        f.setMaximumSize(
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
                        new Color(
                                80,
                                120,
                                200
                        ),
                        18
                )
        );

        return f;
    }

    // PLACEHOLDER
    private void addPlaceholder(
            JTextField f,
            String text
    ) {

        f.setText(text);

        f.setForeground(Color.GRAY);

        f.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e
                    ) {

                        if (f.getText().equals(text)) {

                            f.setText("");

                            f.setForeground(
                                    Color.WHITE
                            );
                        }
                    }

                    public void focusLost(
                            FocusEvent e
                    ) {

                        if (f.getText().isEmpty()) {

                            f.setText(text);

                            f.setForeground(
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
                String t,
                Color base,
                Color hover
        ) {

            super(t);

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

            addMouseListener(
                    new MouseAdapter() {

                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            setBackground(hover);
                        }

                        public void mouseExited(
                                MouseEvent e
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

        private Color c;

        private int r;

        public RoundedBorder(
                Color c,
                int r
        ) {

            this.c = c;

            this.r = r;
        }

        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int w,
                int h
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(this.c);

            g2.drawRoundRect(
                    x,
                    y,
                    w - 1,
                    h - 1,
                    r,
                    r
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

    // TOAST
    private void showToast(
            String message,
            Color accent
    ) {

        JWindow toast =
                new JWindow();

        toast.setSize(350, 70);

        JPanel panel =
                new JPanel() {

                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                new Color(
                                        30,
                                        30,
                                        35
                                )
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                25,
                                25
                        );

                        g2.setColor(accent);

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth()-1,
                                getHeight()-1,
                                25,
                                25
                        );
                    }
                };

        panel.setLayout(
                new BorderLayout()
        );

        JLabel label =
                new JLabel(
                        message,
                        SwingConstants.CENTER
                );

        label.setForeground(accent);

        label.setFont(
                new Font(
                        "Poppins",
                        Font.BOLD,
                        14
                )
        );

        panel.add(label);

        toast.add(panel);

        Dimension screen =
                Toolkit
                        .getDefaultToolkit()
                        .getScreenSize();

        int x =
                screen.width / 2 - 175;

        int y =
                screen.height;

        toast.setLocation(x, y);

        toast.setVisible(true);

        new Thread(() -> {

            try {

                for (int i = 0; i < 120; i++) {

                    toast.setLocation(
                            x,
                            y - i * 2
                    );

                    Thread.sleep(5);
                }

            } catch (Exception ignored) {

            }

        }).start();

        new Timer(
                2000,
                e -> toast.dispose()
        ).start();
    }


}