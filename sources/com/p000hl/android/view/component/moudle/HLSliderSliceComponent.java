package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.HLSliderSliceComponent */
public class HLSliderSliceComponent extends LinearLayout implements Component {
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public boolean hasMoveAuto;
    /* access modifiers changed from: private */
    public boolean isFling;
    private ArrayList<Bitmap> lBitmaps;
    /* access modifiers changed from: private */
    public SubSliderSliceComponent lSingleTouchView;
    private Context mContext;
    private ComponentEntity mEntity;
    /* access modifiers changed from: private */
    public int mLayoutHeight;
    private int mLayoutWidth;
    private ArrayList<Bitmap> rBitmaps;
    /* access modifiers changed from: private */
    public SubSliderSliceComponent rSingleTouchView;
    /* access modifiers changed from: private */
    public float totalDy;

    static /* synthetic */ float access$116(HLSliderSliceComponent x0, float x1) {
        float f = x0.totalDy + x1;
        x0.totalDy = f;
        return f;
    }

    public HLSliderSliceComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
        setClickable(true);
    }

    private void loadBitmaps() {
        this.lBitmaps = new ArrayList<>();
        this.rBitmaps = new ArrayList<>();
        ArrayList<String> sourceIDS = ((MoudleComponentEntity) this.mEntity).getSourceIDList();
        for (int i = 0; i < sourceIDS.size(); i++) {
            this.bitmap = BitmapManager.getBitmapFromCache((String) sourceIDS.get(i));
            if (this.bitmap == null || this.bitmap.isRecycled()) {
                this.bitmap = BitmapUtils.getBitMap((String) sourceIDS.get(i), this.mContext);
                BitmapManager.putBitmapCache((String) sourceIDS.get(i), this.bitmap);
            }
            Bitmap bitmapL = BitmapManager.getBitmapFromCache(((String) sourceIDS.get(i)) + "L");
            if (bitmapL == null) {
                bitmapL = BitmapUtils.getBitmap(this.bitmap, 0, 0, this.bitmap.getWidth() / 2, this.bitmap.getHeight());
                BitmapManager.putBitmapCache(((String) sourceIDS.get(i)) + "L", bitmapL);
            }
            if (bitmapL != null) {
                this.lBitmaps.add(bitmapL);
            }
            Bitmap bitmapR = BitmapManager.getBitmapFromCache(((String) sourceIDS.get(i)) + "R");
            if (bitmapR == null) {
                bitmapR = BitmapUtils.getBitmap(this.bitmap, this.bitmap.getWidth() / 2, 0, this.bitmap.getWidth() / 2, this.bitmap.getHeight());
                BitmapManager.putBitmapCache(((String) sourceIDS.get(i)) + "R", bitmapR);
            }
            if (bitmapR != null) {
                this.rBitmaps.add(bitmapR);
            }
            BitmapUtils.recycleBitmap(this.bitmap);
            this.bitmap = null;
        }
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        this.mLayoutWidth = getLayoutParams().width;
        this.mLayoutHeight = getLayoutParams().height;
        loadBitmaps();
        this.lSingleTouchView = new SubSliderSliceComponent(this.mContext, this.lBitmaps, (float) (this.mLayoutWidth / 2), (float) this.mLayoutHeight, false);
        this.rSingleTouchView = new SubSliderSliceComponent(this.mContext, this.rBitmaps, (float) ((this.mLayoutWidth / 2) + (this.mLayoutWidth % 2)), (float) this.mLayoutHeight, true);
        LayoutParams layoutParams = new LayoutParams(this.mLayoutWidth / 2, this.mLayoutHeight);
        LayoutParams layoutParams4right = new LayoutParams((this.mLayoutWidth / 2) + (this.mLayoutWidth % 2), this.mLayoutHeight);
        setOrientation(0);
        addView(this.lSingleTouchView, layoutParams);
        addView(this.rSingleTouchView, layoutParams4right);
        final GestureDetector detector = new GestureDetector(this.mContext, new OnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            public void onShowPress(MotionEvent e) {
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!HLSliderSliceComponent.this.hasMoveAuto) {
                    HLSliderSliceComponent.access$116(HLSliderSliceComponent.this, distanceY);
                    float curlPosition = HLSliderSliceComponent.this.lSingleTouchView.getPosition();
                    float currPosition = HLSliderSliceComponent.this.rSingleTouchView.getPosition();
                    if (e2.getX() < ((float) HLSliderSliceComponent.this.getLayoutParams().width) / 2.0f) {
                        HLSliderSliceComponent.this.lSingleTouchView.setposition(curlPosition - distanceY);
                        HLSliderSliceComponent.this.rSingleTouchView.setposition(currPosition + distanceY);
                    } else {
                        HLSliderSliceComponent.this.lSingleTouchView.setposition(curlPosition + distanceY);
                        HLSliderSliceComponent.this.rSingleTouchView.setposition(currPosition - distanceY);
                    }
                }
                return false;
            }

            public void onLongPress(MotionEvent e) {
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityY) > Math.abs(velocityX)) {
                    HLSliderSliceComponent.this.isFling = true;
                    HLSliderSliceComponent.this.hasMoveAuto = true;
                    if (e2.getX() > ((float) HLSliderSliceComponent.this.getLayoutParams().width) / 2.0f) {
                        if (velocityY >= 0.0f) {
                            if (HLSliderSliceComponent.this.totalDy < 0.0f) {
                                HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                                HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            } else {
                                HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                                HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                            }
                        } else if (HLSliderSliceComponent.this.totalDy < 0.0f) {
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        } else {
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                        }
                    } else if (velocityY >= 0.0f) {
                        if (HLSliderSliceComponent.this.totalDy < 0.0f) {
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                        } else {
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        }
                    } else if (HLSliderSliceComponent.this.totalDy < 0.0f) {
                        HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                    } else {
                        HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                        HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                    }
                }
                return false;
            }

            public boolean onDown(MotionEvent e) {
                if (!HLSliderSliceComponent.this.lSingleTouchView.isMoveToUp && !HLSliderSliceComponent.this.lSingleTouchView.isMoveToDown) {
                    HLSliderSliceComponent.this.hasMoveAuto = false;
                    HLSliderSliceComponent.this.totalDy = 0.0f;
                    HLSliderSliceComponent.this.isFling = false;
                }
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                detector.onTouchEvent(event);
                if (event.getAction() == 1 && !HLSliderSliceComponent.this.isFling && !HLSliderSliceComponent.this.hasMoveAuto) {
                    HLSliderSliceComponent.this.hasMoveAuto = true;
                    if (event.getX() > ((float) HLSliderSliceComponent.this.getLayoutParams().width) / 2.0f) {
                        if (Math.abs(HLSliderSliceComponent.this.totalDy) > ((float) HLSliderSliceComponent.this.getLayoutParams().height) / 2.0f) {
                            if (HLSliderSliceComponent.this.totalDy <= 0.0f) {
                                HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                                HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            } else {
                                HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                                HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            }
                        } else if (HLSliderSliceComponent.this.totalDy <= 0.0f) {
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        } else {
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        }
                    } else if (Math.abs(HLSliderSliceComponent.this.totalDy) > ((float) HLSliderSliceComponent.this.getLayoutParams().height) / 2.0f) {
                        if (HLSliderSliceComponent.this.totalDy <= 0.0f) {
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                        } else {
                            HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                            HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(((float) HLSliderSliceComponent.this.mLayoutHeight) - Math.abs(HLSliderSliceComponent.this.totalDy), true);
                        }
                    } else if (HLSliderSliceComponent.this.totalDy <= 0.0f) {
                        HLSliderSliceComponent.this.rSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        HLSliderSliceComponent.this.lSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                    } else {
                        HLSliderSliceComponent.this.rSingleTouchView.MoveToUp(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                        HLSliderSliceComponent.this.lSingleTouchView.MoveToDown(Math.abs(HLSliderSliceComponent.this.totalDy), false);
                    }
                }
                return false;
            }
        });
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        for (int i = 0; i < this.lSingleTouchView.rects.size(); i++) {
            BitmapUtils.recycleBitmap(((MyRect) this.lSingleTouchView.rects.get(i)).mDrawBitmap);
        }
        for (int i2 = 0; i2 < this.rSingleTouchView.rects.size(); i2++) {
            BitmapUtils.recycleBitmap(((MyRect) this.rSingleTouchView.rects.get(i2)).mDrawBitmap);
        }
        BitmapUtils.recycleBitmaps(this.lBitmaps);
        BitmapUtils.recycleBitmaps(this.rBitmaps);
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
