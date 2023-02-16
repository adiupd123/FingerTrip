package com.adiupd123.fingertrip;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentUserBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.R.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap, curUserSocialInfoHashMap;
    private String curUserEmail, tempEmail, ownerID;
    private ArrayList<String> ownerFollowers, curUserFollowing;
    private Long ownerFollowerCount, curUserFollowingCount;
    private CurUserDataTask task;
    private UpdateConnectionTask updateTask;
    public UserFragment() {}
    public class CurUserDataTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            databaseReference.child("users/"+curUserEmail.replace('.',',')+"/social_info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        curUserSocialInfoHashMap = (HashMap<String, Object>) snapshot.getValue();
                        if((Long) curUserSocialInfoHashMap.get("followingCount") == 0){
                            curUserFollowing = new ArrayList<>();
                        } else {
                            curUserFollowing = (ArrayList<String>) curUserSocialInfoHashMap.get("following");
                        }
                        curUserFollowingCount = (Long) curUserSocialInfoHashMap.get("followingCount");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("UserFragment.java", error.toException().getMessage());
                }
            });
            return null;
        }
    }
    public class UpdateConnectionTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            databaseReference.child("users/"+curUserEmail.replace('.',',')+"/social_info").setValue(curUserSocialInfoHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        databaseReference.child("users/"+ownerID.replace('.',',')+"/social_info").setValue(socialInfoHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(getContext(),"Connection success", Toast.LENGTH_SHORT).show();
                                else
                                    Log.d("UserFragment.java", task.getException().getMessage());
                            }
                        });
                    }
                    else {
                        Log.d("UserFragment.java", task.getException().getMessage());
                    }
                }
            });
            return null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curUserEmail = getArguments().getString("emailID");
            ownerID = getArguments().getString("ownerID");
            personalInfoHashMap = (HashMap<String, Object>) getArguments().getSerializable("personalInfo");
            socialInfoHashMap = (HashMap<String, Object>) getArguments().getSerializable("socialInfo");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        task = new CurUserDataTask();
        task.execute();
        binding.personNameTextView.setText(personalInfoHashMap.get("name").toString());
        Glide.with(getContext())
                .load(socialInfoHashMap.get("profileCover").toString())
                .placeholder(R.drawable.no_profile_background)
                .into(binding.profileCoverImageView);
        Glide.with(getContext())
                .load(socialInfoHashMap.get("profilePhoto").toString())
                .placeholder(R.drawable.ic_default_profile)
                .into(binding.profilePhotoImageView);
        binding.bioTextView.setText(socialInfoHashMap.get("bio").toString());
        binding.postsCountTextView.setText(socialInfoHashMap.get("postCount").toString());
        binding.followersCountTextView.setText(socialInfoHashMap.get("followerCount").toString());
        binding.followingCountTextView.setText(socialInfoHashMap.get("followingCount").toString());

        if((Long)socialInfoHashMap.get("followerCount") == 0){
            ownerFollowers = new ArrayList<String>();
        } else{
            ownerFollowers = (ArrayList<String>) socialInfoHashMap.get("followers");
            for(String follower: ownerFollowers){
                if(follower.equals(curUserEmail.replace('.',',')))
                    binding.followButton.setText("Unfollow");
                else
                    binding.followButton.setText("Follow");
            }
        }
        ownerFollowerCount = (Long) socialInfoHashMap.get("followerCount");
        binding.followButton.setOnClickListener(click -> {
            if(ownerID != null && curUserEmail != null){
                if(binding.followButton.getText().toString().equalsIgnoreCase("Follow")) {
                    binding.followButton.setText("Unfollow");
                    ownerFollowerCount++;
                    curUserFollowingCount++;
                    ownerFollowers.add(curUserEmail.replace('.',','));
                    curUserFollowing.add(ownerID.replace('.',','));
                }
                else{
                    binding.followButton.setText("Follow");
                    ownerFollowerCount--;
                    curUserFollowingCount--;
                    ownerFollowers.remove(curUserEmail.replace('.',','));
                    curUserFollowing.remove(ownerID.replace('.',','));
                }
                socialInfoHashMap.put("followers", ownerFollowers);
                socialInfoHashMap.put("followerCount", ownerFollowerCount);
                curUserSocialInfoHashMap.put("following", curUserFollowing);
                curUserSocialInfoHashMap.put("followingCount", curUserFollowingCount);
                updateTask = new UpdateConnectionTask();
                updateTask.execute();
            }
        });
    }
}