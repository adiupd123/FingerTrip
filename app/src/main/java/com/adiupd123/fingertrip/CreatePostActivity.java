package com.adiupd123.fingertrip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.ActivityCreatePostBinding;
import com.adiupd123.fingertrip.models.CreatePostModel;
import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class CreatePostActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private CreatePostModel postModel;
    ActivityResultLauncher<Intent> postPhotoPicker;

    private ActivityCreatePostBinding binding;
    private String curUserEmail, tempEmail;
    private String postID, timeStamp;
    private Uri postPhotoUri;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        storageReference = FirebaseStorage.getInstance().getReference("posts");
        // Get the EmailID of current user in CreatePostModel class
        Intent intent = getIntent();
        if(intent !=  null){
            curUserEmail = intent.getStringExtra("emailID");
        }
        if(curUserEmail != null){
            tempEmail = curUserEmail.replace('.', ',');
        }
        postPhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri = result.getData().getData();
                // Use the uri to load the image
                postPhotoUri = uri;
                binding.postPhotoImageView.setImageURI(uri);
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
                        .createIntentFromDialog(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                postPhotoPicker.launch(it);
                            }
                        });
            }
        });

        binding.createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store the post time to display it in home feed of users
                // Store the post object in firebase using RTDB and Firebase Storage
                // Add the post on the userprofile data list and in people's home feed(for fixed duration and then remove)
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                timeStamp = dateFormat.format(calendar.getTime());
                postID = tempEmail + "_" + timeStamp;
                postModel = new CreatePostModel(
                        postID,
                        binding.postTitleEditText.getEditText().getText().toString(),
                        binding.postDescEditText.getEditText().getText().toString(),
                        tempEmail,
                        null, //  Initially set to null, will update when the image is uploaded to firebase storage
                        new ArrayList<>(),
                        0,
                        new HashMap<>(),
                        0,
                        timeStamp
                        );
                progressBar = new ProgressBar(binding.progressBar.getContext());
                progressBar.setVisibility(View.VISIBLE);
                storageReference.child(curUserEmail + "_" + timeStamp + "/").putFile(postPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getTask().isSuccessful()){
                            storageReference.child(curUserEmail + "_" + timeStamp + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    postModel.setPostPhoto(uri.toString());
                                    Glide.with(getApplicationContext())
                                            .load(uri)
                                            .into(binding.postPhotoImageView);
                                }
                            });
                        } else{
                            Toast.makeText(CreatePostActivity.this, taskSnapshot.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(CreatePostActivity.this, "Try Re-uploading post image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                databaseReference.child(postID).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(CreatePostActivity.this, "Your post has been created!", Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(CreatePostActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("CreatePostActivity.java", task.getException().getMessage());
                        }
                    }
                });

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