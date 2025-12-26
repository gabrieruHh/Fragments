package com.gabriell.firebaseauthapp.ui.register;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.util.RegisterUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    //FirebaseAuth para cadastrar usuario com o metodo de login
    private FirebaseAuth mAuth;
    //DatabaseReference para se ligar ao banco de dados do Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressbar);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Nome é obrigatorio");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("E-mail é obrigatorio");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("E-mail inválido");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Senha é obrigatorio");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Senha deve conter no mínimo 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        RegisterUser.registerUserFirebase(this, name, email, password, new RegisterUser.RegistrationCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(GONE);
                btnRegister.setEnabled(true);
                goToLogin();
            }
            @Override
            public void onErro(String erroMenssage) {
                progressBar.setVisibility(GONE);
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, erroMenssage, Toast.LENGTH_LONG).show();
            }
        });
        }



        private void goToLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}