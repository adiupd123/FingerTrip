package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentCommentsBinding;


public class CommentsFragment extends Fragment {
    private FragmentCommentsBinding binding;
    public CommentsFragment() {
        // Required empty public constructor
    }
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
        binding = FragmentCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}