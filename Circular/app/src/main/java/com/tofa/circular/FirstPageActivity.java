package com.tofa.circular;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class FirstPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }

        setContentView(R.layout.activity_firstpage);
        Button btnSignUp = findViewById(R.id.btnSignInRegister);
        btnSignUp.setOnClickListener(view -> signUp());
        Button btnSignIn = findViewById(R.id.btnSignUpLogin);
        btnSignIn.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void signUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}
