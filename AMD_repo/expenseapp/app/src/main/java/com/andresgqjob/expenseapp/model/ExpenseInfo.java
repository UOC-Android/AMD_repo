package com.andresgqjob.expenseapp.model;

import java.util.List;


public class ExpenseInfo {
    public final String description;
    public final String date;
    public final int totalAmount;
    public final List<PayerInfo> payers;

    public ExpenseInfo(String description,
                       String date,
                       int amount,
                       List<PayerInfo> payers) {
        this.date = date;
        this.description = description;
        this.totalAmount = amount;
        this.payers = payers;
    }
}
