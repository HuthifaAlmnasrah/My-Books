package com.example.mybooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Utils {
    private static ArrayList<Book> allBooks;
    private static ArrayList<Book> wantToRead;
    private static ArrayList<Book> favoriteBooks;
    private static ArrayList<Book> alreadyRead;
    private static ArrayList<Book> currentlyRead;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;

    private static Utils instance;

    private Utils() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if(allBooks == null){
            initData();
        }
        if (alreadyRead == null){
            initAlreadyData();
        }
        if(wantToRead == null){
            intiWantToReadData();
        }
        if(favoriteBooks == null){
            initFavoriteData();
        }
        if(currentlyRead == null){
            initCurrentlyData();
        }
    }


    private void initData() {
        DatabaseReference booksRef = database.getReference("Books");

        ArrayList<Book> finalAllBooks = new ArrayList<>();
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    finalAllBooks.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Book book = snapshot.getValue(Book.class);
                        finalAllBooks.add(book);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        allBooks = finalAllBooks;
    }

    private void initCurrentlyData() {

        DatabaseReference usersRef = database.getReference("Users").child(currentUser.getUid()).child("currentlyRead");

        ArrayList<Book> books = new ArrayList<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Book book = snapshot.getValue(Book.class);
                    books.add(book);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        currentlyRead = books;
    }

    private void initFavoriteData() {
        DatabaseReference usersRef = database.getReference("Users").child(currentUser.getUid()).child("favorite");

        ArrayList<Book> books = new ArrayList<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Book book = snapshot.getValue(Book.class);
                    books.add(book);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        favoriteBooks = books;
    }

    private void intiWantToReadData() {
        DatabaseReference usersRef = database.getReference("Users").child(currentUser.getUid()).child("wantToRead");

        ArrayList<Book> books = new ArrayList<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Book book = snapshot.getValue(Book.class);
                    books.add(book);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        wantToRead = books;
    }

    private void initAlreadyData() {
        DatabaseReference usersRef = database.getReference("Users").child(currentUser.getUid()).child("alreadyRead");

        ArrayList<Book> books = new ArrayList<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Book book = snapshot.getValue(Book.class);
                    books.add(book);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        alreadyRead = books;
    }

    public static Utils getInstance() {
        if(null != instance){
            return instance;
        }else{
            instance = new Utils();
            return instance;
        }
    }

    public static ArrayList<Book> getAllBooks() {
        return allBooks;
    }

    public static ArrayList<Book> getWantToRead() {
        return wantToRead;
    }

    public static ArrayList<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public static ArrayList<Book> getAlreadyRead() {
        return alreadyRead;
    }

    public static ArrayList<Book> getCurrentlyRead() {
        return currentlyRead;
    }

    public static Book getBookById(String bookId) {
        ArrayList<Book> books = getAllBooks();
        if(books != null){
            for (Book b : books){
                if(b.getId().equals(bookId)){
                    return b;
                }
            }
        }
        return null;
    }

    public boolean addToAlreadyRead(Book book){
        return alreadyRead.add(book);
    }
    public boolean addToWantToRead(Book book){
        return wantToRead.add(book);
    }
    public boolean addToCurrentlyReading(Book book){
        return currentlyRead.add(book);
    }
    public boolean addToFavorite(Book book){
        return favoriteBooks.add(book);
    }

    public boolean removeFromAlreadyRead(Book book){
        return alreadyRead.remove(book);
    }
    public boolean removeFromCurrentlyReading(Book book){
        return currentlyRead.remove(book);
    }
    public boolean removeFromFavorite(Book book){
        return favoriteBooks.remove(book);
    }
    public boolean removeFromWantToRead(Book book){
        return wantToRead.remove(book);
    }
}
