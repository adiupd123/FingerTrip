package com.adiupd123.fingertrip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class UserViewModel extends ViewModel {
    private MutableLiveData<HashMap<String, Object>> userPersonalData;
    private MutableLiveData<HashMap<String, Object>> userSocialData;
    private MutableLiveData<ArrayList<HashMap<String, Object>>> userPostsData;

    public UserViewModel() {
        userPersonalData = new MutableLiveData<>();
        userSocialData = new MutableLiveData<>();
        userPostsData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<HashMap<String, Object>>> getUserPostsData() {
        return userPostsData;
    }

    public void setUserPostsData(ArrayList<HashMap<String, Object>> postsData) {
        userPostsData.setValue(postsData);
    }

    public MutableLiveData<HashMap<String, Object>> getUserPersonalData() {
        return userPersonalData;
    }

    public void setUserPersonalData(HashMap<String, Object> personalData) {
        userPersonalData.setValue(personalData);
    }

    public MutableLiveData<HashMap<String, Object>> getUserSocialData() {
        return userSocialData;
    }

    public void setUserSocialData(HashMap<String, Object> socialData) {
        userSocialData.setValue(socialData);
    }
}
