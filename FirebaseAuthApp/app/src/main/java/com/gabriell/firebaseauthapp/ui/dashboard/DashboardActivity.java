package com.gabriell.firebaseauthapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.adapters.ItemAdapter;
import com.gabriell.firebaseauthapp.ui.login.LoginActivity;
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

    private TextView tvTitle;
    private EditText etNewItem;
    private Button btnAddItem, btnLogout;
    private RecyclerView rvItems;
    private ItemAdapter adapter;
    private List<String> itemList;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToLogin();
            return;
        }

        // VERIFICAR SE E-MAIL FOI CONFIRMADO
        if (!currentUser.isEmailVerified()) {
            Toast.makeText(this,
                    "Por favor, verifique seu e-mail para acessar o aplicativo.",
                    Toast.LENGTH_LONG).show();
            mAuth.signOut();
            goToLogin();
            return;
        }

        userId = currentUser.getUid();

        // Resto do código permanece o mesmo...
        etNewItem = findViewById(R.id.etNewItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnLogout = findViewById(R.id.btnLogout);
        rvItems = findViewById(R.id.rvItems);
        tvTitle = findViewById(R.id.tvTitle);

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(itemList);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> addItem());
        btnLogout.setOnClickListener(v -> logout());

        loadItems();

        // Mostrar boas-vindas
        tvTitle.setText("Lista de Itens do " + currentUser.getDisplayName());
    }

    private void addItem() {
        String itemText = etNewItem.getText().toString().trim();

        if (TextUtils.isEmpty(itemText)) {
            etNewItem.setError("Digite um item");
            etNewItem.requestFocus();
            return;
        }

        String itemId = mDatabase.child("users").child(userId).child("items").push().getKey();

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("text", itemText);
        itemData.put("timestamp", System.currentTimeMillis());

        if (itemId != null) {
            mDatabase.child("users").child(userId).child("items").child(itemId)
                    .setValue(itemData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            etNewItem.setText("");
                            Toast.makeText(DashboardActivity.this,
                                    "Item adicionado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DashboardActivity.this,
                                    "Erro ao adicionar item", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadItems() {
        mDatabase.child("users").child(userId).child("items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String itemText = snapshot.child("text").getValue(String.class);
                            if (itemText != null) {
                                itemList.add(itemText);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (!isFinishing() && mAuth.getCurrentUser() != null) {
                            Toast.makeText(DashboardActivity.this,
                                    "Erro ao carregar itens: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}