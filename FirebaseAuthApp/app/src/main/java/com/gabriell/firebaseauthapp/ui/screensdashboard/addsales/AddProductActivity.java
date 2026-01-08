package com.gabriell.firebaseauthapp.ui.screensdashboard.addsales;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.databinding.ActivityAddSalesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gabriell.firebaseauthapp.models.Product;
import com.gabriell.firebaseauthapp.models.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

//    private static final int PICK_IMAGES_REQUEST = 1001;
//    private static final int MAX_IMAGES = 8;
//
//    private LinearLayout imageContainer;
//    private EditText etTitle, etDescription, etPrice, etCity, etNeighborhood;
//    private AutoCompleteTextView actvCategory, actvCondition;
//    private Button btnPublish;
//    private ProgressBar progressBar;
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser currentUser;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//
//    private List<Uri> selectedImageUris = new ArrayList<>();
//    private List<String> uploadedImageUrls = new ArrayList<>();
//    private UserProfile userProfile;
//
//    // Arrays para dropdowns
//    private String[] categories = {
//            "Eletrônicos", "Celulares", "Informática", "Moda", "Casa", "Esportes",
//            "Automóveis", "Imóveis", "Serviços", "Outros"
//    };
//
//    private String[] conditions = {"Novo", "Usado", "Semi-novo"};

    ActivityAddSalesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSalesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        initViews();
//        setupDropdowns();
//        loadUserProfile();
//
//        // Configurar botão de adicionar foto
//        imageContainer.findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
//            openImagePicker();
//        });
//
//        btnPublish.setOnClickListener(v -> {
//            publishProduct();
//        });
    }

//    private void initViews() {
//        imageContainer = findViewById(R.id.imageContainer);
//        etTitle = findViewById(R.id.etTitle);
//        etDescription = findViewById(R.id.etDescription);
//        etPrice = findViewById(R.id.etPrice);
//        etCity = findViewById(R.id.etCity);
//        etNeighborhood = findViewById(R.id.etNeighborhood);
//        actvCategory = findViewById(R.id.actvCategory);
//        actvCondition = findViewById(R.id.actvCondition);
//        btnPublish = findViewById(R.id.btnPublish);
//        progressBar = findViewById(R.id.progressBar);
//    }
//
//    private void setupDropdowns() {
//        // Configurar adaptadores para dropdowns
//        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, categories);
//        actvCategory.setAdapter(categoryAdapter);
//
//        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, conditions);
//        actvCondition.setAdapter(conditionAdapter);
//    }
//
//    private void loadUserProfile() {
//        if (currentUser == null) return;
//
//        mDatabase.child("users").child(currentUser.getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            userProfile = snapshot.getValue(UserProfile.class);
//                            if (userProfile != null) {
//                                // Preencher cidade e bairro do perfil do usuário
//                                if (!TextUtils.isEmpty(userProfile.getCity())) {
//                                    etCity.setText(userProfile.getCity());
//                                }
//                                if (!TextUtils.isEmpty(userProfile.getNeighborhood())) {
//                                    etNeighborhood.setText(userProfile.getNeighborhood());
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(AddProductActivity.this,
//                                "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagens"),
//                PICK_IMAGES_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                if (data.getClipData() != null) {
//                    // Múltiplas imagens selecionadas
//                    int count = data.getClipData().getItemCount();
//                    for (int i = 0; i < count && selectedImageUris.size() < MAX_IMAGES; i++) {
//                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                        selectedImageUris.add(imageUri);
//                    }
//                } else if (data.getData() != null) {
//                    // Uma imagem selecionada
//                    selectedImageUris.add(data.getData());
//                }
//
//                // Atualizar interface com miniaturas
//                updateImageThumbnails();
//            }
//        }
//    }
//
//    private void updateImageThumbnails() {
//        // Limpar container (exceto o botão de adicionar)
//        imageContainer.removeAllViews();
//
//        // Adicionar miniaturas das imagens selecionadas
//        for (int i = 0; i < selectedImageUris.size(); i++) {
//            addImageThumbnail(selectedImageUris.get(i), i);
//        }
//
//        // Adicionar botão de adicionar (se ainda couber)
//        if (selectedImageUris.size() < MAX_IMAGES) {
//            addAddPhotoButton();
//        }
//    }
//
//    private void addImageThumbnail(Uri imageUri, int position) {
//        // Criar layout da miniatura
//        LinearLayout thumbnailLayout = new LinearLayout(this);
//        thumbnailLayout.setOrientation(LinearLayout.VERTICAL);
//        thumbnailLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                120, 120, 0));
//        thumbnailLayout.setPadding(0, 0, 8, 0);
//
//        // ImageView para a miniatura
//        ImageView imageView = new ImageView(this);
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageURI(imageUri);
//
//        // Botão para remover
//        ImageView removeButton = new ImageView(this);
//        removeButton.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
//        removeButton.setImageResource(android.R.drawable.ic_delete);
//        removeButton.setBackgroundResource(R.drawable.circle_background);
//        removeButton.setPadding(4, 4, 4, 4);
//        removeButton.setOnClickListener(v -> {
//            selectedImageUris.remove(position);
//            updateImageThumbnails();
//        });
//
//        thumbnailLayout.addView(imageView);
//        thumbnailLayout.addView(removeButton);
//
//        imageContainer.addView(thumbnailLayout);
//    }
//
//    private void addAddPhotoButton() {
//        // Recriar botão de adicionar foto
//        LinearLayout addButton = (LinearLayout) getLayoutInflater()
//                .inflate(R.layout.layout_add_photo_button, null);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                120, 120, 0);
//        params.setMargins(0, 0, 8, 0);
//        addButton.setLayoutParams(params);
//
//        addButton.setOnClickListener(v -> openImagePicker());
//
//        imageContainer.addView(addButton);
//    }
//
//    private void publishProduct() {
//        // Validações
//        if (selectedImageUris.isEmpty()) {
//            Toast.makeText(this, "Adicione pelo menos uma foto do produto",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String title = etTitle.getText().toString().trim();
//        String description = etDescription.getText().toString().trim();
//        String priceStr = etPrice.getText().toString().trim();
//        String category = actvCategory.getText().toString().trim();
//        String condition = actvCondition.getText().toString().trim();
//        String city = etCity.getText().toString().trim();
//        String neighborhood = etNeighborhood.getText().toString().trim();
//
//        if (TextUtils.isEmpty(title)) {
//            etTitle.setError("Digite o título do anúncio");
//            etTitle.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(description)) {
//            etDescription.setError("Digite a descrição do produto");
//            etDescription.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(priceStr)) {
//            etPrice.setError("Digite o preço do produto");
//            etPrice.requestFocus();
//            return;
//        }
//
//        double price;
//        try {
//            price = Double.parseDouble(priceStr);
//            if (price <= 0) {
//                etPrice.setError("Preço deve ser maior que zero");
//                etPrice.requestFocus();
//                return;
//            }
//        } catch (NumberFormatException e) {
//            etPrice.setError("Preço inválido");
//            etPrice.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(category)) {
//            actvCategory.setError("Selecione uma categoria");
//            actvCategory.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(condition)) {
//            actvCondition.setError("Selecione a condição do produto");
//            actvCondition.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(city)) {
//            etCity.setError("Digite a cidade");
//            etCity.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(neighborhood)) {
//            etNeighborhood.setError("Digite o bairro");
//            etNeighborhood.requestFocus();
//            return;
//        }
//
//        // Mostrar progresso
//        progressBar.setVisibility(View.VISIBLE);
//        btnPublish.setEnabled(false);
//
//        // Criar produto
//        Product product = new Product(
//                currentUser.getUid(),
//                title,
//                description,
//                price,
//                category,
//                condition,
//                city,
//                neighborhood
//        );
//
//        // Adicionar informações do vendedor
//        if (userProfile != null) {
//            product.setSellerName(userProfile.getName());
//            product.setSellerPhone(userProfile.getPhone());
//            product.setSellerProfileImage(userProfile.getProfileImageUrl());
//        } else {
//            product.setSellerName(currentUser.getDisplayName());
//            product.setSellerPhone(currentUser.getPhoneNumber());
//        }
//
//        // Primeiro fazer upload das imagens
//        uploadImages(product);
//    }
//
//    private void uploadImages(final Product product) {
//        uploadedImageUrls.clear();
//
//        for (int i = 0; i < selectedImageUris.size(); i++) {
//            Uri imageUri = selectedImageUris.get(i);
//
//            // Criar referência única para cada imagem
//            String imageName = UUID.randomUUID().toString() + ".jpg";
//            StorageReference imageRef = mStorage.child("products")
//                    .child(currentUser.getUid())
//                    .child(imageName);
//
//            final int index = i;
//            imageRef.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Obter URL da imagem
//                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            uploadedImageUrls.add(uri.toString());
//
//                            // Se todas as imagens foram carregadas, salvar produto
//                            if (uploadedImageUrls.size() == selectedImageUris.size()) {
//                                product.setImages(uploadedImageUrls);
//                                saveProductToDatabase(product);
//                            }
//                        });
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(AddProductActivity.this,
//                                "Erro ao fazer upload da imagem: " + e.getMessage(),
//                                Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                        btnPublish.setEnabled(true);
//                    });
//        }
//    }
//
//    private void saveProductToDatabase(Product product) {
//        String productId = mDatabase.child("products").push().getKey();
//        product.setId(productId);
//
//        mDatabase.child("products").child(productId)
//                .setValue(product.toMap())
//                .addOnSuccessListener(aVoid -> {
//                    // Adicionar referência do produto ao usuário
//                    mDatabase.child("users").child(currentUser.getUid())
//                            .child("products").child(productId).setValue(true)
//                            .addOnCompleteListener(task -> {
//                                progressBar.setVisibility(View.GONE);
//                                btnPublish.setEnabled(true);
//
//                                Toast.makeText(AddProductActivity.this,
//                                        "Produto publicado com sucesso!",
//                                        Toast.LENGTH_LONG).show();
//
//                                // Voltar para a tela anterior ou para o dashboard
//                                finish();
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    progressBar.setVisibility(View.GONE);
//                    btnPublish.setEnabled(true);
//                    Toast.makeText(AddProductActivity.this,
//                            "Erro ao publicar produto: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                });
//    }
}