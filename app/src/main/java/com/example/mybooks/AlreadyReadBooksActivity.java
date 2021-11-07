package com.example.mybooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class AlreadyReadBooksActivity extends AppCompatActivity {

    private RecyclerView booksRecycler;
    private BookRecyclerViewAdapter adapter;
    ConstraintLayout layout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_read_books);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        booksRecycler = findViewById(R.id.booksRecyclerView);
        adapter = new BookRecyclerViewAdapter(AlreadyReadBooksActivity.this,"alreadyRead");
        booksRecycler.setAdapter(adapter);
        booksRecycler.setLayoutManager(new LinearLayoutManager(AlreadyReadBooksActivity.this));
        adapter.setBooks(Utils.getAlreadyRead());

        layout = findViewById(R.id.parent);
        if(Utils.getAlreadyRead().size()==0){
            Snackbar.make(AlreadyReadBooksActivity.this, layout,getString(R.string.nobooksadded), LENGTH_INDEFINITE)
                    .setAction(R.string.books, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(AlreadyReadBooksActivity.this,MainActivity.class));
                            finish();
                        }
                    })
                    .setActionTextColor(Color.GREEN)
                    .show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AlreadyReadBooksActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}