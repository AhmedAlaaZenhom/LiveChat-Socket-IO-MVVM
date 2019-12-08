package com.intcore.internship.livechat.data.local.database;

import com.intcore.internship.livechat.data.local.dao.ChatMessageDao;
import com.intcore.internship.livechat.data.model.ChatMessage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class},
        version = DbConstants.DB_VERSION,
        exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract ChatMessageDao chatMessageDao();
}
