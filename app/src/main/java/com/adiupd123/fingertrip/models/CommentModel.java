package com.adiupd123.fingertrip.models;

public class CommentModel {
    private String userID, username, comment, profilePhoto;
    private int commentTimeStamp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public void setCommentTimeStamp(int commentTimeStamp) {
        this.commentTimeStamp = commentTimeStamp;
    }
}
