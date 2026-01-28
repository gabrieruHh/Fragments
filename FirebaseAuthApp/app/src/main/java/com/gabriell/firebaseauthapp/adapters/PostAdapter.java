package com.gabriell.firebaseauthapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gabriell.firebaseauthapp.R;
import com.gabriell.firebaseauthapp.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    DatabaseReference ref;
    private Context context;
    private List<Post> listPost;

    public PostAdapter(Context context, List<Post> listPost) {
        this.context = context;
        this.listPost = listPost;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = listPost.get(position);

        //para pegar os dados do usuario que postou
        String idUser = post.getUserId();
        ref = FirebaseDatabase.getInstance().getReference("users")
                        .child(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.txtName.setText(snapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        });

        holder.txtDescription.setText(post.getDescription());

        ImagePostAdapter imagePostAdapter = new ImagePostAdapter(context, post.getImageUrl());
        holder.rvImages.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );
        holder.rvImages.setAdapter(imagePostAdapter);
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RecyclerView rvImages;
        TextView txtDescription, txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImages = itemView.findViewById(R.id.listImgPost);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtName = itemView.findViewById(R.id.txtNameUser);
        }
    }
}
