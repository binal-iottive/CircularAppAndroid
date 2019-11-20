package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ForgotActivity extends AppCompatActivity {
    Button btnConfirm, btnResend;
    TextView lblConfirm, lblEmail, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }
        setContentView(R.layout.activity_forgot);
        Button btnSignIn = findViewById(R.id.btnSignUpLogin);
        btnSignIn.setOnClickListener(view -> signIn());
        btnResend = findViewById(R.id.btnForgotResend);
        lblConfirm = findViewById(R.id.lblForgotConfirm);
        btnConfirm = findViewById(R.id.btnForgotConfirm);
        lblEmail = findViewById(R.id.lblForgotEmail);
        txtEmail = findViewById(R.id.txtForgotEmail);
        btnConfirm.setOnClickListener(view -> confirm());
    }

    private void confirm() {
        lblEmail.setVisibility(View.GONE);
        txtEmail.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
        lblConfirm.setVisibility(View.VISIBLE);
        btnResend.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
