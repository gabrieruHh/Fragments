package com.gabriell.firebaseauthapp.models;

import android.net.Uri;
import android.widget.TextView;

public class Post {

    String title;
    String imageUrl;

    public Post() { //  Obrigatório para o Firebase
    }

    public Post(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
