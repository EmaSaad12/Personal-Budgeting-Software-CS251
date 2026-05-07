package com.mycompany.a2.us6and7;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DashboardScreen extends JFrame {
    private TransactionRepository transRepo;
    private BudgetService budgetService;
    private GoalController goalController;
    private User currentUser; 

    Color bg = new Color(20, 20, 25);
    Color cardBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);
    Color greenAccent = new Color(46, 204, 113);
    Color redAccent = new Color(231, 76, 60);

    public DashboardScreen(User user) {
        this.currentUser = user;
        transRepo = new TransactionRepository();
        budgetService = new BudgetService();
        goalController = new GoalController();

        setTitle("Financial System - Home Dashboard");
        setSize(950, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bg);

        JLabel headerLabel = new JLabel(" Welcome Back, " + currentUser.getName() + "!", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(20, 20, 0, 20));
        add(headerLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(bg);
        mainContent.setBorder(new EmptyBorder(10, 20, 20, 20));

        mainContent.add(createBalanceCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(createStatsCardsPanel());
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        bottomPanel.setBackground(bg);
        bottomPanel.add(createRecentTransactionsCard());
        bottomPanel.add(createBudgetWarningsCard());
        
        mainContent.add(bottomPanel);
        add(mainContent, BorderLayout.CENTER);
        
        // Navigation Bar
        JPanel navBar = new JPanel(new GridLayout(1, 4, 15, 0));
        navBar.setBackground(cardBg);
        navBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, purple), 
            new EmptyBorder(15, 20, 15, 20) 
        ));
        
        JButton goBudgetBtn = createNavButton("Manage Budgets", purple);
        goBudgetBtn.addActionListener(e -> { new BudgetScreen(currentUser).setVisible(true); this.dispose(); });

        JButton goTransBtn = createNavButton("Transactions", new Color(52, 152, 219)); 
        goTransBtn.addActionListener(e -> { new AddTransactionUI(currentUser).setVisible(true); this.dispose(); });
        
        JButton goGoalsBtn = createNavButton("Saving Goals", greenAccent);
        goGoalsBtn.addActionListener(e -> { new GoalFrame(currentUser).setVisible(true); this.dispose(); });

        JButton goReportBtn = createNavButton("Report", redAccent); 
        goReportBtn.addActionListener(e -> { new ReportScreen(currentUser).setVisible(true); this.dispose(); });

        navBar.add(goBudgetBtn); navBar.add(goTransBtn);
        navBar.add(goGoalsBtn); navBar.add(goReportBtn);
        add(navBar, BorderLayout.SOUTH);
    }

    private JPanel createBalanceCard() {
        JPanel card = new JPanel(new GridLayout(1, 3, 10, 10));
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(purple, 2), new EmptyBorder(20, 20, 20, 20)));

        List<Transaction> allTrans = transRepo.loadTransactions();
        double income = 0, expense = 0;
        for (Transaction t : allTrans) {
            if (t.getUserId().equals(currentUser.getId())) { 
                if (t.getType().equalsIgnoreCase("Income")) income += t.getAmount();
                else expense += t.getAmount();
            }
        }
        double net = income - expense;
        card.add(createLabelData("Total Income", "+$" + income, greenAccent));
        card.add(createLabelData("Total Expenses", "-$" + expense, redAccent));
        card.add(createLabelData("Net Balance", "$" + net, (net >= 0 ? purpleGlow : redAccent)));
        return card;
    }

    private JPanel createRecentTransactionsCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(purple, 1), "Recent Transactions", 0, 0, new Font("Segoe UI", Font.BOLD, 14), purpleGlow));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); textArea.setBackground(cardBg); textArea.setForeground(textColor);
        List<Transaction> list = transRepo.loadTransactions();
        int count = 0;
        for (int i = list.size() - 1; i >= 0 && count < 5; i--) {
            Transaction t = list.get(i);
            if (t.getUserId().equals(currentUser.getId())) { 
                textArea.append(String.format(" %s | %-7s | $%.2f\n", t.getDate(), t.getType(), t.getAmount()));
                count++;
            }
        }
        card.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return card;
    }

    private JPanel createBudgetWarningsCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(redAccent, 1), "Budget Alerts", 0, 0, new Font("Segoe UI", Font.BOLD, 14), redAccent));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); textArea.setBackground(cardBg); textArea.setForeground(Color.WHITE);
       
        card.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return card;
    }

    private JPanel createLabelData(String title, String value, Color valueColor) {
        JPanel p = new JPanel(new GridLayout(2, 1)); p.setBackground(cardBg);
        JLabel tLbl = new JLabel(title, SwingConstants.CENTER); tLbl.setForeground(textColor);
        JLabel vLbl = new JLabel(value, SwingConstants.CENTER); vLbl.setForeground(valueColor); vLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(tLbl); p.add(vLbl); return p;
    }

    private JButton createNavButton(String text, Color c) {
        JButton btn = new JButton(text); btn.setBackground(c); btn.setForeground(Color.WHITE); btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }
private JPanel createStatsCardsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        panel.setBackground(bg);

     
        int transCount = 0;
        for (Transaction t : transRepo.loadTransactions()) {
            if (t.getUserId().equals(currentUser.getId())) {
                transCount++;
            }
        }

        
        int budgetCount = 0;
        for (Budget b : budgetService.getAllBudgets()) {
            budgetCount++; 
        }

    
        int goalCount = 0;
        for (SavingGoal g : goalController.getAllGoals()) {
             goalCount++; 
        }

        
        panel.add(createMiniCard("Total Transactions", String.valueOf(transCount)));
        panel.add(createMiniCard("Active Budgets", String.valueOf(budgetCount)));
        panel.add(createMiniCard("Saving Goals", String.valueOf(goalCount)));

        return panel;
    }

    private JPanel createMiniCard(String title, String value) {
        JPanel card = new JPanel(new GridLayout(2, 1)); card.setBackground(cardBg);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel tLbl = new JLabel(title, SwingConstants.CENTER); tLbl.setForeground(textColor);
        JLabel vLbl = new JLabel(value, SwingConstants.CENTER); vLbl.setForeground(Color.WHITE); vLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(tLbl); card.add(vLbl); return card;
    }
}