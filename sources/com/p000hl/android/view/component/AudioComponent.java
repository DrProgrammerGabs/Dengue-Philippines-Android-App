package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/* renamed from: com.hl.android.view.component.AudioComponent */
public class AudioComponent extends RelativeLayout implements Component, OnCompletionListener, OnPreparedListener, ComponentListener, ComponentPost {
    /* access modifiers changed from: private */
    public int PLAY_MUSIC = 10010;
    ArrayList<AnimationEntity> anims;
    private boolean controlenable = true;
    public ComponentEntity entity;
    /* access modifiers changed from: private */
    public boolean hasPrePared;
    private ImageView img;
    private boolean init = false;
    public boolean isBackGroundMusic = false;
    private boolean isCompleted = false;
    private boolean isPaused = false;
    private boolean isPlaying = false;
    private boolean isStopped = false;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == AudioComponent.this.PLAY_MUSIC) {
                AudioComponent.this.doPlay();
            } else if (AudioComponent.this.mediaPlayer != null && AudioComponent.this.mediaPlayer.isPlaying()) {
                AudioComponent.this.setProgress();
                sendEmptyMessageDelayed(1, 100);
            }
        }
    };
    /* access modifiers changed from: private */
    public MySeekBar mSeekBar;
    /* access modifiers changed from: private */
    public OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        private boolean isPlaying = false;

        public void onStartTrackingTouch(SeekBar bar) {
            if (AudioComponent.this.hasPrePared) {
                AudioComponent.this.mHandler.removeMessages(1);
                this.isPlaying = AudioComponent.this.mediaPlayer.isPlaying();
                if (this.isPlaying) {
                    AudioComponent.this.mediaPlayer.pause();
                }
            }
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!AudioComponent.this.hasPrePared) {
                AudioComponent.this.mSeekBar.setProgress(0);
            } else if (fromuser) {
                AudioComponent.this.totalMileSeconds = AudioComponent.this.mediaPlayer.getDuration();
                AudioComponent.this.mediaPlayer.seekTo((int) (((long) (AudioComponent.this.totalMileSeconds * progress)) / 1000));
            }
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (AudioComponent.this.hasPrePared) {
                AudioComponent.this.setProgress();
                AudioComponent.this.mHandler.sendEmptyMessage(1);
                if (this.isPlaying) {
                    AudioComponent.this.mediaPlayer.start();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mediaDuration;
    /* access modifiers changed from: private */
    public MediaPlayer mediaPlayer;
    private OnComponentCallbackListener onComponentCallbackListener;
    private int[] progressImages;
    /* access modifiers changed from: private */
    public int totalMileSeconds = 0;

    /* renamed from: com.hl.android.view.component.AudioComponent$MySeekBar */
    class MySeekBar extends RelativeLayout {
        Button4Play btnAction;
        Context mContext;
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        int mHeight;
        int mWidth;
        SeekBar seekbar;
        TextView textRight;

        public MySeekBar(Context context, int width, int height) {
            super(context);
            this.mWidth = width;
            this.mHeight = height;
            removeAllViews();
            addView(drawView(context), width, height);
        }

        public void changeToPlayImage() {
            this.btnAction.change2ShowPlay();
        }

        public void changeToPauseImage() {
            this.btnAction.change2ShowStop();
        }

        public void setProgress(int pos) {
            String curString;
            this.seekbar.setProgress(pos);
            if (pos != 0) {
                curString = stringForTime((long) AudioComponent.this.mediaPlayer.getCurrentPosition());
            } else {
                curString = "00:00";
            }
            this.textRight.setText(curString + "/" + stringForTime((long) AudioComponent.this.mediaDuration));
        }

        private String stringForTime(long timeMs) {
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

        private LinearLayout drawView(Context context) {
            LinearLayout drawView = new LinearLayout(context);
            drawView.setOrientation(0);
            drawView.setGravity(16);
            LayoutParams btnLp = new LayoutParams(30, 30);
            btnLp.leftMargin = 5;
            this.btnAction = new Button4Play(context);
            drawView.addView(this.btnAction, btnLp);
            this.seekbar = new SeekBar(context);
            this.seekbar.setMax(1000);
            drawView.addView(this.seekbar, new LayoutParams(-1, 15, 1.0f));
            Drawable progressDrawable = getResources().getDrawable(C0048R.drawable.media_player_seekbar_selector);
            progressDrawable.setBounds(this.seekbar.getProgressDrawable().getBounds());
            this.seekbar.setProgressDrawable(progressDrawable);
            Drawable thumb = getResources().getDrawable(C0048R.drawable.player_seekbar_thumbnail);
            progressDrawable.setBounds(this.seekbar.getProgressDrawable().getBounds());
            this.seekbar.setThumb(thumb);
            this.seekbar.setPadding(10, 0, 10, 0);
            this.mFormatBuilder = new StringBuilder();
            this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
            this.textRight = new TextView(context);
            this.textRight.setText("00:00/00:00");
            this.textRight.setTextColor(-1);
            this.textRight.setGravity(16);
            this.textRight.setTextSize(0, 15.0f);
            LayoutParams righttLp = new LayoutParams(-2, -2);
            righttLp.rightMargin = 10;
            righttLp.leftMargin = 5;
            drawView.addView(this.textRight, righttLp);
            this.seekbar.setOnSeekBarChangeListener(AudioComponent.this.mSeekListener);
            this.btnAction.setActionListener(new ActionListener() {
                public void onDoStop() {
                    AudioComponent.this.playOrStop();
                }

                public void onDoPlay() {
                    AudioComponent.this.playOrStop();
                }
            });
            return drawView;
        }

        public Button4Play getBtnAction() {
            return this.btnAction;
        }

        public SeekBar getSeekbar() {
            return this.seekbar;
        }
    }

    public AudioComponent(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setDisplay(null);
            if (entity2.autoLoop) {
                this.mediaPlayer.setLooping(true);
            }
            this.mediaPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    if (AudioComponent.this.mediaPlayer != null) {
                        AudioComponent.this.mediaPlayer.reset();
                    }
                    return false;
                }
            });
        }
    }

    public AudioComponent(Context context) {
        super(context);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b5 A[SYNTHETIC, Splitter:B:38:0x00b5] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bb A[Catch:{ Exception -> 0x01cd }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void load() {
        /*
            r18 = this;
            r7 = 0
            r11 = 0
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r1 = r0.entity     // Catch:{ Exception -> 0x007a }
            boolean r1 = r1.isOnlineSource()     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0035
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r1 = r0.entity     // Catch:{ Exception -> 0x007a }
            java.lang.String r17 = r1.getLocalSourceId()     // Catch:{ Exception -> 0x007a }
            r0 = r18
            android.media.MediaPlayer r1 = r0.mediaPlayer     // Catch:{ Exception -> 0x007a }
            r0 = r17
            r1.setDataSource(r0)     // Catch:{ Exception -> 0x007a }
        L_0x001d:
            r1 = 1
            r0 = r18
            r0.init = r1     // Catch:{ Exception -> 0x007a }
            r0 = r18
            boolean r1 = r0.isBackGroundMusic     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x00c6
            if (r7 == 0) goto L_0x002e
            r7.close()     // Catch:{ Exception -> 0x00c0 }
            r7 = 0
        L_0x002e:
            if (r11 == 0) goto L_0x0034
            r11.close()     // Catch:{ Exception -> 0x00c0 }
            r11 = 0
        L_0x0034:
            return
        L_0x0035:
            boolean r1 = com.p000hl.android.common.HLSetting.IsResourceSD     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x008b
            com.hl.android.core.utils.FileUtils r1 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r2 = r0.entity     // Catch:{ Exception -> 0x007a }
            java.lang.String r2 = r2.localSourceId     // Catch:{ Exception -> 0x007a }
            java.lang.String r10 = r1.getFilePath(r2)     // Catch:{ Exception -> 0x007a }
            android.content.Context r1 = r18.getContext()     // Catch:{ Exception -> 0x007a }
            java.io.File r1 = r1.getFilesDir()     // Catch:{ Exception -> 0x007a }
            java.lang.String r16 = r1.getAbsolutePath()     // Catch:{ Exception -> 0x007a }
            r0 = r16
            boolean r1 = r10.contains(r0)     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0072
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x007a }
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x007a }
            r1.<init>(r10)     // Catch:{ Exception -> 0x007a }
            r12.<init>(r1)     // Catch:{ Exception -> 0x007a }
            java.io.FileDescriptor r9 = r12.getFD()     // Catch:{ Exception -> 0x01d7, all -> 0x01d3 }
            r0 = r18
            android.media.MediaPlayer r1 = r0.mediaPlayer     // Catch:{ Exception -> 0x01d7, all -> 0x01d3 }
            r1.setDataSource(r9)     // Catch:{ Exception -> 0x01d7, all -> 0x01d3 }
            r11 = r12
            goto L_0x001d
        L_0x0072:
            r0 = r18
            android.media.MediaPlayer r1 = r0.mediaPlayer     // Catch:{ Exception -> 0x007a }
            r1.setDataSource(r10)     // Catch:{ Exception -> 0x007a }
            goto L_0x001d
        L_0x007a:
            r8 = move-exception
        L_0x007b:
            r8.printStackTrace()     // Catch:{ all -> 0x00b2 }
            if (r7 == 0) goto L_0x0084
            r7.close()     // Catch:{ Exception -> 0x01c7 }
            r7 = 0
        L_0x0084:
            if (r11 == 0) goto L_0x0034
            r11.close()     // Catch:{ Exception -> 0x01c7 }
            r11 = 0
            goto L_0x0034
        L_0x008b:
            com.hl.android.core.utils.FileUtils r1 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x007a }
            android.content.Context r2 = r18.getContext()     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r3 = r0.entity     // Catch:{ Exception -> 0x007a }
            java.lang.String r3 = r3.localSourceId     // Catch:{ Exception -> 0x007a }
            android.content.res.AssetFileDescriptor r7 = r1.getFileFD(r2, r3)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            android.media.MediaPlayer r1 = r0.mediaPlayer     // Catch:{ Exception -> 0x007a }
            java.io.FileDescriptor r2 = r7.getFileDescriptor()     // Catch:{ Exception -> 0x007a }
            long r3 = r7.getStartOffset()     // Catch:{ Exception -> 0x007a }
            long r5 = r7.getLength()     // Catch:{ Exception -> 0x007a }
            r1.setDataSource(r2, r3, r5)     // Catch:{ Exception -> 0x007a }
            goto L_0x001d
        L_0x00b2:
            r1 = move-exception
        L_0x00b3:
            if (r7 == 0) goto L_0x00b9
            r7.close()     // Catch:{ Exception -> 0x01cd }
            r7 = 0
        L_0x00b9:
            if (r11 == 0) goto L_0x00bf
            r11.close()     // Catch:{ Exception -> 0x01cd }
            r11 = 0
        L_0x00bf:
            throw r1
        L_0x00c0:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0034
        L_0x00c6:
            r18.removeAllViews()     // Catch:{ Exception -> 0x007a }
            android.widget.ImageView r1 = new android.widget.ImageView     // Catch:{ Exception -> 0x007a }
            android.content.Context r2 = r18.getContext()     // Catch:{ Exception -> 0x007a }
            r1.<init>(r2)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r0.img = r1     // Catch:{ Exception -> 0x007a }
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = new com.hl.android.view.component.AudioComponent$MySeekBar     // Catch:{ Exception -> 0x007a }
            android.content.Context r2 = r18.getContext()     // Catch:{ Exception -> 0x007a }
            android.view.ViewGroup$LayoutParams r3 = r18.getLayoutParams()     // Catch:{ Exception -> 0x007a }
            int r3 = r3.width     // Catch:{ Exception -> 0x007a }
            r4 = -2
            r0 = r18
            r1.<init>(r2, r3, r4)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r0.mSeekBar = r1     // Catch:{ Exception -> 0x007a }
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            r2 = 2130837509(0x7f020005, float:1.7279974E38)
            r1.setImageResource(r2)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            r1.changeToPlayImage()     // Catch:{ Exception -> 0x007a }
            android.widget.RelativeLayout$LayoutParams r15 = new android.widget.RelativeLayout$LayoutParams     // Catch:{ Exception -> 0x007a }
            r1 = -2
            r2 = -2
            r15.<init>(r1, r2)     // Catch:{ Exception -> 0x007a }
            r1 = 13
            r15.addRule(r1)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r0.addView(r1, r15)     // Catch:{ Exception -> 0x007a }
            android.widget.RelativeLayout$LayoutParams r14 = new android.widget.RelativeLayout$LayoutParams     // Catch:{ Exception -> 0x007a }
            r1 = -2
            r2 = -2
            r14.<init>(r1, r2)     // Catch:{ Exception -> 0x007a }
            r1 = 15
            r14.addRule(r1)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r0.addView(r1, r14)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            com.hl.android.view.component.AudioComponent$2 r2 = new com.hl.android.view.component.AudioComponent$2     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r2.<init>()     // Catch:{ Exception -> 0x007a }
            r1.setOnTouchListener(r2)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r1 = r0.entity     // Catch:{ Exception -> 0x007a }
            boolean r1 = r1.isHideAtBegining     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x014d
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            r2 = 4
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            r2 = 4
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
        L_0x014d:
            r0 = r18
            com.hl.android.book.entity.ComponentEntity r1 = r0.entity     // Catch:{ Exception -> 0x007a }
            boolean r1 = r1.showProgress     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0187
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0163
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            r2 = 4
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
        L_0x0163:
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0171
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            r2 = 0
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
        L_0x0171:
            r1 = 2130837517(0x7f02000d, float:1.727999E38)
            r0 = r18
            r0.setBackgroundResource(r1)     // Catch:{ Exception -> 0x007a }
        L_0x0179:
            if (r7 == 0) goto L_0x017f
            r7.close()     // Catch:{ Exception -> 0x01c1 }
            r7 = 0
        L_0x017f:
            if (r11 == 0) goto L_0x0034
            r11.close()     // Catch:{ Exception -> 0x01c1 }
            r11 = 0
            goto L_0x0034
        L_0x0187:
            r1 = 61
            int[] r1 = new int[r1]     // Catch:{ Exception -> 0x007a }
            r0 = r18
            r0.progressImages = r1     // Catch:{ Exception -> 0x007a }
            r13 = 0
        L_0x0190:
            r0 = r18
            int[] r1 = r0.progressImages     // Catch:{ Exception -> 0x007a }
            int r1 = r1.length     // Catch:{ Exception -> 0x007a }
            if (r13 >= r1) goto L_0x01a4
            r0 = r18
            int[] r1 = r0.progressImages     // Catch:{ Exception -> 0x007a }
            r2 = 2130837594(0x7f02005a, float:1.7280146E38)
            int r2 = r2 + r13
            r1[r13] = r2     // Catch:{ Exception -> 0x007a }
            int r13 = r13 + 1
            goto L_0x0190
        L_0x01a4:
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x01b2
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            r2 = 4
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
        L_0x01b2:
            r0 = r18
            com.hl.android.view.component.AudioComponent$MySeekBar r1 = r0.mSeekBar     // Catch:{ Exception -> 0x007a }
            if (r1 == 0) goto L_0x0179
            r0 = r18
            android.widget.ImageView r1 = r0.img     // Catch:{ Exception -> 0x007a }
            r2 = 0
            r1.setVisibility(r2)     // Catch:{ Exception -> 0x007a }
            goto L_0x0179
        L_0x01c1:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0034
        L_0x01c7:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0034
        L_0x01cd:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x00bf
        L_0x01d3:
            r1 = move-exception
            r11 = r12
            goto L_0x00b3
        L_0x01d7:
            r8 = move-exception
            r11 = r12
            goto L_0x007b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.AudioComponent.load():void");
    }

    public void setControlUnable() {
        if (this.img != null) {
            this.img.setEnabled(false);
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.getBtnAction().setEnabled(false);
            this.mSeekBar.getSeekbar().setEnabled(false);
        }
        this.controlenable = false;
    }

    /* access modifiers changed from: private */
    public int setProgress() {
        int position = 0;
        if (this.mediaPlayer != null && this.hasPrePared) {
            position = this.mediaPlayer.getCurrentPosition();
            int duration = this.mediaPlayer.getDuration();
            if (this.entity.showProgress) {
                if (this.mSeekBar != null && duration > 0) {
                    this.mSeekBar.setProgress((int) ((1000 * ((long) position)) / ((long) duration)));
                }
            } else if (this.img != null && duration > 0) {
                if (this.progressImages == null) {
                    this.progressImages = new int[61];
                    for (int i = 0; i < this.progressImages.length; i++) {
                        this.progressImages[i] = C0048R.drawable.jz_00000 + i;
                    }
                }
                int pos = (position * 61) / duration;
                if (pos >= 61) {
                    pos = 60;
                }
                this.img.setImageResource(this.progressImages[pos]);
            }
        }
        return position;
    }

    public void loadStream() {
    }

    public void load(InputStream is) {
    }

    public void play() {
        if (!this.isPlaying) {
            if (this.isPaused) {
                this.mHandler.sendEmptyMessage(this.PLAY_MUSIC);
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(this.PLAY_MUSIC, (long) (this.entity.delay * 1000.0d));
            Log.d("hl", this + "  played after " + (this.entity.delay * 1000.0d));
        }
    }

    /* access modifiers changed from: protected */
    public void doPlay() {
        Log.d("hl", this + "  is doPlay ");
        if (BookSetting.noBackGround) {
            this.mediaPlayer.setVolume(0.0f, 0.0f);
        }
        if (this.mediaPlayer != null) {
            try {
                if (this.init) {
                    this.init = false;
                    this.mediaPlayer.prepare();
                } else if (this.isPaused && this.isCompleted) {
                    this.isPaused = false;
                } else if (this.isPaused && !this.isCompleted) {
                    this.mediaPlayer.start();
                    initPlaying();
                    BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                } else if (this.isStopped) {
                    this.mediaPlayer.prepare();
                } else if (this.isPlaying && !this.isCompleted) {
                    this.mediaPlayer.stop();
                    this.mediaPlayer.prepare();
                } else if (this.isCompleted) {
                    initPlaying();
                    this.mediaPlayer.start();
                    BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                initPlaying();
                this.mediaPlayer.reset();
                try {
                    load();
                    this.mediaPlayer.prepare();
                    this.init = true;
                    this.isPaused = true;
                    this.isCompleted = false;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void initPlaying() {
        this.isPaused = false;
        this.isCompleted = false;
        this.isStopped = false;
        this.isPlaying = true;
        if (this.img != null) {
            this.img.setImageResource(C0048R.drawable.audio_stop_new);
            this.mHandler.sendEmptyMessage(1);
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.changeToPauseImage();
            this.mHandler.sendEmptyMessage(1);
        }
    }

    public void onPrepared(MediaPlayer mp) {
        this.hasPrePared = true;
        if (this.mediaPlayer != null) {
            try {
                this.mediaDuration = mp.getDuration();
                this.mediaPlayer.seekTo(0);
                this.mediaPlayer.start();
                initPlaying();
            } catch (Exception e) {
                load();
                this.mediaPlayer.start();
            }
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
    }

    public void stop() {
        if ((this.mediaPlayer != null && this.mediaPlayer.isPlaying()) || this.isPaused) {
            try {
                if (this.controlenable) {
                    if (this.img != null) {
                        this.img.setImageResource(C0048R.drawable.audio_play_new);
                    }
                    if (this.mSeekBar != null) {
                        this.mSeekBar.setProgress(0);
                        this.mSeekBar.changeToPlayImage();
                    }
                    this.mediaPlayer.stop();
                    this.isStopped = true;
                    this.isPaused = false;
                    this.isPlaying = false;
                    this.mHandler.removeMessages(1);
                }
            } catch (Exception ex) {
                Log.e("AudioComponent", "stop", ex);
            }
        }
    }

    public void pause() {
        if (!this.isStopped) {
            try {
                if (this.controlenable) {
                    this.isPaused = true;
                    this.isPlaying = false;
                    if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
                        this.mediaPlayer.pause();
                        if (this.img != null) {
                            this.img.setImageResource(C0048R.drawable.audio_play_new);
                        }
                        if (this.mSeekBar != null) {
                            this.mSeekBar.changeToPlayImage();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void resume() {
        try {
            if (this.mediaPlayer != null && this.isPaused) {
                this.mediaPlayer.start();
                initPlaying();
            }
            this.isPaused = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playOrStop() {
        if (this.mediaPlayer == null || !this.isPlaying) {
            doPlay();
        } else if (this.controlenable) {
            if (this.img != null) {
                this.img.setImageResource(C0048R.drawable.audio_play_new);
            }
            if (this.mSeekBar != null) {
                this.mSeekBar.changeToPlayImage();
            }
            this.mediaPlayer.pause();
            this.isPlaying = false;
            this.isPaused = true;
            if (this.img != null) {
                this.mHandler.removeMessages(1);
            }
        }
    }

    public void hide() {
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void onCompletion(MediaPlayer mp) {
        if (this.onComponentCallbackListener != null) {
            this.onComponentCallbackListener.setPlayComplete();
        }
        this.isCompleted = true;
        this.isPlaying = false;
        if (this.img != null) {
            this.img.setImageResource(C0048R.drawable.audio_play_new);
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.setProgress(0);
            this.mSeekBar.changeToPlayImage();
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void recyle() {
        MediaPlayer mediaPlayer2 = null;
        Log.d("hl", this + "  is recyled ");
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(this.PLAY_MUSIC);
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                mediaPlayer2 = null;
            }
            this.mediaPlayer = mediaPlayer2;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.mediaPlayer = mediaPlayer2;
        }
    }

    public void callBackListener() {
        this.onComponentCallbackListener.setPlayComplete();
    }
}
