package com.gabriell.firebaseauthapp.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.FragmentDetailsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.MapMaker;


public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng selectedLatLng;

    FragmentDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater());

        //para vincular id do fragmentMap
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        googleMap.setOnMapClickListener(latLng -> {
            googleMap.clear();

            //Para colocar ponteiro no mapa onde for clicado
            googleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Local selecionado"));
            selectedLatLng = latLng;

            Toast.makeText(requireContext(), "Latidute: " + selectedLatLng.latitude, Toast.LENGTH_SHORT).show();
            Toast.makeText(requireContext(), "Longitude: " + selectedLatLng.longitude, Toast.LENGTH_SHORT).show();

            //Para guardar a latitude e longitude
            Bundle result = new Bundle();
            result.putDouble("lat", selectedLatLng.latitude);
            result.putDouble("lng", selectedLatLng.longitude);

            getParentFragmentManager().setFragmentResult("latitude", result);
//            getParentFragmentManager().popBackStack();

        });

    }
}