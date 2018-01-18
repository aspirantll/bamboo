package com.flushest.bamboo.framework.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public interface EventSource {
    List<Listener> listeners = new ArrayList<>();

    default void register(Listener listener) {
        listeners.add(listener);
    }

    default void notifyChanges(Event event) {
        listeners.forEach(listener -> {
            listener.notifyChange(event);
        });
    }
}
