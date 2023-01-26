package com.adiupd123.fingertrip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.content.AsyncTaskLoader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfileFragment extends Fragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    private FragmentUserProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;
    private String curUserEmail, tempEmail;
    private Fragment editProfileFragment;

    private Bundle userBundle;

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
//        mAuth = FirebaseAuth.getInstance();
//        rootNode = FirebaseDatabase.getInstance();
//        databaseReference = rootNode.getReference("users");

        userBundle = getArguments();
        if(userBundle != null){
            curUserEmail = userBundle.getString("emailID");
            personalInfoHashMap = (HashMap<String, Object>) userBundle.getSerializable("personalInfo");
            socialInfoHashMap = (HashMap<String, Object>) userBundle.getSerializable("socialInfo");
        }
        if(curUserEmail != null) {
            tempEmail = curUserEmail.replace('.', ',');
        }
        try{
            if(personalInfoHashMap != null && socialInfoHashMap != null){
                binding.usernameTextView.setText(personalInfoHashMap.get("username").toString());
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
            }
        } catch (Exception e){
            Log.d("UserProfileFragment", e.getMessage());
        }
        editProfileFragment = new EditProfileFragment();
        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfileFragment();
            }
        });

        binding.userProfileToolbar.inflateMenu(R.menu.user_profile_dropdown_items);
        binding.userProfileToolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.item1:
                    Toast.makeText(getActivity(), "You can now configure your Account", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item2:
                    Toast.makeText(getActivity(), "You are now in App Settings", Toast.LENGTH_SHORT).show();
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

        binding.createPostButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPostIntent = new Intent(getActivity(), CreatePostActivity.class);
                createPostIntent.putExtra("emailID", curUserEmail);
                startActivity(createPostIntent);
            }
        });

        binding.userProfileTabLayout.setupWithViewPager(binding.viewPager);

        UserPostsVPAdapter userPostsVPAdapter = new UserPostsVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        userPostsVPAdapter.addFragment(new UserPostsFragment(), "POSTS");
        userPostsVPAdapter.addFragment(new UserSavedPostsFragment(),"SAVED");
        binding.viewPager.setAdapter(userPostsVPAdapter);
    }

    public void openEditProfileFragment(){

        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .add(R.id.fragment_container_view, editProfileFragment, "find this fragment")
                .addToBackStack(null)
                .commit();
    }

    public void signOut(){
        mAuth.signOut();
        onDestroyView();
        startActivity(new Intent(getContext(), SignInActivity.class));
    }

    // This method is for viewing user's posts and saved posts.
    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener listener){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}