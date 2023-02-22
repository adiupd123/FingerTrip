package com.adiupd123.fingertrip.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentCommentsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class CommentsFragment extends Fragment {
    private FragmentCommentsBinding binding;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> post;
    private String postID, curUserEmail;
    private String commentID;
    private Long commentCount;
    public CommentsFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postID = getArguments().getString("postID");
            curUserEmail = getArguments().getString("emailID");
            post = (HashMap<String, Object>) getArguments().getSerializable("post");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Comment ID: postID_commenterID_timeOfComment
        databaseReference = FirebaseDatabase.getInstance().getReference();

        commentCount = (Long) post.get("commentsCount");
    }
}