package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserPostsBinding;
import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;

import java.util.ArrayList;


public class UserPostsFragment extends Fragment {

    public UserPostsFragment() {
        super(R.layout.fragment_user_posts);
    }

    private FragmentUserPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.postsGridView.setAdapter(new UserPostsGridAdapter(new ArrayList<>()));

    }
}