package com.example.myplanningpokeruser.Model;

public class User {
    private String id;//key
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
