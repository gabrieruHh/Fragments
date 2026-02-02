package com.gabriell.firebaseauthapp.ui.details;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.FragmentDetailsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.common.collect.MapMaker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng selectedLatLng;
    private Map<String, Object> location = new HashMap<>();
    private Map<String, Object> anddressMap = new HashMap<>();

    private FusedLocationProviderClient fusedLocationClient;

    //Variavel para identificar result da localização
    private static final int LOCATION_PERMISSION_REQUEST = 1002;

    DatabaseReference databaseReference;
    FragmentDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        //para vincular id do fragmentMap
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Inicializar o Places
        Places.initialize(
                requireContext(),
                getString(R.string.google_maps_key));

        databaseReference = FirebaseDatabase.getInstance().getReference("locations");

        checkLocationPermission();

        binding.edtAddress.requestFocus();
        binding.edtAddress.setOnClickListener(v -> listarAnddress());
        binding.btnSalvarLatitude.setOnClickListener(v -> salvarLatitude());
        binding.btnSalvarAnddress.setOnClickListener(v -> salvarAnddress());

        return binding.getRoot();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST
                );
        } else {
            getCurrentLocation();
        }

    }

    private void getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location1 -> {
            if (location1 != null) {
                double lat = location1.getLatitude();
                double lng = location1.getLongitude();

                LatLng latLng = new LatLng(lat, lng);

                googleMap.clear();
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Minha localização")
                );
                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                );

                getAnddressFromLocation(lat, lng);
            }
        });
    }

    //Para pegar o endereço da localização atualdo usuario e colocar no edt
    private void getAnddressFromLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()){
                Address address = addresses.get(0);

                String fullAddress = address.getAddressLine(0);
                String city = address.getLocality();
                String neighborhood = address.getSubLocality();
                String street = address.getThoroughfare();
                String number = address.getSubThoroughfare();
                String state = address.getAdminArea();

                binding.edtAddress.setText(fullAddress);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarAnddress() {
        String fullAnddress = binding.edtAddress.getText().toString();
        anddressMap.put("fullAnddress", fullAnddress);

        databaseReference = FirebaseDatabase.getInstance().getReference("AnddressSell");
        databaseReference.setValue(anddressMap).addOnSuccessListener(unused ->{
            Toast.makeText(requireContext(), "Endereço salvo!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Falha ao salvar endereço" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void listarAnddress() {
        List<Place.Field> field = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS
        );

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                field
        )
                .setCountries(Arrays.asList("BR"))
                .build(requireContext());

        startActivityForResult(intent, 1001
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            LatLng latLng = place.getLatLng();
            String anddress = place.getAddress();

            binding.edtAddress.setText(anddress);

            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(anddress)
            );

            googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, 16f)
            );

            String city = "";
            String neighborhood = "";
            String street = "";
            String number = "";

            for (AddressComponent component : place.getAddressComponents().asList()){
                if (component.getTypes().contains("route")){
                    street = component.getName();
                    anddressMap.put("street", street);
                }
                if (component.getTypes().contains("street_number")){
                    number = component.getName();
                    anddressMap.put("number", number);
                }
                if (component.getTypes().contains("sublocality")){
                    neighborhood = component.getName();
                    anddressMap.put("neighborhood", neighborhood);
                }
                if (component.getTypes().contains("administrative_area_level_2")){
                    city = component.getName();
                    anddressMap.put("city", city);
                }
            }
        }
    }

    private void salvarLatitude() {

        String idLocations = databaseReference.push().getKey();

        if (idLocations != null) {
            databaseReference.child(idLocations).setValue(location)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(requireContext(), "Localização da venda Salva com sucesso!", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Falha ao savar localização " + e.getMessage(), Toast.LENGTH_SHORT);
                    });
        }
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

            double lat = selectedLatLng.latitude;
            double lgt = selectedLatLng.longitude;

            getAnddressFromLocation(lat, lgt);

            //Para guardar a latitude e longitude no Bundle e pegar em outro fragment
            Bundle result = new Bundle();
            result.putDouble("lat", selectedLatLng.latitude);
            result.putDouble("lng", selectedLatLng.longitude);

            getParentFragmentManager().setFragmentResult("location", result);
//            getParentFragmentManager().popBackStack();

            location.put("latitude", selectedLatLng.latitude);
            location.put("longitude", selectedLatLng.longitude);

        });

    }
}