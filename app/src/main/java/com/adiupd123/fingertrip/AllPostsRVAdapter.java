package com.adiupd123.fingertrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.models.CreatePostModel;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class AllPostsRVAdapter extends RecyclerView.Adapter<AllPostsRVAdapter.PostViewHolder> {
    List<HashMap<String, Object>> posts;
    Context context;

    public AllPostsRVAdapter(List<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_user_profile_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(posts.get(position).get("postPhoto").toString())
                .placeholder(R.drawable.default_post_image)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.getApplicationContext().get
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postImage);
        }
    }
}
