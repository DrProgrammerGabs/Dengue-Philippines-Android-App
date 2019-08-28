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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
/* renamed from: com.hl.android.view.component.moudle.slidecell.HorizontalSlideCellClick */
public class HorizontalSlideCellClick extends HorizontalScrollView implements Component, ComponentPost {
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
            if (HorizontalSlideCellClick.this.f51x != HorizontalSlideCellClick.this.getScrollX()) {
                HorizontalSlideCellClick.this.f51x = HorizontalSlideCellClick.this.getScrollX();
            } else if (HorizontalSlideCellClick.this.direct == 1) {
                HorizontalSlideCellClick.this.direct = -1;
            } else {
                HorizontalSlideCellClick.this.direct = 1;
            }
            HorizontalSlideCellClick.this.doScroll();
            sendMessageDelayed(obtainMessage(HorizontalSlideCellClick.this.FLAG_MESSAGE), (long) HorizontalSlideCellClick.this.speed);
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<String> mNormalImageList;
    /* access modifiers changed from: private */
    public int speed = 90;
    /* access modifiers changed from: private */

    /* renamed from: x */
    public int f51x = 0;

    /* renamed from: com.hl.android.view.component.moudle.slidecell.HorizontalSlideCellClick$ItemTouchListener */
    public class ItemTouchListener implements OnTouchListener {
        public ItemTouchListener() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            HorizontalSlideCellClick.this.currentIndex = ((Integer) v.getTag()).intValue();
            HorizontalSlideCellClick.this.currentButton = (ImageButton) v;
            switch (event.getAction() & 255) {
                case 0:
                    HorizontalSlideCellClick.this.isMove = false;
                    HorizontalSlideCellClick.this.clickUp = false;
                    HorizontalSlideCellClick.this.lastIndex = HorizontalSlideCellClick.this.currentIndex;
                    break;
                case 1:
                    HorizontalSlideCellClick.this.clickUp = true;
                    if (HorizontalSlideCellClick.this.lastIndex == HorizontalSlideCellClick.this.currentIndex) {
                        if (HorizontalSlideCellClick.this.downIndex != HorizontalSlideCellClick.this.currentIndex) {
                            if (HorizontalSlideCellClick.this.downButton != null && HorizontalSlideCellClick.this.downIndex >= 0) {
                                HorizontalSlideCellClick.this.downButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) HorizontalSlideCellClick.this.mNormalImageList.get(HorizontalSlideCellClick.this.downIndex), HorizontalSlideCellClick.this.mContext, HorizontalSlideCellClick.this.itemWidth, HorizontalSlideCellClick.this.itemHeight)));
                            }
                            HorizontalSlideCellClick.this.currentButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) HorizontalSlideCellClick.this.mDownImageList.get(HorizontalSlideCellClick.this.currentIndex), HorizontalSlideCellClick.this.mContext, HorizontalSlideCellClick.this.itemWidth, HorizontalSlideCellClick.this.itemHeight)));
                            HorizontalSlideCellClick.this.downButton = HorizontalSlideCellClick.this.currentButton;
                            HorizontalSlideCellClick.this.downIndex = HorizontalSlideCellClick.this.currentIndex;
                            Iterator i$ = HorizontalSlideCellClick.this.mEntity.behaviors.iterator();
                            while (i$.hasNext()) {
                                BehaviorHelper.doBeheavorForList((BehaviorEntity) i$.next(), HorizontalSlideCellClick.this.currentIndex + 1, HorizontalSlideCellClick.this.mEntity.componentId);
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

    /* renamed from: com.hl.android.view.component.moudle.slidecell.HorizontalSlideCellClick$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (!HorizontalSlideCellClick.this.isMove && !HorizontalSlideCellClick.this.clickUp) {
                HorizontalSlideCellClick.this.currentButton.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap((String) HorizontalSlideCellClick.this.mDownImageList.get(HorizontalSlideCellClick.this.currentIndex), HorizontalSlideCellClick.this.mContext, HorizontalSlideCellClick.this.itemWidth, HorizontalSlideCellClick.this.itemHeight)));
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public HorizontalSlideCellClick(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (MoudleComponentEntity) entity;
        this.bodyLay = new LinearLayout(context);
        this.bodyLay.setOrientation(0);
        setHorizontalScrollBarEnabled(false);
    }

    public HorizontalSlideCellClick(Context context) {
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
        loadView();
    }

    public void loadView() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getLayoutParams().width, getLayoutParams().height);
        int imgSize = this.mNormalImageList.size();
        for (int i = 0; i < this.cellSize; i++) {
            LinearLayout cellLay = new LinearLayout(this.mContext);
            cellLay.setOrientation(1);
            cellLay.setGravity(48);
            for (int j = 0; j < this.cellNum; j++) {
                int imgIndex = (this.cellNum * i) + j;
                if (imgIndex >= imgSize) {
                    break;
                }
                cellLay.addView(loadItemView(imgIndex), this.itemLp);
            }
            Log.d("hl", "circle i");
            this.bodyLay.addView(cellLay);
        }
        this.bodyLay.measure(MeasureSpec.makeMeasureSpec(this.itemWidth * this.cellSize, 1073741824), MeasureSpec.makeMeasureSpec(this.itemHeight * this.cellNum, 1073741824));
        Log.d("hl", "measure");
        addView(this.bodyLay, lp);
        Log.d("hl", "addv");
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

    public void play() {
        this.direct = 1;
        this.mHandler.sendEmptyMessage(this.FLAG_MESSAGE);
    }

    public void stop() {
        recyle();
    }

    public void hide() {
        if (getVisibility() != 4) {
            setVisibility(4);
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
        }
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
        }
    }

    public void resume() {
    }

    public void pause() {
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
}
