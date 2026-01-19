package com.gabriell.firebaseauthapp.ui.screensdashboard.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.FragmentProfileBinding;
import com.gabriell.firebaseauthapp.util.LogoutGoogle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        carregarDados();

        binding.btnSair.setOnClickListener(v -> logAuth());

        return binding.getRoot();
    }

    private void carregarDados() {
        //Para pegar o nome o usuario
        binding.txtNome.setText(user.getDisplayName());

        //Para pegar a imagem da conta do usuario e exibir
        Uri uri = user.getPhotoUrl();
        if (uri != null){
            Glide.with(requireContext())
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.imgProfile);
        }else {
            binding.imgProfile.setImageResource(R.drawable.ic_profile);
        }

    }

    private void logAuth() {
        LogoutGoogle logoutGoogle = new LogoutGoogle(requireContext());
        logoutGoogle.logout();
    }
}