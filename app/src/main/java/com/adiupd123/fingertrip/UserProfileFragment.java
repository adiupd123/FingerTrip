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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public UserProfileFragment(){
        super(R.layout.fragment_user_profile);
    }

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;

    private TextView emailTextView, nameTextView;
    private Button editProfileButton, signOutButton;
    private Fragment editProfileFragment;

    private int followers, following, posts;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("users");
        currentUser = mAuth.getCurrentUser();

        editProfileFragment = new EditProfileFragment();

        nameTextView = view.findViewById(R.id.personName_textView);
//        nameTextView.setText(currentUser.getDisplayName());

        editProfileButton = view.findViewById(R.id.editProfile_button);
        editProfileButton.setOnClickListener(this);

        emailTextView = view.findViewById(R.id.email_textView);
        String email = mAuth.getCurrentUser().getEmail();
        emailTextView.setText("User's Email Address: " + email);

        signOutButton =  view.findViewById(R.id.signOut_button);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signOut_button:
                mAuth.signOut();
                onDestroyView();
                startActivity(new Intent(getContext(), SignInActivity.class));
                break;
            case R.id.editProfile_button:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .add(R.id.fragment_container_view, editProfileFragment, "find this fragment")
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}