package com.adiupd123.fingertrip.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.R;
import com.adiupd123.fingertrip.viewmodels.UserViewModel;
import com.adiupd123.fingertrip.activities.ContactActivity;
import com.adiupd123.fingertrip.activities.SignInActivity;
import com.adiupd123.fingertrip.adapters.AllPostsRVAdapter;
import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileFragment extends Fragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    private FragmentUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap, postInfo;
    private ArrayList<HashMap<String, Object>> userPosts;
    private AllPostsRVAdapter adapter;
    private UserProfilePostsAsyncTask userProfilePostsAsyncTask;
    private String curUserEmail, tempEmail;
    private Fragment editProfileFragment, createPostFragment, followerListFragment, followingListFragment;
    private Bundle userBundle;

    private UserViewModel userViewModel;
    private Observer<HashMap<String, Object>> personalInfoObserver, socialInfoObserver;
    private Observer<ArrayList<HashMap<String, Object>>> postsObserver;
    private boolean isObservingData = false;
    private MyAsyncTask asyncTask;
    private class MyAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Query query = databaseReference.child("users").orderByKey();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot emailSnapshot: snapshot.getChildren()){
                        String key = emailSnapshot.getKey();
                        if(key !=  null && tempEmail != null){
                            if(tempEmail.equals(key)){
                                personalInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("personal_info/").getValue();
                                socialInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("social_info/").getValue();
                                userViewModel.setUserPersonalData(personalInfoHashMap);
                                userViewModel.setUserSocialData(socialInfoHashMap);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("MainActivity.class", error.toException().toString());
                }

            });
            return null;
        }
    }
    public class UserProfilePostsAsyncTask extends AsyncTask<Void, List<HashMap<String, Object>>, Void>{
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
                            if(postInfo.get("postOwnerID").equals(tempEmail))
                                userPosts.add(postInfo);
                        }
                    }
                    Collections.sort(userPosts, new Comparator<Map<String, Object>>() {
                        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                            String time1 = o1.get("postTimeStamp").toString();
                            String time2 = o2.get("postTimeStamp").toString();
                            DateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");
                            try {
                                Date date1 = format.parse(time1);
                                Date date2 = format.parse(time2);
                                return date2.compareTo(date1); // sort in descending order
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });
                    userViewModel.setUserPostsData(userPosts);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserPostsFragment.java", error.toException().getMessage());
                }
            });
            return null;
        }
    }
    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userPosts = new ArrayList<>();
        userBundle = getArguments();
        if(mAuth != null){
            if(userBundle != null){
                curUserEmail = userBundle.getString("emailID");
            }
            if(curUserEmail != null) {
                tempEmail = curUserEmail.replace('.', ',');
                asyncTask = new MyAsyncTask();
                asyncTask.execute();
                userProfilePostsAsyncTask = new UserProfilePostsAsyncTask();
                userProfilePostsAsyncTask.execute();
            }
            try{
                personalInfoObserver = new Observer<HashMap<String, Object>>() {
                    @Override
                    public void onChanged(HashMap<String, Object> personalInfoHashMap) {
                        binding.usernameTextView.setText(personalInfoHashMap.get("username").toString());
                        binding.personNameTextView.setText(personalInfoHashMap.get("name").toString());
                    }
                };
                socialInfoObserver = new Observer<HashMap<String, Object>>() {
                    @Override
                    public void onChanged(HashMap<String, Object> socialInfoHashMap) {
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

                        binding.followersCountTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle followerListBundle = new Bundle();
                                followerListBundle.putSerializable("followerList", (Serializable) socialInfoHashMap.get("followers"));
                                followerListFragment = new FollowerListFragment();
                                followerListFragment.setArguments(followerListBundle);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container_view, followerListFragment)
                                        .addToBackStack("Current:FollowerListFragment")
                                        .commit();
                            }
                        });
                    }
                };
                postsObserver = new Observer<ArrayList<HashMap<String, Object>>>() {
                    @Override
                    public void onChanged(ArrayList<HashMap<String, Object>> postsData) {
                        adapter = new AllPostsRVAdapter(postsData, getContext());
                        binding.userProfilePostsRecyclerView.setAdapter(adapter);
                        binding.userProfilePostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    }
                };
            } catch (Exception e){
                Log.d("UserProfileFragment", e.getMessage());
            }
        }
        binding.userProfileToolbar.inflateMenu(R.menu.user_profile_dropdown_items);
        binding.userProfileToolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.item1:
                    Toast.makeText(getActivity(), "You can now configure your Account", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item2:
                    openEditProfileFragment();
                    return true;
                case R.id.item3:
                    Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
                    startActivity(contactIntent);
                    return true;
                case R.id.item4:
                    signOut();
                    return true;
            }
            return false;
        });

        binding.createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreatePostFragment();
            }
        });
        getActivity().findViewById(R.id.fab).setOnClickListener(click -> openCreatePostFragment());
    }

    private void openCreatePostFragment() {
        createPostFragment = new CreatePostFragment();
        userBundle.putSerializable("personalInfo", personalInfoHashMap);
        userBundle.putSerializable("socialInfo", socialInfoHashMap);
        createPostFragment.setArguments(userBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, createPostFragment)
                .addToBackStack("Current:CreatePostFragment")
                .commit();
    }

    public void openEditProfileFragment(){
        editProfileFragment = new EditProfileFragment();
        userBundle.putSerializable("personalInfo", personalInfoHashMap);
        userBundle.putSerializable("socialInfo", socialInfoHashMap);
        editProfileFragment.setArguments(userBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, editProfileFragment)
                .addToBackStack("Current:EditProfileFragment")
                .commit();
    }

    public void signOut(){
        mAuth.signOut();
        startActivity(new Intent(getActivity(), SignInActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isObservingData){
            userViewModel.getUserPersonalData().observe(getViewLifecycleOwner(), personalInfoObserver);
            userViewModel.getUserSocialData().observe(getViewLifecycleOwner(), socialInfoObserver);
            userViewModel.getUserPostsData().observe(getViewLifecycleOwner(), postsObserver);
            isObservingData = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isObservingData){
            userViewModel.getUserPersonalData().removeObservers(getViewLifecycleOwner());
            userViewModel.getUserSocialData().removeObservers(getViewLifecycleOwner());
            userViewModel.getUserPostsData().removeObservers(getViewLifecycleOwner());
            isObservingData = false;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}