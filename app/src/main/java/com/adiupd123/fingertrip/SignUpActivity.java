package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SignUpActivity.class";
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;

    private EditText emailIDEditText, usernameEditText, newPasswordEditText, nameEditText, birthEditText, mobileEditText, reenterPassEditText;
    private RadioGroup genderRadioGroup;
    private Button signUpButton, logInButton;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("users");

        emailIDEditText = findViewById(R.id.emailID_editText);
        newPasswordEditText = findViewById(R.id.newPassword_editText);

        nameEditText = findViewById(R.id.name_editText);
        birthEditText = findViewById(R.id.birthday_editText);
        genderRadioGroup = findViewById(R.id.gender_radioGroup);
        usernameEditText = findViewById(R.id.username_editText);
        mobileEditText = findViewById(R.id.mobile_editText);
        reenterPassEditText = findViewById(R.id.reenterPass_editText);

        signUpButton = findViewById(R.id.signUp_button);
        logInButton = findViewById(R.id.logIn_button);

        progressBar = findViewById(R.id.progressBar1);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Username should be unique that is, usernames of two users with different accounts(email IDs) must be different.
                String curUsername = usernameEditText.getText().toString().trim();
                if(curUsername.isEmpty()){
                    usernameEditText.setError("Username is Required!");
                    usernameEditText.requestFocus();
                    return;
                }
                if(curUsername.length()>20){
                    usernameEditText.setError("Max. length of username: 20");
                    usernameEditText.requestFocus();
                    return;
                }
                if(curUsername.length()<6){
                    usernameEditText.setError("Min. length of username: 6");
                    usernameEditText.requestFocus();
                    return;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        usernameEditText.addTextChangedListener(textWatcher);


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
        String name = nameEditText.getText().toString().trim();
        String birthday = birthEditText.getText().toString().trim();
        String mobileNo = mobileEditText.getText().toString().trim();
        String gender = getGenderRadioOption(genderRadioGroup);
        String emailID = emailIDEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String reNewPassword = reenterPassEditText.getText().toString().trim();

        if(name.isEmpty()){
            nameEditText.setError("Name is Required!");
            nameEditText.requestFocus();
            return;
        }
        if(birthday.isEmpty()){
            birthEditText.setError("Birthday is Required!");
            birthEditText.requestFocus();
            return;
        }
        if(mobileNo.isEmpty()){
            mobileEditText.setError("Mobile no. is Required!");
            mobileEditText.requestFocus();
            return;
        }
        if(gender.isEmpty()){
            genderRadioGroup.requestFocus();
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if(emailID.isEmpty()){
            // Also check whether the emailID is present in the database already or not
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
            newPasswordEditText.setError("Min. length of Password: 6");
            newPasswordEditText.requestFocus();
            return;
        }
        if(newPassword.length()>30){
            newPasswordEditText.setError("Min. length of Password: 6");
            newPasswordEditText.requestFocus();
            return;
        }
        if(!newPassword.equals(reNewPassword)){
            reenterPassEditText.setError("Re-entered password does not match original password");
            reenterPassEditText.requestFocus();
            return;
        }

        UserHelperClass user = new UserHelperClass(name, birthday, emailID, username, mobileNo, gender);

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailID, newPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            // Here, emailID is the unique identifier - Done
                            // Normal emailID is used as Key by encoding '.' as ','
                            String tempEmail = emailID.replace('.', ',');
                            Query query = databaseReference.orderByChild("personal_info/username").equalTo(username);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        Log.i("SignUpActivity.class","Unique Username:" + username);
                                        databaseReference.child(tempEmail+"/personal_info").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignUpActivity.this, "Your account is created.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        // Passing Username and Password to MainActivity
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        intent.putExtra("emailID", emailID);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else {
                                        deleteUser(username);
                                        usernameEditText.setError("This username already exists");
                                        usernameEditText.requestFocus();
                                        return;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(SignUpActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
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

    private void deleteUser(String username) {
        mAuth.getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Invalid User with redundant username:"+username, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(SignUpActivity.this, "Deletion of Invalid User failed: Use a different username", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getGenderRadioOption(RadioGroup radioGroup){
        switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.male_option:return "Male";
            case R.id.female_option:return "Female";
            case R.id.others_option:return "Others";
            case R.id.not_option:return "Prefer not to say";
        }
        return "";
    }
}