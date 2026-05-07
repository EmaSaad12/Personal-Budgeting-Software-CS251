/*package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import repository.TransactionRepository;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportScreen extends JFrame {
    private JTextField startDateField, endDateField;
    private JPanel chartsPanel;
    private JLabel insightLabel;
    private TransactionRepository repo;


    Color bg = new Color(20, 20, 25);
    Color panelBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);

    public ReportScreen() {
        repo = new TransactionRepository();

        setTitle("Financial Dashboard & Analytics");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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
            new DashboardScreen().setVisible(true);
            this.dispose();
        });

        topPanel.add(dashBtn);

        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
        northContainer.setBackground(bg);
        northContainer.add(topPanel);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        datePanel.setBackground(panelBg);
        datePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, purple));

        JLabel startLbl = createStyledLabel("Start Date (YYYY-MM-DD):");
        startDateField = createStyledField(LocalDate.now().withDayOfMonth(1).toString());

        JLabel endLbl = createStyledLabel("End Date (YYYY-MM-DD):");
        endDateField = createStyledField(LocalDate.now().toString());

        JButton generateBtn = createStyledButton("Update Analytics", purple);

        datePanel.add(startLbl);
        datePanel.add(startDateField);
        datePanel.add(endLbl);
        datePanel.add(endDateField);
        datePanel.add(generateBtn);
        datePanel.setBackground(bg);


        chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setBackground(bg);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(chartsPanel, BorderLayout.CENTER);


        insightLabel = new JLabel("Click 'Update Analytics' to see insights.", SwingConstants.CENTER);
        insightLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        insightLabel.setForeground(purpleGlow);
        insightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));


        northContainer.add(topPanel);
        northContainer.add(datePanel);
        northContainer.add(insightLabel);
        northContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, purple));

        add(northContainer, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(panelBg);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, purple),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        bottomPanel.add(insightLabel, BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(1000, 80));
        bottomPanel.setBackground(bg);

        add(bottomPanel, BorderLayout.SOUTH);

        generateBtn.addActionListener(e -> generateReport());
        generateReport();
    }

    private void generateReport() {
        try {
            LocalDate start = LocalDate.parse(startDateField.getText());
            LocalDate end = LocalDate.parse(endDateField.getText());
            List<Transaction> transactions = repo.loadTransactions();

            Map<String, Double> expenseMap = new HashMap<>();
            double totalIn = 0, totalOut = 0;

            for (Transaction t : transactions) {
                LocalDate d = LocalDate.parse(t.getDate());
                if (!d.isBefore(start) && !d.isAfter(end)) {
                    if (t.getType().equalsIgnoreCase("Expense")) {
                        totalOut += t.getAmount();
                        expenseMap.put(t.getCategory(), expenseMap.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                    } else if (t.getType().equalsIgnoreCase("Income")) {
                        totalIn += t.getAmount();
                    }
                }
            }

            chartsPanel.removeAll();


            DefaultPieDataset pieData = new DefaultPieDataset();
            String topCat = "None"; double maxExp = 0;
            for (var entry : expenseMap.entrySet()) {
                pieData.setValue(entry.getKey(), entry.getValue());
                if (entry.getValue() > maxExp) { maxExp = entry.getValue(); topCat = entry.getKey(); }
            }
            JFreeChart pieChart = ChartFactory.createPieChart("Expense Breakdown", pieData, true, true, false);
            styleChart(pieChart);


            PiePlot piePlot = (PiePlot) pieChart.getPlot();
            piePlot.setBackgroundPaint(panelBg);
            piePlot.setOutlinePaint(null);
            piePlot.setLabelBackgroundPaint(new Color(50, 50, 55));
            piePlot.setLabelPaint(Color.WHITE);

            chartsPanel.add(new ChartPanel(pieChart));


            DefaultCategoryDataset barData = new DefaultCategoryDataset();
            barData.addValue(totalIn, "Value", "Total Income");
            barData.addValue(totalOut, "Value", "Total Expenses");

            JFreeChart barChart = ChartFactory.createBarChart("Income vs Spending", "Category", "Amount ($)", barData);
            styleChart(barChart);

            CategoryPlot barPlot = barChart.getCategoryPlot();
            barPlot.setBackgroundPaint(bg);
            barPlot.getDomainAxis().setTickLabelPaint(Color.WHITE);
            barPlot.getDomainAxis().setLabelPaint(Color.WHITE);
            barPlot.getRangeAxis().setTickLabelPaint(Color.WHITE);
            barPlot.getRangeAxis().setLabelPaint(Color.WHITE);

            chartsPanel.add(new ChartPanel(barChart));


            if (totalOut > 0) {
                double perc = (maxExp / totalOut) * 100;
                insightLabel.setText(String.format("💡 Key Insight: Your '%s' spending is %.1f%% of your total expenses!", topCat, perc));
            } else {
                insightLabel.setText("💡 Insight: No expenses in this period. Great job saving!");
            }

            chartsPanel.revalidate(); chartsPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Check date format (YYYY-MM-DD)");
        }
    }


    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(panelBg);
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(panelBg);
        chart.getLegend().setItemPaint(Color.WHITE);
    }


    private JTextField createStyledField(String text) {
        JTextField field = new JTextField(text, 10);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(40, 40, 45));
        field.setBorder(BorderFactory.createLineBorder(purple, 2));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JButton createStyledButton(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createStyledLabel(String t) {
        JLabel lbl = new JLabel(t);
        lbl.setForeground(textColor);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }
}
*/
package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import repository.TransactionRepository;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportScreen extends JFrame {

    private JTextField startDateField, endDateField;
    private JPanel chartsPanel;
    private JLabel insightLabel;
    private TransactionRepository repo;

    // COLORS
    Color bg = new Color(8, 8, 12);
    Color cardBg = new Color(15, 15, 22);
    Color fieldBg = new Color(25, 25, 35);

    Color bankBlue = new Color(120, 200, 255);

    Color purple = new Color(70, 20, 120);

    Color hover = new Color(95, 35, 155);

    Color textColor = Color.WHITE;

    public ReportScreen() {

        repo = new TransactionRepository();

        setTitle("Financial Analytics");

        setSize(1100, 760);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(bg);

        // ================= HEADER =================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(bg);

        header.setBorder(
                new EmptyBorder(20, 25, 10, 25)
        );

        // LEFT
        JPanel left =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT,
                        15,
                        0
                ));

        left.setOpaque(false);

        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        45
                )
        );

        logo.setForeground(bankBlue);

        JLabel title =
                new JLabel("Financial Reports");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );

        left.add(logo);

        left.add(title);

        // RIGHT BUTTONS
        JPanel right =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT,
                        15,
                        0
                ));

        right.setOpaque(false);

        RoundedButton dashBtn =
                new RoundedButton(
                        "Dashboard",
                        bankBlue,
                        new Color(150, 220, 255)
                );

        dashBtn.setForeground(Color.BLACK);

        dashBtn.setPreferredSize(
                new Dimension(170, 45)
        );

        RoundedButton closeBtn =
                new RoundedButton(
                        "Close",
                        new Color(180, 45, 45),
                        new Color(220, 70, 70)
                );

        closeBtn.setPreferredSize(
                new Dimension(130, 45)
        );

        right.add(dashBtn);

        right.add(closeBtn);

        header.add(left, BorderLayout.WEST);

        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ================= CENTER =================

        JPanel center =
                new JPanel();

        center.setBackground(bg);

        center.setLayout(
                new BorderLayout(15, 15)
        );

        center.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        // FILTER CARD
        JPanel filterCard =
                createCardPanel();

        filterCard.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        20,
                        20
                )
        );

        JLabel startLbl =
                createStyledLabel("Start Date");

        startDateField =
                createStyledField(
                        LocalDate.now()
                                .withDayOfMonth(1)
                                .toString()
                );

        JLabel endLbl =
                createStyledLabel("End Date");

        endDateField =
                createStyledField(
                        LocalDate.now()
                                .toString()
                );

        RoundedButton generateBtn =
                new RoundedButton(
                        "Generate Report",
                        purple,
                        hover
                );

        generateBtn.setPreferredSize(
                new Dimension(220, 45)
        );

        filterCard.add(startLbl);

        filterCard.add(startDateField);

        filterCard.add(endLbl);

        filterCard.add(endDateField);

        filterCard.add(generateBtn);

        // CHARTS PANEL
        chartsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                20
                        )
                );

        chartsPanel.setBackground(bg);

        // INSIGHT CARD
        JPanel insightCard =
                createCardPanel();

        insightCard.setLayout(
                new BorderLayout()
        );

        insightLabel =
                new JLabel(
                        "Click Generate Report To View Insights",
                        SwingConstants.CENTER
                );

        insightLabel.setForeground(bankBlue);

        insightLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        insightCard.add(
                insightLabel,
                BorderLayout.CENTER
        );

        center.add(
                filterCard,
                BorderLayout.NORTH
        );

        center.add(
                chartsPanel,
                BorderLayout.CENTER
        );

        center.add(
                insightCard,
                BorderLayout.SOUTH
        );

        add(center);

        // ================= ACTIONS =================

        dashBtn.addActionListener(e -> {

            new DashboardScreen();

            dispose();
        });

        closeBtn.addActionListener(
                e -> System.exit(0)
        );

        generateBtn.addActionListener(
                e -> generateReport()
        );

        generateReport();

        setVisible(true);
    }

    // ================= REPORT =================

    private void generateReport() {

        try {

            LocalDate start =
                    LocalDate.parse(
                            startDateField.getText()
                    );

            LocalDate end =
                    LocalDate.parse(
                            endDateField.getText()
                    );

            List<Transaction> transactions =
                    repo.loadTransactions();

            Map<String, Double> expenseMap =
                    new HashMap<>();

            double totalIn = 0;

            double totalOut = 0;

            for (Transaction t : transactions) {

                LocalDate d =
                        LocalDate.parse(
                                t.getDate()
                        );

                if (!d.isBefore(start)
                        && !d.isAfter(end)) {

                    if (t.getType()
                            .equalsIgnoreCase(
                                    "Expense"
                            )) {

                        totalOut += t.getAmount();

                        expenseMap.put(
                                t.getCategory(),
                                expenseMap.getOrDefault(
                                        t.getCategory(),
                                        0.0
                                ) + t.getAmount()
                        );

                    } else {

                        totalIn += t.getAmount();
                    }
                }
            }

            chartsPanel.removeAll();

            // ================= PIE CHART =================

            DefaultPieDataset pieData =
                    new DefaultPieDataset();

            String topCat = "None";

            double maxExp = 0;

            for (var entry :
                    expenseMap.entrySet()) {

                pieData.setValue(
                        entry.getKey(),
                        entry.getValue()
                );

                if (entry.getValue() > maxExp) {

                    maxExp = entry.getValue();

                    topCat = entry.getKey();
                }
            }

            JFreeChart pieChart =
                    ChartFactory.createPieChart(
                            "Expense Breakdown",
                            pieData,
                            true,
                            true,
                            false
                    );

            styleChart(pieChart);

            PiePlot piePlot =
                    (PiePlot) pieChart.getPlot();

            piePlot.setBackgroundPaint(cardBg);

            piePlot.setOutlinePaint(null);

            piePlot.setLabelBackgroundPaint(
                    new Color(30, 30, 40)
            );

            piePlot.setLabelPaint(Color.WHITE);

            piePlot.setShadowPaint(null);

            ChartPanel piePanel =
                    new ChartPanel(pieChart);

            piePanel.setBorder(
                    new RoundedBorder(
                            bankBlue,
                            25
                    )
            );

            piePanel.setBackground(cardBg);

            chartsPanel.add(piePanel);

            // ================= BAR CHART =================

            DefaultCategoryDataset barData =
                    new DefaultCategoryDataset();

            barData.addValue(
                    totalIn,
                    "Value",
                    "Income"
            );

            barData.addValue(
                    totalOut,
                    "Value",
                    "Expenses"
            );

            JFreeChart barChart =
                    ChartFactory.createBarChart(
                            "Income vs Expenses",
                            "",
                            "Amount",
                            barData
                    );

            styleChart(barChart);

            CategoryPlot barPlot =
                    barChart.getCategoryPlot();

            barPlot.setBackgroundPaint(cardBg);

            barPlot.getDomainAxis()
                    .setTickLabelPaint(
                            Color.WHITE
                    );

            barPlot.getRangeAxis()
                    .setTickLabelPaint(
                            Color.WHITE
                    );

            barPlot.getDomainAxis()
                    .setLabelPaint(
                            Color.WHITE
                    );

            barPlot.getRangeAxis()
                    .setLabelPaint(
                            Color.WHITE
                    );

            ChartPanel barPanel =
                    new ChartPanel(barChart);

            barPanel.setBorder(
                    new RoundedBorder(
                            bankBlue,
                            25
                    )
            );

            barPanel.setBackground(cardBg);

            chartsPanel.add(barPanel);

            // ================= INSIGHT =================

            if (totalOut > 0) {

                double perc =
                        (maxExp / totalOut) * 100;

                insightLabel.setText(
                        "Top Spending Category: "
                                + topCat
                                + " ("
                                + String.format(
                                "%.1f",
                                perc
                        )
                                + "% of expenses)"
                );

            } else {

                insightLabel.setText(
                        "Excellent! No expenses found in this period."
                );
            }

            chartsPanel.revalidate();

            chartsPanel.repaint();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Date Format (YYYY-MM-DD)"
            );
        }
    }

    // ================= STYLE CHART =================

    private void styleChart(
            JFreeChart chart
    ) {

        chart.setBackgroundPaint(cardBg);

        chart.getTitle().setPaint(Color.WHITE);

        if (chart.getLegend() != null) {

            chart.getLegend()
                    .setBackgroundPaint(cardBg);

            chart.getLegend()
                    .setItemPaint(Color.WHITE);
        }
    }

    // ================= FIELD =================

    private JTextField createStyledField(
            String text
    ) {

        JTextField field =
                new JTextField(text);

        field.setPreferredSize(
                new Dimension(180, 42)
        );

        field.setBackground(fieldBg);

        field.setForeground(Color.WHITE);

        field.setCaretColor(Color.WHITE);

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        field.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        return field;
    }

    // ================= LABEL =================

    private JLabel createStyledLabel(
            String text
    ) {

        JLabel l =
                new JLabel(text);

        l.setForeground(textColor);

        l.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        return l;
    }

    // ================= CARD =================

    private JPanel createCardPanel() {

        JPanel p =
                new JPanel();

        p.setBackground(cardBg);

        p.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                bankBlue,
                                28
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        return p;
    }

    // ================= BUTTON =================

    class RoundedButton extends JButton {

        private Color base;

        private Color hoverColor;

        public RoundedButton(
                String text,
                Color base,
                Color hoverColor
        ) {

            super(text);

            this.base = base;

            this.hoverColor = hoverColor;

            setFocusPainted(false);

            setBorderPainted(false);

            setContentAreaFilled(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setForeground(Color.WHITE);

            setBackground(base);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            15
                    )
            );

            addMouseListener(
                    new MouseAdapter() {

                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            setBackground(
                                    hoverColor
                            );
                        }

                        public void mouseExited(
                                MouseEvent e
                        ) {

                            setBackground(base);
                        }
                    }
            );
        }

        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    25,
                    25
            );

            super.paintComponent(g);
        }
    }

    // ================= BORDER =================

    class RoundedBorder
            extends AbstractBorder {

        private Color color;

        private int radius;

        public RoundedBorder(
                Color color,
                int radius
        ) {

            this.color = color;

            this.radius = radius;
        }

        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );
        }

        public Insets getBorderInsets(
                Component c
        ) {

            return new Insets(
                    10,
                    15,
                    10,
                    15
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                ReportScreen::new
        );
    }
}