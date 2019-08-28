package com.p000hl.android.view.component.moudle.slide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

/* renamed from: com.hl.android.view.component.moudle.slide.VerticleSlideClick */
public class VerticleSlideClick extends ScrollView implements Component, ComponentPost {
    public static boolean CHANGEBUTTON = false;
    public static boolean ISHORIZONTAL = true;
    Context _con;
    Options _option = new Options();
    Bitmap bitmap = null;
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> bitmapList = new ArrayList<>();
    /* access modifiers changed from: private */
    public boolean cantMove = false;
    boolean clickUp = false;
    ImageView currentButton = null;
    int currentIndex = -1;
    /* access modifiers changed from: private */
    public ArrayList<String> downIDList = null;
    ComponentEntity entity;
    LinearLayout galleryrl;
    private ArrayList<String> imagelist;
    boolean isMove = false;
    private int itemHeight;
    private int itemWidth;
    private float oldTouchValue = 0.0f;

    /* renamed from: com.hl.android.view.component.moudle.slide.VerticleSlideClick$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (!VerticleSlideClick.this.isMove && !VerticleSlideClick.this.clickUp) {
                VerticleSlideClick.this.currentButton.setImageBitmap(VerticleSlideClick.this.getBitmap((String) VerticleSlideClick.this.downIDList.get(VerticleSlideClick.this.currentIndex)));
                VerticleSlideClick.this.cantMove = true;
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.slide.VerticleSlideClick$runview */
    class runview implements Runnable {
        int _index;
        String _name;

        public runview(String name, int index) {
            this._name = name;
            this._index = index;
        }

        public void run() {
            try {
                if (HLSetting.IsResourceSD) {
                    VerticleSlideClick.this.load(FileUtils.getInstance().getFileInputStream(this._name));
                } else {
                    VerticleSlideClick.this.load(FileUtils.getInstance().getFileInputStream(VerticleSlideClick.this.getContext(), this._name));
                }
            } catch (OutOfMemoryError e) {
                Log.e("hl", "load  error", e);
            }
        }
    }

    public VerticleSlideClick(Context context, ComponentEntity entity2) {
        super(context);
        this._con = context;
        this.entity = entity2;
        this.galleryrl = new LinearLayout(context);
        this.imagelist = new ArrayList<>();
        setVerticalScrollBarEnabled(false);
    }

    public VerticleSlideClick(Context context) {
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
        this.itemWidth = ((MoudleComponentEntity) this.entity).getItemWidth();
        this.itemHeight = ((MoudleComponentEntity) this.entity).getItemHeight();
        this.itemHeight = (getLayoutParams().width * this.itemHeight) / this.itemWidth;
        this.itemWidth = getLayoutParams().width;
        if (((MoudleComponentEntity) this.entity).getSourceIDList() != null && ((MoudleComponentEntity) this.entity).getSourceIDList().size() > 0) {
            this.imagelist = ((MoudleComponentEntity) this.entity).getSourceIDList();
            this.downIDList = ((MoudleComponentEntity) this.entity).getDownIDList();
            LayoutParams lp = new LayoutParams(getLayoutParams().width, getLayoutParams().height);
            int imageCount = this.imagelist.size();
            for (int i = 0; i < imageCount; i++) {
                if (HLSetting.IsResourceSD) {
                    load(FileUtils.getInstance().getFileInputStream((String) this.imagelist.get(i)));
                } else {
                    load(FileUtils.getInstance().getFileInputStream(getContext(), (String) this.imagelist.get(i)));
                }
            }
            this.galleryrl.setOrientation(1);
            this.galleryrl.measure(MeasureSpec.makeMeasureSpec(lp.width, 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
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
        Bitmap bitmap2 = null;
        try {
            bitmap2 = BitmapFactory.decodeStream(is, null, this._option);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            this._option.inSampleSize = 2;
            bitmap2 = BitmapFactory.decodeStream(is, null, this._option);
        }
        this.bitmapList.add(bitmap2);
        ImageView ib = new ImageView(this._con);
        ib.setScaleType(ScaleType.FIT_XY);
        ib.setImageBitmap(bitmap2);
        ib.measure(MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), MeasureSpec.makeMeasureSpec(getLayoutParams().height, 1073741824));
        LayoutParams lp = new LayoutParams(this.itemWidth, this.itemHeight);
        lp.gravity = 16;
        this.galleryrl.addView(ib, lp);
        ib.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int i = VerticleSlideClick.this.galleryrl.indexOfChild(v);
                VerticleSlideClick.this.currentIndex = i;
                VerticleSlideClick.this.currentButton = (ImageView) v;
                switch (event.getAction() & 255) {
                    case 0:
                        BookController.getInstance().runBehavior(VerticleSlideClick.this.getEntity(), Behavior.BEHAVIOR_ON_MOUSE_DOWN);
                        VerticleSlideClick.this.isMove = false;
                        VerticleSlideClick.this.clickUp = false;
                        new MyCount(300, 100).start();
                        break;
                    case 1:
                        VerticleSlideClick.this.clickUp = true;
                        VerticleSlideClick.this.currentButton.setImageBitmap((Bitmap) VerticleSlideClick.this.bitmapList.get(i));
                        BookController.getInstance().runBehavior(VerticleSlideClick.this.getEntity(), "BEHAVIOR_ON_MOUSE_UP");
                        Iterator i$ = VerticleSlideClick.this.entity.behaviors.iterator();
                        while (i$.hasNext()) {
                            BehaviorEntity behavior = (BehaviorEntity) i$.next();
                            if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                                BehaviorHelper.doBeheavorForList(behavior, i, VerticleSlideClick.this.entity.componentId);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setRotation(float rotation) {
    }

    public void stop() {
    }

    public void hide() {
        setVisibility(8);
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

    public void recyle() {
    }

    /* access modifiers changed from: private */
    public Bitmap getBitmap(String id) {
        Bitmap bitmap2 = BitmapManager.getBitmapFromCache(id);
        if (bitmap2 != null) {
            return bitmap2;
        }
        Bitmap bitmap3 = BitmapUtils.getBitMap(id, this._con);
        BitmapManager.putBitmapCache(id, bitmap3);
        return bitmap3;
    }

    public void play() {
    }
}
