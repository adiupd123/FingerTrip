package com.adiupd123.fingertrip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    private FragmentHomeBinding binding;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}