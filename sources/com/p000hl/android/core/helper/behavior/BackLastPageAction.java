package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.BackLastPageAction */
public class BackLastPageAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        if (BookController.lastPageID != null && BookController.lastPageID.trim().length() != 0) {
            PageEntity pageEntity = BookController.getInstance().getViewPage().getEntity();
            String effectType = pageEntity.getPageChangeEffectType();
            String effectDir = pageEntity.getPageChangeEffectDir();
            long duration = pageEntity.getPageChangeEffectDuration();
            if (effectType == null || effectType.equals("")) {
                BookController.getInstance().changePageById(BookController.lastPageID);
            } else {
                BookController.getInstance().changePageWithEffect(effectType, effectDir, duration, 0, BookController.lastPageID);
            }
        }
    }
}
