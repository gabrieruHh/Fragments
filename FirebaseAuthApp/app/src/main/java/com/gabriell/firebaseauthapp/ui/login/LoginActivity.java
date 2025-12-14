package com.gabriell.firebaseauthapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.dashboard.DashboardActivity;
import com.gabriell.firebaseauthapp.ui.main.MainActivity;
import com.google.firebase.Firebase;

// Gerência login/logout
import com.google.firebase.auth.FirebaseAuth;

// Representa o usuário
import com.google.firebase.auth.FirebaseUser;

// 'extends AppCompatActivity -> Essa class é uma tela/Tem ciclo de vida (oncreate/onStart)'
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth; // Objeto princimpal do FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Inicializa a Activity
        setContentView(R.layout.activity_login); // Carrega layout XML

        mAuth = FirebaseAuth.getInstance(); // Inicializa o FirebaseAuth

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);


        // Verificar se o usuário está logado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            goToDashboard();
        }

        btnLogin.setOnClickListener(v -> LoginUser());
    }

    private void LoginUser() {
        String emal = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emal)){
            etEmail.setError("E-mail é obrigatória");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emal).matches()){
            etEmail.setError("E-mail inválido ");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            etPassword.setError("Senha é obrigatória");
            etPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            etPassword.setError("senha deve ter no mínimo 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Firebase verifica Email/senha no servidor
        mAuth.signInWithEmailAndPassword(emal, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                goToDashboard();
            } else {
                Toast.makeText(this, "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}