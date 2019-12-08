package com.intcore.internship.livechat.ui.login;

import android.app.Application;

import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends BaseViewModel {

    private DataManager dataManager ;

    private MutableLiveData<Boolean> loggedInSuccessLD;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        dataManager = getCompositionRoot().getDataManager() ;
        loggedInSuccessLD = new MutableLiveData<>() ;
    }

    MutableLiveData<Boolean> getLoggedInSuccessLD() {
        return loggedInSuccessLD;
    }

    void loginWithUserName(String userName){
        dataManager.loginWithUserData(userName);
        loggedInSuccessLD.postValue(true);
    }

}
