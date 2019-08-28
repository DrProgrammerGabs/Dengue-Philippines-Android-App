package com.p000hl.android.controller;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.GestureDetector.OnGestureListener;
import com.p000hl.android.view.component.inter.Component;
import java.util.ArrayList;

/* renamed from: com.hl.android.controller.EventDispatcher */
public class EventDispatcher implements OnGestureListener {
    private static EventDispatcher eventDispatcher;
    private boolean canDoDetector = true;
    private ArrayList<Component> componentList;
    private GestureDetector detector;
    private boolean isDown;
    private MotionEvent oldEvent;
    float oldTouchValue = 0.0f;
    private float totaldeltaY;

    public static EventDispatcher getInstance() {
        if (eventDispatcher == null) {
            eventDispatcher = new EventDispatcher();
        }
        return eventDispatcher;
    }

    public void init() {
        if (this.componentList != null) {
            this.componentList.clear();
        }
    }

    public void init(Context context) {
        this.detector = new GestureDetector(context, (OnGestureListener) this);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.MotionEvent r8) {
        /*
            r7 = this;
            r6 = 0
            r5 = 0
            r4 = 1
            int r1 = r8.getPointerCount()
            r2 = 2
            if (r1 < r2) goto L_0x000c
            r7.canDoDetector = r6
        L_0x000c:
            if (r8 != 0) goto L_0x000f
        L_0x000e:
            return r4
        L_0x000f:
            com.hl.android.controller.GestureDetector r1 = r7.detector
            if (r1 == 0) goto L_0x000e
            int r1 = r8.getAction()
            switch(r1) {
                case 0: goto L_0x002a;
                case 1: goto L_0x00d5;
                case 2: goto L_0x0043;
                default: goto L_0x001a;
            }
        L_0x001a:
            boolean r1 = r7.canDoDetector
            if (r1 == 0) goto L_0x0023
            com.hl.android.controller.GestureDetector r1 = r7.detector
            r1.onTouchEvent(r8)
        L_0x0023:
            android.view.MotionEvent r1 = android.view.MotionEvent.obtain(r8)
            r7.oldEvent = r1
            goto L_0x000e
        L_0x002a:
            r7.isDown = r4
            r7.totaldeltaY = r5
            r7.canDoDetector = r4
            java.lang.String r1 = "ww"
            java.lang.String r2 = "ACTION_DOWN"
            android.util.Log.d(r1, r2)
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            com.hl.android.view.ViewPage r1 = r1.getViewPage()
            r1.hideMenu()
            goto L_0x001a
        L_0x0043:
            boolean r1 = r7.isDown
            if (r1 != 0) goto L_0x004a
            r7.totaldeltaY = r5
            goto L_0x000e
        L_0x004a:
            java.lang.String r1 = "ww"
            java.lang.String r2 = "ACTION_MOVE"
            android.util.Log.d(r1, r2)
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            com.hl.android.view.ViewPage r1 = r1.getViewPage()
            float r1 = r1.getOffSetY()
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 >= 0) goto L_0x007f
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            com.hl.android.view.ViewPage r1 = r1.getViewPage()
            float r1 = r1.getOffSetY()
            int r2 = com.p000hl.android.common.BookSetting.BOOK_HEIGHT
            com.hl.android.controller.BookController r3 = com.p000hl.android.controller.BookController.getInstance()
            com.hl.android.view.ViewPage r3 = r3.getViewPage()
            int r3 = r3.pageHeight
            int r2 = r2 - r3
            float r2 = (float) r2
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 > 0) goto L_0x001a
        L_0x007f:
            float r1 = r8.getRawY()
            android.view.MotionEvent r2 = r7.oldEvent
            float r2 = r2.getRawY()
            float r0 = r1 - r2
            float r1 = r7.totaldeltaY
            float r1 = r1 + r0
            r7.totaldeltaY = r1
            float r1 = r7.totaldeltaY
            r2 = 1120403456(0x42c80000, float:100.0)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 <= 0) goto L_0x00b3
            com.hl.android.controller.BookState r1 = com.p000hl.android.controller.BookState.getInstance()
            boolean r1 = r1.isFliping
            if (r1 != 0) goto L_0x001a
            int r1 = com.p000hl.android.common.BookSetting.FLIPCODE
            if (r1 != r4) goto L_0x00ac
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            r2 = -1
            r1.flipSubPage(r2)
        L_0x00ac:
            r7.totaldeltaY = r5
            r0 = 0
            r7.isDown = r6
            goto L_0x000e
        L_0x00b3:
            float r1 = r7.totaldeltaY
            r2 = -1027080192(0xffffffffc2c80000, float:-100.0)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 >= 0) goto L_0x001a
            com.hl.android.controller.BookState r1 = com.p000hl.android.controller.BookState.getInstance()
            boolean r1 = r1.isFliping
            if (r1 != 0) goto L_0x001a
            int r1 = com.p000hl.android.common.BookSetting.FLIPCODE
            if (r1 != r4) goto L_0x00ce
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            r1.flipSubPage(r4)
        L_0x00ce:
            r7.totaldeltaY = r5
            r0 = 0
            r7.isDown = r6
            goto L_0x000e
        L_0x00d5:
            java.lang.String r1 = "ww"
            java.lang.String r2 = "ACTION_UP"
            android.util.Log.d(r1, r2)
            com.hl.android.controller.BookController r1 = com.p000hl.android.controller.BookController.getInstance()
            com.hl.android.view.ViewPage r1 = r1.getViewPage()
            r1.playSequence()
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.controller.EventDispatcher.onTouch(android.view.MotionEvent):boolean");
    }

    public void registComponent(Component component) {
        if (this.componentList == null) {
            this.componentList = new ArrayList<>();
        }
        this.componentList.add(component);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (BookSetting.FLIPCODE == 1 && BookSetting.FLIP_CHANGE_PAGE) {
            return false;
        }
        if (BookController.getInstance().mainViewPage == null || BookController.getInstance().mainViewPage.getEntity() == null) {
            return true;
        }
        if (!BookController.getInstance().mainViewPage.getEntity().enablePageTurnByHand || !BookController.getInstance().mainViewPage.getEntity().isEnableNavigation() || e1 == null || e2 == null) {
            return false;
        }
        try {
            if (Math.abs(e1.getX() - e2.getX()) <= Math.abs(e1.getY() - e2.getY())) {
                return false;
            }
            if (e1.getX() - e2.getX() > 80.0f && BookController.getInstance().mainViewPage.isHorMove) {
                BookController.getInstance().flipPage(1);
            }
            if (e1.getX() - e2.getX() >= -80.0f || !BookController.getInstance().mainViewPage.isHorMove) {
                return false;
            }
            BookController.getInstance().flipPage(-1);
            return false;
        } catch (Exception e) {
            Log.e("hl", " onFling ", e);
            return false;
        }
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
