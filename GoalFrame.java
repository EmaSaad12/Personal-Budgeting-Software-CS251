package com.mycompany.a2.us6and7;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class GoalFrame extends JFrame {
    private GoalController controller = new GoalController();
    private JTextField nameField, targetField, deadlineField, idField, linkGoalIdField;
    private JComboBox<String> transComboBox; 
    private JTextArea displayArea;
    private JProgressBar goalProgressBar; 
    private JButton addBtn, clearBtn, refreshBtn, linkBtn;

   
    Color bg = new Color(20, 20, 25);
    Color panelBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);

    public GoalFrame() {
        setTitle("Saving Goal Manager");
        setSize(650, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bg);

        
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
        goalProgressBar.setValue(0);
        goalProgressBar.setStringPainted(true); 
        goalProgressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        goalProgressBar.setForeground(purpleGlow); 
        goalProgressBar.setBackground(new Color(45, 45, 50)); 
        goalProgressBar.setBorder(BorderFactory.createLineBorder(purple, 1));

     
        addBtn = createStyledButton("Add Goal", purple);
        clearBtn = createStyledButton("Clear", new Color(192, 57, 43));
        refreshBtn = createStyledButton("Refresh List", purple);
        linkBtn = createStyledButton("Link to Goal & Show Progress", purple);

        
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
        scrollPane.setPreferredSize(new Dimension(600, 150));
        statusPanel.add(scrollPane, BorderLayout.CENTER); 

      
        mainContent.add(inputPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(linkPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(statusPanel);

        JPanel marginWrapper = new JPanel(new BorderLayout());
        marginWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        marginWrapper.setBackground(bg);
        marginWrapper.add(mainContent, BorderLayout.CENTER);
        
        add(marginWrapper);

        
        addBtn.addActionListener(e -> {
            try {
                controller.addGoal(nameField.getText(), Double.parseDouble(targetField.getText()), deadlineField.getText(), Integer.parseInt(idField.getText()));
                displayArea.append(">> Goal '" + nameField.getText() + "' added successfully.\n");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check inputs!"); }
        });

        refreshBtn.addActionListener(e -> {
            transComboBox.removeAllItems();
            TransactionRepository repo = new TransactionRepository();
            for (Transaction t : repo.loadTransactions()) {
                transComboBox.addItem(t.getType() + ": $" + t.getAmount() + " (" + t.getId() + ")");
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
            
            nameField.setText("");
            targetField.setText("");
            idField.setText("");
            linkGoalIdField.setText("");
            deadlineField.setText("2026-12-31"); 
            
            
            displayArea.setText(""); 
            
           
            goalProgressBar.setValue(0);
            
            
            System.out.println("All fields cleared.");
        });

        setLocationRelativeTo(null);
    }

    
    private void updateVisualProgress(int goalId) {
        TransactionRepository repo = new TransactionRepository();
        List<Transaction> allTrans = repo.loadTransactions();
        
        
        String report = controller.generateGoalsReportOnly(allTrans);
        
        
        double saved = 0;
        double target = 0;
        for (SavingGoal g : controller.getAllGoals()) {
            if (g.getGoalID() == goalId) {
                target = g.getTargetAmount();
                break;
            }
        }
        
      
        java.util.Map<String, Integer> mappings = loadMappingsFromController();
        for (Transaction t : allTrans) {
            if (mappings.containsKey(t.getId()) && mappings.get(t.getId()) == goalId) {
                if (t.getType().equalsIgnoreCase("Income")) saved += t.getAmount();
                else saved -= t.getAmount();
            }
        }

        int percentage = (target > 0) ? (int)((saved / target) * 100) : 0;
        if (percentage > 100) percentage = 100; 
        if (percentage < 0) percentage = 0;

        goalProgressBar.setValue(percentage);
        displayArea.append(">> Visual Progress Updated: " + percentage + "%\n");
    }

    private java.util.Map<String, Integer> loadMappingsFromController() {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        try (java.util.Scanner r = new java.util.Scanner(new File("goal_links.txt"))) {
            while (r.hasNextLine()) {
                String[] d = r.nextLine().split(",");
                if (d.length == 2) map.put(d[0], Integer.parseInt(d[1]));
            }
        } catch (Exception e) {}
        return map;
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