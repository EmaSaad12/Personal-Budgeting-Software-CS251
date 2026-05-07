package com.mycompany.a2.us6and7;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

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