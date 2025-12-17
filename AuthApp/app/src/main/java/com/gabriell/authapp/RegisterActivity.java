package com.gabriell.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister, btnLogin;
    private TextView tvStatus;
    private FirebaseAuth mAuth;

    private FirebaseUser userMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLoginn);
        tvStatus = findViewById(R.id.tvStatus);

        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        tvStatus.setText("Criando conta...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null){
                            sendEmailVerification(user);
                        }
                    }else {
                        tvStatus.setText("Falha ao cadastrar");
                        btnRegister.setEnabled(true);
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user){
        tvStatus.setText("Enviando email de verificação...");

        user.sendEmailVerification()
                .addOnCompleteListener(emailTask -> {
                   if (emailTask.isSuccessful()){
                       tvStatus.setText("Email de verificação enviado para " + user.getEmail());
                       Toast.makeText(RegisterActivity.this,
                               "Verifique seu email para confirmar o cadastro", Toast.LENGTH_LONG).show();

//                       mAuth.signOut();
                       Toast.makeText(RegisterActivity.this,
                               user.getEmail(), Toast.LENGTH_LONG).show();
                       btnRegister.setEnabled(true);
                   } else {
                       tvStatus.setText("falha ao enviar o email de verificarção para " + user.getEmail() + emailTask.getException().getMessage());
                       btnRegister.setEnabled(true);
                   }
                });
    }
}