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
import model.User;

public class ReportScreen extends JFrame {

    private JTextField startDateField, endDateField;
    private JPanel chartsPanel;
    private JLabel insightLabel;
    private TransactionRepository repo;
    private User currentUser;

    // COLORS
    Color bg = new Color(8, 8, 12);
    Color cardBg = new Color(15, 15, 22);
    Color fieldBg = new Color(25, 25, 35);

    Color bankBlue = new Color(120, 200, 255);

    Color purple = new Color(70, 20, 120);

    Color hover = new Color(95, 35, 155);

    Color textColor = Color.WHITE;

    public ReportScreen(User user) {
        this.currentUser = user;

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

            new DashboardScreen(currentUser);

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
                if (t.getUserId().equals(currentUser.getId())){

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
                }}

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

}