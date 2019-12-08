package com.intcore.internship.livechat.ui.main;

import android.app.Application;
import android.util.Log;

import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.data.DataManager;
import com.intcore.internship.livechat.data.model.ChatMessage;
import com.intcore.internship.livechat.ui.baseClasses.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends BaseViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private static final int HISTORY_MESSAGES_DEFAULT_COUNT = 25 ;

    private DataManager dataManager;

    private MutableLiveData<ChatMessage> newMessagesLD;
    private MutableLiveData<List<ChatMessage>> historyMessagesLD;
    private MutableLiveData<Integer> connectivityStateLD;

    public MainViewModel(@NonNull Application application) {
        super(application);
        dataManager = getCompositionRoot().getDataManager();
        newMessagesLD = dataManager.getNewMessagesLD() ;
        historyMessagesLD = new MutableLiveData<>();
        connectivityStateLD = dataManager.getConnectivityStateLD();
    }

    MutableLiveData<ChatMessage> getNewMessagesLD() {
        return newMessagesLD;
    }

    MutableLiveData<List<ChatMessage>> getHistoryMessagesLD() {
        return historyMessagesLD;
    }

    MutableLiveData<Integer> getConnectivityStateLD() {
        return connectivityStateLD;
    }

    String getSavedLocale() {
        return dataManager.getSavedLocale();
    }

    void getHistoryChatMessages() {
        getCompositeDisposable().add(
                dataManager.getChatMessages(HISTORY_MESSAGES_DEFAULT_COUNT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatMessageList -> historyMessagesLD.postValue(chatMessageList),
                                throwable -> Log.d(TAG, "getHistoryChatMessages Throwable: " + throwable.getMessage())
                        )
        );
    }

    void getHistoryChatMessages(long withTimeStampBefore) {
        getCompositeDisposable().add(
                dataManager.getChatMessages(HISTORY_MESSAGES_DEFAULT_COUNT, withTimeStampBefore)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatMessageList -> historyMessagesLD.postValue(chatMessageList),
                                throwable -> Log.d(TAG, "getHistoryChatMessages(withTimeStampBefore) Throwable: " + throwable.getMessage())
                        )
        );
    }

    void removeChatHistory() {
        getCompositeDisposable().add(
                dataManager.deleteAllChatMessages()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> setToastMessagesLD(getApplication().getString(R.string.chat_history_deleted)),
                                throwable -> Log.d(TAG, "removeChatHistory Throwable: " + throwable.getMessage())
                        )
        );
    }

    void sendMessage(String messageText) {
        dataManager.sendMessage(messageText);
    }
}
