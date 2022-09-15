package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FirebaseAuth mAuth;

//    private TextView emailTextView;
//    private Button signOutButton;

    private Fragment homeFragment, exploreFragment, messagesFragment, userProfileFragment;
    private NavigationBarView navigationBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAuth = FirebaseAuth.getInstance();
//
//        emailTextView = findViewById(R.id.email_textView);
//        String email = mAuth.getCurrentUser().getEmail();
//        emailTextView.setText("User's Email Address: " + email);

//        signOutButton =  findViewById(R.id.signOut_button);
//        signOutButton.setOnClickListener(this);

        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        messagesFragment = new MessagesFragment();
        userProfileFragment = new UserProfileFragment();

        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigationBarView.setSelectedItemId(R.id.user_item);
        }
    }

//    @Override
//    public void onClick(View view) {
//        mAuth.signOut();
//        startActivity(new Intent(MainActivity.this, SignInActivity.class));
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_item:
                setFragment(homeFragment);
//                item.setIcon(R.drawable.ic_home_selected);
                break;
            case R.id.explore_item:
                setFragment(exploreFragment);
//                item.setIcon(R.drawable.ic_explore_selected);
                break;
            case R.id.messages_item:
                setFragment(messagesFragment);
//                item.setIcon(R.drawable.ic_messages_selected);
                break;
            case R.id.user_item:
                setFragment(userProfileFragment);
//                item.setIcon(R.drawable.ic_user_selected);
                break;
        }
        return true;
    }

    public void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit();
    }
}