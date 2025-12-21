package com.gabriell.firebaseauthapp.ui.register;

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
import com.gabriell.firebaseauthapp.util.CallDatabase;
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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        String menssage ="";

        CallDatabase.RegisterAccountApp result = CallDatabase.validadetionRegistration(name, email, password, mAuth, mDatabase, menssage);

        if (!result.isValid()){
            switch (result.getField()){
                case "name":
                    etName.setError(result.getErroMenssage());
                    etName.requestFocus();
                    break;
                case "email":
                    etEmail.setError(result.getErroMenssage());
                    etEmail.requestFocus();
                    break;
                case "password":
                    etPassword.setError(result.getErroMenssage());
                    etPassword.requestFocus();
                    break;
                case "passwordLength":
                    etPassword.setError(result.getErroMenssage());
                    etPassword.requestFocus();
                    break;
            }
        }else{
                Toast.makeText(this, result.getErroMenssage(), Toast.LENGTH_LONG).show();
        }

        btnRegister.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    //Verifica se o usuario foi criardo com sucesso
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Atualizar perfil do usuário com o nome'
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        user.updateProfile(profileUpdate).addOnCompleteListener(profileTask ->{
                           if (profileTask.isSuccessful()){
                               //Salvar dados adicionais no Realtime Database
                               saveUserDataToDatabase(user.getUid(), name, email);

                               user.sendEmailVerification().addOnCompleteListener(emailTask ->{
                                  if (emailTask.isSuccessful()){
                                      Toast.makeText(this, "Cadastro realizado! Verifique seu email "+ user.getEmail(), Toast.LENGTH_LONG).show();

                                      startActivity(new Intent(this, LoginActivity.class));
                                  }
                               });
                           }
                        });
                    }else {
                        Toast.makeText(this, "Falha ao cadastrar ", Toast.LENGTH_LONG).show();
                        btnRegister.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        btnRegister.setEnabled(true);

    }

    private void saveUserDataToDatabase(String uid, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("createdAt", System.currentTimeMillis());

        mDatabase.child("users").child(uid).setValue(userData);
    }
}