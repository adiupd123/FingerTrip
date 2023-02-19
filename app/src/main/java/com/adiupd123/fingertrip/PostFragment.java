package com.adiupd123.fingertrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentPostBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PostFragment extends DialogFragment {
    private FragmentPostBinding binding;
    private String postID, ownerID, curUserEmail, tempEmail;
    private boolean liked;
    private long likesCount, commentsCount;
    private ArrayList<String> likes;
    private HashMap<String, Object> post, personalInfoHashMap, socialInfoHashMap;
    private DatabaseReference databaseReference;
    public PostFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (HashMap<String, Object>) getArguments().getSerializable("post");
            curUserEmail = getArguments().getString("emailID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postID = post.get("postID").toString();
        ownerID = post.get("postOwnerID").toString();
        tempEmail = curUserEmail.replace('.',',');
        if (Boolean.TRUE.equals(post.get("liked"))) liked = true;
        else liked = false;
        likesCount = (Long) post.get("likesCount");

        Query query = databaseReference.child("users").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot emailSnapshot: snapshot.getChildren()){
                    String key = emailSnapshot.getKey();
                    if(key !=  null && ownerID != null){
                        if(ownerID.equals(key)){
                            personalInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("personal_info/").getValue();
                            socialInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("social_info/").getValue();
                            binding.userPostLayout.postUsernameTextView.setText("@" + personalInfoHashMap.get("username"));
                            binding.userPostLayout.postPersonNameTextView.setText(personalInfoHashMap.get("name").toString());
                            Glide.with(getContext())
                                    .load(socialInfoHashMap.get("profilePhoto"))
                                    .into(binding.userPostLayout.postUserPhotoImageView);
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
        Glide.with(this)
                        .load(post.get("postPhoto").toString())
                                .into(binding.userPostLayout.postImageView);
        binding.userPostLayout.postTimeTextView.setText(post.get("postTimeStamp").toString());
        binding.userPostLayout.postTitleTextView.setText(post.get("postTitle").toString());
        binding.userPostLayout.postDescTextView.setText(post.get("postDesc").toString());
        binding.userPostLayout.commentTextView.setText(post.get("commentsCount").toString());
        if(liked){
            likes = (ArrayList<String>) post.get("likes");
            for (String likedUser: likes){
                if(likedUser.equals(tempEmail)){
                    binding.userPostLayout.likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
                    break;
                }
            }
        }
        binding.userPostLayout.likeTextView.setText(String.valueOf(likesCount));

        binding.userPostLayout.likeImageView.setOnClickListener(click -> likeFunction());

        binding.userPostLayout.commentImageView.setOnClickListener(click -> commentFunction());

        binding.userPostLayout.postUserPhotoImageView.setOnClickListener(click -> openUserProfile());

    }

    private void commentFunction() {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle commentBundle = new Bundle();
        commentBundle.putString("emailID", curUserEmail);
        commentBundle.putString("postID", postID);
        commentBundle.putSerializable("post", post);
        commentsFragment.setArguments(commentBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, commentsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openUserProfile() {
        Bundle bundle = new Bundle();
        if(!ownerID.equals(curUserEmail.replace('.',','))){
            UserFragment userFragment = new UserFragment();
            bundle.putString("emailID", curUserEmail);
            bundle.putString("ownerID", ownerID);
            bundle.putSerializable("personalInfo", personalInfoHashMap);
            bundle.putSerializable("socialInfo", socialInfoHashMap);
            userFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, userFragment)
                    .addToBackStack(null)
                    .commit();
        } else{
            bundle.putString("emailID", ownerID);
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            userProfileFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, userProfileFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void likeFunction() {
        if(liked){
            int isFound = 0;
            for (String likedUser: likes){
                if(likedUser.equals(tempEmail)){
                    // It means user has liked already and the user has tapped to remove like
                    isFound = 1;
                    binding.userPostLayout.likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    likesCount--;
                    binding.userPostLayout.likeTextView.setText(String.valueOf(likesCount));
                    likes.remove(likedUser);
                    if(likesCount == 0){
                        liked = false;
                        post.put("liked", liked);
                    }
                    break;
                }
            }
            if(isFound == 0){
                binding.userPostLayout.likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
                likesCount++;
                binding.userPostLayout.likeTextView.setText(String.valueOf(likesCount));
                likes.add(tempEmail);
            }
        } else {
            likes = new ArrayList<>();
            likes.add(tempEmail);
            likesCount++;
            liked = true;
            binding.userPostLayout.likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
            binding.userPostLayout.likeTextView.setText(String.valueOf(likesCount));
            post.put("liked", liked);
        }
        post.put("likesCount", likesCount);
        post.put("likes", likes);
        databaseReference.child("posts/"+postID+"/").setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Like Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}