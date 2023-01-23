package com.adiupd123.fingertrip;

import android.net.Uri;

public class UserSocialProfileModel {
    private String profilePhoto, profileCover, bio;

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfileCover() {
        return profileCover;
    }

    public void setProfileCover(String profileCover) {
        this.profileCover = profileCover;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
