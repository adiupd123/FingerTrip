package com.adiupd123.fingertrip;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.models.CreatePostModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AllPostsRVAdapter extends RecyclerView.Adapter<AllPostsRVAdapter.PostViewHolder> {
    List<HashMap<String, Object>> posts;
    Context context;
    FirebaseAuth  mAuth;
    public AllPostsRVAdapter(List<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_user_profile_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        HashMap<String, Object> post = posts.get(position);
        Glide.with(holder.imageView.getContext())
                .load(post.get("postPhoto").toString())
                .placeholder(R.drawable.default_post_image)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PostFragment fragment = new PostFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                bundle.putString("emailID", mAuth.getCurrentUser().getEmail());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
