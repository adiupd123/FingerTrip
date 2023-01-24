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

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileFragment extends Fragment {

    ImageView profilePhotoImageView, profileCoverImageView;
    EditText nameEditText, dobEditText, bioEditText;
    ImageButton editProfilePhoto, editProfileCover;
    Button saveButton, discardButton;

    Bundle userNewData;

    ActivityResultLauncher<Intent> profilePhotoPicker, profileCoverPicker;
    Context fragmentContext;

    EditUserProfileViewModel editUserProfileViewModel;

    public EditProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNewData = new Bundle();

        profilePhotoImageView = view.findViewById(R.id.profilePhoto_imageView);
        profileCoverImageView = view.findViewById(R.id.profileCover_imageView);
        nameEditText = view.findViewById(R.id.editName_editText);
        dobEditText = view.findViewById(R.id.editDOB_editText);
        bioEditText = view.findViewById(R.id.editBio_editText);

        editProfilePhoto = view.findViewById(R.id.editProfilePhoto_imageButton);
        editProfileCover = view.findViewById(R.id.editProfileCover_imageButton);

        saveButton = view.findViewById(R.id.save_button);
        discardButton = view.findViewById(R.id.discard_button);

        fragmentContext = getActivity();

        editUserProfileViewModel = new EditUserProfileViewModel();

        editUserProfileViewModel.setName(nameEditText.getText().toString());
        editUserProfileViewModel.setDob(dobEditText.getText().toString());
        editUserProfileViewModel.setBio(bioEditText.getText().toString());

        profilePhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                editUserProfileViewModel.setProfilePhoto(uri);
                profilePhotoImageView.setImageURI(uri);
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                Toast.makeText(fragmentContext, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        profileCoverPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                editUserProfileViewModel.setProfileCover(uri);
                profileCoverImageView.setImageURI(uri);
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                Toast.makeText(fragmentContext, ImagePicker.Companion.getError(result.getData()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        editProfilePhoto.setOnClickListener(new View.OnClickListener() {
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

        editProfileCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with((Activity) fragmentContext)
                        .crop(profileCoverImageView.getWidth(), profileCoverImageView.getHeight())
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfileFragment();
            }
        });
    }
    public void openUserProfileFragment(){
        userNewData.putString("name",editUserProfileViewModel.getName());
        userNewData.putString("username",editUserProfileViewModel.getDob());
        userNewData.putString("bio",editUserProfileViewModel.getBio());
        userNewData.putString("photoUri",editUserProfileViewModel.getProfilePhoto().toString());
        userNewData.putString("coverUri",editUserProfileViewModel.getProfileCover().toString());
    }
}
