package com.example.mybooks;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowBookActivity extends AppCompatActivity {

    public static final String BOOK_ID_KEY = "bookId";

    private ImageView bookImage;
    private Button btnAddToWantToRead, btnAddToAlreadyRead, btnAddToFavorite, btnAddToCurrentlyReading;
    private TextView bookName, bookAuthuor, bookPages, bookDescription;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);


        initViews();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Intent intent = getIntent();
        if(intent != null){
            String bookId = intent.getStringExtra(BOOK_ID_KEY);
            if(bookId != null){
                Book incomingBook = Utils.getInstance().getBookById(bookId);
                getSupportActionBar().setTitle(incomingBook.getName());
                if(incomingBook != null){
                    setData(incomingBook);
                    handleAlreadyRead(incomingBook);
                    handleWantToRead(incomingBook);
                    handleCurrentlyReading(incomingBook);
                    handleFavoriteBooks(incomingBook);
                }
            }
        }

    }

    private void setData(Book book) {
        bookName.setText(book.getName());
        bookAuthuor.setText(book.getAuthor());
        bookPages.setText(book.getPages());
        bookDescription.setText(book.getLongDesc());
        Glide.with(this)
                .asBitmap()
                .load(book.getImageUrl())
                .into(bookImage);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initViews() {
        bookImage = findViewById(R.id.bookImage);
        btnAddToCurrentlyReading = findViewById(R.id.btnCurrently);
        btnAddToWantToRead = findViewById(R.id.btnWant);
        btnAddToAlreadyRead = findViewById(R.id.btnAlready);
        btnAddToFavorite = findViewById(R.id.btnFavorite);

        bookName = findViewById(R.id.txtName);
        bookAuthuor = findViewById(R.id.txtAuthorName);
        bookPages = findViewById(R.id.txtpages);
        bookDescription = findViewById(R.id.txtDescrioption);
        bookDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
    }
    private void handleFavoriteBooks(Book book) {
        ArrayList<Book> favorite = Utils.getFavoriteBooks();
        boolean existInFavorite = false;
        for(Book b : favorite){
            if(b.getId().equals(book.getId())){
                existInFavorite = true;
            }
        }
        if(existInFavorite){
            btnAddToFavorite.setEnabled(false);
        }else{
            btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.getInstance().addToFavorite(book)){
                        Toast.makeText(ShowBookActivity.this, R.string.bookadded, Toast.LENGTH_SHORT).show();

                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        myRef.child(currentUser.getUid()).child("favorite").child(book.getId()).setValue(book);

                        startActivity(new Intent(ShowBookActivity.this, FavoriteActivity.class));
                    }else{
                        Toast.makeText(ShowBookActivity.this, R.string.somethingworng, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleCurrentlyReading(Book book) {
        ArrayList<Book> currentlyReading = Utils.getCurrentlyRead();
        boolean existInCurrentlyReading = false;
        for(Book b : currentlyReading){
            if(b.getId().equals(book.getId())){
                existInCurrentlyReading = true;
            }
        }
        if(existInCurrentlyReading){
            btnAddToCurrentlyReading.setEnabled(false);
        }else{
            btnAddToCurrentlyReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.getInstance().addToCurrentlyReading(book)){
                        Toast.makeText(ShowBookActivity.this,R.string.bookadded, Toast.LENGTH_SHORT).show();

                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        myRef.child(currentUser.getUid()).child("currentlyRead").child(book.getId()).setValue(book);

                        startActivity(new Intent(ShowBookActivity.this, CurrentlyReadingActivity.class));
                    }else{
                        Toast.makeText(ShowBookActivity.this, R.string.somethingworng, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleWantToRead(Book book) {
        ArrayList<Book> wantToRead = Utils.getWantToRead();
        boolean existInWantToRead = false;
        for(Book b : wantToRead){
            if(b.getId().equals(book.getId())){
                existInWantToRead = true;
            }
        }
        if(existInWantToRead){
            btnAddToWantToRead.setEnabled(false);
        }else{
            btnAddToWantToRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.getInstance().addToWantToRead(book)){
                        Toast.makeText(ShowBookActivity.this, R.string.bookadded, Toast.LENGTH_SHORT).show();

                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        myRef.child(currentUser.getUid()).child("wantToRead").child(book.getId()).setValue(book);

                        startActivity(new Intent(ShowBookActivity.this, WantToReadActivity.class));
                    }else{
                        Toast.makeText(ShowBookActivity.this, R.string.somethingworng, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleAlreadyRead(Book book) {
        ArrayList<Book> alreadyReadBooks = Utils.getInstance().getAlreadyRead();
        boolean existInAlreadyBooks = false;
        for (Book b : alreadyReadBooks){
            if(b.getId().equals(book.getId())){
                existInAlreadyBooks = true;
            }
        }
        if(existInAlreadyBooks){
            btnAddToAlreadyRead.setEnabled(false);
        }else{
            btnAddToAlreadyRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.getInstance().addToAlreadyRead(book)){
                        Toast.makeText(ShowBookActivity.this, R.string.bookadded, Toast.LENGTH_SHORT).show();

                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        myRef.child(currentUser.getUid()).child("alreadyRead").child(book.getId()).setValue(book);


                        startActivity(new Intent(ShowBookActivity.this, AlreadyReadBooksActivity.class));
                    }else{
                        Toast.makeText(ShowBookActivity.this, R.string.somethingworng, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}