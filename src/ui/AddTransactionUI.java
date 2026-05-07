package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import service.TransactionService;
import model.Transaction;
import model.User;

public class AddTransactionUI extends JFrame {

    private JComboBox<String> typeBox;
    private JComboBox<String> categoryBox;
    private JTextField amountField;
    private JButton saveBtn;

    private String[] incomeCategories = {"Salary", "Bonus", "Freelance", "Investment", "Gift"};
    private String[] expenseCategories = {"Food", "Rent", "Transport", "Shopping", "Health", "Entertainment", "Electricity", "Water", "Gas"};

    private TransactionService service = new TransactionService();
    private User currentUser;

    Color bg = new Color(8, 8, 12);
    Color cardBg = new Color(15, 15, 22);
    Color bankBlue = new Color(120, 200, 255);
    Color purple = new Color(70, 20, 120);
    Color hover = new Color(95, 35, 155);

    public AddTransactionUI(User user) {

        this.currentUser = user;

        setTitle("Add Transaction");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        // ================= TOP =================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bg);

        JButton dashBtn = new JButton("← Dashboard");

        dashBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dashBtn.setForeground(Color.BLACK);
        dashBtn.setBackground(bankBlue);

        // 🔥 دايرة أكتر
        dashBtn.setBorder(new RoundedBorder(bankBlue, 30));
        dashBtn.setFocusPainted(false);
        dashBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🔽 نزلناه تحت شوية
        dashBtn.setPreferredSize(new Dimension(140, 38));

        dashBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                dashBtn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                dashBtn.setBackground(bankBlue);
            }
        });

        dashBtn.addActionListener(e -> {
            new DashboardScreen().setVisible(true);
            this.dispose();
        });

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        topPanel.add(dashBtn);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel mainWrapper = new JPanel(new GridBagLayout());
        mainWrapper.setBackground(bg);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(cardBg);
        card.setPreferredSize(new Dimension(420, 520));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;

        JLabel logo = new JLabel("🏦");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        logo.setForeground(bankBlue);

        c.gridy = 0;
        c.insets = new Insets(10,0,10,0);
        card.add(logo, c);

        JLabel title = new JLabel("Add Transaction");
        title.setForeground(bankBlue);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        c.gridy++;
        card.add(title, c);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);

        typeBox = new JComboBox<>(new String[]{"Income", "Expense"});
        categoryBox = new JComboBox<>();
        amountField = new JTextField();
        saveBtn = new JButton("Save Transaction");

        styleInput(typeBox, inputFont);
        styleInput(categoryBox, inputFont);
        styleTextField(amountField);

        addRow(card, "Transaction Type", typeBox, labelFont, c);
        addRow(card, "Amount", amountField, labelFont, c);
        addRow(card, "Category", categoryBox, labelFont, c);

        // 🔥 SAVE BUTTON دايرى قوي
        saveBtn.setBackground(purple);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setBorder(new RoundedBorder(purple, 30));
        saveBtn.setPreferredSize(new Dimension(200, 42));

        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                saveBtn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                saveBtn.setBackground(purple);
            }
        });

        c.gridy++;
        c.insets = new Insets(20,0,10,0);
        card.add(saveBtn, c);

        mainWrapper.add(card);
        add(mainWrapper, BorderLayout.CENTER);

        updateCategories();
        typeBox.addActionListener(e -> updateCategories());
        saveBtn.addActionListener(e -> save());

        setVisible(true);
    }

    private void addRow(JPanel panel, String text, JComponent comp, Font font, GridBagConstraints c) {

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(font);

        c.gridy++;
        c.insets = new Insets(10, 0, 5, 0);
        panel.add(label, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 10, 0);

        comp.setPreferredSize(new Dimension(320, 42));
        panel.add(comp, c);
    }

    private void updateCategories() {
        categoryBox.removeAllItems();

        if (typeBox.getSelectedItem().equals("Income")) {
            for (String cat : incomeCategories)
                categoryBox.addItem(cat);
        } else {
            for (String cat : expenseCategories)
                categoryBox.addItem(cat);
        }
    }

    private void save() {
        try {
            double amount = Double.parseDouble(amountField.getText());

            Transaction t = new Transaction(
                    typeBox.getSelectedItem().toString(),
                    amount,
                    categoryBox.getSelectedItem().toString(),
                    LocalDate.now().toString(),
                    currentUser.getId()
            );

            service.addTransaction(t);

            JOptionPane.showMessageDialog(this, "Saved Successfully!");
            amountField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount");
        }
    }

    private void styleInput(JComboBox<String> box, Font f) {
        box.setBackground(new Color(25,25,35));
        box.setForeground(Color.WHITE);
        box.setFont(f);
        box.setBorder(new RoundedBorder(bankBlue, 30));
    }

    private void styleTextField(JTextField f) {
        f.setBackground(new Color(25,25,35));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setBorder(new RoundedBorder(bankBlue, 30));
    }

    class RoundedBorder extends AbstractBorder {
        private Color color;
        private int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(10,15,10,15);
        }
    }
}