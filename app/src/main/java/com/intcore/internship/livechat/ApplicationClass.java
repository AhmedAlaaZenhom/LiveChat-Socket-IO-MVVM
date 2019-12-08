package com.intcore.internship.livechat;

import android.app.Application;
import android.util.Log;

import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.data.sharedPreferences.PreferenceHelper;
import com.intcore.internship.livechat.di.CompositionRoot;
import com.yariksoffice.lingver.Lingver;

import io.reactivex.disposables.CompositeDisposable;

public class ApplicationClass extends Application{

    private static final String TAG = ApplicationClass.class.getSimpleName() ;

    private CompositionRoot compositionRoot ;
    private DataManager dataManager ;
    private CompositeDisposable applicationCompositeDisposable;

    public CompositeDisposable getApplicationCompositeDisposable() {
        return applicationCompositeDisposable;
    }

    public CompositionRoot getCompositionRoot() {
        return compositionRoot;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(this, PreferenceHelper.LOCALE_ENGLISH) ;
        compositionRoot = new CompositionRoot(this);
        dataManager = new DataManager(this) ;
    }

    // Called on main activity create
    public void onAppStart(){
        Log.d(TAG,"onAppStart");
        applicationCompositeDisposable = new CompositeDisposable() ;
        dataManager.startListeningToServer();
    }

    // Called on main activity destroy
    public void onAppStop(){
        Log.d(TAG,"onAppStop");
        dataManager.stopListeningToServer();
        applicationCompositeDisposable.dispose();
    }

}
