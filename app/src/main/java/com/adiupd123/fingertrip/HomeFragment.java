package com.adiupd123.fingertrip;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.fragment_home);
    }
    private FragmentHomeBinding binding;
    private DatabaseReference databaseReference;
    private String curUserEmail, tempEmail;
    private HashMap<String, Object> postInfo, curUserSocialInfo;
    private List<String> following;
    private List<HashMap<String, Object>> homeFeedPosts;
    private HomeFeedRVAdapter adapter;
    private HomeFeedAsyncTask asyncTask;

    public class HomeFeedAsyncTask extends AsyncTask<Void, List<HashMap<String, Object>>, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Query query = databaseReference.child("posts").orderByKey();
            homeFeedPosts = new ArrayList<>();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        if(key != null){
                            postInfo = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.d("Aditya", postInfo.toString());
                            if(!postInfo.get("postOwnerID").equals(tempEmail)){
                                // All the posts by the users that current user follows and those that have been done in the last 24 hours
                                DateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
                                try {
                                    Date postDateTime = simpleDateFormat.parse(postInfo.get("postTimeStamp").toString());
                                    Date currentTime = Calendar.getInstance().getTime();
                                    Date currentDateTime = simpleDateFormat.parse(simpleDateFormat.format(currentTime));
                                    long t1 = postDateTime.getTime();
                                    long t2 = currentDateTime.getTime();
                                    long diffHour = ((t2-t1)/3600000);
                                    if(diffHour < 24L){
                                        for(String followingUser: following){
                                            String postOwnerID = postInfo.get("postOwnerID").toString();
                                            if(postOwnerID.equals(followingUser)) {
                                                homeFeedPosts.add(postInfo);
                                            }
                                        }
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    onProgressUpdate(homeFeedPosts);
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
            adapter = new HomeFeedRVAdapter(values[0], getContext());
            binding.homefeedRecyclerView.setAdapter(adapter);
            binding.homefeedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curUserEmail = getArguments().getString("emailID");
        }
        if(curUserEmail != null)
            tempEmail = curUserEmail.replace('.',',');
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        asyncTask = new HomeFeedAsyncTask();
        following = new ArrayList<>();
        databaseReference.child("users/" + tempEmail + "/social_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    curUserSocialInfo = (HashMap<String, Object>) snapshot.getValue();
                    if(curUserSocialInfo != null){
                        if((Long) curUserSocialInfo.get("followingCount") > 0){
                            following = (List<String>) curUserSocialInfo.get("following");
                            asyncTask.execute();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HomeFragment.java", error.toException().getMessage());
            }
        });
    }
}