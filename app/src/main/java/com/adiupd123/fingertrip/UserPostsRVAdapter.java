package com.adiupd123.fingertrip;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.models.CreatePostModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserPostsRVAdapter extends RecyclerView.Adapter<UserPostsRVAdapter.UserPostViewHolder> {

    private ArrayList<CreatePostModel> posts;

    public UserPostsRVAdapter(ArrayList<CreatePostModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public UserPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_user_profile_layout, parent, false);
        return new UserPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(posts.get(position).getPostPhoto())
                .placeholder(R.drawable.default_post_image)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.imageView.getContext(), "This Post is clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class UserPostViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public UserPostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postImage);
        }
    }
}
