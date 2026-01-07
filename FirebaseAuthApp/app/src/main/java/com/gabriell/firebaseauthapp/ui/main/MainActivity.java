package com.gabriell.firebaseauthapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.ui.screensdashboard.dashboard.DashboardActivity;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.registeruser.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToLogin, btnGoToRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnGoToRegister.setOnClickListener(v -> registerUser());
        btnGoToLogin.setOnClickListener(v -> loginUser());

        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser updateUser = mAuth.getCurrentUser();
                    if (updateUser != null && updateUser.isEmailVerified()){
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Log.e("RELOAD_ERROR", "Falha ao carregar usuario", task.getException());
                }
            });
        }
    }

    private void registerUser() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    private void loginUser() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}