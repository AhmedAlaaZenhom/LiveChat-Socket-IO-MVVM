package com.intcore.internship.livechat.utils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BaseObservable<LISTENER_CLASS> {

    private final Set<LISTENER_CLASS> mListeners = Collections.newSetFromMap(
            new ConcurrentHashMap<LISTENER_CLASS, Boolean>(1));


    public final void registerListener(LISTENER_CLASS listener) {
        mListeners.add(listener);
    }

    public final void unregisterListener(LISTENER_CLASS listener) {
        mListeners.remove(listener);
    }

    protected final Set<LISTENER_CLASS> getListeners() {
        return Collections.unmodifiableSet(mListeners);
    }

}
