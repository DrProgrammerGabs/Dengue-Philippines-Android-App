package com.p000hl.android.view.component.moudle.slidecell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"HandlerLeak"})
/* renamed from: com.hl.android.view.component.moudle.slidecell.VerticalSlideCellClick */
public class VerticalSlideCellClick extends ScrollView implements Component, ComponentPost {
    public static boolean CHANGEBUTTON = false;
    /* access modifiers changed from: private */
    public int FLAG_MESSAGE = 0;
    Options _option;
    public AnimationSet animationset = null;
    Bitmap bitmap = null;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();
    LinearLayout bodyLay;
    private int cellNum = 1;
    private int cellSize = 0;
    boolean clickUp = false;
    ImageButton currentButton = null;
    int currentIndex = -1;
    /* access modifiers changed from: private */
    public int direct = 0;
    /* access modifiers changed from: private */
    public ImageButton downButton = null;
    /* access modifiers changed from: private */
    public int downIndex = -1;
    boolean isMove = false;
    /* access modifiers changed from: private */
    public int itemHeight;
    private LayoutParams itemLp;
    /* access modifiers changed from: private */
    public int itemWidth;
    /* access modifiers changed from: private */
    public int lastIndex = -1;
    Context mContext;
    /* access modifiers changed from: private */
    public ArrayList<String> mDownImageList;
    MoudleComponentEntity mEntity;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (VerticalSlideCellClick.this.f52x != VerticalSlideCellClick.this.getScrollX()) {
                VerticalSlideCellClick.this.f52x = VerticalSlideCellClick.this.getScrollX();
            } else if (VerticalSlideCellClick.this.direct == 1) {
                VerticalSlideCellClick.this.direct = -1;
            } else {
                VerticalSlideCellClick.this.direct = 1;
            }
            VerticalSlideCellClick.this.doScroll();
            sendMessageDelayed(obtainMessage(VerticalSlideCellClick.this.FLAG_MESSAGE), (long) VerticalSlideCellClick.this.speed);
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<String> mNormalImageList;
    /* access modifiers changed from: private */
    public int speed = 90;
    /* access modifiers changed from: private */

    /* renamed from: x */
    public int f52x = 0;

    /* renamed from: com.hl.android.view.component.moudle.slidecell.VerticalSlideCellClick$ItemTouchListener */
    public class ItemTouchListener implements OnTouchListener {
        public ItemTouchListener() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            VerticalSlideCellClick.this.currentIndex = ((Integer) v.getTag()).intValue();
            VerticalSlideCellClick.this.currentButton = (ImageButton) v;
            switch (event.getAction() & 255) {
                case 0:
                    VerticalSlideCellClick.this.isMove = false;
                    VerticalSlideCellClick.this.clickUp = false;
                    VerticalSlideCellClick.this.lastIndex = VerticalSlideCellClick.this.currentIndex;
                    break;
                case 1:
                    VerticalSlideCellClick.this.clickUp = true;
                    if (VerticalSlideCellClick.this.lastIndex == VerticalSlideCellClick.this.currentIndex) {
                        if (VerticalSlideCellClick.this.downIndex != VerticalSlideCellClick.this.currentIndex) {
                            if (VerticalSlideCellClick.this.downButton != null && VerticalSlideCellClick.this.downIndex >= 0) {
                                BitmapDrawable obd = (BitmapDrawable) VerticalSlideCellClick.this.downButton.getBackground();
                                VerticalSlideCellClick.this.downButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) VerticalSlideCellClick.this.mNormalImageList.get(VerticalSlideCellClick.this.downIndex), VerticalSlideCellClick.this.mContext, VerticalSlideCellClick.this.itemWidth, VerticalSlideCellClick.this.itemHeight)));
                                obd.getBitmap().recycle();
                            }
                            BitmapDrawable obd2 = (BitmapDrawable) VerticalSlideCellClick.this.currentButton.getBackground();
                            VerticalSlideCellClick.this.currentButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) VerticalSlideCellClick.this.mDownImageList.get(VerticalSlideCellClick.this.currentIndex), VerticalSlideCellClick.this.mContext, VerticalSlideCellClick.this.itemWidth, VerticalSlideCellClick.this.itemHeight)));
                            obd2.getBitmap().recycle();
                            VerticalSlideCellClick.this.downButton = VerticalSlideCellClick.this.currentButton;
                            VerticalSlideCellClick.this.downIndex = VerticalSlideCellClick.this.currentIndex;
                            Iterator i$ = VerticalSlideCellClick.this.mEntity.behaviors.iterator();
                            while (i$.hasNext()) {
                                BehaviorHelper.doBeheavorForList((BehaviorEntity) i$.next(), VerticalSlideCellClick.this.currentIndex + 1, VerticalSlideCellClick.this.mEntity.componentId);
                            }
                            break;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
            }
            return true;
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.slidecell.VerticalSlideCellClick$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (!VerticalSlideCellClick.this.isMove && !VerticalSlideCellClick.this.clickUp) {
                VerticalSlideCellClick.this.currentButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) VerticalSlideCellClick.this.mDownImageList.get(VerticalSlideCellClick.this.currentIndex), VerticalSlideCellClick.this.mContext, VerticalSlideCellClick.this.itemWidth, VerticalSlideCellClick.this.itemHeight)));
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public VerticalSlideCellClick(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (MoudleComponentEntity) entity;
        this.bodyLay = new LinearLayout(context);
        this.bodyLay.setOrientation(1);
        setHorizontalScrollBarEnabled(false);
    }

    public VerticalSlideCellClick(Context context) {
        super(context);
        this.mContext = context;
        this.bodyLay = new LinearLayout(context);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.itemWidth = this.mEntity.getItemWidth();
        this.itemHeight = this.mEntity.getItemHeight();
        this.itemWidth = (int) ScreenUtils.getHorScreenValue((float) this.itemWidth);
        this.itemHeight = (int) ScreenUtils.getVerScreenValue((float) this.itemHeight);
        this.itemLp = new LayoutParams(this.itemWidth, this.itemHeight);
        this.mNormalImageList = this.mEntity.getSourceIDList();
        this.mDownImageList = this.mEntity.getDownIDList();
        this.cellNum = this.mEntity.getCellNumber();
        int imgSize = this.mNormalImageList.size();
        this.cellSize = imgSize / this.cellNum;
        if (imgSize % this.cellNum > 0) {
            this.cellSize++;
        }
        loadView(this.cellSize);
    }

    public void loadView(int cellSize2) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getLayoutParams().width, getLayoutParams().height);
        int imgSize = this.mNormalImageList.size();
        for (int i = 0; i < cellSize2; i++) {
            LinearLayout cellLay = new LinearLayout(this.mContext);
            cellLay.setOrientation(0);
            cellLay.setGravity(48);
            int j = 0;
            while (j < this.cellNum) {
                int imgIndex = (this.cellNum * i) + j;
                if (imgIndex < imgSize) {
                    cellLay.addView(loadItemView(imgIndex), this.itemLp);
                    j++;
                } else {
                    return;
                }
            }
            this.bodyLay.addView(cellLay);
        }
        this.bodyLay.measure(MeasureSpec.makeMeasureSpec(lp.width, 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
        addView(this.bodyLay, lp);
        bringToFront();
    }

    private View loadItemView(int index) {
        Bitmap bitmap2 = BitmapUtils.getBitMap((String) this.mNormalImageList.get(index), this.mContext, this.itemWidth, this.itemHeight);
        this.bitmapList.add(bitmap2);
        Drawable dbg = new BitmapDrawable(bitmap2);
        ImageButton ib = new ImageButton(this.mContext);
        ib.setBackgroundDrawable(dbg);
        ib.measure(MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824));
        ib.setOnTouchListener(new ItemTouchListener());
        ib.setTag(Integer.valueOf(index));
        return ib;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void load(InputStream is) {
    }

    /* access modifiers changed from: private */
    public void doScroll() {
        if (this.direct < 0) {
            smoothScrollBy(-1, 0);
        } else if (this.direct > 0) {
            smoothScrollBy(1, 0);
        }
    }

    public void setRotation(float rotation) {
        m1d("setRotation nothing");
    }

    public void play() {
        this.direct = 1;
        this.mHandler.sendEmptyMessage(this.FLAG_MESSAGE);
    }

    public void stop() {
        m1d("stop nothing");
        recyle();
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
        m1d("resume nothing");
    }

    public void pause() {
        m1d("pause nothing");
    }

    public void recyle() {
        if (this.bitmapList != null) {
            BitmapUtils.recycleBitmaps(this.bitmapList);
        }
        if (this.bodyLay != null) {
            this.bodyLay.removeAllViews();
            this.bodyLay = null;
        }
        if (this.bitmap != null) {
            BitmapUtils.recycleBitmap(this.bitmap);
        }
        removeAllViews();
        System.gc();
    }

    /* renamed from: d */
    private void m1d(String message) {
        if (1 != 0) {
            Log.d("hl", message + "    || id is " + this);
        }
    }
}
