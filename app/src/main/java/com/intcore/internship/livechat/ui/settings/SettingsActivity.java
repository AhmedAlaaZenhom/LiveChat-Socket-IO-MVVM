package com.intcore.internship.livechat.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.intcore.internship.livechat.BR;
import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.data.sharedPreferences.PreferenceHelper;
import com.intcore.internship.livechat.databinding.ActivitySettingsBinding;
import com.intcore.internship.livechat.ui.baseClasses.BaseActivity;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.yariksoffice.lingver.Lingver;

import java.util.Objects;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> {

    public static void startActivity(Context context){
        Intent intent = new Intent(context,SettingsActivity.class) ;
        context.startActivity(intent);
    }

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsViewModel viewModel;
    private ActivitySettingsBinding binding;
    private boolean isCurrentLocaleAR;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public BaseViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this,
                getCompositionRoot().getViewModelProviderFactory()).get(SettingsViewModel.class);
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        isCurrentLocaleAR = viewModel.getSavedLocale().equals(PreferenceHelper.LOCALE_ARABIC);
        setUpViews();
    }

    private void setUpViews() {
        setTitle(R.string.settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initLanguageViews();
        initLogoutViews();
    }

    private void initLanguageViews() {
        String[] values = {getString(R.string.arabic), getString(R.string.english)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_language);
        builder.setSingleChoiceItems(values, isCurrentLocaleAR ? 0 : 1, (dialog, item) -> {

            switch (item) {
                case 0:
                    askToChangeLanguageAndFinish(PreferenceHelper.LOCALE_ARABIC);
                    break;
                case 1:
                    askToChangeLanguageAndFinish(PreferenceHelper.LOCALE_ENGLISH);
                    break;
            }
            dialog.dismiss();
        });
        AlertDialog languageAlertDialog = builder.create();
        binding.languageSettingsLayout.setOnClickListener(view -> languageAlertDialog.show());
    }

    private void initLogoutViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.logout_question);
        builder.setPositiveButton(R.string.logout, (dialogInterface, i) -> {
            try {
                ((ActivityManager) Objects.requireNonNull(getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            } catch (NullPointerException e) {
                Log.d(TAG, e.toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

        });
        AlertDialog signOutAlertDialog = builder.create();
        binding.signOutLayout.setOnClickListener(view -> signOutAlertDialog.show());
    }

    private void askToChangeLanguageAndFinish(String locale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.application_language);
        builder.setMessage(R.string.change_language_text);
        builder.setPositiveButton(R.string.close_app, (dialogInterface, i) -> {
            Lingver.getInstance().setLocale(this,locale);
            viewModel.changeLocale(locale);
            restartApplication();
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void restartApplication() {
        ProcessPhoenix.triggerRebirth(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
