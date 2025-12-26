package com.gabriell.firebaseauthapp.util;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutGoogle extends AppCompatActivity {

    protected GoogleSignInClient getgoogleSignInAccount(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(this, gso);
    }

    public void logout(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseAuth.getInstance().signOut();

        if (account!= null){
            getgoogleSignInAccount().signOut().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(this, "Você deslogou da sua conta", Toast.LENGTH_LONG).show();
                    redirectedToLogin();
                }
            });
        }

    }

    private void redirectedToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
