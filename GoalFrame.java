

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.util.List;

public class GoalFrame extends JFrame {
    private GoalController controller = new GoalController();
    private JTextField nameField, targetField, deadlineField, idField, linkGoalIdField;
    private JComboBox<String> transComboBox; 
    private JTextArea displayArea;
    private JProgressBar goalProgressBar; 
    private JButton addBtn, clearBtn, refreshBtn, linkBtn;
    private User currentUser;

    
    Color bg = new Color(20, 20, 25);
    Color panelBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);

    public GoalFrame(User user) { 
        this.currentUser = user;
        
        setTitle("Saving Goal Manager");
        setSize(650, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); 
        getContentPane().setBackground(bg);

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bg);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));

        JButton dashBtn = new JButton("\u2190 Back to Dashboard");
        dashBtn.setBackground(bg); 
        dashBtn.setForeground(Color.LIGHT_GRAY);
        dashBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashBtn.setBorderPainted(false); 
        dashBtn.setFocusPainted(false);
        dashBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        dashBtn.addActionListener(e -> {
            new DashboardScreen(currentUser).setVisible(true); // 
            this.dispose(); 
        });
        
        topPanel.add(dashBtn);
        add(topPanel, BorderLayout.NORTH);

     
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        nameField = createStyledField(fieldFont, purple, purpleGlow);
        targetField = createStyledField(fieldFont, purple, purpleGlow);
        deadlineField = createStyledField(fieldFont, purple, purpleGlow);
        deadlineField.setText("2026-12-31");
        idField = createStyledField(fieldFont, purple, purpleGlow);
        linkGoalIdField = createStyledField(fieldFont, purple, purpleGlow);
        
        transComboBox = new JComboBox<>();
        transComboBox.setBackground(new Color(40, 40, 45));
        transComboBox.setForeground(Color.WHITE);

      
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(40, 40, 45));
        displayArea.setForeground(Color.WHITE);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        goalProgressBar = new JProgressBar(0, 100);
        goalProgressBar.setStringPainted(true); 
        goalProgressBar.setForeground(purpleGlow); 
        goalProgressBar.setBackground(new Color(45, 45, 50)); 

        addBtn = createStyledButton("Add Goal", purple);
        clearBtn = createStyledButton("Clear", new Color(192, 57, 43));
        refreshBtn = createStyledButton("Refresh List", purple);
        linkBtn = createStyledButton("Link to Goal", purple);

     
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(bg);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(panelBg);
        inputPanel.setBorder(createCustomTitledBorder("Create Saving Goal"));
        inputPanel.add(createStyledLabel(" Goal Name:")); inputPanel.add(nameField);
        inputPanel.add(createStyledLabel(" Target:")); inputPanel.add(targetField);
        inputPanel.add(createStyledLabel(" Deadline:")); inputPanel.add(deadlineField);
        inputPanel.add(createStyledLabel(" Goal ID:")); inputPanel.add(idField);
        inputPanel.add(addBtn); inputPanel.add(clearBtn);

        JPanel linkPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        linkPanel.setBackground(panelBg);
        linkPanel.setBorder(createCustomTitledBorder("Link & View Progress"));
        linkPanel.add(createStyledLabel(" Select Transaction:")); linkPanel.add(transComboBox);
        linkPanel.add(createStyledLabel(" Refresh:")); linkPanel.add(refreshBtn);
        linkPanel.add(createStyledLabel(" Enter Goal ID:")); linkPanel.add(linkGoalIdField);
        linkPanel.add(createStyledLabel(" Confirm Action:")); linkPanel.add(linkBtn);

        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        statusPanel.setBackground(panelBg);
        statusPanel.setBorder(createCustomTitledBorder("Visual Progress"));
        statusPanel.add(goalProgressBar, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        scrollPane.setPreferredSize(new Dimension(600, 80)); 
        scrollPane.setMaximumSize(new Dimension(600, 80));
        statusPanel.add(scrollPane, BorderLayout.CENTER); 

        mainContent.add(inputPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(linkPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(statusPanel);
        mainContent.add(Box.createVerticalGlue()); 

        JPanel marginWrapper = new JPanel(new BorderLayout());
        marginWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        marginWrapper.setBackground(bg);
        marginWrapper.add(mainContent, BorderLayout.CENTER);
        
        add(marginWrapper, BorderLayout.CENTER);

       
        addBtn.addActionListener(e -> {
            try {
                controller.addGoal(nameField.getText(), Double.parseDouble(targetField.getText()), deadlineField.getText(), Integer.parseInt(idField.getText()));
                displayArea.setText(">> Goal '" + nameField.getText() + "' added successfully."); // ⭐ setText بدلاً من append
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check inputs!"); }
        });

        refreshBtn.addActionListener(e -> {
            transComboBox.removeAllItems();
            TransactionRepository repo = new TransactionRepository();
            for (Transaction t : repo.loadTransactions()) {
             
                if(t.getUserId().equals(currentUser.getId())) {
                    transComboBox.addItem(t.getType() + ": $" + t.getAmount() + " (" + t.getId() + ")");
                }
            }
        });

        linkBtn.addActionListener(e -> {
            String selected = (String) transComboBox.getSelectedItem();
            String gIdStr = linkGoalIdField.getText();
            if (selected != null && !gIdStr.isEmpty()) {
                String uuid = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
                int goalId = Integer.parseInt(gIdStr);
                linkTransactionToGoal(uuid, goalId);
                updateVisualProgress(goalId);
            }
        });

        clearBtn.addActionListener(e -> {
            nameField.setText(""); targetField.setText(""); idField.setText("");
            linkGoalIdField.setText(""); deadlineField.setText("2026-12-31"); 
            displayArea.setText(""); goalProgressBar.setValue(0);
        });

        setLocationRelativeTo(null);
    }

    private void updateVisualProgress(int goalId) {
        TransactionRepository repo = new TransactionRepository();
        List<Transaction> allTrans = repo.loadTransactions();
        double saved = 0, target = 0;
        for (SavingGoal g : controller.getAllGoals()) {
            if (g.getGoalID() == goalId) { target = g.getTargetAmount(); break; }
        }
        
        
        try (java.util.Scanner r = new java.util.Scanner(new File("goal_links.txt"))) {
            while (r.hasNextLine()) {
                String[] d = r.nextLine().split(",");
                if (Integer.parseInt(d[1]) == goalId) {
                    for (Transaction t : allTrans) {
                        if (t.getId().equals(d[0]) && t.getUserId().equals(currentUser.getId())) {
                            if (t.getType().equalsIgnoreCase("Income")) saved += t.getAmount();
                            else saved -= t.getAmount();
                        }
                    }
                }
            }
        } catch (Exception e) {}

        int percentage = (target > 0) ? (int)((saved / target) * 100) : 0;
        goalProgressBar.setValue(Math.min(100, Math.max(0, percentage)));
        displayArea.setText(">> Analysis: Goal ID " + goalId + " is " + percentage + "% complete."); // 
    }

    public void linkTransactionToGoal(String transId, int goalId) {
        try (FileWriter fw = new FileWriter("goal_links.txt", true)) {
            fw.write(transId + "," + goalId + "\n");
        } catch (IOException e) {}
    }
    
  
    private JTextField createStyledField(Font f, Color b, Color gl) {
        JTextField field = new JTextField();
        field.setFont(f); field.setForeground(Color.WHITE);
        field.setBackground(new Color(40, 40, 45));
        field.setBorder(BorderFactory.createLineBorder(b, 2));
        return field;
    }

    private JButton createStyledButton(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createStyledLabel(String t) {
        JLabel lbl = new JLabel(t);
        lbl.setForeground(textColor);
        return lbl;
    }

    private TitledBorder createCustomTitledBorder(String t) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(purple, 1), t);
        border.setTitleColor(purpleGlow);
        return border;
    }
}