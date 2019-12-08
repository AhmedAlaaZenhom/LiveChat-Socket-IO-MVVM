package com.intcore.internship.livechat.data.model;

import com.intcore.internship.livechat.data.local.database.DbConstants;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DbConstants.TABLE_CHAT_MESSAGES)
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "by_me")
    private boolean byMe ;

    @ColumnInfo(name = "user_nick_name")
    private String userNickName ;

    @ColumnInfo(name = "message_text")
    private String messageText ;

    @ColumnInfo(name = "time_stamp")
    private long timeStamp ;

    @ColumnInfo(name = "removed")
    private boolean removed ;

    @ColumnInfo(name = "sent")
    private boolean sent ;

    public ChatMessage(boolean byMe, String userNickName, String messageText, long timeStamp, boolean removed, boolean sent) {
        this.byMe = byMe;
        this.userNickName = userNickName;
        this.messageText = messageText;
        this.timeStamp = timeStamp;
        this.removed = removed;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isByMe() {
        return byMe;
    }

    public void setByMe(boolean byMe) {
        this.byMe = byMe;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
