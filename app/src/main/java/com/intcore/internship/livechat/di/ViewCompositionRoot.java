package com.intcore.internship.livechat.di;

import com.intcore.internship.livechat.ui.commonClasses.ProgressHudHelper;
import com.intcore.internship.livechat.ui.commonClasses.ToastsHelper;
import com.intcore.internship.livechat.utils.ViewModelProviderFactory;

import androidx.fragment.app.FragmentActivity;

public class ViewCompositionRoot {

    private CompositionRoot compositionRoot ;
    private FragmentActivity activity ;
    private ToastsHelper toastsHelper ;
    private ProgressHudHelper progressHudHelper ;

    public ViewCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        this.compositionRoot = compositionRoot;
        this.activity = activity;
    }

    private CompositionRoot getCompositionRoot() {
        return compositionRoot;
    }

    private FragmentActivity getContext() {
        return activity;
    }

    public ViewModelProviderFactory getViewModelProviderFactory(){
        return getCompositionRoot().getViewModelProviderFactory() ;
    }

    public ToastsHelper getToastsHelper(){
        if(toastsHelper==null)
            toastsHelper = new ToastsHelper(getContext()) ;
        return toastsHelper ;
    }

    public ProgressHudHelper getProgressHudHelper(){
        if(progressHudHelper==null)
            progressHudHelper = new ProgressHudHelper(getContext()) ;
        return progressHudHelper ;
    }
}
