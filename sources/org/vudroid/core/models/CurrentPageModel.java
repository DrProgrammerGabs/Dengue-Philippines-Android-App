package org.vudroid.core.models;

import org.vudroid.core.events.CurrentPageListener.CurrentPageChangedEvent;
import org.vudroid.core.events.EventDispatcher;

public class CurrentPageModel extends EventDispatcher {
    private int currentPageIndex;

    public void setCurrentPageIndex(int currentPageIndex2) {
        if (this.currentPageIndex != currentPageIndex2) {
            this.currentPageIndex = currentPageIndex2;
            dispatch(new CurrentPageChangedEvent(currentPageIndex2));
        }
    }
}
