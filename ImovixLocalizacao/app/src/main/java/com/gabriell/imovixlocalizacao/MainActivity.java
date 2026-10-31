package com.gabriell.imovixlocalizacao;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.gabriell.imovixlocalizacao.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mapa;

    //Solicitação para conferir localização do usúario
    private final ActivityResultLauncher<String[]> solicitacaoPermissao =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    resultado -> {

                        boolean precisa = Boolean.TRUE.equals(
                                resultado.get(Manifest.permission.ACCESS_FINE_LOCATION)
                        );

                        boolean aproximada = Boolean.TRUE.equals(
                                resultado.get(Manifest.permission.ACCESS_COARSE_LOCATION)
                        );

                        if (precisa || aproximada) {
                            habilitarLocalizacao();
                        } else {
                            Toast.makeText(
                                    this,
                                    "Permissão de localização não concedida.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapa = googleMap;

        verificarPermissao();
    }

    private void verificarPermissao() {

        boolean precisa = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean aproximada = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (precisa || aproximada) {
            habilitarLocalizacao();
        } else {
            solicitacaoPermissao.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void habilitarLocalizacao() {

        if (mapa == null) return;

        boolean precisa = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean aproximada = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!precisa && !aproximada) return;

        try {
            // Exibe o ponto azul no mapa.
            mapa.setMyLocationEnabled(true);

            // Busca a posição atual do dispositivo.
            int prioridade = precisa
                    ? Priority.PRIORITY_HIGH_ACCURACY
                    : Priority.PRIORITY_BALANCED_POWER_ACCURACY;

            CancellationTokenSource token =
                    new CancellationTokenSource();

            LocationServices.getFusedLocationProviderClient(this)
                    .getCurrentLocation(prioridade, token.getToken())
                    .addOnSuccessListener(this, location -> {

                        if (location != null) {

                            LatLng posicaoAtual = new LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                            );

                            mapa.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                            posicaoAtual,
                                            16f
                                    )
                            );

                        } else {

                            Toast.makeText(
                                    this,
                                    "Não foi possível obter sua localização. Verifique o GPS.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    })
                    .addOnFailureListener(this, erro -> {

                        Toast.makeText(
                                this,
                                "Erro ao obter localização: " + erro.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    });

        } catch (SecurityException e) {

            Toast.makeText(
                    this,
                    "Permissão de localização necessária.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
