package com.gabriell.firebaseauthapp.ui.screensdashboard.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriell.firebaseauthapp.adapters.PostAdapter;
import com.gabriell.firebaseauthapp.databinding.FragmentHomeBinding;
import com.gabriell.firebaseauthapp.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Post> listPost = new ArrayList<>();
    PostAdapter postAdapter;

    DatabaseReference databaseRef;

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(requireContext()));

        postAdapter = new PostAdapter(requireContext(), listPost);

        binding.recyclerViewPost.setAdapter(postAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("post");

        carregarPosts();

        return binding.getRoot();
    }

    private void carregarPosts() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPost.clear();
                Toast.makeText(requireContext(), "Qtd: " + snapshot.getChildrenCount(), Toast.LENGTH_LONG).show();

                for (DataSnapshot snap : snapshot.getChildren()){
                    Post post = snap.getValue(Post.class);
                    listPost.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Erro: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}