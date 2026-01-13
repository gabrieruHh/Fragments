package com.gabriell.firebaseauthapp.ui.screensdashboard.addsales;

import android.app.ComponentCaller;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.ShaderKt;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.ActivityTestProductBinding;
import com.gabriell.firebaseauthapp.models.Post;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class TestProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    Uri imageUri;

    DatabaseReference databaseRef;
    StorageReference storageRef;
    ActivityTestProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseRef = FirebaseDatabase.getInstance().getReference("post");
        storageRef = FirebaseStorage.getInstance().getReference("post");

        binding.btnEscolher.setOnClickListener(v -> escolherImagem());
        binding.btnEnviar.setOnClickListener(v -> postar());
    }

    private void escolherImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            binding.imgPreview.setImageURI(imageUri);
        }
    }

    private void postar() {
        String title = binding.edtTitulo.getText().toString();
        if (imageUri == null || title.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        String nomeArquivo = UUID.randomUUID().toString();
        StorageReference ref = storageRef.child(nomeArquivo);

        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {

                            String url = uri.toString();
                            Post post = new Post(title, url);

                            String id = databaseRef.push().getKey();
                            databaseRef.child(id).setValue(post);

                            Toast.makeText(this, "Post criado!", Toast.LENGTH_LONG).show();
                        })
                ).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        imageUri = null;
        binding.imgPreview.setImageURI(null);
    }
}