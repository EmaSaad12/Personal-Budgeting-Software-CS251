package com.mycompany.a2.us6and7;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BudgetRepository {
    private ArrayList<Budget> budgets;
    private final String FILE_NAME = "budgets.txt"; 

    public BudgetRepository() {
        budgets = new ArrayList<>();
    }

    public void addBudget(Budget b) {
        budgets.add(b);
        saveBudgets();
    }

    public ArrayList<Budget> getAllBudg() {
        return budgets;
    }

    public void saveBudgets() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Budget b : budgets) {
               
                writer.write(b.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving budgets: " + e.getMessage());
        }
    }

    public void loadBudgets() {
        budgets.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                
             
                if (data.length >= 3) {
                    double amount = Double.parseDouble(data[0]);
                    String category = data[1];
                    String userId = data[2]; 
                    
                    
                    budgets.add(new Budget(amount, category, userId)); 
                } 
           
                else if (data.length == 2) {
                    double amount = Double.parseDouble(data[0]);
                    String category = data[1];
                    budgets.add(new Budget(amount, category, "unknown")); 
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading budgets: " + e.getMessage());
        }
    }
}