package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText emailEditText, passwordEditText;
    private Button createNewAccountButton, signInButton, forgotPasswordButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);

        signInButton = findViewById(R.id.signIn_button);
        createNewAccountButton = findViewById(R.id.createNewAccount_button);
        forgotPasswordButton = findViewById(R.id.forgotPass_button);

        progressBar = findViewById((R.id.progressBar0));

        signInButton.setOnClickListener(this);
        createNewAccountButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.createNewAccount_button:
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                break;

            case R.id.signIn_button:
                signInUser();
                break;
            case R.id.forgotPass_button:
                resetUserPassword();
                break;

        }
    }
    private void signInUser(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is Invalid!");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordEditText.setError("Password should be atleast 6 characters long!");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetUserPassword(){
        startActivity(new Intent(MainActivity.this, PasswordResetActivity.class));
    }
}