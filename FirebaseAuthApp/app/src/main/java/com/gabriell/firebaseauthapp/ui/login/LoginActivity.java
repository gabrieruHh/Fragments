package com.gabriell.firebaseauthapp.ui.login;

import android.app.ComponentCaller;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gabriell.firebaseauthapp.databinding.ActivityLoginBinding;
import com.gabriell.firebaseauthapp.ui.forgotpassword.ForgotPasswordActivity;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.screensdashboard.dashboard.DashboardActivity;
import com.gabriell.firebaseauthapp.ui.registeruser.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private SignInButton btnSignGoogle;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;

    private final static int RC_SIGN_IN = 100;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        btnRegister = binding.btnLogin;
        btnSignGoogle = binding.btnSignWithGoogle;
        tvRegister = binding.tvRegister;
        progressBar = binding.progressBar;
        tvForgotPassword = binding.tvForgotPassword;

        configureGoogleSignIn();
        btnSignGoogle.setOnClickListener(v -> signWithGoogle());
        checkCurrentUser();

        btnRegister.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> registerUser());
        tvForgotPassword.setOnClickListener(v -> goToForgotPassword());

        View rootNav = binding.scrollCenter;
        ViewCompat.setOnApplyWindowInsetsListener(rootNav, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    0,
                    systemBars.top,                 //fica abaixo do statusBar
                    0,
                    systemBars.bottom               //fica acima do navigationBar
            );
            return insets;
        });

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

        showLoad(false);

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
                                }

                            }else {
                                Toast.makeText(this, "Falha ao verifcar email " + user.getEmail(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Toast.makeText(this, "Falha ao logar com ", Toast.LENGTH_LONG).show();
                    }
                });
        showLoad(true);
    }

    //CONFIGURANDO O GOOGLESIGN
    private void configureGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signWithGoogle(){
        showLoad(false);

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Toast.makeText(this, "Erro no login do Google", Toast.LENGTH_LONG).show();
                showLoad(true);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            showLoad(true);

            if (task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(this, "Seja bem vindo "+ user.getDisplayName()+"!", Toast.LENGTH_LONG).show();
                goToDashboard();
            }else {
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_LONG);
            }
        });
    }

    private void registerUser() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void checkCurrentUser() {
        showLoad(false);

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
        showLoad(true);
    }

    private void goToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        String email = etEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email)){
            intent.putExtra("email", email);
        }else {
            Toast.makeText(this, "Campo email obrigatio!", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        startActivity(intent);
    }

    private void showLoad(Boolean isShow){
        if (isShow){
            progressBar.setVisibility(View.INVISIBLE);
            btnRegister.setEnabled(true);
            btnSignGoogle.setEnabled(true);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
            btnSignGoogle.setEnabled(false);
        }
    }
}