import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransactionUI extends JFrame {

    JComboBox<String> typeBox;
    JTextField amountField;
    JTextField categoryField;
    JButton saveBtn;

    TransactionService service = new TransactionService();
    String userId;

    public AddTransactionUI(String userId) {

        this.userId = userId;

        setTitle("Add Transaction");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setResizable(true);



       

        // ===== Background 
        JPanel background = new JPanel();
        background.setBackground(new Color(25, 25, 25));
        background.setLayout(new GridBagLayout());

        // ===== Card 
        JPanel card = new JPanel(new GridLayout(4, 2, 10, 15));
        card.setBackground(new Color(35, 35, 35));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Color purple = new Color(128, 0, 128);

        // ===== Labels 
        JLabel typeLabel = new JLabel("Type");
        JLabel amountLabel = new JLabel("Amount");
        JLabel categoryLabel = new JLabel("Category");

        JLabel[] labels = {typeLabel, amountLabel, categoryLabel};
        for (JLabel lbl : labels) {
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setFont(font);
        }

        // ===== Inputs 
        typeBox = new JComboBox<>(new String[]{"Income", "Expense"});
        amountField = new JTextField();
        categoryField = new JTextField();

        typeBox.setPreferredSize(new Dimension(400, 35));
amountField.setPreferredSize(new Dimension(400, 35));
categoryField.setPreferredSize(new Dimension(400, 35));

        JTextField[] fields = {amountField, categoryField};

        for (JTextField field : fields) {
            field.setBackground(new Color(45, 45, 45));
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
            field.setFont(font);
            field.setBorder(BorderFactory.createLineBorder(purple, 2));
        }

        typeBox.setBackground(new Color(45, 45, 45));
        typeBox.setForeground(Color.WHITE);
        typeBox.setFont(font);

        // ===== Button 
        saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(102, 0, 153));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(128, 0, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(102, 0, 153));
            }
        });

        // ===== Layout 
        card.add(typeLabel);
        card.add(typeBox);

        card.add(amountLabel);
        card.add(amountField);

        card.add(categoryLabel);
        card.add(categoryField);

        card.add(new JLabel(""));
        card.add(saveBtn);
        JLabel title = new JLabel("Add Transaction");
title.setForeground(Color.WHITE);
title.setFont(new Font("Segoe UI", Font.BOLD, 22));

GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 0;
gbc.insets = new Insets(10, 0, 20, 0);
gbc.anchor = GridBagConstraints.CENTER; 

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
