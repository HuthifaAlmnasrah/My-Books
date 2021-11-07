package com.example.mybooks;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private ArrayList<Book> currentlyReading;
    private ArrayList<Book> alreadyRead;
    private ArrayList<Book> favorite;
    private ArrayList<Book> wantToRead;

    public User() {
    }

    public User(String id, String name, String email, String password, ArrayList<Book> currentlyReading, ArrayList<Book> alreadyRead, ArrayList<Book> favorite, ArrayList<Book> wantToRead) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.currentlyReading = currentlyReading;
        this.alreadyRead = alreadyRead;
        this.favorite = favorite;
        this.wantToRead = wantToRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Book> getCurrentlyReading() {
        return currentlyReading;
    }

    public void setCurrentlyReading(ArrayList<Book> currentlyReading) {
        this.currentlyReading = currentlyReading;
    }

    public ArrayList<Book> getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(ArrayList<Book> alreadyRead) {
        this.alreadyRead = alreadyRead;
    }

    public ArrayList<Book> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<Book> favorite) {
        this.favorite = favorite;
    }

    public ArrayList<Book> getWantToRead() {
        return wantToRead;
    }

    public void setWantToRead(ArrayList<Book> wantToRead) {
        this.wantToRead = wantToRead;
    }
}
