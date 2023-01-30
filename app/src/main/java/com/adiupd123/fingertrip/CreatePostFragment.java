package com.adiupd123.fingertrip;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentCreatePostBinding;
import com.adiupd123.fingertrip.models.CreatePostModel;
import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class CreatePostFragment extends Fragment {
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private CreatePostModel postModel;
    ActivityResultLauncher<Intent> postPhotoPicker;
    private FragmentCreatePostBinding binding;
    private String curUserEmail, tempEmail;
    private String postID, timeStamp;
    private Uri postPhotoUri;
    private ProgressBar progressBar;
    private UserProfileFragment userProfileFragment;
    private Bundle userBundle;
    public CreatePostFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        storageReference = FirebaseStorage.getInstance().getReference("posts");
        userProfileFragment = new UserProfileFragment();
        // Get the EmailID of current user in CreatePostModel class
        userBundle = getArguments();
        if(userBundle !=  null){
            curUserEmail = userBundle.getString("emailID");
        }
        if(curUserEmail != null) {
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
                ImagePicker.Companion.with(getActivity())
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
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                timeStamp = sdf.format(calendar.getTime());
                postID = tempEmail + "_" + timeStamp;
                postModel = new CreatePostModel();
                postModel.setPostID(postID);
                postModel.setPostTitle(binding.postTitleEditText.getEditText().getText().toString());
                postModel.setPostDesc(binding.postDescEditText.getEditText().getText().toString());
                postModel.setPostOwnerID(tempEmail);
                postModel.setLikes(new ArrayList<>());
                postModel.setLikesCount(0);
                postModel.setComments(new HashMap<>());
                postModel.setCommentsCount(0);
                postModel.setPostTimeStamp(timeStamp);
                progressBar = new ProgressBar(binding.progressBar.getContext());
                progressBar.setVisibility(View.VISIBLE);
                storageReference.child(curUserEmail + "_" + timeStamp + "/post_photo/").putFile(postPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getTask().isSuccessful()){
                            storageReference.child(curUserEmail + "_" + timeStamp + "/post_photo/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    postModel.setPostPhoto(uri.toString());
                                    Glide.with(getActivity())
                                            .load(uri)
                                            .into(binding.postPhotoImageView);
                                }
                            });
                        } else{
                            Toast.makeText(getActivity(), taskSnapshot.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Try Re-uploading post image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                databaseReference.child(postID).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Your post has been created!", Toast.LENGTH_SHORT).show();
                            openUserProfileFragment();
                        } else {
                            Log.d("CreatePostFragment.java", task.getException().getMessage());
                        }
                    }
                });
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "You have cancelled post creation", Toast.LENGTH_SHORT).show();
                openUserProfileFragment();
            }
        });
    }

    private void openUserProfileFragment() {
        userProfileFragment.setArguments(userBundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, userProfileFragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
        bottomAppBar.setVisibility(View.GONE);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
        bottomAppBar.setVisibility(View.VISIBLE);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }
}