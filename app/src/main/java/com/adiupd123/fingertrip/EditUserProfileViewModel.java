package com.adiupd123.fingertrip;

import android.net.Uri;

public class EditUserProfileViewModel {
    String name;
    String username;
    String bio;
    Uri profilePhoto, profileCover;

    public EditUserProfileViewModel() {
    }

    public EditUserProfileViewModel(String name, String username, String bio, Uri profilePhoto, Uri profileCover) {
        this.name = name;
        this.username = username;
        this.bio = bio;
        this.profilePhoto = profilePhoto;
        this.profileCover = profileCover;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public Uri getProfileCover() {
        return profileCover;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePhoto(Uri profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setProfileCover(Uri profileCover) {
        this.profileCover = profileCover;
    }
}
