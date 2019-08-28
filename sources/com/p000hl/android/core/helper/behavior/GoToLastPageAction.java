package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.GoToLastPageAction */
public class GoToLastPageAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        if (BookController.lastPageID != null && BookController.lastPageID.trim().length() != 0) {
            BookController.getInstance().playPageById(BookController.lastPageID);
        }
    }
}
