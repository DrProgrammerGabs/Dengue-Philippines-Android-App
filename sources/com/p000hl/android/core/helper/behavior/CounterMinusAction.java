package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.HLCounterComponent;

/* renamed from: com.hl.android.core.helper.behavior.CounterMinusAction */
public class CounterMinusAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell != null) {
            ((HLCounterComponent) viewCell.getComponent()).minus(Integer.valueOf(entity.Value).intValue());
        }
    }
}
