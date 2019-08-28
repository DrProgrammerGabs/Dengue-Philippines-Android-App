package com.p000hl.android.view.component.moudle.slide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"HandlerLeak"})
/* renamed from: com.hl.android.view.component.moudle.slide.HorizontalSlideClick */
public class HorizontalSlideClick extends HorizontalScrollView implements Component, ComponentPost {
    public static boolean CHANGEBUTTON = false;
    public static boolean ISHORIZONTAL = true;
    /* access modifiers changed from: private */
    public int FLAG_MESSAGE = 0;
    Context _con;
    Options _option;
    public AnimationSet animationset = null;
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> bitmapList = new ArrayList<>();
    /* access modifiers changed from: private */
    public boolean cantMove = false;
    boolean clickUp = false;
    ImageView currentButton = null;
    int currentIndex = -1;
    /* access modifiers changed from: private */
    public int direct = 0;
    /* access modifiers changed from: private */
    public ArrayList<String> downIDList;
    ComponentEntity entity;
    LinearLayout galleryrl;
    private ArrayList<String> imagelist;
    boolean isMove = false;
    private int itemHeight;
    private int itemWidth;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (HorizontalSlideClick.this.f46x != HorizontalSlideClick.this.getScrollX()) {
                HorizontalSlideClick.this.f46x = HorizontalSlideClick.this.getScrollX();
            } else if (HorizontalSlideClick.this.direct == 1) {
                HorizontalSlideClick.this.direct = -1;
            } else {
                HorizontalSlideClick.this.direct = 1;
            }
            HorizontalSlideClick.this.doScroll();
            sendMessageDelayed(obtainMessage(HorizontalSlideClick.this.FLAG_MESSAGE), (long) HorizontalSlideClick.this.speed);
        }
    };
    private float oldTouchValue = 0.0f;
    /* access modifiers changed from: private */
    public int speed = 90;
    /* access modifiers changed from: private */

    /* renamed from: x */
    public int f46x = 0;

    /* renamed from: com.hl.android.view.component.moudle.slide.HorizontalSlideClick$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (!HorizontalSlideClick.this.isMove && !HorizontalSlideClick.this.clickUp) {
                HorizontalSlideClick.this.currentButton.setImageBitmap(HorizontalSlideClick.this.getBitmap((String) HorizontalSlideClick.this.downIDList.get(HorizontalSlideClick.this.currentIndex)));
                HorizontalSlideClick.this.cantMove = true;
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public HorizontalSlideClick(Context context, ComponentEntity entity2) {
        super(context);
        this._con = context;
        this.entity = entity2;
        this.galleryrl = new LinearLayout(context);
        this.imagelist = new ArrayList<>();
        setHorizontalScrollBarEnabled(false);
    }

    public HorizontalSlideClick(Context context) {
        super(context);
        this._con = context;
        this.galleryrl = new LinearLayout(context);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (MoudleComponentEntity) entity2;
    }

    public void load() {
        LayoutParams lp = new LayoutParams(getLayoutParams().width, getLayoutParams().height);
        this.itemWidth = ((MoudleComponentEntity) this.entity).getItemWidth();
        this.itemHeight = ((MoudleComponentEntity) this.entity).getItemHeight();
        this.itemWidth = (getLayoutParams().height * this.itemWidth) / this.itemHeight;
        this.itemHeight = getLayoutParams().height;
        if (((MoudleComponentEntity) this.entity).getSourceIDList() != null && ((MoudleComponentEntity) this.entity).getSourceIDList().size() > 0) {
            this.imagelist = ((MoudleComponentEntity) this.entity).getSourceIDList();
            this.downIDList = ((MoudleComponentEntity) this.entity).getDownIDList();
            for (int i = 0; i < this.imagelist.size(); i++) {
                if (HLSetting.IsResourceSD) {
                    load(FileUtils.getInstance().getFileInputStream((String) this.imagelist.get(i)));
                } else {
                    load(FileUtils.getInstance().getFileInputStream(getContext(), (String) this.imagelist.get(i)));
                }
            }
            if (ISHORIZONTAL) {
                this.galleryrl.setOrientation(0);
            } else {
                this.galleryrl.setOrientation(1);
            }
            this.galleryrl.measure(MeasureSpec.makeMeasureSpec(this.itemWidth * this.imagelist.size(), 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
            addView(this.galleryrl, lp);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public boolean onTouchEvent(MotionEvent touchevent) {
        if (this.oldTouchValue == 0.0f) {
            this.oldTouchValue = touchevent.getX();
        }
        switch (touchevent.getAction() & 255) {
            case 0:
                this.isMove = false;
                this.oldTouchValue = touchevent.getX();
                break;
            case 1:
                BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_MOUSE_UP");
                this.oldTouchValue = 0.0f;
                if (this.currentButton != null) {
                    this.currentButton.setImageBitmap((Bitmap) this.bitmapList.get(this.currentIndex));
                    this.cantMove = false;
                    break;
                }
                break;
            case 2:
                this.isMove = true;
                break;
        }
        if (!this.cantMove) {
            return super.onTouchEvent(touchevent);
        }
        return true;
    }

    public void load(InputStream is) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, this._option);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            this._option.inSampleSize = 2;
            BitmapUtils.recycleBitmap(bitmap);
            bitmap = BitmapFactory.decodeStream(is, null, this._option);
        }
        this.bitmapList.add(bitmap);
        ImageView ib = new ImageView(this._con);
        ib.setScaleType(ScaleType.FIT_XY);
        ib.setImageBitmap(bitmap);
        ib.measure(MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), MeasureSpec.makeMeasureSpec(getLayoutParams().height, 1073741824));
        LayoutParams lp = new LayoutParams(this.itemWidth, this.itemHeight);
        lp.gravity = 16;
        this.galleryrl.addView(ib, lp);
        ib.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int i = HorizontalSlideClick.this.galleryrl.indexOfChild(v);
                HorizontalSlideClick.this.currentIndex = i;
                HorizontalSlideClick.this.currentButton = (ImageView) v;
                switch (event.getAction() & 255) {
                    case 0:
                        BookController.getInstance().runBehavior(HorizontalSlideClick.this.getEntity(), Behavior.BEHAVIOR_ON_MOUSE_DOWN);
                        HorizontalSlideClick.this.isMove = false;
                        HorizontalSlideClick.this.clickUp = false;
                        new MyCount(300, 100).start();
                        break;
                    case 1:
                        HorizontalSlideClick.this.clickUp = true;
                        ((ImageView) v).setImageBitmap((Bitmap) HorizontalSlideClick.this.bitmapList.get(i));
                        BookController.getInstance().runBehavior(HorizontalSlideClick.this.getEntity(), "BEHAVIOR_ON_MOUSE_UP");
                        Iterator i$ = HorizontalSlideClick.this.entity.behaviors.iterator();
                        while (i$.hasNext()) {
                            BehaviorEntity behavior = (BehaviorEntity) i$.next();
                            if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                                BehaviorHelper.doBeheavorForList(behavior, i, HorizontalSlideClick.this.entity.componentId);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void doScroll() {
        if (this.direct < 0) {
            smoothScrollBy(-1, 0);
        } else if (this.direct > 0) {
            smoothScrollBy(1, 0);
        }
    }

    public void play() {
        this.direct = 1;
        this.mHandler.sendEmptyMessage(this.FLAG_MESSAGE);
    }

    public void stop() {
    }

    public void hide() {
        setVisibility(4);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }

    /* access modifiers changed from: private */
    public Bitmap getBitmap(String id) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(id);
        if (bitmap != null) {
            return bitmap;
        }
        Bitmap bitmap2 = BitmapUtils.getBitMap(id, this._con);
        BitmapManager.putBitmapCache(id, bitmap2);
        return bitmap2;
    }

    public void recyle() {
    }
}
