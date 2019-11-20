package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }
        setContentView(R.layout.activity_signup);
        Button btnSignIn = findViewById(R.id.btnSignUpLogin);
        btnSignIn.setOnClickListener(view -> signIn());
        Button btnSignUp = findViewById(R.id.btnSignUpConfirm);
        btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, UserInfoActivity.class));
            finish();
        });
    }

    private void signIn() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}