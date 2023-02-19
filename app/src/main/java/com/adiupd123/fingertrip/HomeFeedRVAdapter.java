package com.adiupd123.fingertrip;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.databinding.PostLayoutBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFeedRVAdapter extends RecyclerView.Adapter<HomeFeedRVAdapter.HomeFeedViewHolder> {
    private List<HashMap<String, Object>> posts;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;
    private String ownerID;
    private final String curUserEmailID;
    private ArrayList<String> likes;
    private long likesCount;
    private boolean liked;
    private final Context context;

    public HomeFeedRVAdapter(List<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        curUserEmailID = mAuth.getCurrentUser().getEmail().replace('.',',');
        liked = false;
        likesCount = 0L;
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

        ownerID = post.get("postOwnerID").toString();

        if (Boolean.TRUE.equals(post.get("liked"))) liked = true;
        else liked = false;
        likesCount = Long.valueOf(post.get("likesCount").toString());
        if(liked){
            likes = (ArrayList<String>) post.get("likes");
            for (String likedUser: likes){
                if(likedUser.equals(curUserEmailID)){
                    holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_done));
                    break;
                }
            }
        }
        holder.likeCountTV.setText(String.valueOf(likesCount));


        holder.likeIV.setOnClickListener(click -> likeFunction(holder, post));



        Glide.with(context)
                .load(post.get("postPhoto").toString())
                .placeholder(R.drawable.default_post_image)
                .into(holder.postPhotoIV);
        holder.postTimeTV.setText(post.get("postTimeStamp").toString());
        holder.postTitleTV.setText(post.get("postTitle").toString());
        holder.postDescTV.setText(post.get("postDesc").toString());

        loadUserData(holder);

        holder.postUserPhotoIV.setOnClickListener(click -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserFragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("personalInfo", personalInfoHashMap);
            bundle.putSerializable("socialInfo", socialInfoHashMap);
            bundle.putString("ownerID", ownerID);
            bundle.putString("emailID", curUserEmailID);
            userFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container_view, userFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private void likeFunction(@NonNull HomeFeedViewHolder holder, HashMap<String, Object> post) {
        String postID = (String) post.get("postID");
        if(liked){
            int isFound = 0;
            for (String likedUser: likes){
                if(likedUser.equals(curUserEmailID)){
                    // It means user has liked already and the user has tapped to remove like
                    isFound = 1;
                    holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                    likesCount--;
                    holder.likeCountTV.setText(String.valueOf(likesCount));
                    likes.remove(likedUser);
                    if(likesCount == 0){
                        liked = false;
                        post.put("liked", liked);
                    }
                    break;
                }
            }
            if(isFound == 0){
                holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_done));
                likesCount++;
                holder.likeCountTV.setText(String.valueOf(likesCount));
                likes.add(curUserEmailID);
            }
        } else {
            likes = new ArrayList<>();
            likes.add(curUserEmailID);
            likesCount++;
            liked = true;
            holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_done));
            holder.likeCountTV.setText(String.valueOf(likesCount));
            post.put("liked", liked);
        }
        post.put("likesCount", likesCount);
        post.put("likes", likes);
        FirebaseDatabase.getInstance().getReference().child("posts/"+postID+"/").setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(context, "Like Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUserData(@NonNull HomeFeedViewHolder holder) {
        FirebaseDatabase.getInstance().getReference("users").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot emailSnapshot: snapshot.getChildren()){
                    String key = emailSnapshot.getKey();
                    if(key !=  null && ownerID != null){
                        if(ownerID.equals(key)){
                            personalInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("personal_info/").getValue();
                            socialInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("social_info/").getValue();
                            holder.usernameTV.setText("@" + personalInfoHashMap.get("username"));
                            holder.nameTV.setText(personalInfoHashMap.get("name").toString());
                            Glide.with(context)
                                    .load(socialInfoHashMap.get("profilePhoto"))
                                    .into(holder.postUserPhotoIV);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PostFragment.class", error.toException().toString());
            }

        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class HomeFeedViewHolder extends RecyclerView.ViewHolder{
        private ImageView postPhotoIV, postUserPhotoIV, likeIV;
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
            likeIV = itemView.findViewById(R.id.like_imageView);
            likeCountTV = itemView.findViewById(R.id.like_textView);
        }
    }
}
