package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }
        setContentView(R.layout.activity_signin);
        Button btnSignUp = findViewById(R.id.btnSignInRegister);
        btnSignUp.setOnClickListener(view -> signUp());
        TextView btnForgot = findViewById(R.id.btnSignInForgot);
        btnForgot.setOnClickListener(view -> forgot());
        Button btnSignIn = findViewById(R.id.btnSignInConfirm);
        btnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, UserInfoActivity.class));
            finish();
        });
    }

    private void signUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private void forgot() {
        startActivity(new Intent(this, ForgotActivity.class));
        finish();
    }
}
