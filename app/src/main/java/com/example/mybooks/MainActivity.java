package com.example.mybooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView booksRecycler;
    private BookRecyclerViewAdapter adapter;

    private LottieAnimationView loadingAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLoadingAnimation();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        booksRecycler = findViewById(R.id.booksRecyclerView);
        adapter = new BookRecyclerViewAdapter(MainActivity.this,"allBooks");
        booksRecycler.setAdapter(adapter);
        booksRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.setBooks(Utils.getInstance().getAllBooks());
    }

    public void startLoadingAnimation(){
        loadingAnim = findViewById(R.id.loading_animation);
        loadingAnim.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                booksRecycler.setVisibility(View.VISIBLE);
                loadingAnim.setVisibility(View.GONE);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_alreadyRead:
                startActivity(new Intent(MainActivity.this, AlreadyReadBooksActivity.class));
                break;
            case R.id.nav_currentlyRead:
                startActivity(new Intent(MainActivity.this, CurrentlyReadingActivity.class));
                break;
            case R.id.nav_favorite:
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                break;
            case R.id.nav_wantToRead:
                startActivity(new Intent(MainActivity.this, WantToReadActivity.class));
                break;
            case R.id.nav_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage(R.string.aboutdeveloper);
                builder.setNegativeButton(R.string.dissmiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
                break;
            case R.id.nav_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}