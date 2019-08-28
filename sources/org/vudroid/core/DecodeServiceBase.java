package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.vudroid.core.DecodeService.DecodeCallback;
import org.vudroid.core.codec.CodecContext;
import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;

public class DecodeServiceBase implements DecodeService {
    public static final String DECODE_SERVICE = "ViewDroidDecodeService";
    private static final int PAGE_POOL_SIZE = 1;
    /* access modifiers changed from: private */
    public final CodecContext codecContext;
    private View containerView;
    private final Map<Object, Future<?>> decodingFutures = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public CodecDocument document;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean isRecycled;
    private Queue<Integer> pageEvictionQueue = new LinkedList();
    /* access modifiers changed from: private */
    public final HashMap<Integer, SoftReference<CodecPage>> pages = new HashMap<>();

    private class DecodeTask {
        /* access modifiers changed from: private */
        public final DecodeCallback decodeCallback;
        /* access modifiers changed from: private */
        public final Object decodeKey;
        /* access modifiers changed from: private */
        public final int pageNumber;
        /* access modifiers changed from: private */
        public final RectF pageSliceBounds;
        /* access modifiers changed from: private */
        public final float zoom;

        private DecodeTask(int pageNumber2, DecodeCallback decodeCallback2, float zoom2, Object decodeKey2, RectF pageSliceBounds2) {
            this.pageNumber = pageNumber2;
            this.decodeCallback = decodeCallback2;
            this.zoom = zoom2;
            this.decodeKey = decodeKey2;
            this.pageSliceBounds = pageSliceBounds2;
        }
    }

    public DecodeServiceBase(CodecContext codecContext2) {
        this.codecContext = codecContext2;
    }

    public void setContainerView(View containerView2) {
        this.containerView = containerView2;
    }

    public void open(String filePath) {
        this.document = this.codecContext.openDocument(filePath);
    }

    public Bitmap getBitmap(int pageIndex, int width, int height) {
        Bitmap renderBitmap;
        CodecPage vuPage = getPage(pageIndex);
        synchronized (this.decodingFutures) {
            if (this.isRecycled) {
                renderBitmap = null;
            } else {
                renderBitmap = vuPage.renderBitmap(width, height, new RectF(0.0f, 0.0f, 1.0f, 1.0f));
            }
        }
        return renderBitmap;
    }

    public void decodePage(Object decodeKey, int pageNum, DecodeCallback decodeCallback) {
        decodePage(decodeKey, pageNum, decodeCallback, 1.0f, new RectF(0.0f, 0.0f, 1.0f, 1.0f));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void decodePage(java.lang.Object r11, int r12, org.vudroid.core.DecodeService.DecodeCallback r13, float r14, android.graphics.RectF r15) {
        /*
            r10 = this;
            org.vudroid.core.DecodeServiceBase$DecodeTask r0 = new org.vudroid.core.DecodeServiceBase$DecodeTask
            r7 = 0
            r1 = r10
            r2 = r12
            r3 = r13
            r4 = r14
            r5 = r11
            r6 = r15
            r0.<init>(r2, r3, r4, r5, r6)
            java.util.Map<java.lang.Object, java.util.concurrent.Future<?>> r2 = r10.decodingFutures
            monitor-enter(r2)
            boolean r1 = r10.isRecycled     // Catch:{ all -> 0x0030 }
            if (r1 == 0) goto L_0x0015
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
        L_0x0014:
            return
        L_0x0015:
            java.util.concurrent.ExecutorService r1 = r10.executorService     // Catch:{ all -> 0x0030 }
            org.vudroid.core.DecodeServiceBase$1 r3 = new org.vudroid.core.DecodeServiceBase$1     // Catch:{ all -> 0x0030 }
            r3.<init>(r0)     // Catch:{ all -> 0x0030 }
            java.util.concurrent.Future r8 = r1.submit(r3)     // Catch:{ all -> 0x0030 }
            java.util.Map<java.lang.Object, java.util.concurrent.Future<?>> r1 = r10.decodingFutures     // Catch:{ all -> 0x0030 }
            java.lang.Object r9 = r1.put(r11, r8)     // Catch:{ all -> 0x0030 }
            java.util.concurrent.Future r9 = (java.util.concurrent.Future) r9     // Catch:{ all -> 0x0030 }
            if (r9 == 0) goto L_0x002e
            r1 = 0
            r9.cancel(r1)     // Catch:{ all -> 0x0030 }
        L_0x002e:
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            goto L_0x0014
        L_0x0030:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.vudroid.core.DecodeServiceBase.decodePage(java.lang.Object, int, org.vudroid.core.DecodeService$DecodeCallback, float, android.graphics.RectF):void");
    }

    public void stopDecoding(Object decodeKey) {
        Future<?> future = (Future) this.decodingFutures.remove(decodeKey);
        if (future != null) {
            future.cancel(false);
        }
    }

    /* access modifiers changed from: private */
    public void performDecode(DecodeTask currentDecodeTask) throws IOException {
        if (isTaskDead(currentDecodeTask)) {
            Log.d(DECODE_SERVICE, "Skipping decode task for page " + currentDecodeTask.pageNumber);
            return;
        }
        Log.d(DECODE_SERVICE, "Starting decode of page: " + currentDecodeTask.pageNumber);
        CodecPage vuPage = getPage(currentDecodeTask.pageNumber);
        if (!isTaskDead(currentDecodeTask)) {
            Log.d(DECODE_SERVICE, "Start converting map to bitmap");
            float scale = calculateScale(vuPage) * currentDecodeTask.zoom;
            Bitmap bitmap = vuPage.renderBitmap(getScaledWidth(currentDecodeTask, vuPage, scale), getScaledHeight(currentDecodeTask, vuPage, scale), currentDecodeTask.pageSliceBounds);
            Log.d(DECODE_SERVICE, "Converting map to bitmap finished");
            if (isTaskDead(currentDecodeTask)) {
                bitmap.recycle();
            } else {
                finishDecoding(currentDecodeTask, bitmap);
            }
        }
    }

    private int getScaledHeight(DecodeTask currentDecodeTask, CodecPage vuPage, float scale) {
        return Math.round(((float) getScaledHeight(vuPage, scale)) * currentDecodeTask.pageSliceBounds.height());
    }

    private int getScaledWidth(DecodeTask currentDecodeTask, CodecPage vuPage, float scale) {
        return Math.round(((float) getScaledWidth(vuPage, scale)) * currentDecodeTask.pageSliceBounds.width());
    }

    private int getScaledHeight(CodecPage vuPage, float scale) {
        return (int) (((float) vuPage.getHeight()) * scale);
    }

    private int getScaledWidth(CodecPage vuPage, float scale) {
        return (int) (((float) vuPage.getWidth()) * scale);
    }

    private float calculateScale(CodecPage codecPage) {
        return (1.0f * ((float) getTargetWidth())) / ((float) codecPage.getWidth());
    }

    private void finishDecoding(DecodeTask currentDecodeTask, Bitmap bitmap) {
        updateImage(currentDecodeTask, bitmap);
        stopDecoding(Integer.valueOf(currentDecodeTask.pageNumber));
    }

    /* access modifiers changed from: protected */
    public void preloadNextPage(int pageNumber) throws IOException {
        int nextPage = pageNumber + 1;
        if (nextPage < getPageCount()) {
            getPage(nextPage);
        }
    }

    private CodecPage getPage(int pageIndex) {
        if (!this.pages.containsKey(Integer.valueOf(pageIndex)) || ((SoftReference) this.pages.get(Integer.valueOf(pageIndex))).get() == null) {
            this.pages.put(Integer.valueOf(pageIndex), new SoftReference(this.document.getPage(pageIndex)));
            this.pageEvictionQueue.remove(Integer.valueOf(pageIndex));
            this.pageEvictionQueue.offer(Integer.valueOf(pageIndex));
            if (this.pageEvictionQueue.size() > 1) {
                CodecPage evictedPage = (CodecPage) ((SoftReference) this.pages.remove((Integer) this.pageEvictionQueue.poll())).get();
                if (evictedPage != null) {
                    evictedPage.recycle();
                }
            }
        }
        return (CodecPage) ((SoftReference) this.pages.get(Integer.valueOf(pageIndex))).get();
    }

    /* access modifiers changed from: protected */
    public void waitForDecode(CodecPage vuPage) {
        vuPage.waitForDecode();
    }

    private int getTargetWidth() {
        return this.containerView.getWidth();
    }

    public int getEffectivePagesWidth() {
        CodecPage page = getPage(0);
        return getScaledWidth(page, calculateScale(page));
    }

    public int getEffectivePagesHeight() {
        CodecPage page = getPage(0);
        return getScaledHeight(page, calculateScale(page));
    }

    public int getEffectivePagesWidth(int index) {
        CodecPage page = getPage(index);
        return getScaledWidth(page, calculateScale(page));
    }

    public int getEffectivePagesHeight(int index) {
        CodecPage page = getPage(index);
        return getScaledHeight(page, calculateScale(page));
    }

    public int getPageWidth(int pageIndex) {
        return getPage(pageIndex).getWidth();
    }

    public int getPageHeight(int pageIndex) {
        return getPage(pageIndex).getHeight();
    }

    private void updateImage(DecodeTask currentDecodeTask, Bitmap bitmap) {
        currentDecodeTask.decodeCallback.decodeComplete(bitmap, currentDecodeTask.zoom);
    }

    private boolean isTaskDead(DecodeTask currentDecodeTask) {
        boolean z;
        synchronized (this.decodingFutures) {
            z = !this.decodingFutures.containsKey(currentDecodeTask.decodeKey);
        }
        return z;
    }

    public int getPageCount() {
        return this.document.getPageCount();
    }

    public void recycle() {
        synchronized (this.decodingFutures) {
            this.isRecycled = true;
        }
        for (Object key : this.decodingFutures.keySet()) {
            stopDecoding(key);
        }
        this.executorService.submit(new Runnable() {
            public void run() {
                for (SoftReference<CodecPage> codecPageSoftReference : DecodeServiceBase.this.pages.values()) {
                    CodecPage page = (CodecPage) codecPageSoftReference.get();
                    if (page != null) {
                        page.recycle();
                    }
                }
                DecodeServiceBase.this.document.recycle();
                DecodeServiceBase.this.codecContext.recycle();
            }
        });
        this.executorService.shutdown();
    }
}
