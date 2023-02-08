package com.adiupd123.fingertrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
    private Fragment editProfileFragment, createPostFragment;
    private Bundle userBundle;

    private UserViewModel userViewModel;


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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        UserPostsFragment userPostsFragment = new UserPostsFragment();
        UserSavedPostsFragment userSavedPostsFragment = new UserSavedPostsFragment();
        userBundle = getArguments();
        if(userBundle != null){
            curUserEmail = userBundle.getString("emailID");
            userViewModel.setUserPersonalData((HashMap<String, Object>) userBundle.getSerializable("personalInfo"));
            userViewModel.setUserSocialData((HashMap<String, Object>) userBundle.getSerializable("socialInfo"));
            userPostsFragment.setArguments(userBundle);
            userSavedPostsFragment.setArguments(userBundle);
        }
        if(curUserEmail != null) {
            tempEmail = curUserEmail.replace('.', ',');
        }
        try{
            userViewModel.getUserPersonalData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
                @Override
                public void onChanged(HashMap<String, Object> personalInfoHashMap) {
                    binding.usernameTextView.setText(personalInfoHashMap.get("username").toString());
                    binding.personNameTextView.setText(personalInfoHashMap.get("name").toString());
                }
            });
            userViewModel.getUserSocialData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
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
                }
            });
            binding.userProfileTabLayout.setupWithViewPager(binding.viewPager);
            UserPostsVPAdapter userPostsVPAdapter = new UserPostsVPAdapter(
                    getActivity().getSupportFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            userPostsVPAdapter.addFragment(userPostsFragment, "POSTS");
            userPostsVPAdapter.addFragment(userSavedPostsFragment,"SAVED_POSTS");
            binding.viewPager.setAdapter(userPostsVPAdapter);
        } catch (Exception e){
            Log.d("UserProfileFragment", e.getMessage());
        }
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

        binding.createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreatePostFragment();
            }
        });



    }

    private void openCreatePostFragment() {
        createPostFragment = new CreatePostFragment();
        createPostFragment.setArguments(userBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, createPostFragment)
                .addToBackStack("Current:CreatePostFragment")
                .commit();
    }

    public void openEditProfileFragment(){
        editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(userBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, editProfileFragment)
                .addToBackStack("Current:EditProfileFragment")
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