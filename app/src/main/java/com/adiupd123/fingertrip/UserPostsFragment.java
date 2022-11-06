package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserPostsBinding;
import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;


public class UserPostsFragment extends Fragment {

    public UserPostsFragment() {
        super(R.layout.fragment_user_posts);
    }

    private FragmentUserPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserPostsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}