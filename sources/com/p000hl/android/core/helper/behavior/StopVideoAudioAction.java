package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.TimerComponent;

/* renamed from: com.hl.android.core.helper.behavior.StopVideoAudioAction */
public class StopVideoAudioAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell == null) {
            return;
        }
        if (viewCell.getComponent() instanceof TimerComponent) {
            ((TimerComponent) viewCell.getComponent()).stopTimer();
        } else {
            viewCell.stop();
        }
    }
}
