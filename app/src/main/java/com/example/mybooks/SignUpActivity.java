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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout name, email, password, confirmPassword;
    private Button register;
    private TextView login;

    private LottieAnimationView anim;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        initViews();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handleAllRegisterInfo()){
                    mAuth = FirebaseAuth.getInstance();
                    String txt_email,txt_password;
                    txt_email = email.getEditText().getText().toString().trim();
                    txt_password = password.getEditText().getText().toString().trim();
                    String txt_name = name.getEditText().getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        User newUser = new User(user.getUid(),txt_name,txt_email,txt_password,null,null, null,null);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("Users");
                                        myRef.child(user.getUid()).setValue(newUser);

                                        startAnimation();
                                    }else {
                                        String err =  task.getException().getMessage()+"";
                                        if(err.contains("password")){
                                            password.setError(err);
                                        }
                                        if(err.contains("email")){
                                            email.setError(err);
                                        }
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
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        }, 1000);
    }

    private boolean handleAllRegisterInfo() {
        String txt_name, txt_email, txt_password,txt_convirmPassword;
        txt_name = name.getEditText().getText().toString().trim();
        txt_email = email.getEditText().getText().toString().trim();
        txt_password = password.getEditText().getText().toString().trim();
        txt_convirmPassword = confirmPassword.getEditText().getText().toString().trim();

        if(txt_name.equals("")){
            name.setError(getString(R.string.namehere));
            return false;
        }else {
            name.setError("");
        }
        if(txt_email.equals("")){
            email.setError(getString(R.string.emailhere));
            return false;
        }else{
            email.setError("");
        }
        if(txt_password.equals("")){
            password.setError(getString(R.string.passwordhere));
            return false;
        }else {
            password.setError("");
        }
        if(txt_convirmPassword.equals("")){
            confirmPassword.setError(getString(R.string.confirmpasswordhere));
            return false;
        }else{
            confirmPassword.setError("");
        }
        if(!txt_password.equals(txt_convirmPassword)){
            confirmPassword.setError(getString(R.string.confirmpasswordhere));
            return false;
        }
        return true;
    }

    private void initViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        register = findViewById(R.id.register);

        login = findViewById(R.id.btn_login);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}