package com.gabriell.firebaseauthapp.util;

import android.content.Context;
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

public class LogoutGoogle{

    private Context context;

    public LogoutGoogle(Context context){
        this.context = context;
    }

    protected GoogleSignInClient getgoogleSignInAccount(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        )
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(context, gso);
    }

    public void logout(){
        //Desloga do Firebase
        FirebaseAuth.getInstance().signOut();

            getgoogleSignInAccount().signOut().addOnCompleteListener(task -> {

                Toast.makeText(context, "Você deslogou da sua conta", Toast.LENGTH_LONG).show();
                redirectedToLogin();
            });

    }

    private void redirectedToLogin() {
        Intent intent = new Intent(this.context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.context.startActivity(intent);
    }
}
