package com.intcore.internship.livechat.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.BR;
import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.data.model.ChatMessage;
import com.intcore.internship.livechat.data.model.ConnectivityStates;
import com.intcore.internship.livechat.data.sharedPreferences.PreferenceHelper;
import com.intcore.internship.livechat.databinding.ActivityMainBinding;
import com.intcore.internship.livechat.ui.baseClasses.BaseActivity;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;
import com.intcore.internship.livechat.ui.commonClasses.PicassoHelper;
import com.intcore.internship.livechat.ui.settings.SettingsActivity;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements ChatViewManager.Listener {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private ChatViewManager chatViewManager;
    private boolean scrolledToBottom = true ;

    // DataBinding-ViewModel implementations

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BaseViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this,
                getCompositionRoot().getViewModelProviderFactory()).get(MainViewModel.class);
        return viewModel;
    }

    // Activity-ViewModel Functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ApplicationClass) getApplication()).onAppStart();
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        setupViews();
        viewModel.getHistoryChatMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearChat:
                viewModel.removeChatHistory();
                chatViewManager.clearMessages();
                break;
            case R.id.settings:
                openSettingsActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        ((ApplicationClass) getApplication()).onAppStop();
        super.onDestroy();
    }

    private void setupViews() {
        setTitle(R.string.app_name);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (chatViewManager.isEmpty()) {
                toastsHelper.showMessageError(getString(R.string.no_more_messages_found));
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            else
                viewModel.getHistoryChatMessages(chatViewManager.getFirstMessageTimeStamp());
        });
        PicassoHelper.loadResourceImage(R.drawable.chat_background, binding.backgroundImage);
        boolean isCurrentLocaleAR = viewModel.getSavedLocale().equals(PreferenceHelper.LOCALE_ARABIC);
        chatViewManager = new ChatViewManager(binding.messagesRV, this, isCurrentLocaleAR, this);
        chatViewManager.clearMessages();
        binding.sendMessageBtn.setOnClickListener(v -> validateAndSendMessage());
        binding.scrollToBottomLayout.setOnClickListener(v -> chatViewManager.scrollToBottom());
        binding.newMessagesBtn.setOnClickListener(v -> chatViewManager.scrollToBottom());
    }

    @Override
    protected void setUpObservers() {
        super.setUpObservers();
        final Observer<ChatMessage> newMessagesObserver = chatMessage ->
                chatViewManager.insertMessagesToBottom(
                        chatMessage.isByMe() && chatViewManager.findIfExisting(chatMessage) == -1,
                        chatMessage
                );
        viewModel.getNewMessagesLD().observe(this, newMessagesObserver);

        final Observer<List<ChatMessage>> historyMessagesObserver = chatMessageList -> {
            if (binding.swipeRefreshLayout.isRefreshing())
                binding.swipeRefreshLayout.setRefreshing(false);
            if (chatMessageList.isEmpty())
                toastsHelper.showMessageError(getString(R.string.no_more_messages_found));
            else
                chatViewManager.insertMessagesToTop(chatMessageList);
        };
        viewModel.getHistoryMessagesLD().observe(this, historyMessagesObserver);

        final Observer<Integer> connectivityStatusObserver = this::refreshConnectivityStatusUI;
        viewModel.getConnectivityStateLD().observe(this,connectivityStatusObserver);
    }

    private void refreshConnectivityStatusUI(int status){
        switch (status){
            case ConnectivityStates.STATE_DISCONNECTED:
                binding.connectivityStatusTV.setVisibility(View.VISIBLE);
                binding.connectivityStatusTV.setText(R.string.disconnected);
                binding.connectivityStatusTV.setBackgroundColor(Color.RED);
                break;
            case ConnectivityStates.STATE_NOT_CONNECTED:
                binding.connectivityStatusTV.setVisibility(View.VISIBLE);
                binding.connectivityStatusTV.setText(R.string.not_connected);
                binding.connectivityStatusTV.setBackgroundColor(Color.RED);
                break;
            case ConnectivityStates.STATE_RECONNECTING:
                binding.connectivityStatusTV.setVisibility(View.VISIBLE);
                binding.connectivityStatusTV.setText(R.string.reconnecting);
                binding.connectivityStatusTV.setBackgroundColor(Color.YELLOW);
                break;
            case ConnectivityStates.STATE_CONNECTED:
                binding.connectivityStatusTV.setVisibility(View.VISIBLE);
                binding.connectivityStatusTV.setText(R.string.connected);
                binding.connectivityStatusTV.setBackgroundColor(Color.GREEN);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.connectivityStatusTV.setVisibility(View.GONE);
                    }
                },500);
                break;
        }
    }

    private void validateAndSendMessage() {
        final String message = binding.enteredMessageET.getText().toString().trim();
        binding.enteredMessageET.setText("");
        if (!TextUtils.isEmpty(message)) {
            viewModel.sendMessage(message);
        }
    }

    private void openSettingsActivity(){
        SettingsActivity.startActivity(this);
    }

    // Chat Manager Callbacks

    @Override
    public void onRecyclerViewNeedsToBeScrolledManually() {
        binding.newMessagesBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrolledToBottom() {
        scrolledToBottom = true ;
        binding.scrollToBottomLayout.setVisibility(View.GONE);
        binding.newMessagesBtn.setVisibility(View.GONE);
    }

    @Override
    public void onScrolledAwayFromBottom() {
        scrolledToBottom = false ;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!scrolledToBottom)
                    binding.scrollToBottomLayout.setVisibility(View.VISIBLE);
            }
        },200);
    }

}
