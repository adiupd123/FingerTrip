package com.adiupd123.fingertrip.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.R;
import com.adiupd123.fingertrip.adapters.AllPostsRVAdapter;
import com.adiupd123.fingertrip.adapters.UsersRVAdapter;
import com.adiupd123.fingertrip.databinding.FragmentExploreBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExploreFragment extends Fragment {
    public ExploreFragment(){
        super(R.layout.fragment_explore);
    }
    private FragmentExploreBinding binding;
    private DatabaseReference databaseReference;
    private Bundle postsBundle;
    private String curUserEmail, tempEmail;
    private HashMap<String, Object> personalInfo, socialInfo, postInfo;
    private ArrayList<HashMap<String, Object>> posts, searchedUsers;
    private AllPostsRVAdapter adapter;
    private UsersRVAdapter searchRVAdapter;
    private PostsAsyncTask postsAsyncTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    public class PostsAsyncTask extends AsyncTask<Void, List<HashMap<String, Object>>, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            Query query = databaseReference.child("posts").orderByKey();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        if(key != null){
                            postInfo = (HashMap<String, Object>) dataSnapshot.getValue();
                            if(!postInfo.get("postOwnerID").equals(tempEmail))
                                posts.add(postInfo);
                        }
                    }
                    onProgressUpdate(posts);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserPostsFragment.java", error.toException().getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(List<HashMap<String, Object>>... values) {
            super.onProgressUpdate(values);
            adapter = new AllPostsRVAdapter(values[0], getContext());
            binding.postsRecyclerView.setAdapter(adapter);
            binding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postsBundle = getArguments();
        posts = new ArrayList<>();
        if(savedInstanceState == null){
            if(postsBundle != null){
                curUserEmail = postsBundle.getString("emailID");
                socialInfo = (HashMap<String, Object>) postsBundle.getSerializable("socialInfo");
                personalInfo = (HashMap<String, Object>) postsBundle.getSerializable("personalInfo");
            }
        } else{
            posts = (ArrayList<HashMap<String, Object>>) savedInstanceState.getSerializable("posts");
            socialInfo = (HashMap<String, Object>) savedInstanceState.getSerializable("socialInfo");
            personalInfo = (HashMap<String, Object>) savedInstanceState.getSerializable("personalInfo");
            curUserEmail = savedInstanceState.getString("emailID");
        }
        if(curUserEmail != null){
            tempEmail = curUserEmail.replace('.',',');
        }
        postsAsyncTask = new PostsAsyncTask();
        postsAsyncTask.execute();


        binding.searchUsersEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchedUsername = binding.searchUsersEditText.getText().toString();
                if(!searchedUsername.equals("")){
                    binding.searchedUsersRecyclerView.setVisibility(View.VISIBLE);
                    Query query  = databaseReference.child("users")
                            .orderByChild("personal_info/username");
                    searchedUsers = new ArrayList<>();
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                // This loop will iterate over all users whose username matches the search query
                                if (
                                        userSnapshot.child("personal_info/username").getValue().toString().startsWith(searchedUsername.toLowerCase())
                                                && !userSnapshot.getKey().equals(tempEmail)
                                ) {
                                    searchedUsers.add((HashMap<String, Object>) userSnapshot.getValue());
                                }
                                // You can access other fields of the user using userSnapshot.child("fieldName").getValue() method
                            }
                            searchRVAdapter = new UsersRVAdapter(getContext(), searchedUsers);
                            binding.searchedUsersRecyclerView.setAdapter(searchRVAdapter);
                            binding.searchedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ExploreFragment.java", "Database Error: " + error.getMessage());
                        }
                    });
                } else{
                    searchedUsers.clear();
                    binding.searchedUsersRecyclerView.removeAllViewsInLayout();
                    binding.searchedUsersRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("posts", posts);
        outState.putString("emailID", curUserEmail);
        outState.putSerializable("socialInfo", socialInfo);
        outState.putSerializable("personalInfo", personalInfo);
    }
}