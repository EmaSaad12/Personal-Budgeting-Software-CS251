package repository;
import model.Budget;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class BudgetRepository {
    private ArrayList<Budget> budgets = new ArrayList<>();
    public void addBudget(Budget budget){
        budgets.add(budget);
        saveBudgets();
    }
    public ArrayList<Budget> getAllBudg(){
        return budgets;
    }
    public void saveBudgets() {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("budgets.txt"));

            for (Budget b : budgets) {

                writer.write(b.get_categ() + "," + b.get_amount()+','+b.getUserId());
                writer.newLine();
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadBudgets() {
        System.out.println("LOADING...");

        try {
            File file = new File("budgets.txt");

            if (!file.exists()) return;

            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {

                String line = input.nextLine();

                String[] data = line.split(",");

                String category = data[0];
                double amount = Double.parseDouble(data[1]);
                String userId = data[2];

                Budget b = new Budget(amount, category,userId);

                budgets.add(b);
                System.out.println(line);
            }

            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}