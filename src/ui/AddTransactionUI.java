package ui;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import service.TransactionService;
import model.Transaction;

public class AddTransactionUI extends JFrame {

    JComboBox<String> typeBox;
    JTextField amountField;
    JTextField categoryField;
    JButton saveBtn;

    TransactionService service = new TransactionService();

    String userId;

    public AddTransactionUI(String userId) {

        this.userId = userId;

        setTitle("Budget App");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Background Panel (Gradient) =====
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Color c1 = new Color(236, 240, 241);
                Color c2 = new Color(189, 195, 199);
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        background.setLayout(new GridBagLayout());

        // ===== Card Panel =====
        JPanel card = new JPanel(new GridLayout(4, 2, 15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel title = new JLabel("Add Transaction");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));

        typeBox = new JComboBox<>(new String[]{"Income", "Expense"});
        amountField = new JTextField();
        categoryField = new JTextField();

        typeBox.setFont(inputFont);
        amountField.setFont(inputFont);
        categoryField.setFont(inputFont);

        saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(46, 204, 113));
            }
        });

        card.add(new JLabel("Type"));
        card.add(typeBox);

        card.add(new JLabel("Amount"));
        card.add(amountField);

        card.add(new JLabel("Category"));
        card.add(categoryField);

        card.add(new JLabel(""));
        card.add(saveBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10);
        background.add(title, gbc);

        gbc.gridy = 1;
        background.add(card, gbc);

        add(background);

        saveBtn.addActionListener(e -> save());

        setVisible(true);
    }

    private void save() {

        try {

            String type = typeBox.getSelectedItem().toString();
            String amountText = amountField.getText();
            String category = categoryField.getText();
            String date = LocalDate.now().toString();

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter amount");
                return;
            }

            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive");
                return;
            }

            Transaction t = new Transaction(type, amount, category, date, userId);

            service.addTransaction(t);

            JOptionPane.showMessageDialog(this, "Saved Successfully!");
            amountField.setText("");
            categoryField.setText("");
            typeBox.setSelectedIndex(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    public static void main(String[] args) {
        new AddTransactionUI("TEST_USER_ID");
    }
}
