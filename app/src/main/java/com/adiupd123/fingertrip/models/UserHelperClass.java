package com.adiupd123.fingertrip.models;

import android.net.Uri;

public class UserHelperClass {
    String name, birthday, emailID, username, mobileNo, gender;
    int followers, following, posts;

    public UserHelperClass(String name, String birthday, String emailID, String username, String mobileNo, String gender) {
        this.name = name;
        this.birthday = birthday;
        this.emailID = emailID;
        this.username = username;
        this.mobileNo = mobileNo;
        this.gender = gender;
        followers = 0;
        following = 0;
        posts = 0;
    }

    public UserHelperClass(){
        followers = 0;
        following = 0;
        posts = 0;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getUsername() {
        return username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
