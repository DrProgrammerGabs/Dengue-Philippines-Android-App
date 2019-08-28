package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.PageChangeAction */
public class PageChangeAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        PageEntity pageEntity = BookController.getInstance().getViewPage().getEntity();
        String effectType = pageEntity.getPageChangeEffectType();
        String effectDir = pageEntity.getPageChangeEffectDir();
        long duration = pageEntity.getPageChangeEffectDuration();
        if (effectType == null || effectType.equals("")) {
            BookController.getInstance().changePageById(entity.Value);
        } else {
            BookController.getInstance().changePageWithEffect(effectType, effectDir, duration, 0, entity.Value);
        }
    }
}
