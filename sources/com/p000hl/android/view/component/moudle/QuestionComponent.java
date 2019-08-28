package com.p000hl.android.view.component.moudle;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.book.entity.moudle.OptionEntity;
import com.p000hl.android.book.entity.moudle.QuestionEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.QuestionComponent */
public class QuestionComponent extends LinearLayout implements Component, OnClickListener {
    private static final int btnNextID = 1212006;
    private static final int btnPrevID = 1212007;
    private static final int imgAttachID = 1212004;
    private static final int imgAudioID = 1212003;
    private static int imgQuestionHeadID = 1212002;
    private static final int textAnswerID = 1212005;
    /* access modifiers changed from: private */
    public int checkIndex;
    private LinearLayout container;
    ImageView imgAttach;
    ImageView imgAudio;
    LayoutParams imgAudioLP;
    /* access modifiers changed from: private */
    public ImageView imgOption;
    private ImageView imgQuestionHead;
    LinearLayout layOption;
    private LinearLayout layResult;
    private Context mContext;
    private MoudleComponentEntity mEntity;
    MediaPlayer media;
    QuestionEntity question;
    private int questionIndex;
    private ScrollView scrollView;
    /* access modifiers changed from: private */
    public OptionEntity selectOptionEntity;
    private Button textAnswer;
    private TextView textResult;
    private boolean textSwitch;
    private Button textTitle;

    /* renamed from: com.hl.android.view.component.moudle.QuestionComponent$OptionView */
    class OptionView extends LinearLayout {
        /* access modifiers changed from: private */
        public ImageView img;
        /* access modifiers changed from: private */
        public OptionEntity mEntity;
        /* access modifiers changed from: private */
        public int mIndex = -1;

        public OptionView(Context context, OptionEntity entity, int index) {
            super(context);
            this.mEntity = entity;
            this.mIndex = index;
            setGravity(16);
            setOrientation(0);
            this.img = new ImageView(context);
            this.img.setBackgroundResource(this.mEntity.optionType);
            if (this.mEntity.optionType != C0048R.drawable.radio_up) {
                QuestionComponent.this.selectOptionEntity = this.mEntity;
                QuestionComponent.this.imgOption = this.img;
                QuestionComponent.this.checkIndex = this.mIndex;
            }
            setOnTouchListener(new OnTouchListener(QuestionComponent.this) {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == 0) {
                        QuestionComponent.this.checkIndex = OptionView.this.mIndex;
                        if (QuestionComponent.this.imgOption != null) {
                            QuestionComponent.this.selectOptionEntity.optionType = C0048R.drawable.radio_up;
                            QuestionComponent.this.imgOption.setBackgroundResource(QuestionComponent.this.selectOptionEntity.optionType);
                        }
                        OptionView.this.mEntity.optionType = C0048R.drawable.radio_down;
                        QuestionComponent.this.selectOptionEntity = OptionView.this.mEntity;
                        OptionView.this.img.setBackgroundResource(C0048R.drawable.radio_down);
                        QuestionComponent.this.imgOption = OptionView.this.img;
                        QuestionComponent.this.question.chooseIndex = OptionView.this.mIndex;
                    }
                    return false;
                }
            });
            addView(this.img, new LinearLayout.LayoutParams(30, 30));
            TextView textOption = new TextView(context);
            textOption.setTextColor(-16777216);
            textOption.setText(this.mEntity.optionText);
            addView(textOption);
        }
    }

    public QuestionComponent(Context context) {
        super(context);
        this.questionIndex = 0;
        this.checkIndex = -1;
        this.textSwitch = false;
        this.imgOption = null;
        this.selectOptionEntity = null;
        this.textAnswer = null;
        this.media = null;
        this.mContext = context;
    }

    public QuestionComponent(Context context, ComponentEntity entity) {
        this(context);
        setEntity(entity);
        if (this.mEntity.questionList.size() != 0) {
            setOrientation(1);
            setGravity(1);
            setPadding(0, 20, 0, 2);
        }
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.question = (QuestionEntity) this.mEntity.questionList.get(0);
        drawResultText();
        drawTitle();
        drawQuestion();
        drawOption();
        drawFoot();
        setQuestion();
    }

    private void drawResultText() {
        LinearLayout.LayoutParams layResultLp = new LinearLayout.LayoutParams(-1, -2, 1.0f);
        this.layResult = new LinearLayout(this.mContext);
        this.layResult.setGravity(17);
        addView(this.layResult, layResultLp);
        this.textResult = new TextView(this.mContext);
        this.textResult.setTextColor(-16777216);
        this.layResult.addView(this.textResult);
        this.layResult.setVisibility(8);
    }

    private void setResult() {
        try {
            if (this.media != null && this.media.isPlaying()) {
                this.media.stop();
            }
        } catch (Exception e) {
        }
        this.layResult.setVisibility(0);
        ((View) this.textTitle.getParent()).setVisibility(8);
        this.textResult.setText(this.mContext.getString(C0048R.string.questionresult).replaceAll("all", this.mEntity.questionList.size() + "").replaceAll("check", getRightCnt() + ""));
        this.textAnswer.setText(C0048R.string.examredo);
        this.container.setVisibility(8);
        findViewById(btnPrevID).setVisibility(8);
        findViewById(btnNextID).setVisibility(8);
        this.question = null;
        this.imgOption = null;
    }

    private int getRightCnt() {
        int cnt = 0;
        Iterator i$ = this.mEntity.questionList.iterator();
        while (i$.hasNext()) {
            QuestionEntity q = (QuestionEntity) i$.next();
            if (q.getRightAnswerList().contains(Integer.valueOf(q.chooseIndex))) {
                cnt++;
            }
        }
        return cnt;
    }

    private void setQuestion() {
        findViewById(btnPrevID).setVisibility(0);
        findViewById(btnNextID).setVisibility(0);
        this.layResult.setVisibility(8);
        this.selectOptionEntity = null;
        this.imgOption = null;
        this.textSwitch = false;
        this.textAnswer.setText(C0048R.string.checkanswer);
        ((View) this.textTitle.getParent()).setVisibility(0);
        this.container.setVisibility(0);
        setTitle();
        try {
            setQuestionHead();
        } catch (Exception e) {
        }
        setOption();
    }

    private void drawFoot() {
        RelativeLayout layFoot = new RelativeLayout(this.mContext);
        layFoot.setGravity(17);
        LayoutParams btnPrevLp = new LayoutParams(-2, -2);
        btnPrevLp.topMargin = 10;
        btnPrevLp.addRule(9);
        btnPrevLp.addRule(15);
        ImageButton btnPrev = new ImageButton(this.mContext);
        btnPrev.setBackgroundResource(C0048R.drawable.left_arraw_select);
        btnPrev.setId(btnPrevID);
        btnPrev.setOnClickListener(this);
        layFoot.addView(btnPrev, btnPrevLp);
        this.textAnswer = new Button(this.mContext);
        this.textAnswer.setText("核对答案");
        this.textAnswer.setId(textAnswerID);
        this.textAnswer.setOnClickListener(this);
        LayoutParams layAnswerLp = new LayoutParams(-2, -2);
        layAnswerLp.addRule(13);
        layFoot.addView(formatOrangeView(this.textAnswer), layAnswerLp);
        ImageButton btnNext = new ImageButton(this.mContext);
        btnNext.setBackgroundResource(C0048R.drawable.right_arraw_select);
        LayoutParams btnNextLp = new LayoutParams(-2, -2);
        btnNextLp.topMargin = 10;
        btnNext.setId(btnNextID);
        btnNext.setOnClickListener(this);
        btnNextLp.addRule(11);
        layFoot.addView(btnNext, btnNextLp);
        addView(layFoot, new LinearLayout.LayoutParams(-1, -2));
    }

    private void drawQuestion() {
        this.container = new LinearLayout(this.mContext);
        this.container.setOrientation(1);
        addView(this.container, new LinearLayout.LayoutParams(-1, -1, 1.0f));
        RelativeLayout relativeHead = new RelativeLayout(this.mContext);
        this.container.addView(relativeHead, new LinearLayout.LayoutParams(-1, -2));
        this.imgQuestionHead = new ImageView(this.mContext);
        this.imgQuestionHead.setId(imgQuestionHeadID);
        LayoutParams imgQuestionHeadLP = new LayoutParams(-1, -2);
        this.imgQuestionHead.setScaleType(ScaleType.FIT_XY);
        imgQuestionHeadLP.addRule(10);
        imgQuestionHeadLP.addRule(9);
        relativeHead.addView(this.imgQuestionHead, imgQuestionHeadLP);
        this.imgAudio = new ImageView(this.mContext);
        this.imgAudio.setId(imgAudioID);
        this.imgAudioLP = new LayoutParams(-2, -2);
        this.imgAudioLP.topMargin = 18;
        this.imgAudio.setOnClickListener(this);
        this.imgAudioLP.addRule(11);
        this.imgAudioLP.addRule(10);
        relativeHead.addView(this.imgAudio, this.imgAudioLP);
        this.imgAttach = new ImageView(this.mContext);
        if (!StringUtils.isEmpty(this.question.imgSource)) {
            this.imgAttach.setImageBitmap(BitmapUtils.getBitMap(this.question.imgSource, this.mContext));
        }
        this.imgAttach.setId(imgAttachID);
        this.imgAttach.setOnClickListener(this);
        LayoutParams imgAttachLP = new LayoutParams(120, 120);
        imgAttachLP.addRule(11);
        imgAttachLP.addRule(3, imgAudioID);
        relativeHead.addView(this.imgAttach, imgAttachLP);
        TextView textLine = new TextView(this.mContext);
        textLine.setBackgroundColor(-16777216);
        LinearLayout.LayoutParams textLineLP = new LinearLayout.LayoutParams(-1, 1);
        textLineLP.bottomMargin = 10;
        textLineLP.topMargin = 10;
        this.container.addView(textLine, textLineLP);
    }

    private void setQuestionHead() throws Exception {
        this.imgQuestionHead.setVisibility(0);
        this.imgQuestionHead.setImageBitmap(BitmapUtils.getBitMap(this.question.titleResource, this.mContext));
        if (!StringUtils.isEmpty(this.question.imgSource)) {
            this.imgAttach.setImageBitmap(BitmapUtils.getBitMap(this.question.imgSource, this.mContext));
            this.imgAttach.setVisibility(0);
        } else {
            this.imgAttach.setVisibility(8);
        }
        if (this.media == null) {
            this.media = new MediaPlayer();
            this.media.setAudioStreamType(3);
        } else {
            this.media.reset();
        }
        this.imgAudio.setImageResource(C0048R.drawable.audio_play);
        if (StringUtils.isEmpty(this.question.soundSource)) {
            this.imgAudio.setVisibility(8);
            return;
        }
        if (HLSetting.IsResourceSD) {
            String filePath = FileUtils.getInstance().getFilePath(this.question.soundSource);
            if (filePath.contains(getContext().getFilesDir().getAbsolutePath())) {
                FileInputStream fis = null;
                try {
                    FileInputStream fis2 = new FileInputStream(new File(filePath));
                    try {
                        this.media.setDataSource(fis2.getFD());
                        fis2.close();
                    } catch (Throwable th) {
                        th = th;
                        fis = fis2;
                        fis.close();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fis.close();
                    throw th;
                }
            } else {
                this.media.setDataSource(filePath);
            }
        } else {
            AssetFileDescriptor ass = FileUtils.getInstance().getFileFD(getContext(), this.question.soundSource);
            this.media.setDataSource(ass.getFileDescriptor(), ass.getStartOffset(), ass.getLength());
        }
        this.media.prepare();
        this.imgAudio.setVisibility(0);
    }

    private void drawOption() {
        this.layOption = new LinearLayout(this.mContext);
        this.layOption.setOrientation(1);
        this.container.addView(this.layOption, new LinearLayout.LayoutParams(-2, -2));
    }

    private void setOption() {
        this.layOption.removeAllViews();
        LinearLayout.LayoutParams wrapLp = new LinearLayout.LayoutParams(-2, -2);
        wrapLp.topMargin = 10;
        for (int i = 0; i < this.question.getOptionList().size(); i++) {
            this.layOption.addView(new OptionView(this.mContext, (OptionEntity) this.question.getOptionList().get(i), i), wrapLp);
        }
    }

    private void drawTitle() {
        this.textTitle = new Button(this.mContext);
        this.textTitle.setGravity(17);
        this.textTitle.setTextColor(-16777216);
        LinearLayout.LayoutParams textTitleLP = new LinearLayout.LayoutParams(-2, -2);
        textTitleLP.topMargin = 18;
        addView(formatOrangeView(this.textTitle), textTitleLP);
    }

    private void setTitle() {
        this.textTitle.setText(this.mContext.getResources().getString(C0048R.string.questionexam) + (this.questionIndex + 1) + " of " + this.mEntity.questionList.size());
    }

    private View formatOrangeView(Button btn) {
        btn.setBackgroundResource(C0048R.drawable.orange_btn_corner);
        btn.setGravity(1);
        btn.setTextColor(-16777216);
        btn.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams textAnswerLp = new LinearLayout.LayoutParams(120, -2);
        textAnswerLp.setMargins(1, 1, 1, 1);
        LinearLayout layAnswer = new LinearLayout(this.mContext);
        layAnswer.setBackgroundResource(C0048R.drawable.orange_corner_border);
        layAnswer.addView(btn, textAnswerLp);
        return layAnswer;
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        if (this.media != null) {
            this.media.release();
        }
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }

    public void onClick(View v) {
        boolean z = false;
        switch (v.getId()) {
            case imgAudioID /*1212003*/:
                if (this.media.isPlaying()) {
                    this.media.pause();
                    this.imgAudio.setImageResource(C0048R.drawable.audio_play);
                    this.imgAudio.setLayoutParams(this.imgAudioLP);
                    return;
                }
                playAudio();
                return;
            case imgAttachID /*1212004*/:
                if (v.getVisibility() == 0) {
                    showAttachImg();
                    return;
                }
                return;
            case textAnswerID /*1212005*/:
                if (this.imgOption != null && this.question != null) {
                    if (this.textSwitch) {
                        this.imgOption.setBackgroundResource(C0048R.drawable.radio_up);
                        this.textAnswer.setText(C0048R.string.checkanswer);
                        this.imgOption = null;
                    } else {
                        if (this.question.getRightAnswerList().contains(Integer.valueOf(this.checkIndex))) {
                            this.imgOption.setBackgroundResource(C0048R.drawable.radio_correct);
                        } else {
                            this.imgOption.setBackgroundResource(C0048R.drawable.radio_incorrect);
                        }
                        this.textAnswer.setText(C0048R.string.clearexam);
                    }
                    if (!this.textSwitch) {
                        z = true;
                    }
                    this.textSwitch = z;
                    return;
                } else if (this.questionIndex == this.mEntity.questionList.size()) {
                    this.questionIndex = 0;
                    this.question = (QuestionEntity) this.mEntity.questionList.get(this.questionIndex);
                    setQuestion();
                    return;
                } else {
                    return;
                }
            case btnNextID /*1212006*/:
                if (this.questionIndex < this.mEntity.questionList.size() - 1) {
                    this.questionIndex++;
                    this.question = (QuestionEntity) this.mEntity.questionList.get(this.questionIndex);
                    setQuestion();
                    return;
                }
                this.questionIndex = this.mEntity.questionList.size();
                setResult();
                return;
            case btnPrevID /*1212007*/:
                if (this.questionIndex > 0) {
                    this.questionIndex--;
                    this.question = (QuestionEntity) this.mEntity.questionList.get(this.questionIndex);
                    setQuestion();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void playAudio() {
        this.imgAudio.setImageResource(C0048R.drawable.audio_stop);
        this.imgAudio.setLayoutParams(this.imgAudioLP);
        this.media.start();
    }

    public void showAttachImg() {
        if (!StringUtils.isEmpty(this.question.imgSource)) {
            TextView img = new TextView(this.mContext);
            img.setBackgroundDrawable(new BitmapDrawable(this.mContext.getResources(), BitmapUtils.getBitMap(this.question.imgSource, this.mContext)));
            AlertDialog alert = new Builder(this.mContext).setTitle("").setIcon(C0048R.drawable.icon).setView(img).create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();
        }
    }
}
