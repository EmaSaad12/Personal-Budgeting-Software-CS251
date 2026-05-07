package controller;
import model.Transaction;
import java.util.*;
import java.io.*;
import java.util.List;
import model.SavingGoal;

public class GoalController {
    private final ArrayList<SavingGoal> allGoals = new ArrayList<>();
    private final String GOALS_FILE = "goals.txt";
    private final String LINKS_FILE = "goal_links.txt";

    public GoalController() {
        loadGoalsFromFile();
    }
    private Map<String, Integer> loadMappings() {
        Map<String, Integer> map = new HashMap<>();
        File file = new File(LINKS_FILE);
        if (!file.exists()) return map;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length == 2) {
                    map.put(data[0], Integer.parseInt(data[1]));
                }
            }
        } catch (Exception e) { System.err.println("Error loading mapping file."); }
        return map;
    }
    public void addGoal(String name, double targetAmount, String deadline, int goalid) {
        allGoals.add(new SavingGoal(goalid, name, deadline, targetAmount));
        saveGoalsToFile();
    }

    public List<SavingGoal> getAllGoals() { return allGoals; }
    public String generateGoalsReportOnly(List<Transaction> allTransactions) {
        StringBuilder report = new StringBuilder();
        Map<String, Integer> mappings = loadMappings();

        if (allGoals.isEmpty()) {
            report.append("No saving goals currently set.\n\n");
            return report.toString();
        }

        for (SavingGoal goal : allGoals) {
            double goalSavings = 0;
            report.append("Goal: " + goal.getName() + " (Target: $" + goal.getTargetAmount() + ")\n");

            for (Transaction t : allTransactions) {
                if (mappings.containsKey(t.getId()) && mappings.get(t.getId()) == goal.getGoalID()) {
                    if (t.getType().equalsIgnoreCase("Income")) goalSavings += t.getAmount();
                    else goalSavings -= t.getAmount();
                }
            }
            double progress = goal.getTargetAmount() > 0 ? (goalSavings / goal.getTargetAmount()) * 100 : 0;
            report.append(String.format("  > Saved: $%.2f | Progress: %.1f%%\n\n", goalSavings, progress));
        }
        return report.toString();
    }

    private void saveGoalsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GOALS_FILE, false))) {
            for (SavingGoal g : allGoals) {
                writer.println(g.getGoalID() + "," + g.getName() + "," + g.getTargetAmount() + "," + g.getDeadline() + "," + g.getCurrentAmount());
            }
        } catch (IOException e) { System.err.println("Error saving goals."); }
    }

    private void loadGoalsFromFile() {
        File file = new File(GOALS_FILE);
        if (!file.exists()) return;
        allGoals.clear();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length == 5) {
                    SavingGoal g = new SavingGoal(Integer.parseInt(data[0]), data[1], data[3], Double.parseDouble(data[2]));
                    g.setCurrentAmount(Double.parseDouble(data[4]));
                    allGoals.add(g);
                }
            }
        } catch (Exception e) { System.err.println("Error loading goals."); }
    }
}
