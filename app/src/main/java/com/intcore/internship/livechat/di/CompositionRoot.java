package com.intcore.internship.livechat.di;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.utils.ViewModelProviderFactory;

public class CompositionRoot {

    private ApplicationClass application;
    private ViewModelProviderFactory viewModelProviderFactory ;

    public CompositionRoot(ApplicationClass application) {
        this.application = application;
    }

    DataManager getDataManager() {
        return application.getDataManager();
    }

    ViewModelProviderFactory getViewModelProviderFactory() {
        if(viewModelProviderFactory==null){
            viewModelProviderFactory = new ViewModelProviderFactory(application) ;
        }
        return  viewModelProviderFactory ;
    }

}
