package com.intcore.internship.livechat.data;

import android.util.Log;

import com.intcore.internship.livechat.ApplicationClass;
import com.intcore.internship.livechat.data.local.DbHelper;
import com.intcore.internship.livechat.data.model.ChatMessage;
import com.intcore.internship.livechat.data.model.ConnectivityStates;
import com.intcore.internship.livechat.data.remote.EventListener;
import com.intcore.internship.livechat.data.remote.SocketService;
import com.intcore.internship.livechat.data.sharedPreferences.PreferenceHelper;

import java.net.URISyntaxException;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DataManager implements EventListener {

    private static final String TAG = DataManager.class.getSimpleName() ;

    private MutableLiveData<ChatMessage> newMessagesLD;
    private MutableLiveData<Integer> connectivityStateLD;

    public MutableLiveData<ChatMessage> getNewMessagesLD() {
        return newMessagesLD;
    }

    public MutableLiveData<Integer> getConnectivityStateLD() {
        return connectivityStateLD;
    }

    private ApplicationClass applicationClass ;
    private PreferenceHelper preferenceHelper ;
    private SocketService socketService ;
    private DbHelper dbHelper ;

    public DataManager(ApplicationClass applicationClass) {
        this.applicationClass = applicationClass ;
        preferenceHelper = new PreferenceHelper(applicationClass);
        socketService = new SocketService() ;
        dbHelper = new DbHelper(applicationClass);
        newMessagesLD = new MutableLiveData<>() ;
        connectivityStateLD = new MutableLiveData<>() ;
        connectivityStateLD.postValue(ConnectivityStates.STATE_NOT_CONNECTED);
    }

    private CompositeDisposable getApplicationCompositeDisposable() {
        return applicationClass.getApplicationCompositeDisposable();
    }

    // Mixed

    public void sendMessage(String messageText) {
        // First insert message to local database, and onSuccess send it to server
        ChatMessage chatMessage = new ChatMessage(true, getUserName(), messageText, System.currentTimeMillis(), false, false);
        getApplicationCompositeDisposable().add(
                dbHelper.insertChatMessage(chatMessage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageID -> {
                                    chatMessage.setId((int) (long) messageID);
                                    newMessagesLD.postValue(chatMessage);
                                    sendMessageToServer(chatMessage);
                                },
                                throwable -> Log.d(TAG, throwable.getMessage())
                        )
        );
    }

    // Shared Preferences

    public void setCurrentLocale(String locale) {
        preferenceHelper.setCurrentLocale(locale);
    }

    public String getSavedLocale(){
        return preferenceHelper.getSavedLocale() ;
    }

    public void loginWithUserData(String userName){
        preferenceHelper.saveUserName(userName);
        preferenceHelper.setUserLoggedIn();
    }

    public boolean isUserLoggedIn(){
        return preferenceHelper.isUserLoggedIn() ;
    }

    private String getUserName(){
        return preferenceHelper.getUserName() ;
    }

    // Local

    public Single<List<ChatMessage>> getChatMessages(int count){
        return dbHelper.getChatMessages(count);
    }

    public Single<List<ChatMessage>> getChatMessages(int count, long withTimeStampBefore){
        return dbHelper.getChatMessages(count, withTimeStampBefore);
    }

    public Completable removeChatMessage(int messageID){
        return dbHelper.removeChatMessage(messageID);
    }

    public Completable deleteAllChatMessages(){
        return dbHelper.deleteAllChatMessages();
    }

    private void updateChatMessage(ChatMessage chatMessage) {
        getApplicationCompositeDisposable().add(
                dbHelper.updateChatMessage(chatMessage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> newMessagesLD.postValue(chatMessage),
                                throwable -> Log.d(TAG, "updateChatMessage Throwable: " + throwable.getMessage())
                        )
        );
    }

    // Remote

    public void startListeningToServer(){
        if(!isUserLoggedIn())
            return;
        try {
            socketService.registerListener(this);
            socketService.startListening(getUserName());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void stopListeningToServer(){
        if(!isUserLoggedIn())
            return;
        socketService.unregisterListener(this);
        socketService.stopListening();
    }

    private void sendMessageToServer(ChatMessage chatMessage) {
        getApplicationCompositeDisposable().add(
                socketService.sendMessage(chatMessage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                sentChatMessage -> {
                                    sentChatMessage.setSent(true);
                                    updateChatMessage(sentChatMessage);
                                },
                                throwable -> Log.d(TAG, throwable.getMessage())
                        )
        );
    }


    // Server callbacks

    @Override
    public void onConnect() {
        connectivityStateLD.postValue(ConnectivityStates.STATE_CONNECTED);
    }

    @Override
    public void onConnectionError() {
        connectivityStateLD.postValue(ConnectivityStates.STATE_NOT_CONNECTED);
    }

    @Override
    public void onReconnect() {
        connectivityStateLD.postValue(ConnectivityStates.STATE_RECONNECTING);
    }

    @Override
    public void onDisconnect() {
        connectivityStateLD.postValue(ConnectivityStates.STATE_DISCONNECTED);
    }

    @Override
    public void onNewMessage(ChatMessage chatMessage) {
        if(chatMessage.getUserNickName().equals(getUserName()))
            chatMessage.setByMe(true);
        getApplicationCompositeDisposable().add(
                dbHelper.insertChatMessage(chatMessage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(id -> {
                            chatMessage.setId((int) (long) id);
                            newMessagesLD.postValue(chatMessage);
                        })
        );
    }

}
