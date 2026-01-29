package com.gabriell.firebaseauthapp.ui.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriell.firebaseauthapp.databinding.FragmentDetailsBinding;


public class DetailsFragment extends Fragment {

    FragmentDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater());

        if (getArguments()!= null){
            String idPost = getArguments().getString("postId");
            Toast.makeText(requireContext(), idPost, Toast.LENGTH_LONG).show();
        }

        return binding.getRoot();
    }
}