package com.adiupd123.fingertrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private TextView emailTextView;
    private Button signOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email_textView);
        String email = mAuth.getCurrentUser().getEmail();
        emailTextView.setText("User's Email Address: " + email);

        signOutButton = findViewById(R.id.signOut_button);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mAuth.signOut();
        startActivity(new Intent(UserProfileActivity.this, MainActivity.class ));
    }
}