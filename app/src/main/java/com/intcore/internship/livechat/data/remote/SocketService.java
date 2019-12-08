package com.intcore.internship.livechat.data.remote;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.intcore.internship.livechat.data.model.ChatMessage;
import com.intcore.internship.livechat.utils.BaseObservable;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.reactivex.Single;

public class SocketService extends BaseObservable<EventListener> {

    private static final String TAG = SocketService.class.getSimpleName() ;

    private static final String SOCKET_URL = "https://socket-io-chat.now.sh";
    private static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    private static final String EVENT_RECONNECT = Socket.EVENT_RECONNECT;
    private static final String EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    private static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    private static final String EVENT_NEW_MESSAGE = "new message";

    private Socket socket ;
    private String userNickname ;

    private Emitter.Listener connectListener = args -> {
        Log.d(TAG,"onConnect ...");
        socket.emit("add user", userNickname);
        for(EventListener listener: getListeners())
            listener.onConnect();
    };

    private Emitter.Listener reconnectListener = args -> {
        Log.d(TAG,"onReconnect ...");
        for(EventListener listener: getListeners())
            listener.onReconnect();
    };

    private Emitter.Listener connectionErrorListener = args -> {
        Log.d(TAG,"onConnectionError ...");
        for(EventListener listener: getListeners())
            listener.onConnectionError();
    };

    private Emitter.Listener disconnectListener = args -> {
        Log.d(TAG,"onDisconnect ...");
        socket.off();
        for(EventListener listener: getListeners())
            listener.onDisconnect();
    };

    private Emitter.Listener newMessageListener = args -> {
        final String rawMessage = args[0].toString() ;
        Log.d(TAG,"onNewMessage: "+rawMessage);
        try {
            JSONObject rawMessageObject = new JSONObject(rawMessage);
            final String userNickName = rawMessageObject.getString("username");
            final String messageText = rawMessageObject.getString("message");
            final long timeStamp = System.currentTimeMillis();
            ChatMessage chatMessage = new ChatMessage(
                    false,
                    userNickName,
                    messageText,
                    timeStamp,
                    false,
                    true);
            for (EventListener listener : getListeners())
                listener.onNewMessage(chatMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public void startListening(String userNickName) throws URISyntaxException {
        this.userNickname = userNickName ;

        if(socket==null)
            socket = IO.socket(SOCKET_URL) ;

        socket.on(EVENT_CONNECT, connectListener);
        socket.on(EVENT_DISCONNECT, disconnectListener);
        socket.on(EVENT_NEW_MESSAGE, newMessageListener);
        socket.on(EVENT_RECONNECT,reconnectListener);
        socket.on(EVENT_CONNECT_ERROR,connectionErrorListener);

        socket.connect();
    }

    public void stopListening() {
        if (socket != null)
            socket.disconnect();
    }

    public Single<ChatMessage> sendMessage(ChatMessage chatMessage) {
        return Single.create(singleEmitter -> {
            if (socket == null)
                singleEmitter.onError(new NullSocketException());
            socket.emit(EVENT_NEW_MESSAGE, chatMessage.getMessageText());
            chatMessage.setSent(true);
            singleEmitter.onSuccess(chatMessage);
        });
    }
}
