package com.intcore.internship.livechat.ui.commonClasses;

import android.content.Context;
import android.widget.Toast;

import com.intcore.internship.livechat.R;

public class ToastsHelper {

    private final Context mContext;

    public ToastsHelper(Context context) {
        mContext = context;
    }

    // Commons

    public void showMessageError(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showConnectionError() {
        Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    public void showUnknownError() {
        Toast.makeText(mContext, R.string.unknown_error, Toast.LENGTH_SHORT).show();
    }

}
