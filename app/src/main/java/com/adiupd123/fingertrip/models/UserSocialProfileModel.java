package com.adiupd123.fingertrip.models;

import android.net.Uri;

import java.util.ArrayList;

public class UserSocialProfileModel {
    private String profilePhoto, profileCover, bio;
    private int postCount;
    private int followerCount;
    private ArrayList<String> followers; // It contains emailIDs of all followers.
    private int followingCount;
    private ArrayList<String> following;// It contains emailIDs of all users who he follows.

    public UserSocialProfileModel() {
        this.profilePhoto = null;
        this.profileCover = null;
        this.bio = "";
        this.postCount = 0;
        this.followerCount = 0;
        this.followingCount = 0;
    }

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


    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }
}
