package com.gabriell.firebaseauthapp.ui.screensdashboard.addsales;

import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.ShaderKt;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.adapters.ImageAdapter;
import com.gabriell.firebaseauthapp.databinding.ActivityTestProductBinding;
import com.gabriell.firebaseauthapp.models.Post;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestProductActivity extends AppCompatActivity {
    //Variaveis para adicionar produto do usuario no banco de dados
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    //Variavéis para selecionar imagens do usuario
    private List<Uri> uriList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private String idPost;

    //Define o limite maximo de imagens selecionadas
    private static final int MAX_IMAGES = 5;
    private ActivityTestProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View content = binding.containerMain;
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

        imageAdapter = new ImageAdapter(uriList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(imageAdapter);

        //Autenticação e usuario atual
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance().getReference("post");
        storageRef = FirebaseStorage.getInstance().getReference("post");

        binding.btnSelectImages.setOnClickListener(v -> escolherImagem());
        binding.btnPostar.setOnClickListener(v -> postar());

    }
    //Inicializador do seletor de imagens
    private final ActivityResultLauncher<PickVisualMediaRequest> pickVisualMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(MAX_IMAGES), uris ->{
                if (!uris.isEmpty()){
                    uriList.addAll(uris);
                    imageAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this, "Nenhuma imagem foi selecionada", Toast.LENGTH_SHORT).show();
                }
            });
    private void escolherImagem() {
        uriList.clear();
        //abre o novo seletor do android que já mostra o limite de imagens para o usuario
        pickVisualMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void postar() {
        //recebendo dados da postagem
        String idUser = mUser.getUid();
        String description = binding.etDescription.getText().toString();

        if (idUser != null){
            if (uriList.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            Toast.makeText(this, "Usuário vazio", Toast.LENGTH_LONG).show();
        }

        //List para guardar as URLs que o firebase vai gerar
        List<String> urlList = new ArrayList<>();
        final int totalImagens = uriList.size();

        Toast.makeText(this, "Iniciando Upload", Toast.LENGTH_LONG).show();

        for (Uri image : uriList) {

            String nomeArquivo = UUID.randomUUID().toString();
            StorageReference refStorage = storageRef.child(nomeArquivo);

            byte[] imagemComprimida = comprimirImagem(image);
            if (imagemComprimida != null) {
                refStorage.putBytes(imagemComprimida)
                        .addOnSuccessListener(taskSnapshot ->
                                refStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                                    urlList.add(uri.toString());

                                    if (urlList.size() == totalImagens) {
                                        salvarNoFirebase(idUser, description, urlList);
                                        Toast.makeText(this, "aqui inicia o salvar", Toast.LENGTH_LONG).show();
                                    }
                                })).addOnFailureListener(e -> {
                            binding.etDescription.setText(e.toString());
                        });
            }
        }
    }

    //Função para comprimir imagem
    private byte[] comprimirImagem(Uri image) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
            int largura = bitmap.getWidth();
            int altura = bitmap.getHeight();

            int novaLargura = 800;
            int novaAltura = (altura * novaLargura) / largura;
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, novaLargura, novaAltura, true);

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            bitmapReduzido.compress(Bitmap.CompressFormat.JPEG, 70, boas);

            return boas.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void salvarNoFirebase(String idUser, String description, List<String> urlList) {
        Toast.makeText(this, "Iniciando salvamento dentro do metodo", Toast.LENGTH_LONG).show();
        idPost = databaseRef.push().getKey();

        double longitude = 2342413;
        double latitude = 41241341;

        if (idPost != null){
            Toast.makeText(this, "entrando dentro do if do idpost ", Toast.LENGTH_LONG).show();
            Post post = new Post(idPost,idUser, description, urlList, latitude, longitude);
            databaseRef.child(idPost).setValue(post)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Venda publicada com sucesso!", Toast.LENGTH_LONG).show();
                        uriList.clear();
                        imageAdapter.notifyDataSetChanged();
                        finish();
                    }).addOnFailureListener(e ->{
                        Toast.makeText(this, "Erro ao tentar salvar "+ e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }
}