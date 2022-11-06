package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private Fragment homeFragment, exploreFragment, messagesFragment, userProfileFragment;
    private NavigationBarView navigationBarView;
    private String curUsername = "abcde12345";
    private Bundle bundle;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;
    private String curUseremail;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Bundle passing is working but attaching onClickListener to FAB is crashing the app
        * Reason: May be the problem is due to conflict cause by: the MainActivity that contains fragments
        * and opening of CreatePostActivity by MainActivity
        * */
        bundle = new Bundle();
        bundle.putString("username", curUsername);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        curUseremail = user.getEmail();

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("users");

        /*
        * Search the user using email retrieved from firebase auth
        * Retrieve the username and other details
        * and pass them to different fragments and
        * activities and make modifications in it
        * Learn Firebase working NoSQL structure
        * Make UI for FingerTrip App
        * */

        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        messagesFragment = new MessagesFragment();
        userProfileFragment = new UserProfileFragment();


        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent createPostIntent = new Intent(MainActivity.this, CreatePostActivity.class);
//                createPostIntent.putExtra("username", curUsername);
//            }
//        });

        if (savedInstanceState == null) {
            navigationBarView.setSelectedItemId(R.id.user_item);
        }
    }

    // Also enable custom icon to appear when a NavigationItem is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_item:
                setFragment(homeFragment);
                break;
            case R.id.explore_item:
                setFragment(exploreFragment);
                break;
            case R.id.messages_item:
                setFragment(messagesFragment);
                break;
            case R.id.user_item:
                setFragment(userProfileFragment);
                break;
        }
        return true;
    }

    public void setFragment(Fragment fragment){
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .addToBackStack(null)
                .commit();
    }
}