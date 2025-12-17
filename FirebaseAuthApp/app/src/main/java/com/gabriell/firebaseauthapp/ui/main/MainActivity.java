package com.gabriell.firebaseauthapp.ui.main;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}