package com.intcore.internship.livechat.ui.commonClasses;

import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class PicassoHelper {

    private static final String TAG = PicassoHelper.class.getSimpleName() ;

    public static final String STORAGE_BASE_URL = "" ;

    public static void loadImageWithCache(String url , ImageView imageView){
        Picasso.get()
                .load(TextUtils.isEmpty(url)?"fakeUrl":url)
                .fit()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        loadImageSkippingCache(TextUtils.isEmpty(url)?"fakeUrl":url,imageView);
                    }
                });
    }

    public static void loadImageWithCache(String url , ImageView imageView , int errorResID){
        Picasso.get()
                .load(TextUtils.isEmpty(url)?"fakeUrl":url)
                .fit()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        loadImageSkippingCache(TextUtils.isEmpty(url)?"fakeUrl":url,imageView,errorResID);
                    }
                });
    }

    private static void loadImageSkippingCache(String url , ImageView imageView){
        Picasso.get()
                .load(url)
                .fit()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(new ColorDrawable(0xFFFFFF))
                .into(imageView);
    }

    private static void loadImageSkippingCache(String url , ImageView imageView , int errorResID){
        Picasso.get()
                .load(url)
                .fit()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(errorResID)
                .into(imageView);
    }

    public static void loadResourceImage(int imageResID, ImageView imageView){
        Picasso.get()
                .load(imageResID)
                .fit()
                .into(imageView);
    }

}
