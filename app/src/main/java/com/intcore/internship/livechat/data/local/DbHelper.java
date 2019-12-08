package com.intcore.internship.livechat.data.local;

import android.content.Context;

import com.intcore.internship.livechat.data.local.database.ApplicationDatabase;
import com.intcore.internship.livechat.data.local.database.DbConstants;
import com.intcore.internship.livechat.data.model.ChatMessage;

import java.util.List;

import androidx.room.Room;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DbHelper {

    private ApplicationDatabase applicationDatabase ;
    private Context applicationContext;

    public DbHelper(Context applicationContext) {
        this.applicationContext = applicationContext;
        initiateDatabase();
    }

    private void initiateDatabase() {
        applicationDatabase = Room.databaseBuilder(applicationContext, ApplicationDatabase.class, DbConstants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public Single<Long> insertChatMessage(ChatMessage chatMessageList) {
        return applicationDatabase.chatMessageDao().insertChatMessage(chatMessageList);
    }

    public Completable insertChatMessageList(List<ChatMessage> chatMessageList){
        return applicationDatabase.chatMessageDao().insertChatMessageList(chatMessageList);
    }

    public Single<List<ChatMessage>> getChatMessages(int count){
        return applicationDatabase.chatMessageDao().getChatMessages(count);
    }

    public Single<List<ChatMessage>> getChatMessages(int count, long withTimeStampBefore){
        return applicationDatabase.chatMessageDao().getChatMessages(count, withTimeStampBefore);
    }

    public Completable removeChatMessage(int messageID){
        return applicationDatabase.chatMessageDao().removeChatMessage(messageID);
    }

    public Completable deleteAllChatMessages(){
        return applicationDatabase.chatMessageDao().deleteAllChatMessages();
    }

    public Completable updateChatMessage(ChatMessage chatMessage){
        return applicationDatabase.chatMessageDao().updateChatMessage(chatMessage);
    }

}
