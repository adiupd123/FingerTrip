package com.adiupd123.fingertrip;

import static android.app.Activity.RESULT_OK;

import static androidx.activity.result.contract.ActivityResultContracts.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.adiupd123.fingertrip.databinding.FragmentEditProfileBinding;
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

import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    Button saveButton, discardButton;
    ActivityResultLauncher<Intent> profilePhotoPicker, profileCoverPicker;
    Context fragmentContext;
    private Bundle userBundle;

    private HashMap<String, Object> personalInfoHashMap, socialInfoHashMap;
    private HashMap<String, HashMap<String, Object>> userInfo;
    private String curUserEmail, tempEmail;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentContext = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        userBundle = getArguments();
        if(userBundle != null) {
            curUserEmail = userBundle.getString("emailID");
            personalInfoHashMap = (HashMap<String, Object>) userBundle.getSerializable("personalInfo");
            socialInfoHashMap = (HashMap<String, Object>) userBundle.getSerializable("socialInfo");
            updateUIWithOldData();
        }
        if(curUserEmail != null) {
            tempEmail = curUserEmail.replace('.', ',');
        }

        profilePhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                storageReference.child("users/"+curUserEmail+"/profilePhoto/").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getTask().isSuccessful()){
                            storageReference.child("users/"+curUserEmail+"/profilePhoto/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uploadedUri) {
                                    socialInfoHashMap.put("profilePhoto", uploadedUri.toString());
                                    Glide.with(fragmentContext)
                                            .load(uploadedUri)
                                            .placeholder(R.drawable.ic_default_profile)
                                            .into(binding.profilePhotoImageView);
                                }
                            });
                        } else{
                            Toast.makeText(fragmentContext, taskSnapshot.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(fragmentContext, "Try Re-uploading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                Toast.makeText(fragmentContext, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        profileCoverPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                storageReference.child("users/"+curUserEmail+"/profileCover/").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getTask().isSuccessful()){
                            storageReference.child("users/"+curUserEmail+"/profileCover/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uploadedUri) {
                                    socialInfoHashMap.put("profileCover", uploadedUri.toString());
                                    Glide.with(fragmentContext)
                                            .load(uploadedUri)
                                            .placeholder(R.drawable.no_profile_background)
                                            .into(binding.profileCoverImageView);
                                }
                            });
                        } else{
                            Toast.makeText(fragmentContext, taskSnapshot.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(fragmentContext, "Try Re-uploading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                Toast.makeText(fragmentContext, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.editProfilePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with((Activity) fragmentContext)
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

        binding.editProfileCoverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with((Activity) fragmentContext)
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

        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editNameEditText.getText().toString().trim();
                String birthday = binding.editDOBEditText.getText().toString().trim();
                String bio = binding.editBioEditText.getText().toString().trim();
                if(name.isEmpty()){
                    binding.editNameEditText.setError("Name is Required!");
                    binding.editNameEditText.requestFocus();
                    return;
                }
                if(birthday.isEmpty()){
                    binding.editDOBEditText.setError("Birthday is Required!");
                    binding.editDOBEditText.requestFocus();
                    return;
                }
                if(bio.isEmpty()){
                    binding.editBioEditText.setError("Bio is Required!");
                    binding.editBioEditText.requestFocus();
                    return;
                }
                personalInfoHashMap.put("name", name);
                personalInfoHashMap.put("birthday", birthday);
                socialInfoHashMap.put("bio", bio);
                databaseReference.child(tempEmail+"/personal_info").setValue(personalInfoHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child(tempEmail+"/social_info").setValue(socialInfoHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(fragmentContext, "Your profile is updated successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(fragmentContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(fragmentContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(fragmentContext, "YO!", Toast.LENGTH_SHORT).show();
//                openUserProfileFragment();
            }
        });

        discardButton = view.findViewById(R.id.discard_button);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragmentContext, "Profile Editing is discarded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIWithOldData() {
        binding.editNameEditText.setText(personalInfoHashMap.get("name").toString());
        binding.editDOBEditText.setText(personalInfoHashMap.get("birthday").toString());
        binding.editBioEditText.setText(socialInfoHashMap.get("bio").toString());
        Glide.with(fragmentContext)
                .load(socialInfoHashMap.get("profileCover"))
                .placeholder(R.drawable.no_profile_background)
                .into(binding.profileCoverImageView);
        Glide.with(fragmentContext)
                .load(socialInfoHashMap.get("profilePhoto"))
                .placeholder(R.drawable.ic_default_profile)
                .into(binding.profilePhotoImageView);
    }

    public void openUserProfileFragment(){
    }
}
