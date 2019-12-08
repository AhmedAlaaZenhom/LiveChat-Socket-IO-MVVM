package com.intcore.internship.livechat.di;

import com.intcore.internship.livechat.data.DataManager;

public class ViewModelCompositionRoot {

    private CompositionRoot compositionRoot ;

    public ViewModelCompositionRoot(CompositionRoot compositionRoot) {
        this.compositionRoot = compositionRoot ;
    }

    public DataManager getDataManager(){
        return compositionRoot.getDataManager() ;
    }

}
