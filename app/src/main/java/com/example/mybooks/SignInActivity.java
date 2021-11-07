package com.example.mybooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private Button login;
    private TextView register,forgetPassword;

    private LottieAnimationView anim;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        initViews();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handleAllLoginInfo()){
                    String txt_email = email.getEditText().getText().toString().trim();
                    String txt_password = password.getEditText().getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startAnimation();
                                    } else {
                                        String err = task.getException().getMessage();
                                        if(err.contains("email")||err.contains("record")){
                                            email.setError(err);
                                        }
                                        if(err.contains("password")){
                                            password.setError(err);
                                        }
                                    }
                                }
                            });
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email =email.getEditText().getText().toString().trim();
                if(txt_email.equals("")){
                    email.setError(getString(R.string.emailhere));
                    Toast.makeText(SignInActivity.this, R.string.email, Toast.LENGTH_SHORT).show();
                }else{
                    email.setError("");
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = txt_email;

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignInActivity.this, R.string.resetpassword, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void startAnimation() {
        anim = findViewById(R.id.anim);
        anim.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }

    private boolean handleAllLoginInfo() {
        String txt_email, txt_password;
        txt_email = email.getEditText().getText().toString().trim();
        txt_password = password.getEditText().getText().toString().trim();
        if(txt_email.equals("")){
            email.setError(getString(R.string.emailhere));
            return false;
        }else {
            email.setError("");
        }
        if(txt_password.equals("")){
            password.setError(getString(R.string.passwordhere));
            return false;
        }else{
            password.setError("");
        }
        return true;
    }

    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.btnlogin);

        register = findViewById(R.id.btn_sign_up);
        forgetPassword = findViewById(R.id.forget_password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}