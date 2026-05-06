package ui;

import service.BudgetService;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import repository.TransactionRepository;

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
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🌙 Colors
        Color bg = new Color(18,18,18);
        Color purple = new Color(155, 89, 182);
        Color purpleHover = new Color(175, 110, 200);
        Color textColor = Color.WHITE;

        // Panel
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setLayout(new GridLayout(7,1,15,15));
        panel.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setForeground(textColor);
        categoryLabel.setFont(labelFont);

        categoryField = new JTextField();
        styleTextField(categoryField, fieldFont);

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setForeground(textColor);
        amountLabel.setFont(labelFont);

        amountField = new JTextField();
        styleTextField(amountField, fieldFont);

        JButton createButton = createStyledButton("Create Budget", purple, purpleHover);
        JButton editButton = createStyledButton("Edit Budget", purple, purpleHover);
        JButton alertButton = createStyledButton("Check Alert", purple, purpleHover);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(purple);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Add
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(createButton);
        panel.add(editButton);
        panel.add(alertButton);
        panel.add(resultLabel);

        add(panel);

        // Actions (زي ما هي)
        createButton.addActionListener(e -> {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            service.createBudget(amount ,category);
            resultLabel.setText("✔ Budget Created");
        });

        editButton.addActionListener(e -> {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            service.editBudget(amount ,category);
            resultLabel.setText("✔ Budget Updated");
        });

        alertButton.addActionListener(e -> {
            String category = categoryField.getText();
            String result = service.CheckAlert(category, transactions);
            resultLabel.setText(result);
        });

        setVisible(true);
    }

    //  TextField Style
    private void styleTextField(JTextField field, Font font){
        field.setFont(font);
        field.setBackground(new Color(30,30,30));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    // Button Style (مع Hover)
    private JButton createStyledButton(String text, Color bg, Color hover){

        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12,15,12,15));

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new BudgetScreen();
    }
}
