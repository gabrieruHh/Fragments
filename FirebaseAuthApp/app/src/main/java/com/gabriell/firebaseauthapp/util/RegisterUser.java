package com.gabriell.firebaseauthapp.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser {

    public interface RegistrationCallback{
        void onSuccess();
        void onErro(String erroMenssage);
    }

    public static void registerUserFirebase( Context context, String name, String email, String password, RegistrationCallback callback){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();

                user.updateProfile(profileUpdate).addOnCompleteListener(profiletask ->{
                   if (profiletask.isSuccessful()){
                       saveUserToDatabase(user.getUid(), name, email, mDatabase);

                        user.sendEmailVerification().addOnCompleteListener(emailtask ->{
                           if (emailtask.isSuccessful()){
                               Toast.makeText(context, "Casdastro realizado com sucesso! verifique seu email "+ user.getEmail(), Toast.LENGTH_LONG).show();
                               callback.onSuccess();
                           }else {
                               callback.onErro("Erro ao enviar email de verificação");
                           }
                        });
                   }else {
                       callback.onErro("Erro ao atualizar perfil");
                   }
                });

            }else {
                String erro = task.getException() != null ?
                        task.getException().getMessage() : "Erro desconhecido";
                            callback.onErro("Falha ao criar usuario "+erro);
            }
        });
    }

    private static void saveUserToDatabase(String uid, String name, String email, DatabaseReference mDatabase) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("createdAt", System.currentTimeMillis());

        mDatabase.child("users").child(uid).setValue(userData);
    }
}
