package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLActivity;
import com.p000hl.android.VideoActivity;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.VideoComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import com.p000hl.android.view.component.moudle.HLMediaController;
import com.p000hl.callback.Action;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.component.VideoComponent */
public class VideoComponent extends RelativeLayout implements Component, OnCompletionListener, Callback, ComponentListener, ComponentPost, MediaPlayerControl, OnPreparedListener {
    public static int CONTROLERHEIGHT = 50;
    public static int CONTROLERWIDTH = HttpStatus.SC_OK;
    /* access modifiers changed from: private */
    public int PLAY_VEDIO = Button4Play.BTN_STATE_PLAY_NOMAL;
    private HLActivity activity;
    ArrayList<AnimationEntity> anims;
    private Bitmap bitmap = null;
    boolean cShow = false;
    private Context context;
    /* access modifiers changed from: private */
    public boolean controlenable = true;
    private HLMediaController controllerWindow;
    private RelativeLayout coverLayout;
    public Action doCompletAction;
    public VideoComponentEntity entity;
    Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            if (msg.what == VideoComponent.this.PLAY_VEDIO) {
                VideoComponent.this.startPlay();
            }
        }
    };
    private boolean hasRequestPlayBeforeSurfaceViewCreated = false;
    public ViewRecord initRecord;
    private boolean isHide = true;
    private boolean isPause = false;
    private boolean isPlaying = false;
    private boolean isResume = false;
    private boolean isStopped = false;
    public MediaPlayer mediaPlayer;
    private OnComponentCallbackListener onComponentCallbackListener;
    Rect src = null;
    private SurfaceView surfaceView;
    private boolean surfaceViewHasCreated = false;

    public VideoComponent(Context context2) {
        super(context2);
        this.context = context2;
        this.activity = (HLActivity) context2;
        if (this.entity.isHideAtBegining) {
            setVisibility(8);
        }
    }

    public VideoComponent(Context context2, ComponentEntity entity2) {
        super(context2);
        this.context = context2;
        this.entity = (VideoComponentEntity) entity2;
        this.activity = (HLActivity) context2;
        if (HLSetting.isNewActivityForVideo) {
        }
    }

    public VideoComponent(SurfaceView surfaceView2, String xmlID, Context context2) {
        super(context2);
    }

    public void pause() {
        if (!HLSetting.isNewActivityForVideo) {
            Log.d("hl", "pause");
            if (this.mediaPlayer != null && this.mediaPlayer.isPlaying() && this.controlenable) {
                this.mediaPlayer.pause();
                this.isPause = true;
                this.isPlaying = false;
                if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                    this.controllerWindow.updatePausePlay();
                }
            }
            this.activity.setVideoCover(this.entity.f7x, this.entity.f8y, getLayoutParams().width, getLayoutParams().height);
        }
    }

    public void continuePlay() {
        if (this.isPause) {
            initPlaying();
            this.mediaPlayer.start();
            BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
            if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                this.controllerWindow.updatePausePlay();
            }
        }
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension == null) {
            return null;
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (StringUtils.isEmpty(type)) {
            return "video/*";
        }
        return type;
    }

    public void play() {
        if (this.coverLayout != null) {
            removeView(this.coverLayout);
        }
        if (HLSetting.isNewActivityForVideo) {
            Intent videoIntent = new Intent(this.context, VideoActivity.class);
            VideoActivity.resourceID = getEntity().localSourceId;
            this.context.startActivity(videoIntent);
        } else if (this.isPlaying) {
        } else {
            if (this.isHide) {
                getEntity().isPlayVideoOrAudioAtBegining = true;
                setVisibility(0);
                this.surfaceView.setZOrderOnTop(true);
                if (this.surfaceViewHasCreated) {
                    prepareAndLoadVideo();
                } else {
                    this.hasRequestPlayBeforeSurfaceViewCreated = true;
                }
            } else if (!this.isPause) {
                prepareAndLoadVideo();
            } else if (this.controlenable) {
                initPlaying();
                this.mediaPlayer.start();
                BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                    this.controllerWindow.updatePausePlay();
                }
            }
        }
    }

    public HLMediaController getControllerWindow() {
        return this.controllerWindow;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.surfaceView == null) {
            return;
        }
        if (visibility == 4 || visibility == 8) {
            this.surfaceView.setLayoutParams(new LayoutParams(0, 0));
            this.isHide = true;
            if (this.entity.isVideoControlBarIsShow()) {
                this.cShow = false;
                try {
                    dismissControll();
                } catch (Exception e) {
                }
            }
        } else {
            this.isHide = false;
        }
    }

    private void initPlaying() {
        this.isStopped = false;
        this.isPause = false;
        this.isPlaying = true;
        m0d("initPlaying");
    }

    /* access modifiers changed from: private */
    public void startPlay() {
        if (this.mediaPlayer != null) {
            ((ViewCell) getParent()).setVisibility(0);
            try {
                this.mediaPlayer.start();
                if (this.controllerWindow != null) {
                    this.controllerWindow.setTotalDuration((long) this.mediaPlayer.getDuration());
                }
                initPlaying();
                if (!this.isResume) {
                    BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                } else {
                    this.isResume = false;
                    if (this.controllerWindow != null) {
                        this.controllerWindow.updatePlay();
                    }
                }
                if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                    this.controllerWindow.updatePausePlay();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareAndLoadVideo() {
        if (this.entity.isOnlineSource()) {
            loadUrl();
        } else {
            loadStream();
        }
        try {
            this.mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl() {
        try {
            this.mediaPlayer.setDataSource(this.entity.getLocalSourceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x008e A[SYNTHETIC, Splitter:B:32:0x008e] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0093 A[Catch:{ Exception -> 0x009c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadStream() {
        /*
            r14 = this;
            r6 = 0
            r11 = 0
            boolean r0 = com.p000hl.android.common.HLSetting.IsResourceSD     // Catch:{ Exception -> 0x004a }
            if (r0 == 0) goto L_0x0065
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x004a }
            com.hl.android.book.entity.VideoComponentEntity r1 = r14.entity     // Catch:{ Exception -> 0x004a }
            java.lang.String r1 = r1.localSourceId     // Catch:{ Exception -> 0x004a }
            java.lang.String r10 = r0.getFilePath(r1)     // Catch:{ Exception -> 0x004a }
            android.content.Context r0 = r14.getContext()     // Catch:{ Exception -> 0x004a }
            java.io.File r0 = r0.getFilesDir()     // Catch:{ Exception -> 0x004a }
            java.lang.String r13 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x004a }
            boolean r0 = r10.contains(r13)     // Catch:{ Exception -> 0x004a }
            if (r0 == 0) goto L_0x0044
            r9 = 0
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x004a }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x004a }
            r0.<init>(r10)     // Catch:{ Exception -> 0x004a }
            r12.<init>(r0)     // Catch:{ Exception -> 0x004a }
            java.io.FileDescriptor r9 = r12.getFD()     // Catch:{ Exception -> 0x00a4, all -> 0x00a1 }
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x00a4, all -> 0x00a1 }
            r0.setDataSource(r9)     // Catch:{ Exception -> 0x00a4, all -> 0x00a1 }
            r11 = r12
        L_0x0039:
            if (r11 == 0) goto L_0x003e
            r11.close()     // Catch:{ Exception -> 0x0097 }
        L_0x003e:
            if (r6 == 0) goto L_0x0043
            r6.close()     // Catch:{ Exception -> 0x0097 }
        L_0x0043:
            return
        L_0x0044:
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x004a }
            r0.setDataSource(r10)     // Catch:{ Exception -> 0x004a }
            goto L_0x0039
        L_0x004a:
            r8 = move-exception
        L_0x004b:
            java.lang.String r0 = "hl"
            java.lang.String r1 = "load error"
            android.util.Log.e(r0, r1, r8)     // Catch:{ all -> 0x008b }
            r8.printStackTrace()     // Catch:{ all -> 0x008b }
            if (r11 == 0) goto L_0x005a
            r11.close()     // Catch:{ Exception -> 0x0060 }
        L_0x005a:
            if (r6 == 0) goto L_0x0043
            r6.close()     // Catch:{ Exception -> 0x0060 }
            goto L_0x0043
        L_0x0060:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0043
        L_0x0065:
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x004a }
            android.content.Context r1 = r14.getContext()     // Catch:{ Exception -> 0x004a }
            com.hl.android.book.entity.VideoComponentEntity r2 = r14.entity     // Catch:{ Exception -> 0x004a }
            java.lang.String r2 = r2.localSourceId     // Catch:{ Exception -> 0x004a }
            java.lang.String r2 = r2.trim()     // Catch:{ Exception -> 0x004a }
            android.content.res.AssetFileDescriptor r6 = r0.getFileFD(r1, r2)     // Catch:{ Exception -> 0x004a }
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x004a }
            java.io.FileDescriptor r1 = r6.getFileDescriptor()     // Catch:{ Exception -> 0x004a }
            long r2 = r6.getStartOffset()     // Catch:{ Exception -> 0x004a }
            long r4 = r6.getLength()     // Catch:{ Exception -> 0x004a }
            r0.setDataSource(r1, r2, r4)     // Catch:{ Exception -> 0x004a }
            goto L_0x0039
        L_0x008b:
            r0 = move-exception
        L_0x008c:
            if (r11 == 0) goto L_0x0091
            r11.close()     // Catch:{ Exception -> 0x009c }
        L_0x0091:
            if (r6 == 0) goto L_0x0096
            r6.close()     // Catch:{ Exception -> 0x009c }
        L_0x0096:
            throw r0
        L_0x0097:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0043
        L_0x009c:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0096
        L_0x00a1:
            r0 = move-exception
            r11 = r12
            goto L_0x008c
        L_0x00a4:
            r8 = move-exception
            r11 = r12
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.VideoComponent.loadStream():void");
    }

    public void load() {
        int width = getLayoutParams().width;
        if (width < ScreenUtils.dip2px(this.context, (float) CONTROLERWIDTH)) {
            width = ScreenUtils.dip2px(this.context, (float) CONTROLERWIDTH);
        }
        if (this.entity.isVideoControlBarIsShow()) {
            this.controllerWindow = new HLMediaController(this.context, width, ScreenUtils.dip2px(this.context, (float) CONTROLERHEIGHT));
        }
        this.surfaceView = new SurfaceView(this.context);
        this.surfaceView.getHolder().addCallback(this);
        this.surfaceView.getHolder().setFormat(-3);
        this.surfaceView.setBackgroundColor(-16777216);
        addView(this.surfaceView);
    }

    public SurfaceView getSurfaceView() {
        return this.surfaceView;
    }

    public void stop() {
        if (!HLSetting.isNewActivityForVideo) {
            m0d("stop");
            if (!this.isStopped) {
                try {
                    if (this.controlenable) {
                        this.mediaPlayer.stop();
                        this.mediaPlayer.reset();
                        this.isPlaying = false;
                        this.isStopped = true;
                        this.isPause = false;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (VideoComponentEntity) entity2;
    }

    public void load(InputStream is) {
    }

    public void resume() {
        requestFocus();
        bringToFront();
        this.isResume = true;
        if (this.controllerWindow != null) {
            this.controllerWindow.dismiss();
            this.cShow = false;
        }
        if (HLSetting.isNewActivityForVideo) {
        }
    }

    public void onCompletion(MediaPlayer mp) {
        this.onComponentCallbackListener.setPlayComplete();
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
        if (this.controllerWindow != null) {
            this.controllerWindow.completion(this.mediaPlayer);
        }
        if (this.doCompletAction != null) {
            this.doCompletAction.doAction();
            this.doCompletAction = null;
        }
        if (this.cShow) {
            dismissControll();
            this.cShow = false;
        }
        this.isPlaying = false;
        this.isPause = true;
        if (this.entity.autoLoop) {
            play();
        }
    }

    public void hide() {
        if (!HLSetting.isNewActivityForVideo && !this.isHide) {
            setVisibility(4);
            BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
        }
    }

    public void show() {
        if (!HLSetting.isNewActivityForVideo) {
            setVisibility(0);
            this.surfaceView.setLayoutParams(new LayoutParams(-1, -1));
            this.surfaceView.setZOrderOnTop(true);
            bringToFront();
            requestFocus();
            BookController.getInstance().getViewPage().bringChildToFront(this);
            BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceViewHasCreated = true;
        this.isHide = false;
        if (!HLSetting.isNewActivityForVideo) {
            String cover = this.entity.getCoverSourceID();
            if (!StringUtils.isEmpty(cover) && this.bitmap == null) {
                this.bitmap = BitmapUtils.getBitMap(cover, this.context);
            }
            if (this.bitmap == null) {
                this.bitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.hlvidiodefault);
            }
            if (this.bitmap != null) {
                this.coverLayout = new RelativeLayout(this.context);
                this.surfaceView.setBackgroundColor(-1);
                this.coverLayout.setBackgroundDrawable(new BitmapDrawable(this.bitmap));
                this.surfaceView.setBackgroundDrawable(new BitmapDrawable(this.bitmap));
                LayoutParams layoutParams = new LayoutParams(-1, this.surfaceView.getHeight());
                if (this.entity.isVideoControlBarIsShow()) {
                    ImageView imageView = new ImageView(this.context);
                    imageView.setImageResource(C0048R.drawable.audio_play);
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                    LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                    layoutParams2.addRule(13);
                    this.coverLayout.addView(imageView, layoutParams2);
                    imageView.setOnTouchListener(new OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            if (VideoComponent.this.controlenable && event.getAction() == 1) {
                                VideoComponent.this.play();
                            }
                            return true;
                        }
                    });
                }
                addView(this.coverLayout, layoutParams);
            }
            setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    boolean z;
                    if (VideoComponent.this.controlenable && event.getAction() == 0 && VideoComponent.this.entity.isVideoControlBarIsShow()) {
                        if (VideoComponent.this.getVisibility() == 4) {
                            VideoComponent.this.dismissControll();
                            VideoComponent.this.cShow = false;
                        } else {
                            if (VideoComponent.this.cShow) {
                                VideoComponent.this.dismissControll();
                            } else {
                                VideoComponent.this.showControll();
                            }
                            VideoComponent videoComponent = VideoComponent.this;
                            if (!VideoComponent.this.cShow) {
                                z = true;
                            } else {
                                z = false;
                            }
                            videoComponent.cShow = z;
                        }
                    }
                    return false;
                }
            });
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    VideoComponent.this.mediaPlayer.reset();
                    return false;
                }
            });
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setDisplay(this.surfaceView.getHolder());
            if (this.hasRequestPlayBeforeSurfaceViewCreated) {
                prepareAndLoadVideo();
            } else if (this.isResume) {
                play();
            }
        }
    }

    /* access modifiers changed from: private */
    public void showControll() {
        this.controllerWindow.setVideoView(this);
        this.controllerWindow.show();
    }

    /* access modifiers changed from: private */
    public void dismissControll() {
        if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
            this.controllerWindow.dismiss();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            stop();
            this.controllerWindow.dismiss();
        } catch (Exception e) {
        }
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void callBackListener() {
    }

    public void recyle() {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.release();
                if (this.bitmap != null) {
                    BitmapUtils.recycleBitmap(this.bitmap);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getBufferPercentage() {
        return getBufferPercentage();
    }

    public int getCurrentPosition() {
        try {
            return this.mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return -999;
        }
    }

    public int getDuration() {
        return this.mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        if (!HLSetting.isNewActivityForVideo && this.mediaPlayer != null) {
            return this.mediaPlayer.isPlaying();
        }
        return false;
    }

    public void seekTo(int pos) {
        this.mediaPlayer.seekTo(pos);
    }

    public void start() {
        if (!HLSetting.isNewActivityForVideo) {
            m0d("start");
            play();
        }
    }

    public void onPrepared(MediaPlayer mp) {
        initPlaying();
        this.handler.sendEmptyMessageDelayed(this.PLAY_VEDIO, (long) (this.entity.delay * 1000.0d));
    }

    @SuppressLint({"NewApi"})
    public ViewRecord getCurrentRecord() {
        ViewRecord curRecord = new ViewRecord();
        curRecord.mHeight = getLayoutParams().width;
        curRecord.mWidth = getLayoutParams().height;
        curRecord.f28mX = getX();
        curRecord.f29mY = getY();
        curRecord.mRotation = getRotation();
        return curRecord;
    }

    /* renamed from: d */
    private void m0d(String message) {
        if (0 != 0) {
            Log.d("hl", message + "    || id is " + this);
        }
    }

    public void setControlUnable() {
        this.controlenable = false;
    }

    public int getAudioSessionId() {
        return 0;
    }
}
