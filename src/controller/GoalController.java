package controller;

import model.Transaction;
import model.SavingGoal;
import repository.TransactionRepository;
import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class GoalController {
    private final ArrayList<SavingGoal> allGoals = new ArrayList<>();
    private final String GOALS_FILE = "goals.txt";

    public GoalController() {
        loadGoalsFromFile();
    }

    // إضافة هدف جديد مرتبط بـ UserID
    public void addGoal(String name, double targetAmount, String deadline, String userId) {
        int newId = generateNextId();
        allGoals.add(new SavingGoal(newId, name, deadline, targetAmount, userId));
        saveGoalsToFile();
    }

    // 🛑 التعديل الجوهري: إضافة مبلغ للهدف مع تسجيل معاملة مالية
    public void addAmountToGoalByName(String goalName, double amount, String userId) {
        for (SavingGoal goal : allGoals) {
            // التحقق من اسم الهدف وهوية المستخدم
            if (goal.getName().equalsIgnoreCase(goalName) && goal.getUserId().equals(userId)) {

                // 1. تحديث قيمة المدخرات في ملف الأهداف
                goal.setCurrentAmount(goal.getCurrentAmount() + amount);
                saveGoalsToFile();

                // 2. تسجيل معاملة مصروف (Expense) لكي يقل الرصيد في الـ Dashboard
                TransactionRepository transRepo = new TransactionRepository();
                Transaction t = new Transaction(
                        "Expense",
                        amount,
                        "Savings: " + goalName,
                        LocalDate.now().toString(),
                        userId
                );
                transRepo.save(t);
                return;
            }
        }
    }

    public List<SavingGoal> getAllGoals() { return allGoals; }

    private void saveGoalsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GOALS_FILE, false))) {
            for (SavingGoal g : allGoals) {
                // حفظ 6 حقول: ID, Name, Target, Deadline, Current, UserID
                writer.println(g.getGoalID() + "," + g.getName() + "," +
                        g.getTargetAmount() + "," + g.getDeadline() + "," +
                        g.getCurrentAmount() + "," + g.getUserId());
            }
        } catch (IOException e) { System.err.println("Error saving goals."); }
    }

    private void loadGoalsFromFile() {
        File file = new File(GOALS_FILE);
        if (!file.exists()) return;
        allGoals.clear();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length == 6) { // قراءة الـ 6 حقول
                    SavingGoal g = new SavingGoal(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[3],
                            Double.parseDouble(data[2]),
                            data[5] // UserID
                    );
                    g.setCurrentAmount(Double.parseDouble(data[4]));
                    allGoals.add(g);
                }
            }
        } catch (Exception e) { System.err.println("Error loading goals: " + e.getMessage()); }
    }

    private int generateNextId() {
        int maxId = 0;
        for (SavingGoal g : allGoals) {
            if (g.getGoalID() > maxId) maxId = g.getGoalID();
        }
        return maxId + 1;
    }
}