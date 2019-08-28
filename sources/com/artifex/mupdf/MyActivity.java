package com.artifex.mupdf;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyActivity extends Activity implements OnClickListener {
    private final int TAP_PAGE_MARGIN = 5;
    /* access modifiers changed from: private */
    public MuPDFCore core;
    /* access modifiers changed from: private */
    public ReaderView mDocView;
    /* access modifiers changed from: private */
    public LinkState mLinkState = LinkState.DEFAULT;
    private int pageIndex = 0;

    private enum LinkState {
        DEFAULT,
        HIGHLIGHT,
        INHIBIT
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout lay = new LinearLayout(this);
        lay.setBackgroundColor(Color.rgb(220, 220, 220));
        setContentView(lay);
        try {
            this.core = new MuPDFCore(Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.pdf");
        } catch (Exception e) {
        }
        if (this.core == null) {
            Toast.makeText(this, "create pdf core error", 1).show();
            return;
        }
        this.mDocView = new ReaderView(this) {
            private boolean showButtonsDisabled;

            public boolean onSingleTapUp(MotionEvent e) {
                if (e.getX() < ((float) (super.getWidth() / 5))) {
                    super.moveToPrevious();
                } else if (e.getX() > ((float) ((super.getWidth() * 4) / 5))) {
                    super.moveToNext();
                } else if (!this.showButtonsDisabled) {
                    if (MyActivity.this.mLinkState == LinkState.INHIBIT || ((MuPDFPageView) MyActivity.this.mDocView.getDisplayedView()) == null) {
                    }
                    if (-1 != -1) {
                        MyActivity.this.mDocView.setDisplayedViewIndex(-1);
                    }
                }
                return super.onSingleTapUp(e);
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            public boolean onScaleBegin(ScaleGestureDetector d) {
                this.showButtonsDisabled = true;
                return super.onScaleBegin(d);
            }

            public boolean onTouchEvent(MotionEvent event) {
                if (event.getActionMasked() == 0) {
                    this.showButtonsDisabled = false;
                }
                return super.onTouchEvent(event);
            }

            /* access modifiers changed from: protected */
            public void onChildSetup(int i, View v) {
                if (SearchTaskResult.get() == null || SearchTaskResult.get().pageNumber != i) {
                    ((PageView) v).setSearchBoxes(null);
                } else {
                    ((PageView) v).setSearchBoxes(SearchTaskResult.get().searchBoxes);
                }
                ((PageView) v).setLinkHighlighting(MyActivity.this.mLinkState == LinkState.HIGHLIGHT);
            }

            /* access modifiers changed from: protected */
            public void onMoveToChild(int i) {
                if (MyActivity.this.core != null && SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber != i) {
                    SearchTaskResult.set(null);
                    MyActivity.this.mDocView.resetupChildren();
                }
            }

            /* access modifiers changed from: protected */
            public void onSettle(View v) {
                ((PageView) v).addHq();
            }

            /* access modifiers changed from: protected */
            public void onUnsettle(View v) {
                ((PageView) v).removeHq();
            }
        };
        this.mDocView.setAdapter(new MuPDFPageAdapter(this, this.core));
        Button btnNext = new Button(this);
        btnNext.setText("下一页");
        btnNext.setOnClickListener(this);
        lay.setOrientation(1);
        lay.addView(btnNext);
        lay.addView(this.mDocView);
    }

    public void onClick(View v) {
        this.pageIndex++;
        this.mDocView.setDisplayedViewIndex(this.pageIndex);
    }
}
