package ui;
import service.BudgetService;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import repository.TransactionRepository;

public class BudgetScreen extends JFrame {

    private BudgetService service;

    private JTextField categoryField;
    private JTextField amountField;

    private JLabel resultLabel;

    private ArrayList<Transaction> transactions;

    public BudgetScreen() {

        service = new BudgetService();

        TransactionRepository repo = new TransactionRepository();
        transactions = repo.loadTransactions();
        setTitle("Budget System");

        setSize(400,400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        // Labels
        JLabel categoryLabel = new JLabel("Category:");

        JLabel amountLabel = new JLabel("Amount:");

        // TextFields
        categoryField = new JTextField(15);

        amountField = new JTextField(15);

        // Buttons
        JButton createButton = new JButton("Create Budget");

        JButton editButton = new JButton("Edit Budget");

        JButton alertButton = new JButton("Check Alert");

        // Result Label
        resultLabel = new JLabel("");
        resultLabel.setPreferredSize(new Dimension(300,30));

        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add Components
        add(categoryLabel);
        add(categoryField);

        add(amountLabel);
        add(amountField);

        add(createButton);

        add(editButton);

        add(alertButton);

        add(resultLabel);

        // Create Button Action
        createButton.addActionListener(e -> {

            String category = categoryField.getText();

            double amount =
                    Double.parseDouble(amountField.getText());

            service.createBudget( amount ,category);

            resultLabel.setText("Budget Created");
        });

        // Edit Button Action
        editButton.addActionListener(e -> {

            String category = categoryField.getText();

            double amount =
                    Double.parseDouble(amountField.getText());

            service.editBudget(amount ,category);

            resultLabel.setText("Budget Updated");
        });

        // Alert Button Action
        alertButton.addActionListener(e -> {

            String category = categoryField.getText();

            String result =
                    service.CheckAlert(category, transactions);

            resultLabel.setText(result);
        });

        setVisible(true);
    }
    public static void main(String[] args) {
        new BudgetScreen();
    }
}