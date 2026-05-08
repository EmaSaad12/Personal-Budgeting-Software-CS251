package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import controller.GoalController;
import model.SavingGoal;
import model.User;
import repository.TransactionRepository;
import model.Transaction;

public class GoalFrame extends JFrame {
    private User currentUser;
    private GoalController controller = new GoalController();

    private JTextField nameField, targetField, deadlineField, amountToGoalField;
    private JComboBox<String> goalSelector;
    private JTextArea displayArea;
    private JProgressBar goalProgressBar;
    private JButton addBtn, clearBtn, linkBtn;

    Color bg = new Color(8, 8, 12);
    Color panelBg = new Color(15, 15, 22);
    Color purple = new Color(70, 20, 120);
    Color purpleGlow = new Color(130, 70, 180);
    Color bankBlue = new Color(120, 200, 255);

    public GoalFrame(User user) {
        this.currentUser = user;

        setTitle("Saving Goal Manager - " + currentUser.getName());
        setSize(700, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bg);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel title = new JLabel("🏦 Saving Goals");
        title.setForeground(bankBlue);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        RoundedButton dashBtn = new RoundedButton("Dashboard", bankBlue, new Color(150, 220, 255));
        dashBtn.setForeground(Color.BLACK);
        dashBtn.setPreferredSize(new Dimension(140, 42));
        dashBtn.addActionListener(e -> {
            new DashboardScreen(currentUser).setVisible(true);
            dispose();
        });

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(dashBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ================= MAIN CONTENT =================
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(bg);
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // 1. Create Goal Card
        JPanel createPanel = createCardPanel("Create New Goal");
        createPanel.setLayout(new GridLayout(5, 2, 12, 12));
        nameField = createInput(); targetField = createInput(); deadlineField = createInput();
        addPlaceholder(nameField, "Enter Goal Name");
        addPlaceholder(targetField, "Enter Target Amount");
        addPlaceholder(deadlineField, "2026-12-31");

        addBtn = new RoundedButton("Add Goal", purple, purpleGlow);
        clearBtn = new RoundedButton("Clear", new Color(180, 45, 45), new Color(220, 70, 70));

        createPanel.add(label("Goal Name:")); createPanel.add(nameField);
        createPanel.add(label("Target Amount:")); createPanel.add(targetField);
        createPanel.add(label("Deadline:")); createPanel.add(deadlineField);
        createPanel.add(addBtn); createPanel.add(clearBtn);

        // 2. Add Money Card
        JPanel linkPanel = createCardPanel("Add Money to Goal");
        linkPanel.setLayout(new GridLayout(3, 2, 12, 12));
        goalSelector = new JComboBox<>();
        styleComboBox(goalSelector);
        refreshGoalComboBox(); // استدعاء الفلترة

        amountToGoalField = createInput();
        addPlaceholder(amountToGoalField, "Enter Amount to Add");
        linkBtn = new RoundedButton("Confirm Payment", purple, purpleGlow);

        linkPanel.add(label("Select Target Goal:")); linkPanel.add(goalSelector);
        linkPanel.add(label("Amount ($):")); linkPanel.add(amountToGoalField);
        linkPanel.add(new JLabel("")); linkPanel.add(linkBtn);

        // 3. Status Panel
        JPanel statusPanel = createCardPanel("Goal Tracking Progress");
        statusPanel.setLayout(new BorderLayout(10, 10));
        goalProgressBar = new JProgressBar(0, 100);
        goalProgressBar.setStringPainted(true);
        goalProgressBar.setForeground(purpleGlow);
        goalProgressBar.setBackground(new Color(35, 35, 45));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(25, 25, 35));
        displayArea.setForeground(Color.WHITE);
        displayArea.setMargin(new Insets(10, 10, 10, 10));

        statusPanel.add(goalProgressBar, BorderLayout.NORTH);
        statusPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        content.add(createPanel);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(linkPanel);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(statusPanel);

        add(new JScrollPane(content), BorderLayout.CENTER);

        // ================= ACTIONS =================

        addBtn.addActionListener(e -> {
            try {
                controller.addGoal(nameField.getText(), Double.parseDouble(targetField.getText()),
                        deadlineField.getText(), currentUser.getId());
                displayArea.append(">> Goal '" + nameField.getText() + "' created successfully.\n");
                refreshGoalComboBox();
                nameField.setText(""); targetField.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Please check your inputs!"); }
        });

        linkBtn.addActionListener(e -> {
            try {
                String selectedGoalName = (String) goalSelector.getSelectedItem();
                double amountToAdd = Double.parseDouble(amountToGoalField.getText());
                double currentBalance = calculateCurrentBalance();

                if (selectedGoalName != null && amountToAdd > 0) {
                    if (amountToAdd > currentBalance) {
                        JOptionPane.showMessageDialog(this, "Insufficient Balance! Available: $" + currentBalance,
                                "Payment Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // تمرير الـ UserID لضمان تحديث الهدف الصحيح وتسجيل المعاملة
                        controller.addAmountToGoalByName(selectedGoalName, amountToAdd, currentUser.getId());
                        updateVisualProgress(selectedGoalName);
                        displayArea.append(">> $" + amountToAdd + " transferred to goal: " + selectedGoalName + "\n");
                        amountToGoalField.setText("");
                    }
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount"); }
        });

        clearBtn.addActionListener(e -> {
            nameField.setText(""); targetField.setText("");
            amountToGoalField.setText(""); displayArea.setText(""); goalProgressBar.setValue(0);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 🛑 تعديل: عرض أهداف المستخدم الحالي فقط في القائمة
    private void refreshGoalComboBox() {
        goalSelector.removeAllItems();
        for (SavingGoal g : controller.getAllGoals()) {
            if (g.getUserId().equals(currentUser.getId())) {
                goalSelector.addItem(g.getName());
            }
        }
    }

    private void updateVisualProgress(String goalName) {
        for (SavingGoal g : controller.getAllGoals()) {
            if (g.getName().equalsIgnoreCase(goalName) && g.getUserId().equals(currentUser.getId())) {
                double current = g.getCurrentAmount();
                double target = g.getTargetAmount();
                int percentage = (target > 0) ? (int)((current / target) * 100) : 0;
                goalProgressBar.setValue(Math.min(percentage, 100));
                break;
            }
        }
    }

    private double calculateCurrentBalance() {
        TransactionRepository repo = new TransactionRepository();
        List<Transaction> transactions = repo.loadTransactions();
        double balance = 0;
        for (Transaction t : transactions) {
            if (t.getUserId().equals(currentUser.getId())){
                if (t.getType().equalsIgnoreCase("Income")) balance += t.getAmount();
                else balance -= t.getAmount();
            }
        }
        return balance;
    }

    // --- Styling Helpers ---
    private void styleComboBox(JComboBox<String> combo) {
        combo.setBackground(new Color(25, 25, 35));
        combo.setForeground(Color.WHITE);
        combo.setBorder(new RoundedBorder(bankBlue, 18));
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(panelBg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(bankBlue, 25),
                BorderFactory.createTitledBorder(null, title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), bankBlue)
        ));
        return panel;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return l;
    }

    private JTextField createInput() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(300, 42));
        f.setBackground(new Color(25, 25, 35));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(new RoundedBorder(bankBlue, 18));
        return f;
    }

    private void addPlaceholder(JTextField field, String text) {
        field.setText(text); field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (field.getText().equals(text)) { field.setText(""); field.setForeground(Color.WHITE); } }
            public void focusLost(FocusEvent e) { if (field.getText().isEmpty()) { field.setText(text); field.setForeground(Color.GRAY); } }
        });
    }

    class RoundedButton extends JButton {
        private Color base, hover;
        public RoundedButton(String text, Color base, Color hover) {
            super(text); this.base = base; this.hover = hover;
            setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR)); setBackground(base); setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(hover); }
                public void mouseExited(MouseEvent e) { setBackground(base); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
        }
    }

    class RoundedBorder extends AbstractBorder {
        private Color color; private int radius;
        public RoundedBorder(Color color, int radius) { this.color = color; this.radius = radius; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        public Insets getBorderInsets(Component c) { return new Insets(10, 15, 10, 15); }
    }
}