package com.gabriell.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvStatus, tvSendEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvStatus = findViewById(R.id.tvStatus);
        tvSendEmail = findViewById(R.id.tvSendEmail);

        btnLogin.setOnClickListener(v -> loginUser());

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvSendEmail.setOnClickListener(v -> resendEmail());

        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Recarregar dados do usuário para atualizar estado de verificação
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser updatedUser = mAuth.getCurrentUser();
                    if (updatedUser != null && updatedUser.isEmailVerified()) {
                        // Usuário verificado, ir para MainActivity
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                    // Se não estiver verificado, permanece na tela de login
                } else {
                    Log.e("RELOAD_ERROR", "Falha ao recarregar usuário", task.getException());
                }
            });
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos vazios", Toast.LENGTH_LONG).show();
            return;
        }
        btnLogin.setEnabled(false);
        tvStatus.setText("Verificando login...");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    if (user.isEmailVerified()){
                        tvStatus.setText("Login bem-sucedido!");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        tvStatus.setText("Email não verificado. Verifique seus emails");
                        Toast.makeText(LoginActivity.this,
                                "Verifique seu email antes de fazer login",
                                Toast.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                        tvSendEmail.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                tvStatus.setText("Erro "+ task.getException().getMessage());
            }
        });
        btnLogin.setEnabled(true);
    }

    private void resendEmail() {
        FirebaseUser userResendEmail = mAuth.getCurrentUser();
        userResendEmail.sendEmailVerification().addOnCompleteListener(resendTask ->{
            if (resendTask.isSuccessful()){
                Toast.makeText(LoginActivity.this, "Novo email de verificação enviado", Toast.LENGTH_LONG).show();
            }
        });
    }

}