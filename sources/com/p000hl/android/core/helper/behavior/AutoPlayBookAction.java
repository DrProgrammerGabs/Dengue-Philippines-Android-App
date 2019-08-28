package com.p000hl.android.core.helper.behavior;

import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.common.BookSetting;

/* renamed from: com.hl.android.core.helper.behavior.AutoPlayBookAction */
public class AutoPlayBookAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        BookSetting.IS_AUTOPAGE = true;
    }
}
