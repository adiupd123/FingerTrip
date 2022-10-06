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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    ImageButton editProfilePhoto, editProfileCover;
    Button saveButton, discardButton;
    ImageView profilePhotoImageView, profileCoverImageView;

    ActivityResultLauncher<Intent> profilePhotoPicker, profileCoverPicker;
    Uri profilePhotoUri, profileCoverUri;
    Context fragmentContext;

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

        profilePhotoImageView = view.findViewById(R.id.profilePhoto_imageView);
        profileCoverImageView = view.findViewById(R.id.profileCover_imageView);

        editProfilePhoto = view.findViewById(R.id.editProfilePhoto_imageButton);
        editProfileCover = view.findViewById(R.id.editProfileCover_imageButton);

        saveButton = view.findViewById(R.id.save_button);
        discardButton = view.findViewById(R.id.discard_button);

        fragmentContext = getActivity();

        profilePhotoPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                profilePhotoUri = uri;
                profilePhotoImageView.setImageURI(uri);
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                ImagePicker.Companion.getError(result.getData());
            }
        });

        profileCoverPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                Uri uri=result.getData().getData();
                // Use the uri to load the image
                profileCoverUri = uri;
                profileCoverImageView.setImageURI(uri);
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                // Use ImagePicker.Companion.getError(result.getData()) to show an error
                ImagePicker.Companion.getError(result.getData());
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editProfilePhoto_imageButton:
                ImagePicker.Companion.with((Activity) fragmentContext)
                        .crop(1f, 1f)
                        .maxResultSize(1024, 1024,true)
                        .setMultipleAllowed(false)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                profilePhotoPicker.launch(it);
                            }
                        }));
                break;
            case R.id.editProfileCover_imageButton:
                ImagePicker.Companion.with((Activity) fragmentContext)
                        .crop(16f, 9f)
                        .maxResultSize(1920, 1080,false)
                        .setMultipleAllowed(false)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                profileCoverPicker.launch(it);
                            }
                        }));
                break;
        }
    }
}
