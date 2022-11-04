package com.adiupd123.fingertrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

public class UserProfileFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public final String TAG = "UserProfileFragment";

    private FragmentUserProfileBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;
    private String curUsername;

    private Fragment editProfileFragment;

    private Bundle userBundle;

    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();


        userBundle = getArguments();
        if(userBundle!=null){
            curUsername = userBundle.getString("username");
        }

        binding.personNameTextView.setText("Username: " + curUsername);

        String email = currentUser.getEmail();
        binding.emailTextView.setText("User's Email Address: " + email);
//        databaseReference = rootNode.getReference("users/"+curUsername);
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get current user object and use the values to update the UI
//                UserHelperClass currentUser = dataSnapshot.getValue(UserHelperClass.class);
//                binding.personNameTextView.setText(currentUser.getName());
//                //..
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        databaseReference.addValueEventListener(postListener);


        editProfileFragment = new EditProfileFragment();
        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfileFragment();
            }
        });
        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void openEditProfileFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .add(R.id.fragment_container_view, editProfileFragment, "find this fragment")
                .addToBackStack(null)
                .commit();
    }

    public void signOut(){
        mAuth.signOut();
        onDestroyView();
        startActivity(new Intent(getContext(), SignInActivity.class));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}