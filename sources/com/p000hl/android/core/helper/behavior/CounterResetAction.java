package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.HLCounterComponent;

/* renamed from: com.hl.android.core.helper.behavior.CounterResetAction */
public class CounterResetAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell != null) {
            ((HLCounterComponent) viewCell.getComponent()).reset();
        }
    }
}
