package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserPostsBinding;
import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.adiupd123.fingertrip.models.CreatePostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserPostsFragment extends Fragment {

    public UserPostsFragment() {
        super(R.layout.fragment_user_posts);
    }

    DatabaseReference databaseReference;
    private FragmentUserPostsBinding binding;
    private Bundle postsBundle;
    private String curUserEmail, tempEmail;
    private HashMap<String, Object> userInfo, socialInfo;
    private CreatePostModel postInfo;
    private ArrayList<CreatePostModel> userPosts;
    private UserPostsRVAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postsBundle = getArguments();
        if(postsBundle != null){
            socialInfo = (HashMap<String, Object>) postsBundle.getSerializable("socialInfo");
            curUserEmail = postsBundle.getString("emailID");
        }
        if(curUserEmail != null){
            tempEmail = curUserEmail.replace('.',',');
        }
        userPosts = new ArrayList<>();
        postInfo = new CreatePostModel();
        if(tempEmail != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("posts");
            Query query = databaseReference.orderByKey();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        if(key != null && tempEmail != null){
                            if(key.startsWith(tempEmail)){
//                            String time = key.substring(tempEmail.length()+1);
                                postInfo = (CreatePostModel) dataSnapshot.child(key).getValue();
                                userPosts.add(postInfo);
                            }
                        }
                    }
                    adapter = new UserPostsRVAdapter(userPosts);
                    binding.userPostsRecyclerView.setAdapter(adapter);
                    binding.userPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserPostsFragment.java", error.toException().getMessage());
                }
            });
        }
    }
}