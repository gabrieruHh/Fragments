package com.example.logingoogleapp;

import android.app.ComponentCaller;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // Constantes
    private static final int RC_SIGN_IN = 100;

    // Views
    private SignInButton signInButton;
    private Button signOutButton;
    private ImageView profileImageView;
    private TextView nameTextView, emailTextView;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;

    // Google Sign In
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar views
        initViews();

        // Configurar Google Sign In
        configureGoogleSignIn();

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar listeners
        setupListeners();

        // Verificar se usuário já está logado
        checkCurrentUser();
    }

    private void initViews() {
        signInButton = findViewById(R.id.signInButton);
        signOutButton = findViewById(R.id.signOutButton);
        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void configureGoogleSignIn() {
        // Configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Personalizar o botão de login
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void setupListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signInWithGoogle() {
        showLoading(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        showLoading(true);

        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                        showLoading(false);
                        Toast.makeText(MainActivity.this, "Desconectado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkCurrentUser() {
        showLoading(true);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        showLoading(false);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Usuário está logado
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            profileImageView.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);

            // Exibir informações do usuário
            nameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());

            // Carregar foto do perfil (se existir)
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this)
                        .load(photoUrl)
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile_placeholder);
            }

        } else {
            // Usuário não está logado
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            profileImageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            signInButton.setEnabled(false);
            signOutButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            signInButton.setEnabled(true);
            signOutButton.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @Nullable ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In foi bem-sucedido, autenticar com Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In falhou
                showLoading(false);
                Toast.makeText(this, "Erro no login do Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);

                        if (task.isSuccessful()) {
                            // Login bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Se falhar
                            updateUI(null);
                            Toast.makeText(MainActivity.this, "Falha na autenticação: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar se usuário está autenticado quando o app inicia
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}