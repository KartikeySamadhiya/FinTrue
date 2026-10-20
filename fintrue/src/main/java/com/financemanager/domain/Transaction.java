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
            this(null ,desc, cat, amt, dt, type);
    }

    public Transaction(Long id, String desc, String cat, double amt, LocalDate dt, String type){
        this.id = id;
        this.description = desc;
        this.category = cat;
        this.amount = amt;
        this.date = dt;
        this.type = type;
    }

    public Long getId(){    return id ; }
    public void setId(Long id) {    this.id = id;   }

    public String getDescription() { return description; }
    public void setDescription(String description){ this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category){   this.category = category;   }

    public double getAmount() { return amount; }
    public void setAmount(double amount){   this.amount = amount;   }

    public String getType() { return type; }
    public void setType(String type) {   this.type = type;   }


    public String getFormattedDate() {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))  :  "" ;
    }

}
