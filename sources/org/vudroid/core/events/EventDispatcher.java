package org.vudroid.core.events;

import java.util.ArrayList;
import java.util.Iterator;

public class EventDispatcher {
    private final ArrayList<Object> listeners = new ArrayList<>();

    public void dispatch(Event<?> event) {
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            event.dispatchOn(i$.next());
        }
    }

    public void addEventListener(Object listener) {
        this.listeners.add(listener);
    }

    public void removeEventListener(Object listener) {
        this.listeners.remove(listener);
    }
}
