package com.financemanager.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private Long id ;
    private String description ;
    private String category ;
    private double amount ;
    private LocalDate date ;
    private String type ;   // income/expense

    public Transaction(String desc, String cat, double amt, LocalDate dt, String type){
            this.description = desc;
            this.category = cat;
            this.amount = amt;
            this.date = dt;
            this.type = type;
    }


    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getType() { return type; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("MM dd"));
    }
}
