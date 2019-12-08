package com.intcore.internship.livechat.ui.baseClasses;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.di.ViewCompositionRoot;
import com.intcore.internship.livechat.ui.commonClasses.ProgressHudHelper;
import com.intcore.internship.livechat.ui.commonClasses.ToastsHelper;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    private ViewCompositionRoot viewCompositionRoot;
    private T mViewDataBinding;
    private BaseViewModel mViewModel;
    public ProgressHudHelper progressHudHelper;
    public ToastsHelper toastsHelper ;

    public abstract int getBindingVariable();

    public abstract @LayoutRes int getLayoutId();

    public abstract BaseViewModel getViewModel();

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressHudHelper = getCompositionRoot().getProgressHudHelper() ;
        toastsHelper = getCompositionRoot().getToastsHelper() ;

        performDataBinding();
        setUpObservers();
    }

    protected ViewCompositionRoot getCompositionRoot() {
        if (viewCompositionRoot == null) {
            viewCompositionRoot = new ViewCompositionRoot(
                    ((ApplicationClass) getApplication()).getCompositionRoot(),
                    this
            );
        }
        return viewCompositionRoot;
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    protected void setUpObservers(){
        final Observer<Boolean> progressLoadingObserver = loading -> {
            if(loading)
                progressHudHelper.showProgressHud();
            else
                progressHudHelper.hideProgressHud();
        };
        mViewModel.getProgressLoadingLD().observe(this,progressLoadingObserver);

        final Observer<String> toastMessagesObserver = message -> {
            toastsHelper.showMessageError(message);
        };
        mViewModel.getToastMessagesLD().observe(this,toastMessagesObserver);

    }

    protected T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
