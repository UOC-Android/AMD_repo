package com.andresgqjob.expenseapp.model;


public class PayerInfo {
    public final String imageUrl;
    public final String name;
    public final String email;
    public int amount;

    public PayerInfo(String image, String name, String email, int amount) {
        this.imageUrl = image;
        this.name = name;
        this.email = email;
        this.amount = amount;
    }
}
