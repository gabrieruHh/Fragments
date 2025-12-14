package com.gabriell.firebaseauthapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.register.RegisterActivity;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
}