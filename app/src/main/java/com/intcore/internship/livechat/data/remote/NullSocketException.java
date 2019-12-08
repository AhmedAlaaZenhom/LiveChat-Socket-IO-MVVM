package com.intcore.internship.livechat.data.remote;

public class NullSocketException extends Exception {

    private static final String MESSAGE = "Trying to use socket with a null pointer";

    public NullSocketException() {
        super(MESSAGE);
    }

}
