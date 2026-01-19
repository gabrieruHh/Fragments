package com.gabriell.firebaseauthapp.models;

import android.net.Uri;
import android.widget.TextView;

public class Post {

    private String userId;
    private String description;
    private String imageUrl;

    public Post() { //  Obrigatório para o Firebase
    }

    public Post(String userId, String description, String imageUrl) {
        this.userId = userId;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
