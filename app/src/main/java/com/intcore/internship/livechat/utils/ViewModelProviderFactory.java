package com.intcore.internship.livechat.utils;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.ui.login.LoginViewModel;
import com.intcore.internship.livechat.ui.main.MainViewModel;
import com.intcore.internship.livechat.ui.settings.SettingsViewModel;
import com.intcore.internship.livechat.ui.splash.SplashViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApplicationClass application;

    public ViewModelProviderFactory(ApplicationClass application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashViewModel(application);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            //noinspection unchecked
            return (T) new LoginViewModel(application);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(application);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            //noinspection unchecked
            return (T) new SettingsViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}
