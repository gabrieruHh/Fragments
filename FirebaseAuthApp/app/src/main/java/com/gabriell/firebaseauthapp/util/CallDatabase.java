package com.gabriell.firebaseauthapp.util;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CallDatabase {
    String returnMensage;

    public static class RegisterAccountApp{
        private boolean isValid;
        private String erroMenssage;
        private String field;
        public RegisterAccountApp(Boolean isValid, String erroMenssage, String field){
            //RECEBE OS VALORES VERIFICADOS
            this.isValid = isValid;
            this.erroMenssage = erroMenssage;
            this.field = field;
        }

        //RETORNA VALORES DA VERIFICAÇÃO
        public boolean isValid(){return isValid; }
        public String getErroMenssage(){return erroMenssage; }
        public String getField(){return field;}
    }

    public static RegisterAccountApp validadetionRegistration(String name, String email, String password,
                                                              FirebaseAuth mAuth, DatabaseReference mDatabase, String menssage){

        //FAZ A VERIFICAÇÃO DOS VALORES ENVIADOS
        if (TextUtils.isEmpty(name)){
            return new RegisterAccountApp(false, "Nome é obrigatorio", "name");
        }
        if (TextUtils.isEmpty(email)){
            return new RegisterAccountApp(false, "Email é obrigatorio", "email");
        }
        if (TextUtils.isEmpty(password)){
            return new RegisterAccountApp(false, "Senha é obrigatorio", "password");
        }
        if (password.length()<6){
            return new RegisterAccountApp(false, "Senha deve conter no minímo 6 caracter", "passwordLength");
        }

        //CRIANDO USUARIO COM EMAIL E SENHA
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               FirebaseUser user = mAuth.getCurrentUser();
           }else {
               menssage = "Falha ao cadastrar usúario";

           }
        });

        String returnMenssage;
        return new RegisterAccountApp(true, menssage, null);
    }

}
