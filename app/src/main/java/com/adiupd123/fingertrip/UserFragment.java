package com.adiupd123.fingertrip;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.R.*;

import java.util.HashMap;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;

    public UserFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        binding.followButton.setOnClickListener(click -> {
            if(binding.followButton.getText().toString().equalsIgnoreCase("Follow"))
                binding.followButton.setText("Unfollow");
            else
                binding.followButton.setText("Follow");
        });
    }
}