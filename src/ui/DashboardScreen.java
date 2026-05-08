package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import repository.TransactionRepository;
import service.BudgetService;
import controller.GoalController;
import model.Transaction;
import model.Budget;
import model.User;
import model.SavingGoal;

public class DashboardScreen extends JFrame {

    private TransactionRepository transRepo;
    private BudgetService budgetService;
    private GoalController goalController;
    private User currentUser;

    // COLORS
    Color bg = new Color(8, 8, 12);
    Color cardBg = new Color(15, 15, 22);
    Color bankBlue = new Color(120, 200, 255);
    Color purple = new Color(70, 20, 120);
    Color hover = new Color(95, 35, 155);
    Color textColor = Color.WHITE;
    Color greenAccent = new Color(46, 204, 113);
    Color redAccent = new Color(231, 76, 60);

    public DashboardScreen(User user) {
        this.currentUser = user;
        transRepo = new TransactionRepository();
        budgetService = new BudgetService();
        goalController = new GoalController();

        setTitle("Dashboard - " + currentUser.getName());
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bg);
        topPanel.setBorder(new EmptyBorder(20, 25, 10, 25));

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTop.setOpaque(false);
        JLabel logo = new JLabel("🏦");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        logo.setForeground(bankBlue);
        JLabel title = new JLabel("Dashboard");
        title.setForeground(bankBlue);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        leftTop.add(logo);
        leftTop.add(title);

        RoundedButton closeBtn = new RoundedButton("Close Dashboard", redAccent, new Color(255, 90, 90));
        closeBtn.setPreferredSize(new Dimension(180, 42));
        closeBtn.addActionListener(e -> System.exit(0));

        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(closeBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ================= MAIN CONTENT =================
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(bg);
        mainContent.setBorder(new EmptyBorder(10, 25, 20, 25));

        mainContent.add(createBalanceCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 18)));
        mainContent.add(createStatsCardsPanel());
        mainContent.add(Box.createRigidArea(new Dimension(0, 18)));

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 18, 18));
        bottomPanel.setBackground(bg);
        bottomPanel.add(createRecentTransactionsCard());
        bottomPanel.add(createBudgetWarningsCard());
        mainContent.add(bottomPanel);

        add(mainContent, BorderLayout.CENTER);

        // ================= NAVBAR =================
        JPanel navBar = new JPanel(new GridLayout(1, 4, 15, 0));
        navBar.setBackground(bg);
        navBar.setBorder(new EmptyBorder(15, 25, 20, 25));

        RoundedButton goBudgetBtn = new RoundedButton("Manage Budgets", purple, hover);
        goBudgetBtn.addActionListener(e -> new BudgetScreen(currentUser).setVisible(true));

        RoundedButton goTransBtn = new RoundedButton("Transactions", purple, hover);
        goTransBtn.addActionListener(e -> new AddTransactionUI(currentUser).setVisible(true));

        RoundedButton goGoalsBtn = new RoundedButton("Saving Goals", purple, hover);
        goGoalsBtn.addActionListener(e -> new GoalFrame(currentUser).setVisible(true));

        RoundedButton goReportBtn = new RoundedButton("Reports", purple, hover);
        goReportBtn.addActionListener(e -> new ReportScreen(currentUser).setVisible(true));

        navBar.add(goBudgetBtn); navBar.add(goTransBtn); navBar.add(goGoalsBtn); navBar.add(goReportBtn);
        add(navBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= 1. BALANCE CARD (FILTERED) =================
    private JPanel createBalanceCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new GridLayout(1, 3, 15, 15));

        List<Transaction> allTrans = transRepo.loadTransactions();
        double income = 0, expense = 0;

        for (Transaction t : allTrans) {
            // التحقق من أن المعاملة تخص اليوزر الحالي فقط
            if (t.getUserId().equals(currentUser.getId())) {
                if (t.getType().equalsIgnoreCase("Income")) income += t.getAmount();
                else expense += t.getAmount();
            }
        }
        double net = income - expense;

        card.add(createLabelData("Total Income", "+$" + income, greenAccent));
        card.add(createLabelData("Total Expenses", "-$" + expense, redAccent));
        card.add(createLabelData("Net Balance", "$" + net, bankBlue));
        return card;
    }

    // ================= 2. STATS PANEL (FILTERED) =================
    private JPanel createStatsCardsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        panel.setBackground(bg);

        long transCount = transRepo.loadTransactions().stream()
                .filter(t -> t.getUserId().equals(currentUser.getId())).count();

        long budgetCount = budgetService.getAllBudgets().stream()
                .filter(b -> b.getUserId().equals(currentUser.getId())).count();

        long goalCount = goalController.getAllGoals().stream()
                .filter(g -> g.getUserId().equals(currentUser.getId())).count();

        panel.add(createMiniCard("Transactions", String.valueOf(transCount)));
        panel.add(createMiniCard("Budgets", String.valueOf(budgetCount)));
        panel.add(createMiniCard("Goals", String.valueOf(goalCount)));
        return panel;
    }

    // ================= 3. RECENT TRANSACTIONS (FILTERED) =================
    private JPanel createRecentTransactionsCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new BorderLayout());
        card.add(createSectionTitle("Recent Transactions"), BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(cardBg);
        textArea.setForeground(textColor);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        List<Transaction> allList = transRepo.loadTransactions();
        List<Transaction> userList = new ArrayList<>();

        // تصفية المعاملات للمستخدم الحالي فقط
        for (Transaction t : allList) {
            if (t.getUserId().equals(currentUser.getId())) {
                userList.add(t);
            }
        }

        int count = 0;
        for (int i = userList.size() - 1; i >= 0 && count < 5; i--) {
            Transaction t = userList.get(i);
            textArea.append(String.format(" %s | %-7s | $%.2f\n",
                    t.getDate(), t.getType(), t.getAmount()));
            count++;
        }

        if (count == 0) textArea.setText(" No recent transactions.");

        card.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return card;
    }

    // ================= 4. BUDGET ALERTS (FILTERED) =================
    private JPanel createBudgetWarningsCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new BorderLayout());
        card.add(createSectionTitle("Budget Alerts"), BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(cardBg);
        textArea.setForeground(textColor);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        List<Budget> budgets = budgetService.getAllBudgets();
        // تحميل المعاملات مرة واحدة للفلترة
        ArrayList<Transaction> userTrans = new ArrayList<>();
        for (Transaction t : transRepo.loadTransactions()) {
            if (t.getUserId().equals(currentUser.getId())) userTrans.add(t);
        }

        boolean hasWarnings = false;
        for (Budget b : budgets) {
            // التحقق من ميزانيات المستخدم الحالي فقط
            if (b.getUserId().equals(currentUser.getId())) {
                String status = budgetService.CheckAlert(b.get_categ(), userTrans);
                if (status.contains("Warning") || status.contains("Alert")) {
                    textArea.append("⚠ " + b.get_categ() + ": " + status + "\n\n");
                    hasWarnings = true;
                }
            }
        }

        if (!hasWarnings) textArea.setText("✅ All budgets are under control.");

        card.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return card;
    }

    // --- الدوال المساعدة (UI Helpers) تظل كما هي لثبات التصميم ---
    private JPanel createMiniCard(String title, String value) {
        JPanel card = createRoundedCard();
        card.setLayout(new GridLayout(2, 1));
        JLabel tLbl = new JLabel(title, SwingConstants.CENTER);
        tLbl.setForeground(textColor);
        tLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JLabel vLbl = new JLabel(value, SwingConstants.CENTER);
        vLbl.setForeground(bankBlue);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(tLbl); card.add(vLbl);
        return card;
    }

    private JPanel createLabelData(String title, String value, Color vColor) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(cardBg);
        JLabel tLbl = new JLabel(title, SwingConstants.CENTER);
        tLbl.setForeground(textColor);
        tLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel vLbl = new JLabel(value, SwingConstants.CENTER);
        vLbl.setForeground(vColor);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        p.add(tLbl); p.add(vLbl);
        return p;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(bankBlue);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setBorder(new EmptyBorder(10, 15, 10, 10));
        return label;
    }

    private JPanel createRoundedCard() {
        JPanel panel = new JPanel();
        panel.setBackground(cardBg);
        panel.setBorder(new CompoundBorder(new RoundedBorder(bankBlue, 20), new EmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    class RoundedButton extends JButton {
        private Color base, hColor;
        public RoundedButton(String t, Color b, Color h) {
            super(t); this.base = b; this.hColor = bankBlue;
            setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
            setForeground(Color.WHITE); setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(b); setFont(new Font("Segoe UI", Font.BOLD, 14));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(hColor); setForeground(Color.BLACK); }
                public void mouseExited(MouseEvent e) { setBackground(base); setForeground(Color.WHITE); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }
    }

    class RoundedBorder extends AbstractBorder {
        private Color color; private int radius;
        public RoundedBorder(Color c, int r) { this.color = c; this.radius = r; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
        }
        public Insets getBorderInsets(Component c) { return new Insets(10, 15, 10, 15); }
    }
}