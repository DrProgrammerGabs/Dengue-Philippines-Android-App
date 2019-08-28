package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.WebComponent;

/* renamed from: com.hl.android.core.helper.behavior.ChangeUrlAction */
public class ChangeUrlAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell != null) {
            ((WebComponent) viewCell.getComponent()).changeUrl(entity.Value);
        }
    }
}
