package com.mycompany.a2.us6and7;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransactionUI extends JFrame {

    private JComboBox<String> typeBox;
    private JComboBox<String> categoryBox;
    private JTextField amountField;
    private JButton saveBtn;
    
    private String[] incomeCategories = {"Salary", "Bonus", "Freelance", "Investment", "Gift"};
    private String[] expenseCategories = {"Food", "Rent", "Transport", "Shopping", "Health", "Entertainment", "Electricity", "Water", "Gas"};

    private TransactionService service = new TransactionService();
    private User currentUser;

    // الألوان الموحدة
    Color bg = new Color(20, 20, 25);
    Color cardBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);

    public AddTransactionUI(User user) {
        this.currentUser = user;

        setTitle("Add Transaction");
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

     
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bg);
        JButton dashBtn = new JButton("\u2190 Back to Dashboard");
        dashBtn.setBackground(bg); 
        dashBtn.setForeground(Color.LIGHT_GRAY);
        dashBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashBtn.setBorderPainted(false);
        dashBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashBtn.addActionListener(e -> {
            new DashboardScreen(currentUser).setVisible(true); //
            this.dispose(); 
        });
        topPanel.add(dashBtn);
        add(topPanel, BorderLayout.NORTH);

      
        JPanel mainWrapper = new JPanel(new GridBagLayout());
        mainWrapper.setBackground(bg);

     
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(purple, 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40) 
        ));
        card.setPreferredSize(new Dimension(380, 450));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

    
        typeBox = new JComboBox<>(new String[]{"Income", "Expense"});
        categoryBox = new JComboBox<>();
        amountField = new JTextField();
        saveBtn = new JButton("Save Transaction");

        styleCombo(typeBox, inputFont);
        styleCombo(categoryBox, inputFont);
        styleField(amountField, inputFont);
        
        saveBtn.setBackground(purple);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 

        
        addFormField(card, "Transaction Type", typeBox, labelFont);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        addFormField(card, "Amount ($)", amountField, labelFont);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        addFormField(card, "Category", categoryBox, labelFont);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(saveBtn);

        mainWrapper.add(card);
        add(mainWrapper, BorderLayout.CENTER);

   
        updateCategories();
        typeBox.addActionListener(e -> updateCategories());
        saveBtn.addActionListener(e -> save());

        setVisible(true);
    }

    private void addFormField(JPanel container, String text, JComponent field, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(textColor);
        lbl.setFont(font);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        container.add(lbl);
        container.add(field);
    }

    private void updateCategories() {
        categoryBox.removeAllItems();
        if (typeBox.getSelectedItem().equals("Income")) {
            for (String cat : incomeCategories) categoryBox.addItem(cat);
        } else {
            for (String cat : expenseCategories) categoryBox.addItem(cat);
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

    private void styleCombo(JComboBox<String> b, Font f) {
        b.setBackground(new Color(45, 45, 50));
        b.setForeground(Color.WHITE);
        b.setFont(f);
        b.setBorder(BorderFactory.createLineBorder(purple, 1));
    }

    private void styleField(JTextField f, Font font) {
        f.setBackground(new Color(45, 45, 50));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(font);
        f.setBorder(BorderFactory.createLineBorder(purple, 2));
    }
}