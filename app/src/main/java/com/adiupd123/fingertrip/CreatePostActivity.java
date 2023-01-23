package com.adiupd123.fingertrip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.ActivityCreatePostBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class CreatePostActivity extends AppCompatActivity {

    private CreatePostViewModel postViewModel;
    private String postTitle, postDesc, postUsername;
    private Uri uri;
    private int postTime;

    ActivityResultLauncher<Intent> postPhotoPicker;

    private ActivityCreatePostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postViewModel = new CreatePostViewModel();

        postUsername = getIntent().getStringExtra("username");
        postTitle = binding.postTitleEditText.getEditText().getText().toString();
        postDesc = binding.postDescEditText.getEditText().getText().toString();
        postViewModel.setUsername(postUsername);
        postViewModel.setPostTitle(postTitle);
        postViewModel.setPostDesc(postDesc);

        // Get the username of current user in CreatePostViewModel class

        postPhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                uri = result.getData().getData();
                // Use the uri to load the image
                postViewModel.setPostPhotoUri(uri);
                binding.postPhotoImageView.setImageURI(postViewModel.postPhotoUri);
            }else if(result.getResultCode() == ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                ImagePicker.Companion.getError(result.getData());
            }
        });

        binding.uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreatePostActivity.this)
                        .crop(4f, 3f)
                        .maxResultSize(1920, 1440,false)
                        .setMultipleAllowed(false)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                postPhotoPicker.launch(it);
                            }
                        }));
            }
        });

        binding.createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreatePostActivity.this, "You have created your post", Toast.LENGTH_SHORT).show();
                // Store the post time to display it in home feed of users
                // Store the post object in firebase using RTDB and Firebase Storage
                // Add the post on the userprofile data list and in people's home feed(for fixed duration and then remove)
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(CreatePostActivity.this, "You have cancelled post creation", Toast.LENGTH_SHORT).show();
            }
        });
    }
}