package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.HLHorRightUIComponent */
public class HLHorRightUIComponent extends FrameLayout implements Component {
    LinearLayout btnLayout;
    MoudleComponentEntity componentEntity;
    LinearLayout contentLayout;
    private ArrayList<Bitmap> downMapList = new ArrayList<>();
    /* access modifiers changed from: private */
    public boolean isLock = false;
    private Context mContext;
    private ArrayList<Bitmap> upMapList = new ArrayList<>();

    public HLHorRightUIComponent(Context context) {
        super(context);
        this.mContext = context;
        this.contentLayout = new LinearLayout(context);
        addView(this.btnLayout, new LayoutParams(-2, -2));
        addView(this.contentLayout);
    }

    public void hideMenu() {
        if (!this.isLock) {
            this.isLock = true;
            if (this.contentLayout.getVisibility() != 0) {
                this.isLock = false;
                return;
            }
            int width = getLayoutParams().width;
            clearAnimation();
            TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, (float) width, 0.0f, 0.0f);
            translateAnimation.setDuration(1000);
            translateAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    HLHorRightUIComponent.this.isLock = false;
                    HLHorRightUIComponent.this.contentLayout.setVisibility(4);
                    HLHorRightUIComponent.this.btnLayout.setVisibility(0);
                }
            });
            this.contentLayout.startAnimation(translateAnimation);
        }
    }

    public void showMenu() {
        if (!this.isLock) {
            this.isLock = true;
            int width = getLayoutParams().width;
            clearAnimation();
            TranslateAnimation translateAnimation = new TranslateAnimation((float) width, 0.0f, 0.0f, 0.0f);
            translateAnimation.setDuration(1000);
            translateAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    HLHorRightUIComponent.this.btnLayout.setVisibility(4);
                    HLHorRightUIComponent.this.contentLayout.setVisibility(0);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    HLHorRightUIComponent.this.isLock = false;
                }
            });
            this.contentLayout.startAnimation(translateAnimation);
        }
    }

    public HLHorRightUIComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        entity.isPlayAnimationAtBegining = true;
        this.mContext = context;
        TextView directionBtn = new TextView(context);
        this.btnLayout = new LinearLayout(context);
        this.btnLayout.setVerticalGravity(16);
        this.btnLayout.setHorizontalGravity(5);
        this.btnLayout.addView(directionBtn);
        this.contentLayout = new LinearLayout(context);
        this.contentLayout.setOrientation(1);
        Iterator i$ = this.componentEntity.getSourceIDList().iterator();
        while (i$.hasNext()) {
            this.upMapList.add(createBitmap(FileUtils.getInstance().getFileInputStream(getContext(), (String) i$.next())));
        }
        Iterator i$2 = this.componentEntity.getDownIDList().iterator();
        while (i$2.hasNext()) {
            this.downMapList.add(createBitmap(FileUtils.getInstance().getFileInputStream(getContext(), (String) i$2.next())));
        }
        directionBtn.setBackgroundDrawable(getButtonDrawable(0));
        directionBtn.setTag(Boolean.valueOf(false));
        directionBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HLHorRightUIComponent.this.showMenu();
            }
        });
        LayoutParams itemLp = new LayoutParams(-1, -1, 1.0f);
        for (int i = 1; i < this.upMapList.size(); i++) {
            TextView btn = new Button(this.mContext);
            btn.setBackgroundDrawable(getButtonDrawable(i));
            this.contentLayout.addView(btn, itemLp);
            btn.setTag(Integer.valueOf(i));
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    int i = ((Integer) arg0.getTag()).intValue();
                    Iterator i$ = HLHorRightUIComponent.this.componentEntity.behaviors.iterator();
                    while (i$.hasNext()) {
                        BehaviorHelper.doBeheavorForList((BehaviorEntity) i$.next(), i, HLHorRightUIComponent.this.componentEntity.componentId);
                        BookController.lastPageID = BookController.getInstance().getViewPage().getEntity().getID();
                    }
                }
            });
        }
        addView(this.btnLayout, new FrameLayout.LayoutParams(-1, -1));
        addView(this.contentLayout);
        this.contentLayout.setVisibility(4);
        hideMenu();
    }

    public ComponentEntity getEntity() {
        return this.componentEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.componentEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
    }

    public StateListDrawable getButtonDrawable(int index) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = new BitmapDrawable((Bitmap) this.upMapList.get(index));
        Drawable selected = new BitmapDrawable((Bitmap) this.downMapList.get(index));
        bg.addState(View.PRESSED_ENABLED_STATE_SET, new BitmapDrawable((Bitmap) this.downMapList.get(index)));
        bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
        bg.addState(View.ENABLED_STATE_SET, normal);
        bg.addState(View.FOCUSED_STATE_SET, selected);
        bg.addState(View.EMPTY_STATE_SET, normal);
        return bg;
    }

    private Bitmap createBitmap(InputStream is) {
        try {
            Options _option = new Options();
            _option.inDither = false;
            _option.inPurgeable = true;
            _option.inInputShareable = true;
            _option.inTempStorage = new byte[32768];
            Bitmap bitmap = null;
            try {
                return BitmapFactory.decodeStream(is, null, _option);
            } catch (Exception e) {
                e.printStackTrace();
                return bitmap;
            } catch (OutOfMemoryError e2) {
                _option.inSampleSize = 2;
                return BitmapFactory.decodeStream(is, null, _option);
            }
        } catch (Exception e3) {
            Log.e("hl", "load error", e3);
        } catch (OutOfMemoryError e4) {
            Log.e("hl", "load error", e4);
        }
        return null;
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        BitmapUtils.recycleBitmaps(this.downMapList);
        BitmapUtils.recycleBitmaps(this.upMapList);
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }
}
