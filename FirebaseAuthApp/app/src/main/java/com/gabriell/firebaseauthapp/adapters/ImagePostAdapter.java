package com.gabriell.firebaseauthapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gabriell.firebaseauthapp.R;

import java.util.List;

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostAdapter.ImageViewHolder> {
    private Context context;
    private List<String> images;

    public ImagePostAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImagePostAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image_post, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostAdapter.ImageViewHolder holder, int position) {
        Glide.with(context)
                .load(images.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPost);

        }
    }
}
