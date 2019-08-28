package com.p000hl.android.view.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.GestureDetector.OnGestureListener;

/* renamed from: com.hl.android.view.layout.HLRelativeLayout */
public class HLRelativeLayout extends RelativeLayout implements OnGestureListener {
    private Bitmap currentscreen;

    public HLRelativeLayout(Context context) {
        super(context);
        setBackgroundColor(-1);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = false;
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            Log.e("hl", "touch error", e);
            return result;
        }
    }

    public Bitmap getCurrentScreen() {
        try {
            if (this.currentscreen == null || this.currentscreen.getWidth() != getMeasuredWidth()) {
                if (this.currentscreen != null) {
                    this.currentscreen.recycle();
                }
                this.currentscreen = null;
                try {
                    this.currentscreen = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.RGB_565);
                } catch (OutOfMemoryError e) {
                    this.currentscreen = Bitmap.createBitmap(100, 100, Config.ALPHA_8);
                }
            }
            draw(new Canvas(this.currentscreen));
            return this.currentscreen;
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getPointerCount() >= 2) {
                return true;
            }
            if (e1 == null || e2 == null) {
                return false;
            }
            if (e1.getX() - e2.getX() > 80.0f && Math.abs(velocityX) > 100.0f) {
                BookController.getInstance().flipPage(1);
            }
            if (e1.getX() - e2.getX() < -80.0f && Math.abs(velocityX) > 100.0f) {
                BookController.getInstance().flipPage(-1);
            }
            if (!BookSetting.ISSUBPAGE_ENABLE) {
                return true;
            }
            if (e1.getY() - e2.getY() > 80.0f && Math.abs(velocityX) > 50.0f) {
                BookController.getInstance().flipSubPage(1);
            }
            if (e1.getY() - e2.getY() >= -80.0f || Math.abs(velocityX) <= 50.0f) {
                return true;
            }
            BookController.getInstance().flipSubPage(-1);
            return true;
        } catch (Exception e) {
            Log.e("hl", "load error", e);
            return false;
        }
    }
}
