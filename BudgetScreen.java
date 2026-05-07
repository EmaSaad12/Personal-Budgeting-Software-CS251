package com.mycompany.a2.us6and7;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BudgetScreen extends JFrame {
    private BudgetService service;
    private TransactionRepository transRepo;
    private JTextField categoryField, amountField;
    private User currentUser; 

    
    Color bg = new Color(25, 25, 30);
    Color purple = new Color(90, 40, 130);
    Color textColor = new Color(200, 200, 200);

    public BudgetScreen(User user) {
        this.currentUser = user;
        this.service = new BudgetService();
        this.transRepo = new TransactionRepository();

        setTitle("Budget System");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

      
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bg);
        JButton dashBtn = new JButton("\u2190 Back to Dashboard");
        dashBtn.setBackground(bg); 
        dashBtn.setForeground(Color.LIGHT_GRAY);
        dashBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dashBtn.setBorderPainted(false);
        dashBtn.setFocusPainted(false);
        dashBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashBtn.addActionListener(e -> {
            new DashboardScreen(currentUser).setVisible(true);
            this.dispose(); 
        });
        topPanel.add(dashBtn);
        add(topPanel, BorderLayout.NORTH);

       
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(bg);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        categoryField = createStyledField(inputFont);
        amountField = createStyledField(inputFont);

        JButton createBtn = createStyledButton("Create Budget");
        JButton editBtn = createStyledButton("Edit Budget");
        JButton alertBtn = createStyledButton("Check Alert");

      
        addFormField(mainContent, "Category", categoryField, labelFont);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        addFormField(mainContent, "Amount", amountField, labelFont);
        mainContent.add(Box.createRigidArea(new Dimension(0, 25))); 

      
        mainContent.add(createBtn);
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContent.add(editBtn);
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContent.add(alertBtn);

        add(mainContent, BorderLayout.CENTER);

        
        createBtn.addActionListener(e -> {
            try {
                String cat = categoryField.getText();
                double amt = Double.parseDouble(amountField.getText());
                service.addBudget(cat, amt, currentUser.getId());
                JOptionPane.showMessageDialog(this, "Budget Created Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        });

        editBtn.addActionListener(e -> {
            try {
                String cat = categoryField.getText();
                double amt = Double.parseDouble(amountField.getText());
                service.editBudget(amt, cat, currentUser.getId());
                JOptionPane.showMessageDialog(this, "Budget Edited Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        });

        alertBtn.addActionListener(e -> {
            String cat = categoryField.getText();
            if(cat.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a category to check.");
                return;
            }
            ArrayList<Transaction> transactions = (ArrayList<Transaction>) transRepo.loadTransactions();
            String result = service.CheckAlert(cat, transactions, currentUser.getId());
            JOptionPane.showMessageDialog(this, result);
        });

        setVisible(true);
    }

    private void addFormField(JPanel container, String text, JComponent field, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(textColor);
        lbl.setFont(font);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); 
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); 
        container.add(lbl);
        container.add(field);
    }

    private JTextField createStyledField(Font f) {
        JTextField field = new JTextField();
        field.setBackground(bg);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(f);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(purple, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));
        return field;
    }

    private JButton createStyledButton(String t) {
        JButton btn = new JButton(t);
        btn.setBackground(purple); 
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38)); 
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}