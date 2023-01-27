package com.adiupd123.fingertrip.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePostModel {
    private String postID, postTitle, postDesc;
    private String postOwnerID;
    private String postPhoto;
    private ArrayList<String> likes;
    private int likesCount;
    private HashMap<String, CommentModel> comments;
    private int commentsCount;
    private String postTimeStamp;

    public CreatePostModel() {
    }

    public CreatePostModel(String postID, String postTitle, String postDesc, String postOwnerID, String postPhoto, ArrayList<String> likes, int likesCount, HashMap<String, CommentModel> comments, int commentsCount, String postTimeStamp) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postOwnerID = postOwnerID;
        this.postPhoto = postPhoto;
        this.likes = likes;
        this.likesCount = likesCount;
        this.comments = comments;
        this.commentsCount = commentsCount;
        this.postTimeStamp = postTimeStamp;
    }


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostOwnerID() {
        return postOwnerID;
    }

    public void setPostOwnerID(String postOwnerID) {
        this.postOwnerID = postOwnerID;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(String postPhoto) {
        this.postPhoto = postPhoto;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public HashMap<String, CommentModel> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, CommentModel> comments) {
        this.comments = comments;
    }

    public String getPostTimeStamp() {
        return postTimeStamp;
    }

    public void setPostTimeStamp(String postTimeStamp) {
        this.postTimeStamp = postTimeStamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
    /*
    * postID = userID_timeStamp
    * commentID = postID_userID_timeStamp
    * postID: {
    *   postID: userID_timeStamp
    *   postOwnerID: emailID,
    *   postTitle: Title,
    *   postDesc: desc,
    *   postPhoto: PhotoURL,
    *   postTimeStamp: time,
    *   likes{
    *       userID1,
    *       userID2,
    *       ..
    *   }
    *   comments{
    *       commentID1: {
    *           userID: emailID,
    *           comment: Value,
    *           commentTimeStamp: time
    *       },
    *       commentID2: {
     *           userID: emailID,
     *           comment: Value,
     *           commentTimeStamp: time
     *       },
     *       ..
    *   }
    * }
    * */
}
