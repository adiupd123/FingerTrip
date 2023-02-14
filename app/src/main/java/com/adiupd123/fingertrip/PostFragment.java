package com.adiupd123.fingertrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentExploreBinding;
import com.adiupd123.fingertrip.databinding.FragmentPostBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostFragment extends DialogFragment {
    private FragmentPostBinding binding;
    private HashMap<String, Object> post;
    private String postID, ownerID;
    private int likeCount, commentCount;
    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;
    private DatabaseReference databaseReference;
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
        ownerID = post.get("postOwnerID").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("users").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot emailSnapshot: snapshot.getChildren()){
                    String key = emailSnapshot.getKey();
                    if(key !=  null && ownerID != null){
                        if(ownerID.equals(key)){
                            personalInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("personal_info/").getValue();
                            socialInfoHashMap = (HashMap<String, Object>) emailSnapshot.child("social_info/").getValue();
                            binding.userPostLayout.postUsernameTextView.setText("@" + personalInfoHashMap.get("username"));
                            binding.userPostLayout.postPersonNameTextView.setText(personalInfoHashMap.get("name").toString());
                            Glide.with(getContext())
                                    .load(socialInfoHashMap.get("profilePhoto"))
                                    .into(binding.userPostLayout.postUserPhotoImageView);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PostFragment.class", error.toException().toString());
            }

        });
        Glide.with(this)
                        .load(post.get("postPhoto").toString())
                                .into(binding.userPostLayout.postImageView);
        binding.userPostLayout.postTimeTextView.setText(post.get("postTimeStamp").toString());
        binding.userPostLayout.likeTextView.setText(post.get("likesCount").toString());
        binding.userPostLayout.commentTextView.setText(post.get("commentsCount").toString());
        binding.userPostLayout.postTitleTextView.setText(post.get("postTitle").toString());
        binding.userPostLayout.postDescTextView.setText(post.get("postDesc").toString());



        binding.userPostLayout.postUserPhotoImageView.setOnClickListener(click -> {
            UserFragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("emailID", ownerID);
            bundle.putSerializable("personalInfo", personalInfoHashMap);
            bundle.putSerializable("socialInfo", socialInfoHashMap);
            userFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, userFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}