package com.adiupd123.fingertrip;

import android.net.Uri;

public class CreatePostViewModel {
    String postTitle, postDesc;
    String username;
    Uri postPhotoUri;
    int postTime;

    public CreatePostViewModel() {
    }

    public CreatePostViewModel(String postTitle, String postDesc, String username, Uri postPhotoUri, int postTime) {
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.username = username;
        this.postPhotoUri = postPhotoUri;
        this.postTime = postTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public Uri getPostPhotoUri() {
        return postPhotoUri;
    }

    public int getPostTime() {
        return postTime;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public void setPostPhotoUri(Uri postPhotoUri) {
        this.postPhotoUri = postPhotoUri;
    }

    public void setPostTime(int postTime) {
        this.postTime = postTime;
    }
}
