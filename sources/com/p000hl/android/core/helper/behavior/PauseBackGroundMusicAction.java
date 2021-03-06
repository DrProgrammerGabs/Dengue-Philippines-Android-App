package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.PauseBackGroundMusicAction */
public class PauseBackGroundMusicAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        if (BookController.getInstance().getBackgroundMusic() != null) {
            BookController.getInstance().getBackgroundMusic().pause();
        }
    }
}
