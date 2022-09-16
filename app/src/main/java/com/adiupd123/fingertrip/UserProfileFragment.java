package com.adiupd123.fingertrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public UserProfileFragment(){
        super(R.layout.fragment_user_profile);
    }

    private FirebaseAuth mAuth;

    private TextView emailTextView;
    private Button signOutButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = view.findViewById(R.id.email_textView);
        String email = mAuth.getCurrentUser().getEmail();
        emailTextView.setText("User's Email Address: " + email);

        signOutButton =  view.findViewById(R.id.signOut_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                onDestroyView();
                startActivity(new Intent(getContext(), SignInActivity.class));
            }
        });
    }
}