package com.p000hl.android.view.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLActivity;
import com.p000hl.android.VideoActivity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.VideoComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.moudle.HLMediaController;
import java.io.InputStream;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.component.VideoView4Online */
public class VideoView4Online extends RelativeLayout implements Component, OnPreparedListener, OnCompletionListener, OnErrorListener {
    public static int CONTROLERHEIGHT = 50;
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public boolean cShow = false;
    private HLMediaController controllerWindow;
    private RelativeLayout coverLayout;
    private boolean hasPlayAtBegin = false;
    private boolean isHide = true;
    private boolean isPause = false;
    private boolean isPlaying = false;
    private boolean isStopped = false;
    private Context mContext;
    /* access modifiers changed from: private */
    public VideoComponentEntity mEntity;
    private VideoView mVideoView;
    private Uri uri;

    public VideoView4Online(Context context) {
        super(context);
    }

    public VideoView4Online(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (VideoComponentEntity) entity;
        this.mVideoView = new VideoView(context);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (VideoComponentEntity) entity;
    }

    public void load() {
        if (!HLSetting.isNewActivityForVideo) {
            this.controllerWindow = new HLMediaController(this.mContext, getLayoutParams().width, CONTROLERHEIGHT);
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            if (this.mEntity.isHideAtBegining) {
                setVisibility(8);
            }
            this.mVideoView.setOnPreparedListener(this);
            this.mVideoView.setOnCompletionListener(this);
            this.mVideoView.setOnErrorListener(this);
            this.uri = Uri.parse(this.mEntity.getLocalSourceId());
            this.mVideoView.setVideoURI(this.uri);
            if (this.mEntity.isVideoControlBarIsShow()) {
                LayoutParams layoutParams1 = new LayoutParams(-1, getLayoutParams().height - ScreenUtils.dip2px(this.mContext, (float) CONTROLERHEIGHT));
                layoutParams1.addRule(10);
                addView(this.mVideoView, layoutParams1);
            } else {
                addView(this.mVideoView, layoutParams);
            }
            String cover = this.mEntity.getCoverSourceID();
            if (!StringUtils.isEmpty(cover) && this.bitmap == null) {
                this.bitmap = BitmapUtils.getBitMap(cover, this.mContext);
            }
            if (this.bitmap != null) {
                this.coverLayout = new RelativeLayout(this.mContext);
                this.coverLayout.setBackgroundDrawable(new BitmapDrawable(this.bitmap));
                LayoutParams layoutParams11 = new LayoutParams(-1, HttpStatus.SC_BAD_REQUEST);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(C0048R.drawable.audio_play);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                layoutParams2.addRule(13);
                this.coverLayout.addView(imageView, layoutParams2);
                addView(this.coverLayout, layoutParams11);
                this.coverLayout.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 1) {
                            VideoView4Online.this.play();
                        }
                        return true;
                    }
                });
            }
            setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    boolean z;
                    if (event.getAction() == 0 && VideoView4Online.this.mEntity.isVideoControlBarIsShow()) {
                        if (VideoView4Online.this.getVisibility() == 4) {
                            VideoView4Online.this.dismissControll();
                        } else {
                            if (VideoView4Online.this.cShow) {
                                VideoView4Online.this.dismissControll();
                            } else {
                                VideoView4Online.this.showControll();
                            }
                            VideoView4Online videoView4Online = VideoView4Online.this;
                            if (!VideoView4Online.this.cShow) {
                                z = true;
                            } else {
                                z = false;
                            }
                            videoView4Online.cShow = z;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void startPlay() {
        ((ViewCell) getParent()).setVisibility(0);
        this.mVideoView.start();
        this.controllerWindow.setTotalDuration((long) this.mVideoView.getDuration());
        initPlaying();
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
        if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
            this.controllerWindow.updatePausePlay();
        }
    }

    private void initPlaying() {
        this.isStopped = false;
        this.isPause = false;
        this.isPlaying = true;
    }

    /* access modifiers changed from: private */
    public void showControll() {
        this.controllerWindow.setVideoView(this.mVideoView);
        this.controllerWindow.show();
        LayoutParams rl = new LayoutParams(-1, -2);
        rl.addRule(12);
        addView(this.controllerWindow, rl);
    }

    /* access modifiers changed from: private */
    public void dismissControll() {
        if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
            this.controllerWindow.dismiss();
            removeView(this.controllerWindow);
        }
    }

    public void onPrepared(MediaPlayer mp) {
        if (this.mEntity.isPlayVideoOrAudioAtBegining && !this.hasPlayAtBegin) {
            startPlay();
            this.hasPlayAtBegin = true;
        }
    }

    public void onCompletion(MediaPlayer arg0) {
    }

    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        return false;
    }

    public void load(InputStream is) {
    }

    public void play() {
        if (this.coverLayout != null) {
            removeView(this.coverLayout);
        }
        if (HLSetting.isNewActivityForVideo) {
            Intent videoIntent = new Intent(this.mContext, VideoActivity.class);
            VideoActivity.resourceID = getEntity().localSourceId;
            this.mContext.startActivity(videoIntent);
        } else if (this.isPlaying) {
        } else {
            if (this.isHide) {
                getEntity().isPlayVideoOrAudioAtBegining = true;
                setVisibility(0);
                this.mVideoView.setZOrderOnTop(true);
            } else if (this.isPause) {
                initPlaying();
                this.mVideoView.start();
                BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                    this.controllerWindow.updatePausePlay();
                }
            } else if (this.mEntity.isOnlineSource()) {
                this.uri = Uri.parse(this.mEntity.getLocalSourceId());
                this.mVideoView.setVideoURI(this.uri);
            }
        }
    }

    public void stop() {
        if (!HLSetting.isNewActivityForVideo && !this.isStopped) {
            try {
                this.mVideoView.stopPlayback();
                this.isPlaying = false;
                this.isStopped = true;
                this.isPause = false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void hide() {
        if (!HLSetting.isNewActivityForVideo && !this.isHide) {
            setVisibility(4);
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
        }
    }

    public void show() {
        if (!HLSetting.isNewActivityForVideo) {
            setVisibility(0);
            this.mVideoView.setZOrderOnTop(true);
            bringToFront();
            requestFocus();
            BookController.getInstance().getViewPage().bringChildToFront(this);
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
        }
    }

    public void resume() {
    }

    public void pause() {
        if (!HLSetting.isNewActivityForVideo) {
            Log.d("hl", "pause");
            if (this.mVideoView != null && this.mVideoView.isPlaying()) {
                this.mVideoView.pause();
                this.isPause = true;
                this.isPlaying = false;
                if (this.controllerWindow != null && this.controllerWindow.isShowing()) {
                    this.controllerWindow.updatePausePlay();
                }
            }
            ((HLActivity) this.mContext).setVideoCover(this.mEntity.f7x, this.mEntity.f8y, getLayoutParams().width, getLayoutParams().height);
        }
    }
}
