package com.intcore.internship.livechat.data.model;

public class ConnectivityStates {

    private ConnectivityStates() {
        // non-instantiable class
    }

    public static final int STATE_NOT_CONNECTED = 0 ;

    public static final int STATE_RECONNECTING = 1 ;

    public static final int STATE_CONNECTED = 2 ;

    public static final int STATE_DISCONNECTED = 3 ;

}
