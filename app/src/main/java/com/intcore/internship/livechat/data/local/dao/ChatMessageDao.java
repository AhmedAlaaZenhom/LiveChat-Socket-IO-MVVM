package com.intcore.internship.livechat.data.local.dao;

import com.intcore.internship.livechat.data.model.ChatMessage;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Single;

import static com.intcore.internship.livechat.data.local.database.DbConstants.TABLE_CHAT_MESSAGES;

@Dao
public interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertChatMessage(ChatMessage chatMessageList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChatMessageList(List<ChatMessage> chatMessageList);

    @Query("SELECT * FROM "
            + "( SELECT * FROM " + TABLE_CHAT_MESSAGES + " ORDER BY time_stamp DESC LIMIT :count )"
            + " ORDER BY time_stamp ASC")
    Single<List<ChatMessage>> getChatMessages(int count);

    @Query("SELECT * FROM "
            + "( SELECT * FROM " + TABLE_CHAT_MESSAGES + " WHERE time_stamp < :withTimeStampBefore" + " ORDER BY time_stamp DESC LIMIT :count )"
            + " ORDER BY time_stamp ASC")
    Single<List<ChatMessage>> getChatMessages(int count, long withTimeStampBefore);

    @Query("UPDATE " + TABLE_CHAT_MESSAGES + " SET removed = " + true + " WHERE id = :messageID")
    Completable removeChatMessage(int messageID);

    @Query("DELETE FROM "+TABLE_CHAT_MESSAGES)
    Completable deleteAllChatMessages();

    @Update
    Completable updateChatMessage(ChatMessage chatMessage);

}
