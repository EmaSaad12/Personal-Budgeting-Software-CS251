package ui;

import service.BudgetService;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import repository.TransactionRepository;

public class BudgetScreen extends JFrame {

    private BudgetService service;
    private JTextField categoryField;
    private JTextField amountField;
    private ArrayList<Transaction> transactions;

    public BudgetScreen() {

        service = new BudgetService();

        TransactionRepository repo = new TransactionRepository();
        transactions = repo.loadTransactions();

        // NOT full screen (card UI)
        setTitle("Budget System");
        setSize(850, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Colors
        Color bg = new Color(16, 14, 20);
        Color cardBg = new Color(20, 20, 26);
        Color purple = new Color(70, 25, 110);
        Color hoverGold = new Color(255, 215, 0);
        Color gold = new Color(255, 215, 0);

        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(cardBg);
        card.setLayout(new GridLayout(8, 1, 14, 14));
        card.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        //  Labels
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setForeground(new Color(220, 220, 220));
        categoryLabel.setFont(labelFont);

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setForeground(new Color(220, 220, 220));
        amountLabel.setFont(labelFont);

        // Inputs
        categoryField = createInput(fieldFont);
        amountField = createInput(fieldFont);

        // Buttons (pill)
        RoundedButton createButton = new RoundedButton("Create Budget", purple, hoverGold);
        RoundedButton editButton = new RoundedButton("Edit Budget", purple, hoverGold);
        RoundedButton alertButton = new RoundedButton("Check Alert", purple, hoverGold);

        card.add(categoryLabel);
        card.add(categoryField);
        card.add(amountLabel);
        card.add(amountField);
        card.add(createButton);
        card.add(editButton);
        card.add(alertButton);

        panel.add(card);
        add(panel);

        // Actions
        createButton.addActionListener(e -> {
            service.createBudget(
                    Double.parseDouble(amountField.getText()),
                    categoryField.getText()
            );
            showToast("Budget Created ✔", gold);
        });

        editButton.addActionListener(e -> {
            service.editBudget(
                    Double.parseDouble(amountField.getText()),
                    categoryField.getText()
            );
            showToast("Budget Updated ✔", gold);
        });

        alertButton.addActionListener(e -> {
            String result = service.CheckAlert(categoryField.getText(), transactions);
            showToast(result, new Color(120, 200, 255));
        });

        setVisible(true);
    }

    // INPUT
    private JTextField createInput(Font font) {

        JTextField field = new JTextField();

        field.setFont(font);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(25, 25, 28));

        Color normalBorder = new Color(230, 230, 230);
        Color focusBorder = new Color(150, 90, 210);

        field.setBorder(BorderFactory.createLineBorder(normalBorder, 2));

        field.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!field.isFocusOwner())
                    field.setBorder(BorderFactory.createLineBorder(
                            new Color(200, 200, 200), 2));
            }

            public void mouseExited(MouseEvent e) {
                if (!field.isFocusOwner())
                    field.setBorder(BorderFactory.createLineBorder(normalBorder, 2));
            }
        });

        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(focusBorder, 2));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(normalBorder, 2));
            }
        });

        return field;
    }

    // Rounded Button (PILL SHAPE FIXED)
    class RoundedButton extends JButton {

        private Color base;
        private Color hover;

        public RoundedButton(String text, Color base, Color hover) {
            super(text);

            this.base = base;
            this.hover = hover;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);

            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBackground(base);

            setPreferredSize(new Dimension(220, 45));

            addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    setBackground(hover);
                    setForeground(Color.BLACK);
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(base);
                    setForeground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //  الخلفية
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            // border
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);


            FontMetrics fm = g2.getFontMetrics(getFont());
            String text = getText();

            g2.setColor(getForeground());

            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;

            g2.drawString(text, x, y);
        }
    }

    // Toast
    private void showToast(String message, Color accent) {

        JWindow toast = new JWindow();
        toast.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(30, 30, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(accent);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(label, BorderLayout.CENTER);
        toast.add(panel);

        toast.setSize(320, 70);
        toast.setLocationRelativeTo(this);

        new Thread(() -> {
            try {
                for (float i = 0f; i <= 1f; i += 0.1f) {
                    toast.setOpacity(i);
                    Thread.sleep(30);
                }
            } catch (Exception ignored) {}
        }).start();

        toast.setVisible(true);

        new Timer(2000, e -> {
            new Thread(() -> {
                try {
                    for (float i = 1f; i >= 0f; i -= 0.1f) {
                        toast.setOpacity(i);
                        Thread.sleep(30);
                    }
                    toast.dispose();
                } catch (Exception ignored) {}
            }).start();
        }).start();
    }

    public static void main(String[] args) {
        new BudgetScreen();
    }
}
