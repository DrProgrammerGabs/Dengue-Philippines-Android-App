package com.p000hl.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.mediav.ads.sdk.adcore.HttpCacher;
import com.p000hl.android.common.BookSetting;
import java.util.Formatter;
import java.util.Locale;

@SuppressLint({"HandlerLeak"})
/* renamed from: com.hl.android.VideoActivity */
public class VideoActivity extends Activity implements OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, Callback {
    public static String resourceID = "";
    Button btnAction;
    Button btnBack;
    LinearLayout controllerLay;
    Display currentDisplay;
    private boolean isPause = false;
    boolean isShow = false;
    private LinearLayout layout;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            VideoActivity.this.setProgress();
            if (VideoActivity.this.mediaPlayer != null && VideoActivity.this.mediaPlayer.isPlaying()) {
                sendEmptyMessageDelayed(1, 100);
            }
        }
    };
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            VideoActivity.this.mHandler.removeMessages(1);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (fromuser) {
                int totalMileSeconds = VideoActivity.this.mediaPlayer.getDuration();
                long left = ((long) (totalMileSeconds * progress)) / 100;
                long right = ((long) totalMileSeconds) - left;
                if (!(VideoActivity.this.textLeft == null || VideoActivity.this.textRight == null)) {
                    VideoActivity.this.textLeft.setText(VideoActivity.this.stringForTime((int) left));
                    VideoActivity.this.textRight.setText("-" + VideoActivity.this.stringForTime((int) right));
                }
                VideoActivity.this.mediaPlayer.seekTo((int) left);
                VideoActivity.this.mHandler.sendEmptyMessage(1);
            }
        }

        public void onStopTrackingTouch(SeekBar bar) {
            VideoActivity.this.setProgress();
        }
    };
    MediaController mediaController = null;
    MediaPlayer mediaPlayer;
    LinearLayout midLay;
    int orginHeight = 0;
    int orginWidth = 0;
    boolean readyToPlay = false;
    SeekBar seekBar;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    TextView textLeft;
    TextView textRight;
    int videoHeight = 0;
    int videoWidth = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BookSetting.IS_HOR) {
            setRequestedOrientation(0);
        } else {
            setRequestedOrientation(1);
        }
        this.layout = new LinearLayout(this);
        this.layout.setBackgroundColor(Color.rgb(192, 192, 192));
        this.layout.setOrientation(1);
        this.isPause = false;
        LinearLayout topLay = new LinearLayout(this);
        topLay.setGravity(3);
        LayoutParams topLp = new LayoutParams(-1, 60);
        Button btn = new Button(this);
        btn.setText("返回");
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoActivity.this.onBackPressed();
            }
        });
        topLay.setPadding(10, 10, 10, 10);
        topLay.addView(btn);
        this.midLay = new LinearLayout(this);
        this.midLay.setBackgroundColor(-16777216);
        this.midLay.setGravity(17);
        LayoutParams midLp = new LayoutParams(-1, -1, 1.0f);
        LinearLayout bottomLay = new LinearLayout(this);
        LayoutParams bottomLp = new LayoutParams(-1, 100);
        this.layout.addView(topLay, topLp);
        this.layout.addView(this.midLay, midLp);
        this.layout.addView(bottomLay, bottomLp);
        this.surfaceView = new SurfaceView(this);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(3);
        this.surfaceView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);
        this.mediaPlayer.setOnInfoListener(this);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnSeekCompleteListener(this);
        this.mediaPlayer.setOnVideoSizeChangedListener(this);
        this.currentDisplay = getWindowManager().getDefaultDisplay();
        this.btnBack = new Button(this);
        this.btnBack.setText("返回");
        this.btnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoActivity.this.onBackPressed();
            }
        });
        this.midLay.addView(this.surfaceView);
        this.controllerLay = new LinearLayout(this);
        this.btnAction = new Button(this);
        this.textLeft = new TextView(this);
        this.textRight = new TextView(this);
        this.btnAction.setBackgroundResource(C0048R.drawable.audio_stop);
        this.textLeft.setText("00:00");
        this.textLeft.setTextColor(-16777216);
        this.textRight.setText("00:00");
        this.textRight.setTextColor(-16777216);
        this.seekBar = new SeekBar(this);
        this.seekBar.setMax(100);
        this.seekBar.setOnSeekBarChangeListener(this.mSeekListener);
        this.btnAction.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (VideoActivity.this.mediaPlayer.isPlaying()) {
                    VideoActivity.this.mediaPlayer.pause();
                    VideoActivity.this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
                    return;
                }
                VideoActivity.this.mHandler.sendEmptyMessage(1);
                VideoActivity.this.mediaPlayer.start();
                VideoActivity.this.btnAction.setBackgroundResource(C0048R.drawable.audio_stop);
            }
        });
        this.controllerLay.addView(this.btnAction);
        this.controllerLay.addView(this.textLeft);
        this.controllerLay.setGravity(17);
        this.controllerLay.addView(this.seekBar, new LayoutParams(-1, -2, 1.0f));
        this.controllerLay.addView(this.textRight);
        LayoutParams clp = new LayoutParams(-1, -2);
        clp.setMargins(20, 10, 20, 10);
        bottomLay.addView(this.controllerLay, clp);
        setContentView(this.layout);
    }

    public static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x008c A[SYNTHETIC, Splitter:B:35:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0091 A[Catch:{ Exception -> 0x009a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void surfaceCreated(android.view.SurfaceHolder r15) {
        /*
            r14 = this;
            r6 = 0
            r11 = 0
            boolean r0 = com.p000hl.android.common.HLSetting.IsResourceSD     // Catch:{ Exception -> 0x0055 }
            if (r0 == 0) goto L_0x006d
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r1 = resourceID     // Catch:{ Exception -> 0x0055 }
            java.lang.String r10 = r0.getFilePath(r1)     // Catch:{ Exception -> 0x0055 }
            java.io.File r0 = r14.getFilesDir()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r13 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x0055 }
            boolean r0 = r10.contains(r13)     // Catch:{ Exception -> 0x0055 }
            if (r0 == 0) goto L_0x004f
            r9 = 0
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0055 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0055 }
            r0.<init>(r10)     // Catch:{ Exception -> 0x0055 }
            r12.<init>(r0)     // Catch:{ Exception -> 0x0055 }
            java.io.FileDescriptor r9 = r12.getFD()     // Catch:{ Exception -> 0x00a7, all -> 0x00a4 }
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x00a7, all -> 0x00a4 }
            r0.setDataSource(r9)     // Catch:{ Exception -> 0x00a7, all -> 0x00a4 }
            r11 = r12
        L_0x0033:
            if (r11 == 0) goto L_0x0038
            r11.close()     // Catch:{ Exception -> 0x0095 }
        L_0x0038:
            if (r6 == 0) goto L_0x003d
            r6.close()     // Catch:{ Exception -> 0x0095 }
        L_0x003d:
            android.widget.MediaController r0 = new android.widget.MediaController
            r0.<init>(r14)
            r14.mediaController = r0
            android.media.MediaPlayer r0 = r14.mediaPlayer
            r0.setDisplay(r15)
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x009f }
            r0.prepare()     // Catch:{ Exception -> 0x009f }
        L_0x004e:
            return
        L_0x004f:
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x0055 }
            r0.setDataSource(r10)     // Catch:{ Exception -> 0x0055 }
            goto L_0x0033
        L_0x0055:
            r8 = move-exception
        L_0x0056:
            java.lang.String r0 = "hl"
            java.lang.String r1 = " video"
            android.util.Log.e(r0, r1, r8)     // Catch:{ all -> 0x0089 }
            if (r11 == 0) goto L_0x0062
            r11.close()     // Catch:{ Exception -> 0x0068 }
        L_0x0062:
            if (r6 == 0) goto L_0x003d
            r6.close()     // Catch:{ Exception -> 0x0068 }
            goto L_0x003d
        L_0x0068:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x003d
        L_0x006d:
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r1 = resourceID     // Catch:{ Exception -> 0x0055 }
            android.content.res.AssetFileDescriptor r6 = r0.getFileFD(r14, r1)     // Catch:{ Exception -> 0x0055 }
            android.media.MediaPlayer r0 = r14.mediaPlayer     // Catch:{ Exception -> 0x0055 }
            java.io.FileDescriptor r1 = r6.getFileDescriptor()     // Catch:{ Exception -> 0x0055 }
            long r2 = r6.getStartOffset()     // Catch:{ Exception -> 0x0055 }
            long r4 = r6.getLength()     // Catch:{ Exception -> 0x0055 }
            r0.setDataSource(r1, r2, r4)     // Catch:{ Exception -> 0x0055 }
            goto L_0x0033
        L_0x0089:
            r0 = move-exception
        L_0x008a:
            if (r11 == 0) goto L_0x008f
            r11.close()     // Catch:{ Exception -> 0x009a }
        L_0x008f:
            if (r6 == 0) goto L_0x0094
            r6.close()     // Catch:{ Exception -> 0x009a }
        L_0x0094:
            throw r0
        L_0x0095:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x003d
        L_0x009a:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0094
        L_0x009f:
            r7 = move-exception
            r14.finish()
            goto L_0x004e
        L_0x00a4:
            r0 = move-exception
            r11 = r12
            goto L_0x008a
        L_0x00a7:
            r8 = move-exception
            r11 = r12
            goto L_0x0056
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.VideoActivity.surfaceCreated(android.view.SurfaceHolder):void");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    public void onSeekComplete(MediaPlayer mp) {
    }

    public void onBackPressed() {
        try {
            this.mediaPlayer.pause();
        } catch (Exception e) {
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mediaPlayer.release();
    }

    public void setFitSize() {
        int width = this.midLay.getMeasuredWidth();
        int height = this.midLay.getMeasuredHeight();
        float videoRatio = ((float) this.videoWidth) / ((float) this.videoHeight);
        if (((float) width) / ((float) height) > videoRatio) {
            this.videoHeight = height;
            this.videoWidth = (int) (((float) this.videoHeight) * videoRatio);
        } else {
            this.videoWidth = width;
            this.videoHeight = (int) (((float) this.videoWidth) / videoRatio);
        }
        this.surfaceView.setLayoutParams(new LayoutParams(this.videoWidth, this.videoHeight));
    }

    public void onPrepared(MediaPlayer mp) {
        this.videoWidth = mp.getVideoWidth();
        this.videoHeight = mp.getVideoHeight();
        setFitSize();
        mp.start();
        this.mHandler.sendEmptyMessage(1);
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "视频播放出现问题", 1).show();
        finish();
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    /* access modifiers changed from: private */
    public int setProgress() {
        if (this.mediaPlayer == null) {
            return 0;
        }
        int position = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        int right = duration - position;
        if (this.seekBar != null && duration > 0) {
            this.seekBar.setProgress((int) ((100 * ((long) position)) / ((long) duration)));
        }
        if (this.textLeft != null) {
            this.textLeft.setText(stringForTime(position));
        }
        if (this.textRight != null) {
            this.textRight.setText("-" + stringForTime(right));
        }
        if (this.mediaPlayer.isPlaying()) {
            this.btnAction.setBackgroundResource(C0048R.drawable.audio_stop);
            return position;
        }
        this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
        return position;
    }

    /* access modifiers changed from: private */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / HttpCacher.TIME_HOUR;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
            this.btnAction.setBackgroundResource(C0048R.drawable.audio_play);
            this.isPause = true;
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d("hl", "onresume");
        if (this.isPause) {
            this.mHandler.sendEmptyMessage(1);
            this.mediaPlayer.start();
            this.btnAction.setBackgroundResource(C0048R.drawable.audio_stop);
            this.isPause = false;
        }
        super.onResume();
    }
}
