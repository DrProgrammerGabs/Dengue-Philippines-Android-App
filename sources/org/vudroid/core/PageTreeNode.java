package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import java.lang.ref.SoftReference;
import org.vudroid.core.DecodeService.DecodeCallback;

class PageTreeNode {
    private static final int SLICE_SIZE = 65535;
    private Bitmap bitmap;
    private final Paint bitmapPaint = new Paint();
    private SoftReference<Bitmap> bitmapWeakReference;
    private PageTreeNode[] children;
    private boolean decodingNow;
    /* access modifiers changed from: private */
    public DocumentView documentView;
    /* access modifiers changed from: private */
    public boolean invalidateFlag;
    private Matrix matrix = new Matrix();
    /* access modifiers changed from: private */
    public final Page page;
    private final RectF pageSliceBounds;
    private Rect targetRect;
    private RectF targetRectF;
    private final int treeNodeDepthLevel;

    PageTreeNode(DocumentView documentView2, RectF localPageSliceBounds, Page page2, int treeNodeDepthLevel2, PageTreeNode parent) {
        this.documentView = documentView2;
        this.pageSliceBounds = evaluatePageSliceBounds(localPageSliceBounds, parent);
        this.page = page2;
        this.treeNodeDepthLevel = treeNodeDepthLevel2;
    }

    public void updateVisibility() {
        invalidateChildren();
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.updateVisibility();
            }
        }
        if (isVisible() && !thresholdHit()) {
            if (getBitmap() != null && !this.invalidateFlag) {
                restoreBitmapReference();
            } else if (this.documentView.zoomModel.getZoom() != 1.0f) {
                decodePageTreeNode();
            }
        }
        if (!isVisibleAndNotHiddenByChildren()) {
            stopDecodingThisNode();
            setBitmap(null);
        }
    }

    public void invalidate() {
        invalidateChildren();
        invalidateRecursive();
        updateVisibility();
    }

    private void invalidateRecursive() {
        this.invalidateFlag = true;
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.invalidateRecursive();
            }
        }
        stopDecodingThisNode();
    }

    /* access modifiers changed from: 0000 */
    public void invalidateNodeBounds() {
        this.targetRect = null;
        this.targetRectF = null;
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.invalidateNodeBounds();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas) {
        Rect sr;
        Rect tr = getTargetRect();
        Bitmap pbm = this.page.getBitmap();
        float zoom = this.documentView.zoomModel.getZoom();
        if (getBitmap() != null) {
            canvas.drawBitmap(getBitmap(), new Rect(0, 0, getBitmap().getWidth(), getBitmap().getHeight()), tr, this.bitmapPaint);
        } else if (pbm != null) {
            if (zoom > 1.0f) {
                sr = new Rect((int) (((double) (((float) tr.left) / zoom)) + 0.5d), (int) (((double) ((((float) tr.top) - this.page.bounds.top) / zoom)) + 0.5d), (int) (((double) (((float) tr.right) / zoom)) + 0.5d), (int) (((double) ((((float) tr.bottom) - this.page.bounds.top) / zoom)) + 0.5d));
            } else {
                sr = new Rect(tr.left, (int) (((float) tr.top) - this.page.bounds.top), tr.right, (int) (((float) tr.bottom) - this.page.bounds.top));
            }
            canvas.drawBitmap(pbm, sr, tr, this.bitmapPaint);
        }
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.draw(canvas);
            }
        }
    }

    private boolean isVisible() {
        if (!this.page.isVisible()) {
            return false;
        }
        return RectF.intersects(this.documentView.getViewRect(), getTargetRectF());
    }

    private RectF getTargetRectF() {
        if (this.targetRectF == null) {
            this.targetRectF = new RectF(getTargetRect());
        }
        return this.targetRectF;
    }

    /* access modifiers changed from: private */
    public void invalidateChildren() {
        if (thresholdHit() && this.children == null && isVisible()) {
            int newThreshold = this.treeNodeDepthLevel * 2;
            this.children = new PageTreeNode[]{new PageTreeNode(this.documentView, new RectF(0.0f, 0.0f, 0.5f, 0.5f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.5f, 0.0f, 1.0f, 0.5f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.0f, 0.5f, 0.5f, 1.0f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.5f, 0.5f, 1.0f, 1.0f), this.page, newThreshold, this)};
        }
        if ((!thresholdHit() && getBitmap() != null) || !isVisible()) {
            recycleChildren();
        }
    }

    private boolean thresholdHit() {
        float zoom = this.documentView.zoomModel.getZoom();
        int mainWidth = this.documentView.getWidth();
        return ((((float) mainWidth) * zoom) * this.page.getPageHeight(mainWidth, zoom)) / ((float) (this.treeNodeDepthLevel * this.treeNodeDepthLevel)) > 65535.0f;
    }

    public Bitmap getBitmap() {
        if (this.bitmapWeakReference != null) {
            return (Bitmap) this.bitmapWeakReference.get();
        }
        return null;
    }

    private void restoreBitmapReference() {
        setBitmap(getBitmap());
    }

    private void decodePageTreeNode() {
        if (!isDecodingNow()) {
            setDecodingNow(true);
            this.documentView.decodeService.decodePage(this, this.page.index, new DecodeCallback() {
                public void decodeComplete(final Bitmap bitmap, final float zoom) {
                    PageTreeNode.this.documentView.post(new Runnable() {
                        public void run() {
                            if (PageTreeNode.this.documentView.zoomModel.getZoom() != zoom) {
                                PageTreeNode.this.setBitmap(null);
                            } else {
                                PageTreeNode.this.setBitmap(bitmap);
                            }
                            PageTreeNode.this.invalidateFlag = false;
                            PageTreeNode.this.setDecodingNow(false);
                            PageTreeNode.this.page.setAspectRatio(PageTreeNode.this.documentView.decodeService.getPageWidth(PageTreeNode.this.page.index), PageTreeNode.this.documentView.decodeService.getPageHeight(PageTreeNode.this.page.index));
                            PageTreeNode.this.invalidateChildren();
                        }
                    });
                }
            }, this.documentView.zoomModel.getZoom(), this.pageSliceBounds);
        }
    }

    private RectF evaluatePageSliceBounds(RectF localPageSliceBounds, PageTreeNode parent) {
        if (parent == null) {
            return localPageSliceBounds;
        }
        Matrix matrix2 = new Matrix();
        matrix2.postScale(parent.pageSliceBounds.width(), parent.pageSliceBounds.height());
        matrix2.postTranslate(parent.pageSliceBounds.left, parent.pageSliceBounds.top);
        RectF sliceBounds = new RectF();
        matrix2.mapRect(sliceBounds, localPageSliceBounds);
        return sliceBounds;
    }

    /* access modifiers changed from: private */
    public void setBitmap(Bitmap bitmap2) {
        if (bitmap2 == null && isVisible()) {
            return;
        }
        if ((bitmap2 == null || bitmap2.getWidth() != -1 || bitmap2.getHeight() != -1) && this.bitmap != bitmap2) {
            if (bitmap2 != null) {
                if (this.bitmap != null) {
                    this.bitmap.recycle();
                }
                this.bitmapWeakReference = new SoftReference<>(bitmap2);
                this.documentView.postInvalidate();
            }
            this.bitmap = bitmap2;
        }
    }

    private boolean isDecodingNow() {
        return this.decodingNow;
    }

    /* access modifiers changed from: private */
    public void setDecodingNow(boolean decodingNow2) {
        if (this.decodingNow != decodingNow2) {
            this.decodingNow = decodingNow2;
            if (this.documentView.progressModel == null) {
                return;
            }
            if (decodingNow2) {
                this.documentView.progressModel.increase();
            } else {
                this.documentView.progressModel.decrease();
            }
        }
    }

    private Rect getTargetRect() {
        if (this.targetRect == null) {
            this.matrix.reset();
            this.matrix.postScale(this.page.bounds.width(), this.page.bounds.height());
            this.matrix.postTranslate(this.page.bounds.left, this.page.bounds.top);
            RectF r = new RectF();
            this.matrix.mapRect(r, this.pageSliceBounds);
            this.targetRect = new Rect((int) (((double) r.left) + 0.5d), (int) (((double) r.top) + 0.5d), (int) (((double) r.right) + 0.5d), (int) (((double) r.bottom) + 0.5d));
        }
        return this.targetRect;
    }

    private void stopDecodingThisNode() {
        if (isDecodingNow()) {
            this.documentView.decodeService.stopDecoding(this);
            setDecodingNow(false);
        }
    }

    private boolean isHiddenByChildren() {
        if (this.children == null) {
            return false;
        }
        for (PageTreeNode child : this.children) {
            if (child.getBitmap() == null) {
                return false;
            }
        }
        return true;
    }

    private void recycleChildren() {
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.recycle();
            }
            if (!childrenContainBitmaps()) {
                this.children = null;
            }
        }
    }

    private boolean containsBitmaps() {
        return getBitmap() != null || childrenContainBitmaps();
    }

    private boolean childrenContainBitmaps() {
        if (this.children == null) {
            return false;
        }
        for (PageTreeNode child : this.children) {
            if (child.containsBitmaps()) {
                return true;
            }
        }
        return false;
    }

    private void recycle() {
        stopDecodingThisNode();
        setBitmap(null);
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.recycle();
            }
        }
    }

    private boolean isVisibleAndNotHiddenByChildren() {
        return isVisible() && !isHiddenByChildren();
    }
}
