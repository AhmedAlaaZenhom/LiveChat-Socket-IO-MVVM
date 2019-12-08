package com.intcore.internship.livechat.ui.baseClasses;

import android.app.Application;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.di.ViewModelCompositionRoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel extends AndroidViewModel {

    private ViewModelCompositionRoot viewModelCompositionRoot ;
    private CompositeDisposable mCompositeDisposable;

    private MutableLiveData<Boolean> progressLoadingLD ;
    private MutableLiveData<String> toastMessagesLD ;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    protected ViewModelCompositionRoot getCompositionRoot() {
        if (viewModelCompositionRoot == null)
            viewModelCompositionRoot = new ViewModelCompositionRoot(
                    ((ApplicationClass) getApplication()).getCompositionRoot());

        return viewModelCompositionRoot;
    }

    protected CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    MutableLiveData<Boolean> getProgressLoadingLD() {
        if(progressLoadingLD==null)
            progressLoadingLD = new MutableLiveData<>() ;
        return progressLoadingLD;
    }

    protected void setProgressLoadingLD(boolean loading) {
        getProgressLoadingLD().setValue(loading);
    }

    MutableLiveData<String> getToastMessagesLD() {
        if(toastMessagesLD==null)
            toastMessagesLD = new MutableLiveData<>() ;
        return toastMessagesLD;
    }

    protected void setToastMessagesLD(String message) {
        getToastMessagesLD().setValue(message);
    }
}
