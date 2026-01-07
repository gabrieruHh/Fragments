package com.gabriell.firebaseauthapp.ui.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    String email;
    private EditText etEmail;
    private Button btnResetPassword;
    private TextView tvBackToLogin, tvSuccessMessage;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        // Inicializar views
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        if (intent!= null && intent.hasExtra("email")){
            email = intent.getStringExtra("email");
            etEmail.setText(email);
        }

        btnResetPassword.setOnClickListener(v ->{
            resertPassword();
        });

        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });

        etEmail.setOnEditorActionListener((v, actionId, event) ->{
            if (actionId == EditorInfo.IME_ACTION_DONE){
                resertPassword();
                return true;
            }
            return false;
        });

        tvBackToLogin.setOnClickListener(v -> goToLogin());
    }

    private void resertPassword() {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "E-mail inválido", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnResetPassword.setEnabled(false);
        tvSuccessMessage.setVisibility(View.GONE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            btnResetPassword.setEnabled(true);
            if (task.isSuccessful()){
                showSuccessful();
            } else {
                // Erro
                String errorMessage = "Erro ao enviar e-mail";
                if (task.getException() != null) {
                    errorMessage = task.getException().getMessage();

                    // Tratar erros específicos
                    if (errorMessage.contains("user-not-found")) {
                        errorMessage = "E-mail não cadastrado no sistema";
                    } else if (errorMessage.contains("invalid-email")) {
                        errorMessage = "Formato de e-mail inválido";
                    } else if (errorMessage.contains("network-request-failed")) {
                        errorMessage = "Erro de conexão. Verifique sua internet";
                    }
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSuccessful() {
        etEmail.setVisibility(View.GONE);
        findViewById(R.id.tilEmail).setVisibility(View.GONE);
        btnResetPassword.setVisibility(View.GONE);

        String successText = "Enviamos um link para redefinir sua senha para:\n" +
                email + "\n\n" +
                "Verifique sua caixa de entrada e siga as instruções.";

        tvSuccessMessage.setText(successText);
        tvSuccessMessage.setVisibility(View.VISIBLE);
        tvBackToLogin.setText("Voltar para Login");

        Toast.makeText(this, "E-mail de redefinição enviado!", Toast.LENGTH_LONG).show();
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}