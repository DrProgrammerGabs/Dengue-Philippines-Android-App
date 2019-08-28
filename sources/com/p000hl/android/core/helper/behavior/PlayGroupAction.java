package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.PlayGroupAction */
public class PlayGroupAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        BookController.getInstance().getViewPage().startPlay();
    }
}
