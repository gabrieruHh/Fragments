package com.gabriell.firebaseauthapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.dashboard.DashboardActivity;
import com.gabriell.firebaseauthapp.ui.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);

        checkCurrentUser();

        btnRegister.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "E-mail é obrigatorio", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "E-mail invalido", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Senha é obrigatorio", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (password.length() < 6){
            Toast.makeText(this, "Senha deve conter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        //Login com email e senha
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                        //Atualiza verificação de email
                        user.reload().addOnCompleteListener(taskEmail ->{
                            //Verifica email atualizado
                            if (taskEmail.isSuccessful()){
                                FirebaseUser userUpdate = mAuth.getCurrentUser();
                                if (userUpdate.isEmailVerified()){
                                    goToDashboard();
                                }else {
                                    Toast.makeText(this, "E-mail não verificado", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnRegister.setEnabled(true);
                                }

                            }else {
                                Toast.makeText(this, "Falha ao verifcar email " + user.getEmail(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                btnRegister.setEnabled(true);
                            }
                        });
                    }else{
                        Toast.makeText(this, "Falha ao logar com ", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                    }
                });
    }

    private void registerUser() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser updateUser = mAuth.getCurrentUser();
                    if (updateUser != null && updateUser.isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
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

    private void goToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}