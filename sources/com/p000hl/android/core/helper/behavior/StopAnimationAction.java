package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.core.helper.AnimationHelper;
import com.p000hl.android.view.ViewCell;

/* renamed from: com.hl.android.core.helper.behavior.StopAnimationAction */
public class StopAnimationAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell != null) {
            AnimationHelper.stopAnimation(viewCell);
        }
    }
}
