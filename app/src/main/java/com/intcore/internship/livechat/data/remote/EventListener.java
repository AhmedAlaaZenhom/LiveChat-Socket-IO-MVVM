package com.intcore.internship.livechat.data.remote;

import com.intcore.internship.livechat.data.model.ChatMessage;

public interface EventListener {

    void onConnect();

    void onConnectionError() ;

    void onReconnect();

    void onDisconnect();

    void onNewMessage(ChatMessage message);

}
