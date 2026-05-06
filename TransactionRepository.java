package com.mycompany.a2.us6and7;

import java.io.*;
import java.util.*;

public class TransactionRepository {

    public void save(Transaction t) {
        try (FileWriter fw = new FileWriter("transactions.txt", true)) {
            
            fw.write(t.getId() + "," + t.getType() + "," + t.getAmount() + "," + 
                     t.getCategory() + "," + t.getUserId() + "," + t.getDate() + "\n");
        } catch (Exception e) { e.printStackTrace(); }
    }
    
        // ✅ الجديد بقى
    public ArrayList<Transaction> loadTransactions() {

        ArrayList<Transaction> list = new ArrayList<>();

        try {
            File file = new File("transactions.txt");

            if (!file.exists()) return list;

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                String[] data = line.split(",");
                
                if(data.length>=6){
                                String type = data[1];
                double amount = Double.parseDouble(data[2]);
                String category = data[3];
                String userId = data[4];
                String date = data[5];

                Transaction t = new Transaction(type, amount, category, date, userId);
                java.lang.reflect.Field idField = Transaction.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, data[0]);

                list.add(t);
                
                }

            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



}



