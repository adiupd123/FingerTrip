package com.adiupd123.fingertrip.models;

public class CommentModel {
    private String userID, comment;
    private int commentTimeStamp;

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
