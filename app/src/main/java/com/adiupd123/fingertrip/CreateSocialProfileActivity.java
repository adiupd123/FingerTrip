package com.adiupd123.fingertrip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.ActivityCreateSocialProfileBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class CreateSocialProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String curUserEmail;
    private ActivityCreateSocialProfileBinding binding;
    private UserSocialProfileModel socialProfile;
    ActivityResultLauncher<Intent> profilePhotoPicker, profileCoverPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSocialProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if(intent != null)
            curUserEmail = intent.getStringExtra("emailID");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        socialProfile = new UserSocialProfileModel();

        profilePhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result) -> {
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                socialProfile.setProfilePhoto(uri.toString());
                binding.profilePhotoImageView.setImageURI(uri);
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                Toast.makeText(this, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        profileCoverPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result) -> {
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                socialProfile.setProfileCover(uri.toString());
                binding.profileCoverImageView.setImageURI(uri);
            }else if(result.getResultCode() == ImagePicker.RESULT_ERROR){
                // Show  error message
                Toast.makeText(this, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.profilePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateSocialProfileActivity.this)
                        .crop(1f, 1f)
                        .maxResultSize(1024, 1024,true)
                        .setMultipleAllowed(false)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                profilePhotoPicker.launch(it);
                            }
                        });
            }
        });
        binding.profileCoverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateSocialProfileActivity.this)
                        .crop(binding.profileCoverImageView.getWidth(), binding.profileCoverImageView.getHeight())
                        .setMultipleAllowed(false)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                profileCoverPicker.launch(it);
                            }
                        });
            }
        });
        binding.createSocialProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    socialProfile.setBio(binding.bioEditText.getText().toString());
                    socialProfile.setPostCount(0);
                    socialProfile.setFollowers(new ArrayList<>(0));
                    socialProfile.setFollowerCount(socialProfile.getFollowers().size());
                    socialProfile.setFollowing(new ArrayList<>(0));
                    socialProfile.setFollowingCount(socialProfile.getFollowing().size());
                    String tempEmail = curUserEmail.replace('.',',');
                    databaseReference.child(tempEmail+"/social_info").setValue(socialProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CreateSocialProfileActivity.this, "Your social info is saved!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateSocialProfileActivity.this, MainActivity.class);
                                intent.putExtra("emailID",curUserEmail);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(CreateSocialProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch(Exception e){
                    Toast.makeText(CreateSocialProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}