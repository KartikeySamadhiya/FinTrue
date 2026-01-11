package com.financemanager.domain;

public class User {
    private long id ;
    private String email ;
    private String password ;

    public User(long id, String email, String pass){
        this.id = id;
        this.email = email;
        this.password = pass;
    }

    public long getId() { return id; }
    public String getEmail() { return email; }

}