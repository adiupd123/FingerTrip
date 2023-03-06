package com.adiupd123.fingertrip.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.R;
import com.adiupd123.fingertrip.adapters.AllPostsRVAdapter;
import com.adiupd123.fingertrip.databinding.FragmentUserBinding;
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
import java.util.List;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap, curUserSocialInfoHashMap, postInfo;
    private ArrayList<HashMap<String, Object>> userPosts;
    private AllPostsRVAdapter adapter;
    private UserPostsAsyncTask userPostsAsyncTask;
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
    public class UserPostsAsyncTask extends AsyncTask<Void, List<HashMap<String, Object>>, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Query query = databaseReference.child("posts").orderByKey();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        if(key != null){
                            postInfo = (HashMap<String, Object>) dataSnapshot.getValue();
                            if(postInfo.get("postOwnerID").equals(ownerID.replace('.',',')))
                                userPosts.add(postInfo);
                        }
                    }
                    onProgressUpdate(userPosts);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserPostsFragment.java", error.toException().getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(List<HashMap<String, Object>>... values) {
            super.onProgressUpdate(values);
            if(!values[0].equals(null)){
                adapter = new AllPostsRVAdapter(values[0], getContext());
                binding.userPostsRecyclerView.setAdapter(adapter);
                binding.userPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            }
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
        if (socialInfoHashMap.get("profileCover") != null){
            Glide.with(getContext())
                    .load(socialInfoHashMap.get("profileCover").toString())
                    .placeholder(R.drawable.no_profile_background)
                    .into(binding.profileCoverImageView);
        }
        if(socialInfoHashMap.get("profilePhoto") != null){
            Glide.with(getContext())
                    .load(socialInfoHashMap.get("profilePhoto").toString())
                    .placeholder(R.drawable.ic_default_profile)
                    .into(binding.profilePhotoImageView);
        }
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

        binding.followButton.setOnClickListener(click -> followFunction());

        userPosts = new ArrayList<>();
        userPostsAsyncTask = new UserPostsAsyncTask();
        userPostsAsyncTask.execute();
    }

    private void followFunction() {
        if(ownerID != null && curUserEmail != null && !ownerID.equals(curUserEmail)){
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
    }

}