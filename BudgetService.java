package com.mycompany.a2.us6and7;
import java.util.ArrayList;


public class BudgetService {
    private final BudgetRepository repository;

    public BudgetService() {

        repository = new BudgetRepository();
        repository.loadBudgets();
    }
    public void createBudget(double a, String c){
        for (Budget b : repository.getAllBudg()) {

            if (b.get_categ().equalsIgnoreCase(c)) {

                b.set_amount(a);
                repository.saveBudgets();
                return;
            }
        }
        Budget budget= new Budget(a,c);
        repository.addBudget(budget);
        System.out.println("Budget Added Successfully");
    }
    public void editBudget(double New_a, String c){
        for (Budget b : repository.getAllBudg()){
            if (b.get_categ().equals(c)){
                b.set_amount(New_a);
                System.out.println("Budget Updated Successfully");
            }
        }
        repository.saveBudgets();
    }

    public double calcExpense(String c, ArrayList<Transaction> transactions ){
    double total =0;
    for (Transaction t: transactions){
        if(t.getType().equalsIgnoreCase("expense")&& t.getCategory().equalsIgnoreCase(c)){
            total+= t.getAmount();
        }
    }
    return total;
    }
    public String CheckAlert( String c, ArrayList<Transaction> transactions){

        for(Budget b:repository.getAllBudg() ){
            if(b.get_categ().trim().equalsIgnoreCase(c)){
                double exspense = calcExpense(c,transactions);
                if (exspense>=b.get_amount()){
                    System.out.println(exspense);
                    System.out.println(b.get_amount());
                    return "Alert: Budget exceeded!";
                }
                else if (exspense >= b.get_amount()*0.8){
                    return "Warning: 80% reached";
                }
                else {

                    return "Budget is under control";
                }
            }
        }
        return "Budget not found";
    }
    public ArrayList<Budget> getAllBudgets() {
        return repository.getAllBudg();
    }

}

