package com.adiupd123.fingertrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentExploreBinding;
import com.adiupd123.fingertrip.databinding.FragmentPostBinding;
import com.bumptech.glide.Glide;

import java.util.HashMap;

public class PostFragment extends DialogFragment {
    private FragmentPostBinding binding;
    private HashMap<String, Object> post;
    private String postID, ownerID, postPhoto, postTime, ownerName, postTitle, postDesc;
    private int likeCount, commentCount;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (HashMap<String, Object>) getArguments().getSerializable("post");
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
        binding.userPostLayout.postUsernameTextView.setText("@" + post.get("postOwnerID").toString());
        Glide.with(this)
                        .load(post.get("postPhoto").toString())
                                .into(binding.userPostLayout.postImageView);
        binding.userPostLayout.postTimeTextView.setText(post.get("postTimeStamp").toString());
        binding.userPostLayout.likeTextView.setText(post.get("likesCount").toString());
        binding.userPostLayout.commentTextView.setText(post.get("commentsCount").toString());
        binding.userPostLayout.postTitleTextView.setText(post.get("postTitle").toString());
        binding.userPostLayout.postDescTextView.setText(post.get("postDesc").toString());
    }
}