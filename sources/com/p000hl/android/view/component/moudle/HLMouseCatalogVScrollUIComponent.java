package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MRenderBean;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.HLMouseCatalogVScrollUIComponent */
public class HLMouseCatalogVScrollUIComponent extends LinearLayout implements Component {
    /* access modifiers changed from: private */
    public float lMT = 1.0f;
    /* access modifiers changed from: private */
    public SubCatalogVScrollUIComponent leftView;
    private ArrayList<Bitmap> leftViewBitmaps;
    private ArrayList<Integer> leftViewIndexs;
    private Context mContext;
    private ComponentEntity mEntity;
    private float mHeight;
    /* access modifiers changed from: private */
    public float mMT = 1.0f;
    /* access modifiers changed from: private */
    public int mWidth;
    /* access modifiers changed from: private */
    public SubCatalogVScrollUIComponent middleView;
    private ArrayList<Bitmap> middleViewBitmaps;
    private ArrayList<Integer> middleViewIndexs;
    /* access modifiers changed from: private */
    public float rMT = 1.0f;
    /* access modifiers changed from: private */
    public SubCatalogVScrollUIComponent rightView;
    private ArrayList<Bitmap> rightViewBitmaps;
    private ArrayList<Integer> rightViewIndexs;
    protected boolean waitDoUpEvent;

    public HLMouseCatalogVScrollUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        this.mWidth = getLayoutParams().width;
        this.mHeight = (float) getLayoutParams().height;
        this.leftViewBitmaps = new ArrayList<>();
        this.middleViewBitmaps = new ArrayList<>();
        this.rightViewBitmaps = new ArrayList<>();
        this.leftViewIndexs = new ArrayList<>();
        this.middleViewIndexs = new ArrayList<>();
        this.rightViewIndexs = new ArrayList<>();
        for (int i = 0; i < ((MoudleComponentEntity) this.mEntity).leftRenderBean.size(); i++) {
            Bitmap bitmap = BitmapUtils.getBitMap(((MRenderBean) ((MoudleComponentEntity) this.mEntity).leftRenderBean.get(i)).sourceID, this.mContext);
            int sourceIndex = ((MRenderBean) ((MoudleComponentEntity) this.mEntity).leftRenderBean.get(i)).sourceIndex;
            this.leftViewBitmaps.add(bitmap);
            this.leftViewIndexs.add(Integer.valueOf(sourceIndex));
        }
        for (int i2 = 0; i2 < ((MoudleComponentEntity) this.mEntity).middleRenderBean.size(); i2++) {
            Bitmap bitmap2 = BitmapUtils.getBitMap(((MRenderBean) ((MoudleComponentEntity) this.mEntity).middleRenderBean.get(i2)).sourceID, this.mContext);
            int sourceIndex2 = ((MRenderBean) ((MoudleComponentEntity) this.mEntity).middleRenderBean.get(i2)).sourceIndex;
            this.middleViewBitmaps.add(bitmap2);
            this.middleViewIndexs.add(Integer.valueOf(sourceIndex2));
        }
        for (int i3 = 0; i3 < ((MoudleComponentEntity) this.mEntity).rightRenderBean.size(); i3++) {
            Bitmap bitmap3 = BitmapUtils.getBitMap(((MRenderBean) ((MoudleComponentEntity) this.mEntity).rightRenderBean.get(i3)).sourceID, this.mContext);
            int sourceIndex3 = ((MRenderBean) ((MoudleComponentEntity) this.mEntity).rightRenderBean.get(i3)).sourceIndex;
            this.rightViewBitmaps.add(bitmap3);
            this.rightViewIndexs.add(Integer.valueOf(sourceIndex3));
        }
        this.leftView = new SubCatalogVScrollUIComponent(this.mContext, this.leftViewBitmaps, this.leftViewIndexs, (float) (this.mWidth / 3), this.mHeight);
        this.middleView = new SubCatalogVScrollUIComponent(this.mContext, this.middleViewBitmaps, this.middleViewIndexs, (float) ((this.mWidth / 3) + (this.mWidth % 3)), this.mHeight);
        this.rightView = new SubCatalogVScrollUIComponent(this.mContext, this.rightViewBitmaps, this.rightViewIndexs, (float) (this.mWidth / 3), this.mHeight);
        setOrientation(0);
        LayoutParams layoutParams = new LayoutParams(this.mWidth / 3, -2);
        LayoutParams layoutParams4middle = new LayoutParams((this.mWidth / 3) + (this.mWidth % 3), -2);
        addView(this.leftView, layoutParams);
        addView(this.middleView, layoutParams4middle);
        addView(this.rightView, layoutParams);
        this.leftView.doBeginAnim();
        this.middleView.doBeginAnim();
        this.rightView.doBeginAnim();
        setClickable(true);
        final GestureDetector detector = new GestureDetector(this.mContext, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent event) {
                HLMouseCatalogVScrollUIComponent.this.waitDoUpEvent = true;
                return false;
            }

            public void onLongPress(MotionEvent e) {
                HLMouseCatalogVScrollUIComponent.this.waitDoUpEvent = true;
            }

            public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
                for (int i = 0; i < HLMouseCatalogVScrollUIComponent.this.leftView.getRects().size(); i++) {
                    MyRect myRect = (MyRect) HLMouseCatalogVScrollUIComponent.this.leftView.getRects().get(i);
                    myRect.mPositionY -= (int) (HLMouseCatalogVScrollUIComponent.this.lMT * arg3);
                }
                for (int i2 = 0; i2 < HLMouseCatalogVScrollUIComponent.this.middleView.getRects().size(); i2++) {
                    MyRect myRect2 = (MyRect) HLMouseCatalogVScrollUIComponent.this.middleView.getRects().get(i2);
                    myRect2.mPositionY -= (int) (HLMouseCatalogVScrollUIComponent.this.mMT * arg3);
                }
                for (int i3 = 0; i3 < HLMouseCatalogVScrollUIComponent.this.rightView.getRects().size(); i3++) {
                    MyRect myRect3 = (MyRect) HLMouseCatalogVScrollUIComponent.this.rightView.getRects().get(i3);
                    myRect3.mPositionY -= (int) (HLMouseCatalogVScrollUIComponent.this.rMT * arg3);
                }
                return false;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                HLMouseCatalogVScrollUIComponent.this.leftView.moveAutoWidthSpeed(velocityY * 0.01f * HLMouseCatalogVScrollUIComponent.this.lMT);
                HLMouseCatalogVScrollUIComponent.this.middleView.moveAutoWidthSpeed(velocityY * 0.01f * HLMouseCatalogVScrollUIComponent.this.mMT);
                HLMouseCatalogVScrollUIComponent.this.rightView.moveAutoWidthSpeed(velocityY * 0.01f * HLMouseCatalogVScrollUIComponent.this.rMT);
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (!HLMouseCatalogVScrollUIComponent.this.leftView.startBeginAnim && !HLMouseCatalogVScrollUIComponent.this.middleView.startBeginAnim && !HLMouseCatalogVScrollUIComponent.this.rightView.startBeginAnim) {
                    if (event.getAction() == 0) {
                        HLMouseCatalogVScrollUIComponent.this.leftView.mIsMoveAuto = false;
                        HLMouseCatalogVScrollUIComponent.this.middleView.mIsMoveAuto = false;
                        HLMouseCatalogVScrollUIComponent.this.rightView.mIsMoveAuto = false;
                    }
                    if (event.getX() <= ((float) HLMouseCatalogVScrollUIComponent.this.mWidth) / 3.0f) {
                        HLMouseCatalogVScrollUIComponent.this.lMT = 1.0f;
                        HLMouseCatalogVScrollUIComponent.this.mMT = 1.8f;
                        HLMouseCatalogVScrollUIComponent.this.rMT = 1.5f;
                    } else if (event.getX() <= ((float) (HLMouseCatalogVScrollUIComponent.this.mWidth * 2)) / 3.0f) {
                        HLMouseCatalogVScrollUIComponent.this.lMT = 1.8f;
                        HLMouseCatalogVScrollUIComponent.this.mMT = 1.0f;
                        HLMouseCatalogVScrollUIComponent.this.rMT = 1.5f;
                    } else {
                        HLMouseCatalogVScrollUIComponent.this.lMT = 1.5f;
                        HLMouseCatalogVScrollUIComponent.this.mMT = 1.8f;
                        HLMouseCatalogVScrollUIComponent.this.rMT = 1.0f;
                    }
                    detector.onTouchEvent(event);
                    if (event.getAction() == 1) {
                        if (HLMouseCatalogVScrollUIComponent.this.waitDoUpEvent) {
                            if (event.getX() > ((float) HLMouseCatalogVScrollUIComponent.this.mWidth) / 3.0f) {
                                if (event.getX() > ((float) (HLMouseCatalogVScrollUIComponent.this.mWidth * 2)) / 3.0f) {
                                    int i = 0;
                                    while (true) {
                                        if (i >= HLMouseCatalogVScrollUIComponent.this.rightView.getRects().size()) {
                                            break;
                                        }
                                        MyRect curRect = (MyRect) HLMouseCatalogVScrollUIComponent.this.rightView.getRects().get(i);
                                        if (HLMouseCatalogVScrollUIComponent.this.touchInTheRect(event, ((float) (HLMouseCatalogVScrollUIComponent.this.mWidth * 2)) / 3.0f, (float) curRect.mPositionY, curRect.mInWidth, (float) curRect.mInHeight)) {
                                            HLMouseCatalogVScrollUIComponent.this.doClickItemEvent(curRect.mIndex);
                                            break;
                                        }
                                        i++;
                                    }
                                } else {
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 >= HLMouseCatalogVScrollUIComponent.this.middleView.getRects().size()) {
                                            break;
                                        }
                                        MyRect curRect2 = (MyRect) HLMouseCatalogVScrollUIComponent.this.middleView.getRects().get(i2);
                                        if (HLMouseCatalogVScrollUIComponent.this.touchInTheRect(event, ((float) HLMouseCatalogVScrollUIComponent.this.mWidth) / 3.0f, (float) curRect2.mPositionY, curRect2.mInWidth, (float) curRect2.mInHeight)) {
                                            HLMouseCatalogVScrollUIComponent.this.doClickItemEvent(curRect2.mIndex);
                                            break;
                                        }
                                        i2++;
                                    }
                                }
                            } else {
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= HLMouseCatalogVScrollUIComponent.this.leftView.getRects().size()) {
                                        break;
                                    }
                                    MyRect curRect3 = (MyRect) HLMouseCatalogVScrollUIComponent.this.leftView.getRects().get(i3);
                                    if (HLMouseCatalogVScrollUIComponent.this.touchInTheRect(event, 0.0f, (float) curRect3.mPositionY, curRect3.mInWidth, (float) curRect3.mInHeight)) {
                                        HLMouseCatalogVScrollUIComponent.this.doClickItemEvent(curRect3.mIndex);
                                        break;
                                    }
                                    i3++;
                                }
                            }
                        }
                        HLMouseCatalogVScrollUIComponent.this.waitDoUpEvent = false;
                    }
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean touchInTheRect(MotionEvent event, float x, float y, float width, float height) {
        if (event.getX() <= x || event.getX() >= x + width || event.getY() <= y || event.getY() >= y + height) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void doClickItemEvent(int i) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                BehaviorHelper.doBeheavorForList(behavior, i, this.mEntity.componentId);
            }
        }
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        for (int i = 0; i < this.leftView.getRects().size(); i++) {
            BitmapUtils.recycleBitmap(((MyRect) this.leftView.getRects().get(i)).mDrawBitmap);
        }
        for (int i2 = 0; i2 < this.middleView.getRects().size(); i2++) {
            BitmapUtils.recycleBitmap(((MyRect) this.middleView.getRects().get(i2)).mDrawBitmap);
        }
        for (int i3 = 0; i3 < this.rightView.getRects().size(); i3++) {
            BitmapUtils.recycleBitmap(((MyRect) this.rightView.getRects().get(i3)).mDrawBitmap);
        }
        BitmapUtils.recycleBitmaps(this.leftViewBitmaps);
        BitmapUtils.recycleBitmaps(this.middleViewBitmaps);
        BitmapUtils.recycleBitmaps(this.rightViewBitmaps);
    }

    public void hide() {
        setVisibility(4);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }
}
