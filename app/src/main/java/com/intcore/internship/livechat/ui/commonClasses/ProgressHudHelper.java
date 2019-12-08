package com.intcore.internship.livechat.ui.commonClasses;

import android.content.Context;

import com.intcore.internship.livechat.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressHudHelper {

    private Context context ;
    private KProgressHUD kProgressHUD ;

    public ProgressHudHelper(Context context) {
        this.context = context;
        kProgressHUD = newProgressHudInstance() ;
    }

    private KProgressHUD newProgressHudInstance() {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.please_wait))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public void showProgressHud() {
        if (!kProgressHUD.isShowing())
            kProgressHUD.show();
    }

    public void hideProgressHud(){
        if (kProgressHUD.isShowing())
            kProgressHUD.dismiss();
    }
}
