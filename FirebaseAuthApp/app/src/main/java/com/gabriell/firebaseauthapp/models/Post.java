package com.gabriell.firebaseauthapp.models;

import android.net.Uri;
import android.widget.TextView;

import java.util.List;

public class Post {

    private String userId;
    private String description;
    private List<String> listImageUrl;
    private String idProduct;

    public Post() { //  Obrigatório para o Firebase
    }

    public Post(String idProduct, String userId, String description, List<String> listImageUrl) {
        this.idProduct = idProduct;
        this.userId = userId;
        this.description = description;
        this.listImageUrl = listImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String title) {
        this.description = title;
    }

    public List<String> getImageUrl() {
        return listImageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.listImageUrl = imageUrl;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
}
