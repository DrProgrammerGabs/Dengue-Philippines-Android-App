package com.p000hl.android.core.helper.behavior;

import android.content.Intent;
import android.net.Uri;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.core.helper.behavior.CallAction */
public class CallAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        String phone = entity.Value;
        if (phone != null) {
            Intent intent = new Intent("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + phone));
            BookController.getInstance().hlActivity.startActivity(intent);
        }
    }
}
