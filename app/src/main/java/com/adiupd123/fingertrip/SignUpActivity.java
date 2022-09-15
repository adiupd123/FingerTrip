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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText emailIDEditText, newPasswordEditText;
    private Button signUpButton, logInButton;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailIDEditText = findViewById(R.id.emailID_editText);
        newPasswordEditText = findViewById(R.id.newPassword_editText);

        signUpButton = findViewById(R.id.signUp_button);
        logInButton = findViewById(R.id.logIn_button);

        progressBar = findViewById(R.id.progressBar1);

        signUpButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.signUp_button:
                registerUser();
                break;
            case R.id.logIn_button:
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }

    private void registerUser(){
        String emailID = emailIDEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if(emailID.isEmpty()){
            emailIDEditText.setError("Email is Required!");
            emailIDEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()){
            emailIDEditText.setError("Email is Invalid!");
            emailIDEditText.requestFocus();
            return;
        }
        if(newPassword.isEmpty()){
            newPasswordEditText.setError("Password is Required!");
            newPasswordEditText.requestFocus();
            return;
        }
        if(newPassword.length()<6){
            newPasswordEditText.setError("Password should be atleast 6 characters long.");
            newPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailID, newPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this, "You're already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}