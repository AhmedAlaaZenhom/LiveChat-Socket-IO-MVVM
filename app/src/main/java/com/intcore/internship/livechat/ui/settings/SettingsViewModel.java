package com.intcore.internship.livechat.ui.settings;

import android.app.Application;

import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;

import androidx.annotation.NonNull;

public class SettingsViewModel extends BaseViewModel {

    private DataManager dataManager ;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        dataManager = getCompositionRoot().getDataManager() ;
    }

    String getSavedLocale(){
        return dataManager.getSavedLocale() ;
    }

    void changeLocale(String locale) {

    }
}
