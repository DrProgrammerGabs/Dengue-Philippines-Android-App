package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLLayoutActivity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.PageEntityController;
import com.p000hl.android.core.utils.BitmapUtils;
import java.util.Iterator;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.component.IndesignMiddle */
public class IndesignMiddle extends RelativeLayout {
    private RelativeLayout bottomView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Gallery pageSnapshots;
    private TextView textView;
    private RelativeLayout topView;

    public IndesignMiddle(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void checkChangeSelection(float percent, boolean shouldMove) {
        if (percent > 0.0f && percent < 1.0f) {
            if (!shouldMove) {
                this.pageSnapshots.setSelection((int) (((float) this.pageSnapshots.getAdapter().getCount()) * percent), true);
                return;
            }
            while (((int) (((float) this.pageSnapshots.getAdapter().getCount()) * percent)) != this.pageSnapshots.getSelectedItemPosition()) {
                ((HLLayoutActivity) this.mContext).getBottomNav().tagggg = true;
                if (((int) (((float) this.pageSnapshots.getAdapter().getCount()) * percent)) > this.pageSnapshots.getSelectedItemPosition()) {
                    this.pageSnapshots.onKeyDown(22, null);
                } else {
                    this.pageSnapshots.onKeyDown(21, null);
                }
                this.pageSnapshots.setSelection((int) (((float) this.pageSnapshots.getAdapter().getCount()) * percent));
            }
        }
    }

    public void changeSelection(float percent) {
        this.pageSnapshots.setSelection((int) (((float) this.pageSnapshots.getAdapter().getCount()) * percent), true);
    }

    private void init() {
        setBackgroundColor(Color.argb(255, 81, 81, 81));
        this.topView = new RelativeLayout(this.mContext);
        this.topView.setBackgroundResource(C0048R.drawable.indesign_titlebgimg);
        this.textView = new TextView(this.mContext);
        this.textView.setTextColor(-1);
        this.textView.setTextSize(20.0f);
        this.textView.setPadding(20, 0, 20, 0);
        this.textView.setLineSpacing(5.0f, 1.0f);
        this.textView.setMovementMethod(new ScrollingMovementMethod());
        this.topView.addView(this.textView, new LayoutParams(-1, -1));
        addView(this.topView);
        this.bottomView = new RelativeLayout(this.mContext);
        this.pageSnapshots = new Gallery(this.mContext);
        this.pageSnapshots.setGravity(48);
        this.pageSnapshots.setSpacing(BookSetting.BOOK_WIDTH / 30);
        this.pageSnapshots.setAnimationDuration(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        this.pageSnapshots.setUnselectedAlpha(1.0f);
        this.pageSnapshots.setAdapter(new BaseAdapter() {
            public View getView(int position, View convertView, ViewGroup parent) {
                int imageWidth = (BookSetting.BOOK_WIDTH - (BookSetting.BOOK_WIDTH / 15)) / 3;
                LinearLayout layout = new LinearLayout(IndesignMiddle.this.mContext);
                layout.setOrientation(1);
                layout.setLayoutParams(new Gallery.LayoutParams(imageWidth, -2));
                BookController controller = BookController.getInstance();
                PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(controller.viewPage.getContext(), (String) ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages().get(position));
                Bitmap bitmap = BitmapUtils.getBitMap(pageEntity.getSnapShotID(), IndesignMiddle.this.mContext);
                ImageView imageView = new ImageView(IndesignMiddle.this.mContext);
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView.setImageBitmap(bitmap);
                layout.addView(imageView, new LayoutParams(-1, (bitmap.getHeight() * imageWidth) / bitmap.getWidth()));
                Iterator i$ = pageEntity.getNavePageIds().iterator();
                while (i$.hasNext()) {
                    Bitmap bitmap1 = BitmapUtils.getBitMap(PageEntityController.getInstance().getPageEntityByPageId(controller.viewPage.getContext(), (String) i$.next()).getSnapShotID(), IndesignMiddle.this.mContext);
                    ImageView imageView1 = new ImageView(IndesignMiddle.this.mContext);
                    imageView1.setScaleType(ScaleType.FIT_XY);
                    imageView1.setPadding(0, 1, 0, 0);
                    imageView1.setImageBitmap(bitmap1);
                    layout.addView(imageView1, new LayoutParams(-1, (bitmap.getHeight() * imageWidth) / bitmap.getWidth()));
                }
                return layout;
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public Object getItem(int position) {
                return null;
            }

            public int getCount() {
                BookController controller = BookController.getInstance();
                return ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages().size();
            }
        });
        LayoutParams layout4pageSnapshots = new LayoutParams(-1, -1);
        layout4pageSnapshots.setMargins(10, 10, 10, 10);
        this.bottomView.addView(this.pageSnapshots, layout4pageSnapshots);
        addView(this.bottomView);
        this.pageSnapshots.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                BookController controller = BookController.getInstance();
                IndesignMiddle.this.setTopViewText(PageEntityController.getInstance().getPageEntityByPageId(controller.viewPage.getContext(), (String) ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages().get(position)).getTitle());
                if (((HLLayoutActivity) IndesignMiddle.this.mContext).getBottomNav().tagggg) {
                    ((HLLayoutActivity) IndesignMiddle.this.mContext).getBottomNav().tagggg = false;
                } else {
                    ((HLLayoutActivity) IndesignMiddle.this.mContext).getBottomNav().seekTo((((float) position) * 1.0f) / ((float) (IndesignMiddle.this.pageSnapshots.getAdapter().getCount() - 1)));
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.pageSnapshots.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookController controller = BookController.getInstance();
                controller.changePageById((String) ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages().get(position));
                ((HLLayoutActivity) IndesignMiddle.this.mContext).getUPNav().dismiss();
                ((HLLayoutActivity) IndesignMiddle.this.mContext).getBottomNav().dismiss();
                new CountDownTimer(300, 300) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        IndesignMiddle.this.dismiss();
                    }
                }.start();
            }
        });
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void show() {
        Animation animation = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        setVisibility(0);
        startAnimation(animation);
    }

    public void dismiss() {
        Animation animation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f, 1, 0.5f, 1, 0.5f);
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                IndesignMiddle.this.setVisibility(4);
            }
        });
        startAnimation(animation);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LayoutParams layoutParams = new LayoutParams(-1, (b - t) / 4);
        layoutParams.topMargin = 1;
        this.topView.setLayoutParams(layoutParams);
        LayoutParams layoutParams1 = new LayoutParams(-1, ((b - t) * 3) / 4);
        layoutParams1.addRule(12);
        this.bottomView.setLayoutParams(layoutParams1);
        if (this.textView.getLineCount() > 1) {
            this.textView.setGravity(19);
        } else {
            this.textView.setGravity(17);
        }
    }

    public void setTopViewText(String text) {
        this.textView.setText(text);
    }
}
