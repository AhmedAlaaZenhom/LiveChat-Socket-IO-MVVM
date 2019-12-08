package com.intcore.internship.livechat.ui.splash;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;

import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.databinding.ActivitySplashBinding;
import com.intcore.internship.livechat.ui.baseClasses.BaseActivity;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;
import com.intcore.internship.livechat.ui.login.LoginActivity;
import com.intcore.internship.livechat.ui.main.MainActivity;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private SplashViewModel viewModel ;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public BaseViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this,
                getCompositionRoot().getViewModelProviderFactory()).get(SplashViewModel.class);
        return viewModel;
    }

    @Override
    protected void setUpObservers() {
        super.setUpObservers();
        final Observer<Boolean> isLoggedInObserver = isLoggedIn -> {
            if(isLoggedIn)
                openChatActivity();
            else
                openLoginActivity();
        };
        viewModel.getIsLoggedInLD().observe(this,isLoggedInObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            if (!isDestroyed())
                viewModel.decideNextActivity();
        }, 500);
    }

    private void openLoginActivity() {
        LoginActivity.statActivity(this);
        finish();
    }

    private void openChatActivity() {
        MainActivity.startActivity(this);
        finish();
    }
}
