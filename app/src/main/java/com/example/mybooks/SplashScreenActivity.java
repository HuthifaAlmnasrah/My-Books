package com.example.mybooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        setText(getString(R.string.app_name));

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void setText(String s) {
        TextView appName;
        appName = findViewById(R.id.txtAppName);

        final int[] i = new int[1];
        final int length = s.length();

        final Handler handler = new Handler(message -> {
            char c = s.charAt(i[0]);
            appName.append(String.valueOf(c));
            i[0]++;
            return true;
        });

        final Timer timer = new Timer();
        final TimerTask taskEverySplitSecond = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if (i[0] == length - 1) {
                    timer.cancel();
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };
        timer.schedule(taskEverySplitSecond, 1, 400);
    }
}