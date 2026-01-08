package com.gabriell.firebaseauthapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private String id;
    private String sellerId;
    private String sellerName;
    private String sellerPhone;
    private String title;
    private String description;
    private double price;
    private String category;
    private String condition; // Novo, Usado
    private String city;
    private String neighborhood;
    private List<String> images;
    private long createdAt;
    private long updatedAt;
    private String status; // ativo, vendido, inativo
    private int views;
    private int favorites;
    private String sellerProfileImage;

    // Construtores
    public Product() {
        images = new ArrayList<>();
    }

    public Product(String sellerId, String title, String description, double price,
                   String category, String condition, String city, String neighborhood) {
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.city = city;
        this.neighborhood = neighborhood;
        this.images = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.status = "ativo";
        this.views = 0;
        this.favorites = 0;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerPhone() { return sellerPhone; }
    public void setSellerPhone(String sellerPhone) { this.sellerPhone = sellerPhone; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public void addImage(String imageUrl) { this.images.add(imageUrl); }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getFavorites() { return favorites; }
    public void setFavorites(int favorites) { this.favorites = favorites; }

    public String getSellerProfileImage() { return sellerProfileImage; }
    public void setSellerProfileImage(String sellerProfileImage) {
        this.sellerProfileImage = sellerProfileImage;
    }

    // Método para converter para Map (Firebase)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        map.put("sellerName", sellerName);
        map.put("sellerPhone", sellerPhone);
        map.put("title", title);
        map.put("description", description);
        map.put("price", price);
        map.put("category", category);
        map.put("condition", condition);
        map.put("city", city);
        map.put("neighborhood", neighborhood);
        map.put("images", images);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        map.put("status", status);
        map.put("views", views);
        map.put("favorites", favorites);
        map.put("sellerProfileImage", sellerProfileImage);
        return map;
    }

    // Formatar preço para exibição
    public String getFormattedPrice() {
        return String.format("R$ %.2f", price);
    }

    // Tempo desde a publicação
    public String getTimeAgo() {
        long now = System.currentTimeMillis();
        long diff = now - createdAt;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + (days == 1 ? " dia atrás" : " dias atrás");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hora atrás" : " horas atrás");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minuto atrás" : " minutos atrás");
        } else {
            return "Agora mesmo";
        }
    }
}