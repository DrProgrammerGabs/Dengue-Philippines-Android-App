package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;

/* renamed from: com.hl.android.core.helper.behavior.BehaviorAction */
public class BehaviorAction {
    protected BehaviorEntity mEntity;

    public ViewCell getViewCell(BehaviorEntity behavior) {
        ViewCell viewCell = BookController.getInstance().getViewPage().getCellByID(behavior.FunctionObjectID);
        if (viewCell != null || !StringUtils.isEmpty(behavior.FunctionObjectID)) {
            return viewCell;
        }
        behavior.FunctionObjectID = behavior.triggerComponentID;
        return BookController.getInstance().getViewPage().getCellByID(behavior.FunctionObjectID);
    }

    public void doAction(BehaviorEntity entity) {
        this.mEntity = entity;
    }
}
