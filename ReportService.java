package com.mycompany.a2.us6and7;

import java.util.List;
import java.util.ArrayList;

public class ReportService {

    public String generateIntegratedReport() {
        StringBuilder report = new StringBuilder();
        
        TransactionRepository transRepo = new TransactionRepository(); 
        List<Transaction> allTransactions = transRepo.loadTransactions(); 
        GoalController goalController = new GoalController();
        BudgetService budgetService = new BudgetService();

        report.append("==========================================\n");
        report.append("     OVERALL TRANSACTION HISTORY \n");
        report.append("==========================================\n");
        
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : allTransactions) {
            report.append(String.format("[%s] %-10s: $%-8.2f | Cat: %s\n", 
                          t.getDate(), t.getType(), t.getAmount(), t.getCategory()));
            if (t.getType().equalsIgnoreCase("Income")) totalIncome += t.getAmount();
            else totalExpense += t.getAmount();
        }
        
        report.append("------------------------------------------\n");
        report.append("Total Income: $" + totalIncome + " | Total Expenses: $" + totalExpense + "\n");
        report.append("Net Balance:  $" + (totalIncome - totalExpense) + "\n\n");

        report.append("==========================================\n");
        report.append("        BUDGETS & SPENDING STATUS\n");
        report.append("==========================================\n");

        try {
            // ملاحظة: الجزء ده هيشتغل لما صاحبتك تكمل BudgetRepository
            for (Budget b : budgetService.getAllBudgets()) { 
                String category = b.get_categ();
                double budgetLimit = b.get_amount();
                double spentAmount = budgetService.calcExpense(category, (ArrayList<Transaction>) allTransactions);
                String alertStatus = budgetService.CheckAlert(category, (ArrayList<Transaction>) allTransactions);
                
                double consumedPercentage = budgetLimit > 0 ? (spentAmount / budgetLimit) * 100 : 0;
                
                report.append(String.format("Category: %-10s | Limit: $%.2f\n", category, budgetLimit));
                report.append(String.format("  > Spent: $%.2f (%.1f%% consumed)\n", spentAmount, consumedPercentage));
                report.append(String.format("  > Status: %s\n\n", alertStatus));
            }
        } catch (Exception ex) {
            report.append("No budgets found or BudgetRepository not fully implemented yet.\n\n");
        }

        report.append("==========================================\n");
        report.append("        SAVING GOALS PROGRESS\n");
        report.append("==========================================\n");

        report.append(goalController.generateGoalsReportOnly((ArrayList<Transaction>) allTransactions));
        
        return report.toString();
    }
}