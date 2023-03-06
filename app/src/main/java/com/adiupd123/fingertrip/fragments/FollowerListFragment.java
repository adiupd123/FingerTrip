package com.adiupd123.fingertrip.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.R;
import com.adiupd123.fingertrip.databinding.FragmentFollowerListBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowerListFragment extends Fragment {

    public FollowerListFragment() {}

    private FragmentFollowerListBinding binding;
    private ArrayList<HashMap<String, Object>> followers;
    private ArrayList<String> followersIDs;
    public class FollowerAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            followersIDs = (ArrayList<String>) getArguments().get("followerList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowerListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}