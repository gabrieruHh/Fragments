package com.gabriell.firebaseauthapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gabriell.firebaseauthapp.databinding.ActivityAddSalesBinding;

public class AddSalesActivity extends AppCompatActivity {

    ActivityAddSalesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSalesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
}