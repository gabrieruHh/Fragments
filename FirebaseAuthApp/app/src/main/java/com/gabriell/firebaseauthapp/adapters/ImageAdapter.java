package com.gabriell.firebaseauthapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.UiContext;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gabriell.firebaseauthapp.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<Uri> uriList;

    public ImageAdapter(List<Uri> imageUris) {
        this.uriList = imageUris;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Uri uri = uriList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .centerCrop()
                .into(holder.imageView);

        holder.imageButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION){

                uriList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, uriList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton imageButton;
        public ViewHolder(View view) {
            super(view);
            imageButton = view.findViewById(R.id.imgButtonDelete);
            imageView = view.findViewById(R.id.imgSelected);
        }
    }
}
