package com.adiupd123.fingertrip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class UserViewModel extends ViewModel {
    private MutableLiveData<HashMap<String, Object>> userPersonalData;
    private MutableLiveData<HashMap<String, Object>> userSocialData;

    public UserViewModel() {
        userPersonalData = new MutableLiveData<>();
        userSocialData = new MutableLiveData<>();
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
