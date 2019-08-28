package com.p000hl.android.view.component.moudle;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.VideoComponent;
import java.util.Formatter;
import java.util.Locale;

/* renamed from: com.hl.android.view.component.moudle.HLMediaController */
public class HLMediaController extends LinearLayout {
    private static final int SHOW_PROGRESS = 2;
    private ImageButton btnAction;
    private boolean isComplete;
    private LinearLayout layout;
    private Context mContext;
    /* access modifiers changed from: private */
    public boolean mDragging;
    /* access modifiers changed from: private */
    public long mDuration;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private OnClickListener mPauseListener;
    /* access modifiers changed from: private */
    public MediaPlayerControl mPlayer;
    private SeekBar mSeekBar;
    private OnSeekBarChangeListener mSeekListener;
    /* access modifiers changed from: private */
    public boolean mShowing;
    private WindowManager mWindowManager;
    private MediaPlayer mediaPlayer;
    /* access modifiers changed from: private */
    public TextView textLeft;
    /* access modifiers changed from: private */
    public TextView textRight;
    private LayoutParams wmParams;

    public void setTotalDuration(long duration) {
        this.mDuration = duration;
        if (this.textLeft != null) {
            this.textLeft.setText(stringForTime(this.mDuration));
        }
        if (this.textRight != null) {
            this.textRight.setText("-" + stringForTime(0));
        }
    }

    public HLMediaController(Context context) {
        this(context, -2, -2);
    }

    public HLMediaController(Context context, int width, int height) {
        super(context);
        this.mDuration = 0;
        this.isComplete = false;
        this.mSeekListener = new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar bar) {
                HLMediaController.this.mHandler.removeMessages(2);
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (fromuser) {
                    if (HLMediaController.this.mDuration == 0) {
                        HLMediaController.this.setTotalDuration((long) HLMediaController.this.mPlayer.getDuration());
                    }
                    long left = (HLMediaController.this.mDuration * ((long) progress)) / 100;
                    long right = HLMediaController.this.mDuration - left;
                    if (!(HLMediaController.this.textLeft == null || HLMediaController.this.textRight == null)) {
                        HLMediaController.this.textLeft.setText(HLMediaController.this.stringForTime((long) ((int) left)));
                        HLMediaController.this.textRight.setText("-" + HLMediaController.this.stringForTime((long) ((int) right)));
                    }
                    if (HLMediaController.this.mPlayer.isPlaying()) {
                        HLMediaController.this.mPlayer.seekTo((int) left);
                    }
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                if (HLMediaController.this.mPlayer.isPlaying()) {
                    HLMediaController.this.mDragging = false;
                    HLMediaController.this.setProgress();
                    HLMediaController.this.updatePausePlay();
                }
            }
        };
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2:
                        int pos = HLMediaController.this.setProgress();
                        if (!HLMediaController.this.mDragging && HLMediaController.this.mShowing && HLMediaController.this.mPlayer.isPlaying() && pos > 0) {
                            sendMessageDelayed(obtainMessage(2), (long) (100 - (pos % 100)));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                HLMediaController.this.doPauseResume();
            }
        };
        this.mContext = context;
        addView(drawView(), new LinearLayout.LayoutParams(width, height));
        this.mWindowManager = ((Activity) context).getWindowManager();
        this.wmParams = new LayoutParams();
        this.wmParams.type = 2002;
        this.wmParams.format = 1;
        this.wmParams.flags = 131080;
        this.wmParams.gravity = 51;
    }

    private View drawView() {
        LinearLayout rootLay = new LinearLayout(this.mContext);
        this.layout = new LinearLayout(this.mContext);
        this.layout.setBackgroundResource(C0048R.drawable.media_control_bg);
        this.layout.setOrientation(0);
        LinearLayout.LayoutParams layLp = new LinearLayout.LayoutParams(-1, -1);
        int padding = ScreenUtils.dip2px(this.mContext, 20.0f);
        this.layout.setPadding(padding, 0, padding, 0);
        this.layout.setLayoutParams(layLp);
        this.layout.setGravity(16);
        LinearLayout.LayoutParams leftLp = new LinearLayout.LayoutParams(-2, -2);
        this.btnAction = new ImageButton(this.mContext);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(ScreenUtils.dip2px(this.mContext, (float) (VideoComponent.CONTROLERHEIGHT / 2)), ScreenUtils.dip2px(this.mContext, (float) (VideoComponent.CONTROLERHEIGHT / 2)));
        this.btnAction.setScaleType(ScaleType.FIT_XY);
        this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
        this.layout.addView(this.btnAction, btnLp);
        this.textLeft = new TextView(this.mContext);
        this.textLeft.setTextColor(-1);
        this.textLeft.setTextSize(0, 20.0f);
        this.textLeft.setText("     ");
        this.layout.addView(this.textLeft, leftLp);
        this.mSeekBar = new SeekBar(this.mContext);
        this.mSeekBar.setMax(100);
        this.mSeekBar.setMinimumHeight(ScreenUtils.dip2px(this.mContext, (float) (VideoComponent.CONTROLERHEIGHT / 3)));
        this.layout.addView(this.mSeekBar, new LinearLayout.LayoutParams(-1, -2, 1.0f));
        this.textRight = new TextView(this.mContext);
        this.textRight.setTextColor(-1);
        this.textRight.setText("     ");
        this.textRight.setTextSize(0, 20.0f);
        this.layout.addView(this.textRight, leftLp);
        this.layout.setLayoutParams(layLp);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.mSeekBar.setOnSeekBarChangeListener(this.mSeekListener);
        this.btnAction.setOnClickListener(this.mPauseListener);
        rootLay.addView(this.layout);
        rootLay.setBackgroundColor(0);
        return rootLay;
    }

    public void show() {
        try {
            int[] location = new int[2];
            ((View) this.mPlayer).getLocationInWindow(location);
            this.wmParams.width = ((View) this.mPlayer).getLayoutParams().width;
            this.wmParams.height = ScreenUtils.dip2px(this.mContext, (float) VideoComponent.CONTROLERHEIGHT);
            this.wmParams.x = location[0];
            this.wmParams.y = (((View) this.mPlayer).getLayoutParams().height + location[1]) - this.wmParams.height;
            this.mWindowManager.addView(this, this.wmParams);
            updatePausePlay();
            if (this.mPlayer.isPlaying()) {
                setProgress();
            }
            this.mShowing = true;
            if (this.mPlayer.isPlaying()) {
                this.mHandler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
        }
    }

    public void dismiss() {
        try {
            this.mShowing = false;
            this.mWindowManager.removeView(this);
        } catch (Exception e) {
        }
    }

    public void setVideoView(MediaPlayerControl player) {
        this.mPlayer = player;
    }

    public void completion(MediaPlayer player) {
        this.mediaPlayer = player;
        this.isComplete = true;
        this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
    }

    public void updatePausePlay() {
        if (this.layout != null && this.btnAction != null) {
            updatePlay();
        }
    }

    public void updatePlay() {
        if (this.mPlayer.isPlaying()) {
            this.mHandler.sendEmptyMessage(2);
            this.btnAction.setBackgroundResource(C0048R.drawable.audio_stop);
            return;
        }
        this.mHandler.removeMessages(2);
        this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
    }

    public void upDateWindowPosition() {
        try {
            int[] location = new int[2];
            ((View) this.mPlayer).getLocationInWindow(location);
            this.wmParams.width = ((View) this.mPlayer).getLayoutParams().width;
            this.wmParams.height = ScreenUtils.dip2px(this.mContext, (float) VideoComponent.CONTROLERHEIGHT);
            this.wmParams.x = location[0];
            this.wmParams.y = (((View) this.mPlayer).getLayoutParams().height + location[1]) - this.wmParams.height;
            this.mWindowManager.updateViewLayout(this, this.wmParams);
        } catch (Exception e) {
        }
    }

    public void doPauseResume() {
        if (this.isComplete) {
            long left = (this.mDuration * ((long) this.mSeekBar.getProgress())) / 100;
            long right = this.mDuration - left;
            if (left == 0 || right == 0) {
                this.mediaPlayer.seekTo(0);
            } else {
                this.mediaPlayer.seekTo((int) left);
            }
            this.mediaPlayer.start();
            this.mPlayer.start();
            this.isComplete = false;
        } else if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            long left2 = (this.mDuration * ((long) this.mSeekBar.getProgress())) / 100;
            long right2 = this.mDuration - left2;
            if (left2 == 0 || right2 == 0) {
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.seekTo(0);
                }
            } else if (this.mediaPlayer != null) {
                this.mediaPlayer.seekTo((int) left2);
            }
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    /* access modifiers changed from: private */
    public int setProgress() {
        if (this.mPlayer == null || this.mDragging) {
            return 0;
        }
        int position = this.mPlayer.getCurrentPosition();
        if (position < 0) {
            return position;
        }
        long right = this.mDuration - ((long) position);
        if (this.mSeekBar != null && this.mDuration > 0) {
            this.mSeekBar.setProgress((int) ((100 * ((long) position)) / this.mDuration));
        }
        if (this.textLeft != null) {
            this.textLeft.setText(stringForTime((long) position));
        }
        if (this.textRight == null) {
            return position;
        }
        this.textRight.setText("-" + stringForTime(right));
        return position;
    }

    /* access modifiers changed from: private */
    public String stringForTime(long timeMs) {
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)}).toString();
    }

    public boolean isShowing() {
        return this.mShowing;
    }
}
