package com.gabriell.firebaseauthapp.ui.screensdashboard.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriell.firebaseauthapp.AddSalesActivity;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.adapters.ItemAdapter;
import com.gabriell.firebaseauthapp.databinding.ActivityDashboardBinding;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
import com.gabriell.firebaseauthapp.ui.screensdashboard.home.HomeFragment;
import com.gabriell.firebaseauthapp.ui.screensdashboard.profile.ProfileFragment;
import com.gabriell.firebaseauthapp.util.ChangedFragment;
import com.gabriell.firebaseauthapp.util.LogoutGoogle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends LogoutGoogle {

    private FirebaseAuth mAuth;

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToLogin();
            return;
        }

        loadFragment(new HomeFragment());

        //Para que o BottomNavigation fique acima do navigationBar
        View rootNav = binding.bottomNavContainer;
        ViewCompat.setOnApplyWindowInsetsListener(rootNav, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    0,
                    0,                 // 🔥 remove o espaço de cima
                    0,
                    systemBars.bottom
            );
            return insets;
        });

        //Para que o FrameLayout fique abaixo do statusBar
                View content = binding.containerCenter;
                ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
                    Insets statusInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
                    v.setPadding(
                            0,
                            statusInsets.top,
                            0,
                            v.getPaddingBottom()
                    );
                    return insets;
                });

            setupNavClicks();
    }

    private void setupNavClicks() {
        binding.navHome.setOnClickListener(v -> {
            animateClick(v);
            loadFragment(new HomeFragment());
        });

        binding.navAddSale.setOnClickListener(v -> {
            animateClick(v);
            startActivity(new Intent(this, AddSalesActivity.class));
        });

        binding.navProfile.setOnClickListener(v -> {
            animateClick(v);
            loadFragment(new ProfileFragment());
        });

    }

    private void loadFragment(Fragment fragment) {
        ChangedFragment.loadFragment(this, R.id.container_center, fragment, new Bundle());
    }

    private void animateClick(View view) {
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(80)
                .withEndAction(() ->
                        view.animate().scaleX(1f).scaleY(1f).setDuration(80)
                );
    }

    private void goToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}