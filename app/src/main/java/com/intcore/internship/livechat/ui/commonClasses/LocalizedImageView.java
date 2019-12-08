package com.intcore.internship.livechat.ui.commonClasses;


import android.content.Context;
import android.util.AttributeSet;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.data.sharedPreferences.PreferenceHelper;

import androidx.appcompat.widget.AppCompatImageView;

/*
* Class extending AppCompatImageView to automatically flip image vertically
* when the current app locale is arabic
* */
public class LocalizedImageView extends AppCompatImageView {

    private Context context;

    public LocalizedImageView(Context context) {
        super(context);
        this.context = context;
        setScaleXAccordingToLocale();
    }

    public LocalizedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setScaleXAccordingToLocale();
    }

    public LocalizedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setScaleXAccordingToLocale();
    }

    private void setScaleXAccordingToLocale() {
        DataManager dataManager = ((ApplicationClass) context.getApplicationContext()).getDataManager();
        boolean isCurrentLocaleAR = dataManager.getSavedLocale().equals(PreferenceHelper.LOCALE_ARABIC);
        this.setScaleX(isCurrentLocaleAR ? -1 : 1);
    }

}

