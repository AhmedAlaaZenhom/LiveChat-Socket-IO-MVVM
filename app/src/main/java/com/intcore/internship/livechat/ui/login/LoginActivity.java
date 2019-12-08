package com.intcore.internship.livechat.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.intcore.internship.livechat.BR;
import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.databinding.ActivityLoginBinding;
import com.intcore.internship.livechat.ui.baseClasses.BaseActivity;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;
import com.intcore.internship.livechat.ui.main.MainActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    public static void statActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class) ;
        context.startActivity(intent);
    }

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public BaseViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this,
                getCompositionRoot().getViewModelProviderFactory()).get(LoginViewModel.class);
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding() ;
        setUpViews();
    }

    @Override
    protected void setUpObservers() {
        super.setUpObservers();
        final Observer<Boolean> loginSuccessObserver = aBoolean -> openMainActivity();
        viewModel.getLoggedInSuccessLD().observe(this,loginSuccessObserver);
    }

    private void setUpViews() {
        binding.loginBtn.setOnClickListener(v -> validateAndLogin());
    }

    private void validateAndLogin(){
        String userName = binding.nickNameET.getText().toString() ;
        if(userName.isEmpty()){
            binding.nickNameET.setError(getString(R.string.requierd_field));
            return;
        }
        viewModel.loginWithUserName(userName);
    }

    private void openMainActivity(){
        MainActivity.startActivity(this);
        finish();
    }
}
