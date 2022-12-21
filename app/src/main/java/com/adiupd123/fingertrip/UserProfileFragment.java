package com.adiupd123.fingertrip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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


        setHasOptionsMenu(true);
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
//        binding.emailTextView.setText("User's Email Address: " + email);
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

//        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });


        binding.userProfileToolbar.inflateMenu(R.menu.user_profile_dropdown_items);
        binding.userProfileToolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.item1:
                    Toast.makeText(getActivity(), "You can now configure your Account", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item2:
                    Toast.makeText(getActivity(), "You are now in App Settings", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item3:
                    Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
                    startActivity(contactIntent);
                    return true;
                case R.id.item4:
                    signOut();
                    return true;
            }
            return false;
        });

        binding.createPostButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPostIntent = new Intent(getActivity(), CreatePostActivity.class);
                createPostIntent.putExtra("username", curUsername);
                startActivity(createPostIntent);
            }
        });




        binding.userProfileTabLayout.setupWithViewPager(binding.viewPager);

        UserPostsVPAdapter userPostsVPAdapter = new UserPostsVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        userPostsVPAdapter.addFragment(new UserPostsFragment(), "POSTS");
        userPostsVPAdapter.addFragment(new UserSavedPostsFragment(),"SAVED");
        binding.viewPager.setAdapter(userPostsVPAdapter);
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

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener listener){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}