package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.BookState;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.moudle.HLMouseVerScrollNoSelectedComponent */
public class HLMouseVerScrollNoSelectedComponent extends ScrollView implements Component {
    private boolean isFliped;
    private Context mContext;
    private MoudleComponentEntity mEntity;
    MotionEvent oldMotion;

    public HLMouseVerScrollNoSelectedComponent(Context context, ComponentEntity entity) {
        this(context);
        setEntity(entity);
    }

    public HLMouseVerScrollNoSelectedComponent(Context context) {
        super(context);
        this.isFliped = false;
        this.oldMotion = null;
        this.mContext = context;
        setVerticalScrollBarEnabled(false);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        LinearLayout layout = new LinearLayout(this.mContext);
        layout.setOrientation(1);
        int width = this.mEntity.getItemWidth();
        int width2 = (int) ScreenUtils.getHorScreenValue((float) width);
        int height = (int) ScreenUtils.getVerScreenValue((float) this.mEntity.getItemHeight());
        LayoutParams lp = new LayoutParams(width2, height);
        String sourceID = (String) this.mEntity.getSourceIDList().get(0);
        ImageView imageView = new ImageView(getContext());
        imageView.measure(MeasureSpec.makeMeasureSpec(lp.width, 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
        Bitmap bitmap = BitmapUtils.getBitMap(sourceID, this.mContext, width2, height);
        if (bitmap == null) {
            Toast.makeText(this.mContext, "图片为空", 1).show();
            return;
        }
        imageView.setImageBitmap(bitmap);
        layout.addView(imageView, lp);
        addView(layout);
        requestLayout();
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.oldMotion = MotionEvent.obtain(ev);
                super.onTouchEvent(ev);
                break;
            case 2:
                float disY = ev.getY() - this.oldMotion.getY();
                float disX = ev.getX() - this.oldMotion.getX();
                if (!this.isFliped && Math.abs(disY) < 100.0f && Math.abs(disX) > 100.0f) {
                    this.isFliped = true;
                    if (disX <= 0.0f) {
                        if (!BookState.getInstance().isFliping) {
                            BookController.getInstance().flipPage(1);
                            break;
                        }
                    } else if (!BookState.getInstance().isFliping) {
                        BookController.getInstance().flipPage(-1);
                        break;
                    }
                } else {
                    super.onTouchEvent(ev);
                    break;
                }
                break;
        }
        return true;
    }
}
