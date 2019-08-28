package com.p000hl.android.view.compositeview;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.compositeview.AdapterViewPage */
public class AdapterViewPage extends AdapterView<Adapter> implements OnGestureListener, OnScaleGestureListener, Runnable {
    private static final int FLING_MARGIN = 100;
    private static final int GAP = 20;
    private static final float MAX_SCALE = 5.0f;
    private static final float MIN_SCALE = 1.0f;
    private static final int MOVING_DIAGONALLY = 0;
    private static final int MOVING_DOWN = 4;
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;
    private static final int MOVING_UP = 3;
    private Adapter mAdapter;
    private final SparseArray<View> mChildViews = new SparseArray<>(3);
    private int mCurrent;
    private final GestureDetector mGestureDetector = new GestureDetector(this);
    private boolean mResetLayout;
    private float mScale = 1.0f;
    private final ScaleGestureDetector mScaleGestureDetector;
    private boolean mScaling;
    private boolean mScrollDisabled;
    private final Scroller mScroller;
    private int mScrollerLastX;
    private int mScrollerLastY;
    private boolean mUserInteracting;
    private final LinkedList<View> mViewCache = new LinkedList<>();
    private int mXScroll;
    private int mYScroll;

    public AdapterViewPage(Context context) {
        super(context);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScroller = new Scroller(context);
    }

    public AdapterViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScroller = new Scroller(context);
    }

    public AdapterViewPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScroller = new Scroller(context);
    }

    public int getDisplayedViewIndex() {
        return this.mCurrent;
    }

    public void setDisplayedViewIndex(int i) {
        if (i >= 0 && i < this.mAdapter.getCount()) {
            this.mCurrent = i;
            onMoveToChild(i);
            this.mResetLayout = true;
            requestLayout();
        }
    }

    public void moveToNext() {
        View v = (View) this.mChildViews.get(this.mCurrent + 1);
        if (v != null) {
            slideViewOntoScreen(v);
        }
    }

    public void moveToPrevious() {
        View v = (View) this.mChildViews.get(this.mCurrent - 1);
        if (v != null) {
            slideViewOntoScreen(v);
        }
    }

    public void resetupChildren() {
        for (int i = 0; i < this.mChildViews.size(); i++) {
            onChildSetup(this.mChildViews.keyAt(i), (View) this.mChildViews.valueAt(i));
        }
    }

    /* access modifiers changed from: protected */
    public void onChildSetup(int i, View v) {
    }

    /* access modifiers changed from: protected */
    public void onMoveToChild(int i) {
    }

    /* access modifiers changed from: protected */
    public void onSettle(View v) {
    }

    /* access modifiers changed from: protected */
    public void onUnsettle(View v) {
    }

    public View getDisplayedView() {
        return (View) this.mChildViews.get(this.mCurrent);
    }

    public void run() {
        if (!this.mScroller.isFinished()) {
            this.mScroller.computeScrollOffset();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            this.mXScroll += x - this.mScrollerLastX;
            this.mYScroll += y - this.mScrollerLastY;
            this.mScrollerLastX = x;
            this.mScrollerLastY = y;
            requestLayout();
            post(this);
        } else if (!this.mUserInteracting) {
            postSettle((View) this.mChildViews.get(this.mCurrent));
        }
    }

    public boolean onDown(MotionEvent arg0) {
        this.mScroller.forceFinished(true);
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (this.mScrollDisabled) {
            return true;
        }
        View v = (View) this.mChildViews.get(this.mCurrent);
        if (v != null) {
            Rect bounds = getScrollBounds(v);
            switch (directionOfTravel(velocityX, velocityY)) {
                case 1:
                    if (bounds.left >= 0) {
                        View vl = (View) this.mChildViews.get(this.mCurrent + 1);
                        if (vl != null) {
                            slideViewOntoScreen(vl);
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (bounds.right <= 0) {
                        View vr = (View) this.mChildViews.get(this.mCurrent - 1);
                        if (vr != null) {
                            slideViewOntoScreen(vr);
                            return true;
                        }
                    }
                    break;
            }
            this.mScrollerLastY = 0;
            this.mScrollerLastX = 0;
            Rect expandedBounds = new Rect(bounds);
            expandedBounds.inset(-100, -100);
            if (withinBoundsInDirectionOfTravel(bounds, velocityX, velocityY) && expandedBounds.contains(0, 0)) {
                this.mScroller.fling(0, 0, (int) velocityX, (int) velocityY, bounds.left, bounds.right, bounds.top, bounds.bottom);
                post(this);
            }
        }
        return true;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!this.mScrollDisabled) {
            this.mXScroll = (int) (((float) this.mXScroll) - distanceX);
            this.mYScroll = (int) (((float) this.mYScroll) - distanceY);
            requestLayout();
        }
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScale(ScaleGestureDetector detector) {
        float previousScale = this.mScale;
        this.mScale = Math.min(Math.max(this.mScale * detector.getScaleFactor(), 1.0f), 5.0f);
        float factor = this.mScale / previousScale;
        View v = (View) this.mChildViews.get(this.mCurrent);
        if (v != null) {
            int viewFocusX = ((int) detector.getFocusX()) - (v.getLeft() + this.mXScroll);
            int viewFocusY = ((int) detector.getFocusY()) - (v.getTop() + this.mYScroll);
            this.mXScroll = (int) (((float) this.mXScroll) + (((float) viewFocusX) - (((float) viewFocusX) * factor)));
            this.mYScroll = (int) (((float) this.mYScroll) + (((float) viewFocusY) - (((float) viewFocusY) * factor)));
            requestLayout();
        }
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector detector) {
        this.mScaling = true;
        this.mYScroll = 0;
        this.mXScroll = 0;
        this.mScrollDisabled = true;
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        this.mScaling = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mScaleGestureDetector.onTouchEvent(event);
        if (!this.mScaling) {
            this.mGestureDetector.onTouchEvent(event);
        }
        if (event.getActionMasked() == 0) {
            this.mUserInteracting = true;
        }
        if (event.getActionMasked() == 1) {
            this.mScrollDisabled = false;
            this.mUserInteracting = false;
            View v = (View) this.mChildViews.get(this.mCurrent);
            if (v != null) {
                if (this.mScroller.isFinished()) {
                    slideViewOntoScreen(v);
                }
                if (this.mScroller.isFinished()) {
                    postSettle(v);
                }
            }
        }
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            measureView(getChildAt(i));
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int cvLeft;
        int cvTop;
        super.onLayout(changed, left, top, right, bottom);
        View cv = (View) this.mChildViews.get(this.mCurrent);
        if (!this.mResetLayout) {
            if (cv != null) {
                Point cvOffset = subScreenSizeOffset(cv);
                if (cv.getLeft() + cv.getMeasuredWidth() + cvOffset.x + 10 + this.mXScroll < getWidth() / 2 && this.mCurrent + 1 < this.mAdapter.getCount()) {
                    postUnsettle(cv);
                    post(this);
                    this.mCurrent++;
                    onMoveToChild(this.mCurrent);
                }
                if (((cv.getLeft() - cvOffset.x) - 10) + this.mXScroll >= getWidth() / 2 && this.mCurrent > 0) {
                    postUnsettle(cv);
                    post(this);
                    this.mCurrent--;
                    onMoveToChild(this.mCurrent);
                }
            }
            int numChildren = this.mChildViews.size();
            int[] childIndices = new int[numChildren];
            for (int i = 0; i < numChildren; i++) {
                childIndices[i] = this.mChildViews.keyAt(i);
            }
            for (int i2 = 0; i2 < numChildren; i2++) {
                int ai = childIndices[i2];
                if (ai < this.mCurrent - 1 || ai > this.mCurrent + 1) {
                    View v = (View) this.mChildViews.get(ai);
                    this.mViewCache.add(v);
                    removeViewInLayout(v);
                    this.mChildViews.remove(ai);
                }
            }
        } else {
            this.mResetLayout = false;
            this.mYScroll = 0;
            this.mXScroll = 0;
            int numChildren2 = this.mChildViews.size();
            for (int i3 = 0; i3 < numChildren2; i3++) {
                View v2 = (View) this.mChildViews.valueAt(i3);
                postUnsettle(v2);
                this.mViewCache.add(v2);
                removeViewInLayout(v2);
            }
            this.mChildViews.clear();
            post(this);
        }
        boolean notPresent = this.mChildViews.get(this.mCurrent) == null;
        View cv2 = getOrCreateChild(this.mCurrent);
        Point cvOffset2 = subScreenSizeOffset(cv2);
        if (notPresent) {
            cvLeft = cvOffset2.x;
            cvTop = cvOffset2.y;
        } else {
            cvLeft = cv2.getLeft() + this.mXScroll;
            cvTop = cv2.getTop() + this.mYScroll;
        }
        this.mYScroll = 0;
        this.mXScroll = 0;
        int cvRight = cvLeft + cv2.getMeasuredWidth();
        int cvBottom = cvTop + cv2.getMeasuredHeight();
        if (this.mUserInteracting || !this.mScroller.isFinished()) {
            if (cv2.getMeasuredHeight() <= getHeight()) {
                Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
                cvTop += corr.y;
                cvBottom += corr.y;
            }
        } else {
            Point corr2 = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvRight += corr2.x;
            cvLeft += corr2.x;
            cvTop += corr2.y;
            cvBottom += corr2.y;
        }
        cv2.layout(cvLeft, cvTop, cvRight, cvBottom);
        if (this.mCurrent > 0) {
            View lv = getOrCreateChild(this.mCurrent - 1);
            int gap = subScreenSizeOffset(lv).x + 20 + cvOffset2.x;
            lv.layout((cvLeft - lv.getMeasuredWidth()) - gap, ((cvBottom + cvTop) - lv.getMeasuredHeight()) / 2, cvLeft - gap, ((cvBottom + cvTop) + lv.getMeasuredHeight()) / 2);
        }
        if (this.mCurrent + 1 < this.mAdapter.getCount()) {
            View rv = getOrCreateChild(this.mCurrent + 1);
            int gap2 = cvOffset2.x + 20 + subScreenSizeOffset(rv).x;
            rv.layout(cvRight + gap2, ((cvBottom + cvTop) - rv.getMeasuredHeight()) / 2, rv.getMeasuredWidth() + cvRight + gap2, ((cvBottom + cvTop) + rv.getMeasuredHeight()) / 2);
        }
        invalidate();
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        throw new UnsupportedOperationException("Not supported");
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        this.mChildViews.clear();
        removeAllViewsInLayout();
        requestLayout();
    }

    public void setSelection(int arg0) {
        throw new UnsupportedOperationException("Not supported");
    }

    private View getCached() {
        if (this.mViewCache.size() == 0) {
            return null;
        }
        return (View) this.mViewCache.removeFirst();
    }

    private View getOrCreateChild(int i) {
        View v = (View) this.mChildViews.get(i);
        if (v == null) {
            v = this.mAdapter.getView(i, getCached(), this);
            addAndMeasureChild(i, v);
        }
        onChildSetup(i, v);
        return v;
    }

    private void addAndMeasureChild(int i, View v) {
        LayoutParams params = v.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-2, -2);
        }
        addViewInLayout(v, 0, params, true);
        this.mChildViews.append(i, v);
        measureView(v);
    }

    private void measureView(View v) {
        v.measure(0, 0);
        float scale = Math.min(((float) getWidth()) / ((float) v.getMeasuredWidth()), ((float) getHeight()) / ((float) v.getMeasuredHeight()));
        v.measure(((int) (((float) v.getMeasuredWidth()) * scale * this.mScale)) | 1073741824, ((int) (((float) v.getMeasuredHeight()) * scale * this.mScale)) | 1073741824);
    }

    private Rect getScrollBounds(int left, int top, int right, int bottom) {
        int xmin = getWidth() - right;
        int xmax = -left;
        int ymin = getHeight() - bottom;
        int ymax = -top;
        if (xmin > xmax) {
            xmax = (xmin + xmax) / 2;
            xmin = xmax;
        }
        if (ymin > ymax) {
            ymax = (ymin + ymax) / 2;
            ymin = ymax;
        }
        return new Rect(xmin, ymin, xmax, ymax);
    }

    private Rect getScrollBounds(View v) {
        return getScrollBounds(v.getLeft() + this.mXScroll, v.getTop() + this.mYScroll, v.getLeft() + v.getMeasuredWidth() + this.mXScroll, v.getTop() + v.getMeasuredHeight() + this.mYScroll);
    }

    private Point getCorrection(Rect bounds) {
        return new Point(Math.min(Math.max(0, bounds.left), bounds.right), Math.min(Math.max(0, bounds.top), bounds.bottom));
    }

    private void postSettle(final View v) {
        post(new Runnable() {
            public void run() {
                AdapterViewPage.this.onSettle(v);
            }
        });
    }

    private void postUnsettle(final View v) {
        post(new Runnable() {
            public void run() {
                AdapterViewPage.this.onUnsettle(v);
            }
        });
    }

    private void slideViewOntoScreen(View v) {
        Point corr = getCorrection(getScrollBounds(v));
        if (corr.x != 0 || corr.y != 0) {
            this.mScrollerLastY = 0;
            this.mScrollerLastX = 0;
            this.mScroller.startScroll(0, 0, corr.x, corr.y, HttpStatus.SC_BAD_REQUEST);
            post(this);
        }
    }

    private Point subScreenSizeOffset(View v) {
        return new Point(Math.max((getWidth() - v.getMeasuredWidth()) / 2, 0), Math.max((getHeight() - v.getMeasuredHeight()) / 2, 0));
    }

    private static int directionOfTravel(float vx, float vy) {
        if (Math.abs(vx) > Math.abs(vy) * 2.0f) {
            return vx > 0.0f ? 2 : 1;
        }
        if (Math.abs(vy) > Math.abs(vx) * 2.0f) {
            return vy > 0.0f ? 4 : 3;
        }
        return 0;
    }

    private static boolean withinBoundsInDirectionOfTravel(Rect bounds, float vx, float vy) {
        switch (directionOfTravel(vx, vy)) {
            case 0:
                return bounds.contains(0, 0);
            case 1:
                if (bounds.left > 0) {
                    return false;
                }
                return true;
            case 2:
                if (bounds.right < 0) {
                    return false;
                }
                return true;
            case 3:
                if (bounds.top > 0) {
                    return false;
                }
                return true;
            case 4:
                return bounds.bottom >= 0;
            default:
                throw new NoSuchElementException();
        }
    }
}
