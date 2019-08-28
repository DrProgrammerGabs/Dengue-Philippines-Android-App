package org.vudroid.core.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import org.vudroid.core.events.BringUpZoomControlsListener;
import org.vudroid.core.models.ZoomModel;

public class PageViewZoomControls extends LinearLayout implements BringUpZoomControlsListener {
    public PageViewZoomControls(Context context, ZoomModel zoomModel) {
        super(context);
        show();
        setOrientation(0);
        setGravity(80);
        addView(new ZoomRoll(context, zoomModel));
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void toggleZoomControls() {
        if (getVisibility() == 0) {
            hide();
        } else {
            show();
        }
    }

    private void show() {
        fade(0, (float) getWidth(), 0.0f);
    }

    private void hide() {
        fade(8, 0.0f, (float) getWidth());
    }

    private void fade(int visibility, float startDelta, float endDelta) {
        Animation anim = new TranslateAnimation(0.0f, 0.0f, startDelta, endDelta);
        anim.setDuration(500);
        startAnimation(anim);
        setVisibility(visibility);
    }
}
