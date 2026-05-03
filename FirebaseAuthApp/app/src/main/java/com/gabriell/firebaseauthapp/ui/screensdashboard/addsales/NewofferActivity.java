package com.gabriell.firebaseauthapp.ui.screensdashboard.addsales;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.ActivityNewofferBinding;

public class NewofferActivity extends AppCompatActivity {

    ActivityNewofferBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewofferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}