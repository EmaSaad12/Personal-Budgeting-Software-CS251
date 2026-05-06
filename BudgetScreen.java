package com.mycompany.a2.us6and7;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BudgetScreen extends JFrame {

    private BudgetService service;
    private JTextField categoryField;
    private JTextField amountField;
    private JLabel resultLabel;
    private ArrayList<Transaction> transactions;

    public BudgetScreen() {

        service = new BudgetService();

        TransactionRepository repo = new TransactionRepository();
        transactions = repo.loadTransactions();

        setTitle("Budget System");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Colors
        Color bg = new Color(20, 20, 25);
        Color panelBg = new Color(30, 30, 35);
        Color purple = new Color(90, 40, 130);
        Color purpleGlow = new Color(130, 70, 180);
        Color textColor = new Color(230, 230, 230);

        // Main panel
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setLayout(new BorderLayout());

        // Card
        JPanel card = new JPanel();
        card.setBackground(panelBg);
        card.setLayout(new GridLayout(8, 1, 12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Category
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setForeground(textColor);
        categoryLabel.setFont(labelFont);

        categoryField = createStyledField(fieldFont, purple, purpleGlow);

        // Amount
        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setForeground(textColor);
        amountLabel.setFont(labelFont);

        amountField = createStyledField(fieldFont, purple, purpleGlow);

        // Buttons
        JButton createButton = createStyledButton("Create Budget", purple);
        JButton editButton = createStyledButton("Edit Budget", purple);
        JButton alertButton = createStyledButton("Check Alert", purple);
        
        
        JButton goalButton = createStyledButton("Manage Saving Goals", new Color(41, 128, 185)); // لون أزرق هادي
        JButton reportButton = createStyledButton("View Analytics Dashboard (SRS)", new Color(39, 174, 96)); // لون أخضر

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(purpleGlow);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Result
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(purpleGlow);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Add to card
        card.add(categoryLabel);
        card.add(categoryField);

        card.add(amountLabel);
        card.add(amountField);

        card.add(createButton);
        card.add(editButton);
        card.add(alertButton);

        card.add(goalButton);   
        card.add(reportButton);
        
        card.add(resultLabel);

        panel.add(card, BorderLayout.CENTER);
        add(panel);

        // Actions 
        createButton.addActionListener(e -> {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            service.createBudget(amount, category);
            resultLabel.setText("✔ Budget Created");
        });

        editButton.addActionListener(e -> {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            service.editBudget(amount, category);
            resultLabel.setText("✔ Budget Updated");
        });

        alertButton.addActionListener(e -> {
            String category = categoryField.getText();
            String result = service.CheckAlert(category, transactions);
            resultLabel.setText(result);
        });
        
        goalButton.addActionListener(e -> {
            new GoalFrame().setVisible(true); 
        });

       
        reportButton.addActionListener(e -> {
            new ReportScreen().setVisible(true);
        });

        setVisible(true);
    }

    // TextField Style + Focus Effect
    private JTextField createStyledField(Font font, Color borderColor, Color focusColor) {

        JTextField field = new JTextField();
        field.setFont(font);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(40, 40, 45));

        field.setBorder(BorderFactory.createLineBorder(borderColor, 2));

        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(focusColor, 2));
                field.setBackground(new Color(50, 50, 55));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(borderColor, 2));
                field.setBackground(new Color(40, 40, 45));
            }
        });

        return field;
    }

    // Button Style + Hover
    private JButton createStyledButton(String text, Color color) {

        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(110, 60, 160)); // darker hover
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        new BudgetScreen();
    }
}

