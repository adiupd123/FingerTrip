package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.adiupd123.fingertrip.databinding.FragmentUserSavedPostsBinding;

public class UserSavedPostsFragment extends Fragment {


    public UserSavedPostsFragment() {
        super(R.layout.fragment_user_saved_posts);
    }

    private FragmentUserSavedPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserSavedPostsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}