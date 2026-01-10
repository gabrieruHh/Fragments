package com.gabriell.firebaseauthapp.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gabriell.firebaseauthapp.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<String> listaUrls;

    public ImageAdapter(Context context, List<String> listaUrls) {
        this.context = context;
        this.listaUrls = listaUrls;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_imagem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        String url = listaUrls.get(position);

        Glide.with(context)
                .load(url)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listaUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItem);
        }
    }
}
