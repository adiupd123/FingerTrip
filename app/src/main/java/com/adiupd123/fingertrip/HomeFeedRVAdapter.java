package com.adiupd123.fingertrip;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.databinding.PostLayoutBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFeedRVAdapter extends RecyclerView.Adapter<HomeFeedRVAdapter.HomeFeedViewHolder> {
    private List<HashMap<String, Object>> posts;
    private Context context;
    private FirebaseAuth mAuth;

    public HomeFeedRVAdapter(List<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public HomeFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new HomeFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFeedViewHolder holder, int position) {
        HashMap<String, Object> post = posts.get(position);
        String ownerID = post.get("postOwnerID").toString();
        String curUserEmailID = mAuth.getCurrentUser().getEmail().replace('.',',');
        Glide.with(context)
                .load(post.get("postPhoto").toString())
                .placeholder(R.drawable.default_post_image)
                .into(holder.postPhotoIV);
        holder.postTimeTV.setText(post.get("postTimeStamp").toString());
        holder.postTitleTV.setText(post.get("postTitle").toString());
        holder.postDescTV.setText(post.get("postDesc").toString());
        holder.postUserPhotoIV.setOnClickListener(click -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserFragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", post);
            bundle.putString("ownerID", ownerID);
            bundle.putString("emailID", curUserEmailID);
            userFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container_view, userFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class HomeFeedViewHolder extends RecyclerView.ViewHolder{
        private ImageView postPhotoIV, postUserPhotoIV;
        private TextView usernameTV, nameTV, postTimeTV, likeCountTV, postTitleTV, postDescTV;
        public HomeFeedViewHolder(View itemView) {
            super(itemView);
            postPhotoIV = itemView.findViewById(R.id.post_imageView);
            postUserPhotoIV = itemView.findViewById(R.id.postUserPhoto_imageView);
            usernameTV = itemView.findViewById(R.id.postUsername_textView);
            nameTV = itemView.findViewById(R.id.postPersonName_textView);
            postTimeTV = itemView.findViewById(R.id.postTime_textView);
            postTitleTV = itemView.findViewById(R.id.postTitle_textView);
            postDescTV = itemView.findViewById(R.id.postDesc_textView);
        }
    }
}
