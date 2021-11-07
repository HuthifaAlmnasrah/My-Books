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

import com.google.android.material.snackbar.Snackbar;

public class WantToReadActivity extends AppCompatActivity {

    private RecyclerView booksRecycler;
    private BookRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_read);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        booksRecycler = findViewById(R.id.booksRecyclerView);
        adapter = new BookRecyclerViewAdapter(WantToReadActivity.this,"wantToRead");
        booksRecycler.setAdapter(adapter);
        booksRecycler.setLayoutManager(new LinearLayoutManager(WantToReadActivity.this));
        adapter.setBooks(Utils.getWantToRead());

        ConstraintLayout layout = findViewById(R.id.parent);
        if(Utils.getWantToRead().size()==0){
            Snackbar.make(this, layout,getString(R.string.nobooksadded),Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.books, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(WantToReadActivity.this,MainActivity.class));
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
        startActivity(new Intent(WantToReadActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}