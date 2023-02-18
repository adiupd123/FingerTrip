package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    private Fragment homeFragment, exploreFragment, messagesFragment, userProfileFragment, createPostFragment;
    private NavigationBarView navigationBarView;
    private String curUserEmail = "Null EmailID", tempEmail;
//    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;
//    private MyAsyncTask asyncTask;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
//        rootNode = FirebaseDatabase.getInstance();
//        databaseReference = rootNode.getReference("users");

        Intent intent = getIntent();
        if(intent != null)
            curUserEmail = intent.getStringExtra("emailID");
        if(curUserEmail != null) {
            tempEmail = curUserEmail.replace('.', ',');
        }
        bundle = new Bundle();
        bundle.putString("emailID", curUserEmail);
//        asyncTask = new MyAsyncTask();
//        asyncTask.execute();

        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        messagesFragment = new MessagesFragment();
        userProfileFragment = new UserProfileFragment();

        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.user_item);
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
                .addToBackStack("Current:"+fragment)
                .commit();
    }
}