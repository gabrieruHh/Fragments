package com.gabriell.firebaseauthapp.ui.screensdashboard.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.adapters.ImageAdapter;
import com.gabriell.firebaseauthapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<String> listImagens;
    ImageAdapter imageAdapter;

    DatabaseReference databaseRef;

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.recyclerViewImg.setLayoutManager(new LinearLayoutManager(requireContext()));

        listImagens = new ArrayList<>();

        imageAdapter = new ImageAdapter(requireContext(), listImagens);

        binding.recyclerViewImg.setAdapter(imageAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("imagens");

        carregarImagens();

        return binding.getRoot();
    }

    private void carregarImagens() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listImagens.clear();
                Toast.makeText(requireContext(), "Qtd: " + snapshot.getChildrenCount(), Toast.LENGTH_LONG).show();

                for (DataSnapshot snap : snapshot.getChildren()){
                    String url = snap.getValue(String.class);
                    listImagens.add(url);
                }

                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Erro: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}