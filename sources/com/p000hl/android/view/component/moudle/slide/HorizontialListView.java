package com.p000hl.android.view.component.moudle.slide;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.Scroller;
import java.util.LinkedList;
import java.util.Queue;

/* renamed from: com.hl.android.view.component.moudle.slide.HorizontialListView */
public class HorizontialListView extends AdapterView<ListAdapter> {
    protected int FLAG_MESSAGE = 0;
    protected int bottom;
    private boolean isTouch = false;
    protected int left;
    protected ListAdapter mAdapter;
    public boolean mAlwaysOverrideTouch = true;
    protected int mCurrentX;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private DataSetObserver mDataObserver = new DataSetObserver() {
        public void onChanged() {
            synchronized (HorizontialListView.this) {
                HorizontialListView.this.mDataChanged = true;
            }
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontialListView.this.reset();
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }
    };
    private int mDisplayOffset = 0;
    private GestureDetector mGesture;
    protected final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            HorizontialListView.this.doScroll();
            sendMessageDelayed(obtainMessage(HorizontialListView.this.FLAG_MESSAGE), 100);
        }
    };
    /* access modifiers changed from: private */
    public int mLeftViewIndex = -1;
    private int mMaxX = Integer.MAX_VALUE;
    protected int mNextX;
    private OnGestureListener mOnGesture = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            return HorizontialListView.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            synchronized (HorizontialListView.this) {
                HorizontialListView.this.mNextX += (int) distanceX;
            }
            float d = e1.getX() - e2.getX();
            HorizontialListView.this.startX = HorizontialListView.this.mNextX;
            if (d > 0.0f) {
                HorizontialListView.this.toleft = true;
            } else if (d < 0.0f) {
                HorizontialListView.this.toleft = false;
            }
            HorizontialListView.this.onLayout(true, HorizontialListView.this.left, HorizontialListView.this.top, HorizontialListView.this.right, HorizontialListView.this.bottom);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            int i = 0;
            while (true) {
                if (i >= HorizontialListView.this.getChildCount()) {
                    break;
                }
                View child = HorizontialListView.this.getChildAt(i);
                viewRect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (HorizontialListView.this.mOnItemClicked != null) {
                        HorizontialListView.this.mOnItemClicked.onItemClick(HorizontialListView.this, child, HorizontialListView.this.mLeftViewIndex + 1 + i, HorizontialListView.this.mAdapter.getItemId(HorizontialListView.this.mLeftViewIndex + 1 + i));
                    }
                    if (HorizontialListView.this.mOnItemSelected != null) {
                        HorizontialListView.this.mOnItemSelected.onItemSelected(HorizontialListView.this, child, HorizontialListView.this.mLeftViewIndex + 1 + i, HorizontialListView.this.mAdapter.getItemId(HorizontialListView.this.mLeftViewIndex + 1 + i));
                    }
                } else {
                    i++;
                }
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClicked;
    /* access modifiers changed from: private */
    public OnItemSelectedListener mOnItemSelected;
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private int mRightViewIndex = 0;
    protected Scroller mScroller;
    Runnable myRunnable = new Runnable() {
        public void run() {
            HorizontialListView.this.requestLayout();
        }
    };
    protected int right;
    protected int startX = 0;
    protected boolean toleft = true;
    protected int top;

    public HorizontialListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HorizontialListView(Context context) {
        super(context);
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mScroller = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelected = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClicked = listener;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        return null;
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    /* access modifiers changed from: private */
    public synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    public void setSelection(int position) {
    }

    private void addAndMeasureChild(View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-1, -1);
        }
        addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
    }

    /* access modifiers changed from: protected */
    public synchronized void onLayout(boolean changed, int left2, int top2, int right2, int bottom2) {
        super.onLayout(changed, left2, top2, right2, bottom2);
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
        if (this.mAdapter != null) {
            if (this.mDataChanged) {
                int oldCurrentX = this.mCurrentX;
                initView();
                removeAllViewsInLayout();
                this.mNextX = oldCurrentX;
                this.mDataChanged = false;
            }
            if (this.mScroller.computeScrollOffset()) {
                this.mNextX = this.mScroller.getCurrX();
            }
            if (this.mNextX < 0) {
                this.toleft = true;
                this.mNextX = 0;
                this.mScroller.forceFinished(true);
            }
            if (this.mNextX > this.mMaxX) {
                this.toleft = false;
                this.mNextX = this.mMaxX;
                this.mScroller.forceFinished(true);
            }
            int dx = this.mCurrentX - this.mNextX;
            removeNonVisibleItems(dx);
            fillList(dx);
            positionItems(dx);
            this.mCurrentX = this.mNextX;
            if (!this.mScroller.isFinished()) {
                post(this.myRunnable);
            }
        }
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);
        int edge2 = 0;
        View child2 = getChildAt(0);
        if (child2 != null) {
            edge2 = child2.getLeft();
        }
        fillListLeft(edge2, dx);
    }

    private void fillListRight(int rightEdge, int dx) {
        while (rightEdge + dx < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View child = this.mAdapter.getView(this.mRightViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + rightEdge) - getWidth();
            }
            this.mRightViewIndex++;
        }
    }

    private void fillListLeft(int leftEdge, int dx) {
        while (leftEdge + dx > 0 && this.mLeftViewIndex >= 0) {
            View child = this.mAdapter.getView(this.mLeftViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            this.mDisplayOffset += child.getMeasuredWidth();
            this.mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            this.mLeftViewIndex++;
            child = getChildAt(0);
        }
        View child2 = getChildAt(getChildCount() - 1);
        while (child2 != null && child2.getLeft() + dx >= getWidth()) {
            this.mRemovedViewQueue.offer(child2);
            removeViewInLayout(child2);
            this.mRightViewIndex--;
            child2 = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int dx) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += dx;
            int left2 = this.mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left2, 0, left2 + childWidth, child.getMeasuredHeight());
                left2 += childWidth;
            }
        }
    }

    public synchronized void scrollTo(int x) {
        this.mScroller.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            this.isTouch = true;
            this.mHandler.removeMessages(this.FLAG_MESSAGE);
        } else if (ev.getAction() == 1) {
            if (this.isTouch) {
                this.mHandler.sendEmptyMessageDelayed(this.FLAG_MESSAGE, 100);
            }
            this.isTouch = false;
        }
        return this.mGesture.onTouchEvent(ev);
    }

    /* access modifiers changed from: protected */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-velocityX), 0, 0, this.mMaxX, 0, 0);
        }
        onLayout(true, this.left, this.top, this.right, this.bottom);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onDown(MotionEvent e) {
        this.mScroller.forceFinished(true);
        return true;
    }

    /* access modifiers changed from: protected */
    public void doScroll() {
        if (this.toleft) {
            this.mScroller.startScroll(this.startX, 0, 1, 0);
            this.startX++;
        } else {
            this.mScroller.startScroll(this.startX, 0, -1, 0);
            this.startX--;
        }
        onLayout(true, this.left, this.top, this.right, this.bottom);
    }
}
