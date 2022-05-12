package com.andresgqjob.expenseapp.model;

import java.util.ArrayList;

public class ExpenseInfo {
    public String description;
    public String date;
    public int totalAmount;
    public ArrayList<PayerInfo> payers;

    public ExpenseInfo(String description,
                       String date,
                       int amount,
                       ArrayList<PayerInfo> payers) {
        this.date = date;
        this.description = description;
        this.totalAmount = amount;
        this.payers = payers;
    }
}
