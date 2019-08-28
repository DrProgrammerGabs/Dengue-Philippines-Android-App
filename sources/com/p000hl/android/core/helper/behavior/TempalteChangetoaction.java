package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.HLSliderEffectComponent;
import com.p000hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent;
import com.p000hl.android.view.component.moudle.HLViewFlipperInter;
import com.p000hl.android.view.component.moudle.HLViewFlipperVerticleInter;

/* renamed from: com.hl.android.core.helper.behavior.TempalteChangetoaction */
public class TempalteChangetoaction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        ViewCell viewCell = getViewCell(entity);
        if (viewCell == null) {
            return;
        }
        if (viewCell.getComponent() instanceof HLVerSlideImageSelectUIComponent) {
            ((HLVerSlideImageSelectUIComponent) viewCell.getComponent()).doSelectItemEvent(Integer.parseInt(entity.Value));
        } else if (viewCell.getComponent() instanceof HLViewFlipperVerticleInter) {
            ((HLViewFlipperVerticleInter) viewCell.getComponent()).doClickCircle(Integer.parseInt(entity.Value));
        } else if (viewCell.getComponent() instanceof HLViewFlipperInter) {
            ((HLViewFlipperInter) viewCell.getComponent()).doClickCircle(Integer.parseInt(entity.Value));
        } else if (viewCell.getComponent() instanceof HLSliderEffectComponent) {
            ((HLSliderEffectComponent) viewCell.getComponent()).doChangeToAction(Integer.parseInt(entity.Value));
        }
    }
}
