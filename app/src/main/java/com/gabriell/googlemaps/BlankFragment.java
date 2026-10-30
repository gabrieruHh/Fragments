package com.gabriell.googlemaps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriell.googlemaps.databinding.FragmentBlankBinding;



public class BlankFragment extends Fragment{
    FragmentBlankBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlankBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}