package com.adiupd123.fingertrip;

import android.content.AsyncQueryHandler;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentExploreBinding;
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


public class ExploreFragment extends Fragment {
    public ExploreFragment(){
        super(R.layout.fragment_explore);
    }
    private FragmentExploreBinding binding;
    private DatabaseReference databaseReference;
    private Bundle postsBundle;
    private String curUserEmail, tempEmail;
    private HashMap<String, Object> userInfo, socialInfo, postInfo;
    private List<HashMap<String, Object>> posts;
    private AllPostsRVAdapter adapter;
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
            databaseReference = FirebaseDatabase.getInstance().getReference("posts");
            Query query = databaseReference.orderByKey();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        if(key != null){
                            postInfo = (HashMap<String, Object>) dataSnapshot.getValue();
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
            adapter = new AllPostsRVAdapter(values[0]);
            binding.postsRecyclerView.setAdapter(adapter);
            binding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postsBundle = getArguments();
        if(postsBundle != null){
            postsAsyncTask = new PostsAsyncTask();
            postsAsyncTask.execute();
            curUserEmail = postsBundle.getString("emailID");
            socialInfo = (HashMap<String, Object>) postsBundle.getSerializable("socialInfo");
        }
        if(curUserEmail != null){
            tempEmail = curUserEmail.replace('.',',');
        }
        posts = new ArrayList<>();
    }
}