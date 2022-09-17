package com.adiupd123.fingertrip;

public class UserHelperClass {
    String name, birthday, emailID, mobileNo, gender;

    public UserHelperClass(String name, String birthday, String emailID, String mobileNo, String gender) {
        this.name = name;
        this.birthday = birthday;
        this.emailID = emailID;
        this.mobileNo = mobileNo;
        this.gender = gender;
    }

    public UserHelperClass(){
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

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}