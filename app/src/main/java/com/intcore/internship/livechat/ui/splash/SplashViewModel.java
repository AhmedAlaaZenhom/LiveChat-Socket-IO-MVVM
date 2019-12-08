package com.intcore.internship.livechat.ui.splash;

import android.app.Application;

import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class SplashViewModel extends BaseViewModel {

    private DataManager dataManager;

    private MutableLiveData<Boolean> isLoggedInLD;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        dataManager = getCompositionRoot().getDataManager();
        isLoggedInLD = new MutableLiveData<>();
    }

    MutableLiveData<Boolean> getIsLoggedInLD() {
        return isLoggedInLD;
    }

    void decideNextActivity(){
        isLoggedInLD.setValue(getCompositionRoot().getDataManager().isUserLoggedIn());
    }
}
