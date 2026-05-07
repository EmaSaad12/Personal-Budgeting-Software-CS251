package com.mycompany.a2.us6and7;

import java.util.ArrayList;

public class BudgetService {
    private final BudgetRepository repository;

    public BudgetService() {
        repository = new BudgetRepository();
        repository.loadBudgets();
    }

 
    public void addBudget(String c, double a, String userId) {
        for (Budget b : repository.getAllBudg()) {
            
            if (b.get_categ().equalsIgnoreCase(c) && b.getUserId().equals(userId)) {
                b.set_amount(a);
                repository.saveBudgets();
                return;
            }
        }
        
        Budget budget = new Budget(a, c, userId); 
        repository.addBudget(budget);
        System.out.println("Budget Added Successfully");
    }

  
    public void editBudget(double New_a, String c, String userId) {
        for (Budget b : repository.getAllBudg()){
            if (b.get_categ().equals(c) && b.getUserId().equals(userId)){
                b.set_amount(New_a);
                System.out.println("Budget Updated Successfully");
            }
        }
        repository.saveBudgets();
    }

  
    public double calcExpense(String c, ArrayList<Transaction> transactions, String userId){
        double total = 0;
        for (Transaction t: transactions){
            if(t.getType().equalsIgnoreCase("expense") && 
               t.getCategory().equalsIgnoreCase(c) && 
               t.getUserId().equals(userId)) { 
                total += t.getAmount();
            }
        }
        return total;
    }

  
    public String CheckAlert(String c, ArrayList<Transaction> transactions, String userId) {
        for(Budget b : repository.getAllBudg() ){
            if(b.get_categ().trim().equalsIgnoreCase(c) && b.getUserId().equals(userId)){
                double exspense = calcExpense(c, transactions, userId);
                if (exspense >= b.get_amount()){
                    return "Alert: Budget exceeded!";
                }
                else if (exspense >= b.get_amount() * 0.8){
                    return "Warning: 80% reached";
                }
                else {
                    return "Budget is under control";
                }
            }
        }
        return "Budget not found";
    }
    
    //added by sama===============================================
    public ArrayList<Budget> getAllBudgets() {
        return repository.getAllBudg();
    }
}