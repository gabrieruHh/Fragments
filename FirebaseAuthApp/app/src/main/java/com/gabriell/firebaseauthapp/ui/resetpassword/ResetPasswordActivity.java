package com.gabriell.firebaseauthapp.ui.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    // Código de ação para redefinição (do link do Firebase)
    private String actionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resert_password);

        mAuth = FirebaseAuth.getInstance();

        // Obter ação do intent (se veio de um deep link)
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            String data = intent.getData().toString();
            // Extrair código de ação da URL
            // Implementação específica para deep linking
        }

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        progressBar = findViewById(R.id.progressBar);

        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validações
        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Digite a nova senha");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Senha deve ter no mínimo 6 caracteres");
            etNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirme a nova senha");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("As senhas não coincidem");
            etConfirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnChangePassword.setEnabled(false);

        // Aqui você implementaria a lógica para alterar a senha
        // usando o código de ação (actionCode) do Firebase

        // Para simplificar, vamos apenas mostrar uma mensagem
        // Em produção, você implementaria a lógica completa com Firebase Auth

        Toast.makeText(this,
                "Funcionalidade de alteração direta requer configuração adicional.\n" +
                        "Use o link enviado por e-mail para redefinir sua senha.",
                Toast.LENGTH_LONG).show();

        progressBar.setVisibility(View.GONE);
        btnChangePassword.setEnabled(true);

        // Voltar para login após 3 segundos
        new android.os.Handler().postDelayed(() -> {
            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
            finish();
        }, 3000);
    }
}