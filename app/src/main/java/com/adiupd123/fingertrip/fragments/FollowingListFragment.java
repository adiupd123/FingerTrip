package com.adiupd123.fingertrip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.adiupd123.fingertrip.databinding.FragmentFollowingListBinding;
import com.adiupd123.fingertrip.R;

public class FollowingListFragment extends Fragment {

    public FollowingListFragment() {
        // Required empty public constructor
    }

    private FragmentFollowingListBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFollowingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}