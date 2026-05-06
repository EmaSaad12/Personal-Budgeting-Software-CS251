package model;
import java.util.UUID;

public class Transaction {



        private String id;
        private String userId;
        private String type;
        private double amount;
        private String category;
        private String date;

        public Transaction(String type, double amount, String category, String date, String userId) {
            this.id = java.util.UUID.randomUUID().toString();

            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
            this.userId = userId; // للربط
        }

        public String getId() { return id; }
        public String getUserId() { return userId; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getDate() { return date; }


}
