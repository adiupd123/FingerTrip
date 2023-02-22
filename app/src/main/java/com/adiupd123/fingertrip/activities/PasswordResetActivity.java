package com.adiupd123.fingertrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adiupd123.fingertrip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText pwdResetEmailEditText;
    private Button pwdResetButton, pwdResetSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAuth = FirebaseAuth.getInstance();

        pwdResetEmailEditText = findViewById(R.id.pwdResetEmail_editText);
        pwdResetButton = findViewById(R.id.pwdReset_button);
        pwdResetSignInButton = findViewById(R.id.pwdResetSignIn_button);

        pwdResetButton.setOnClickListener(this);
        pwdResetSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pwdReset_button:
                String email = pwdResetEmailEditText.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(PasswordResetActivity.this, "Password reset email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(PasswordResetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.pwdResetSignIn_button:
                startActivity(new Intent(PasswordResetActivity.this, SignInActivity.class));
                break;
        }
    }
}