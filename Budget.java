package com.mycompany.a2.us6and7;


public class Budget {
    private double amount;
    private String category;
    private String userId;

    public Budget(double a, String c, String userId) { 
        this.amount = a;
        this.category = c;
        this.userId = userId;
    }

    public String get_categ() {
        return category;
    }

    public double get_amount() {
        return amount;
    }

    public void set_amount(double a) {
        this.amount = a;
    }

    public void set_categ(String c) {
        this.category = c;
    }

 
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

   
    @Override
    public String toString() {
        return amount + "," + category + "," + userId;
    }
}