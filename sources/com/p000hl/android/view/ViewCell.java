package com.p000hl.android.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.ContainerEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.EventDispatcher;
import com.p000hl.android.core.helper.AnimationHelper;
import com.p000hl.android.core.helper.animation.HLAnimatorUpdateListener;
import com.p000hl.android.core.utils.AppUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.WebUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.HLCounterComponent;
import com.p000hl.android.view.component.HLSWFFileComponent;
import com.p000hl.android.view.component.HorizontalImageComponent;
import com.p000hl.android.view.component.InputTextComponent;
import com.p000hl.android.view.component.PDFDocumentViewComponentMU;
import com.p000hl.android.view.component.ScrollTextViewComponent;
import com.p000hl.android.view.component.TimerComponent;
import com.p000hl.android.view.component.VerticalImageComponent;
import com.p000hl.android.view.component.VideoComponent;
import com.p000hl.android.view.component.WebComponent;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.helper.ComponentHelper;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.moudle.HLMediaController;
import com.p000hl.android.view.component.moudle.HLPaintingUIComponent;
import com.p000hl.android.view.component.moudle.HLPuzzleGameUIComponent;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.ViewCell */
public class ViewCell extends LinearLayout {
    private static int flipdistance = 20;
    private HLAnimatorUpdateListener animationUpdateListener = new HLAnimatorUpdateListener();
    /* access modifiers changed from: private */
    public boolean canChangeState;
    public int cellID = 0;
    protected CountDownTimer countdownTimer;
    private ViewRecord curRecord = new ViewRecord();
    protected int curSpeed;
    MotionEvent downEvent;
    Rect downRect = new Rect();
    private float downY = 0.0f;
    /* access modifiers changed from: private */
    public boolean editChangeDo = false;
    private int endPositionx;
    private int endPositiony;
    float initHeight;
    float initWidth;
    boolean isInExcute = false;
    boolean isOutExcute = false;
    protected boolean isPlayingLeftGoback;
    protected boolean isPlayingRightGoback;
    private boolean isTwoPointDown;
    private boolean isZoomInner;
    private boolean isslided = false;
    float lastX;
    float lastY;
    float lastY4viewcell;
    public Animator mAnimator;
    private Component mComponent = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mCurWidth;
    private ComponentEntity mEntity = null;
    private Sensor mSensor;
    private SensorEventListener mSensorListener;
    private SensorManager mSensorMgr;
    private ViewPage mViewPage = null;
    private PointF mid = new PointF();
    public int moveX = 0;
    public int moveY = 0;
    private float oldDistance;
    private MotionEvent oldEvent;
    private float posionCenterHeitht;
    private float posionCenterWidth;
    private float posionXBeforeMove;
    private float posionYBeforeMove;
    private float postionXBeforeScale;
    private float postionYBeforeScale;
    private ArrayList<ViewCell> relatives = new ArrayList<>();
    protected int tSpeed;
    private PointF targetPonit;
    private int touchW;
    private float touchX;
    private float touchY;
    private int widthBeforeScale;

    public ViewCell(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewCell(Context context, ViewPage viewPage) {
        super(context);
        this.mContext = context;
        this.mViewPage = viewPage;
    }

    public ViewRecord getViewRecord() {
        return this.curRecord;
    }

    public void resetViewRecord() {
        this.curRecord.f28mX = (float) getEntity().f7x;
        this.curRecord.f29mY = (float) getEntity().f8y;
        this.curRecord.mRotation = this.mEntity.rotation;
    }

    public ViewCell(Context context, ViewPage viewPage, ContainerEntity containerEntity) {
        super(context);
        this.mContext = context;
        this.mViewPage = viewPage;
        this.cellID = Integer.parseInt(containerEntity.getID().substring(8));
        setId(this.cellID);
        try {
            ArrayList<String> pageIds = this.mViewPage.getEntity().getNavePageIds();
            if (pageIds == null || pageIds.size() > 0) {
            }
        } catch (Exception e) {
        }
        load(containerEntity);
    }

    public HLAnimatorUpdateListener getAnimatorUpdateListener() {
        return this.animationUpdateListener;
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public Component getComponent() {
        return this.mComponent;
    }

    public void setMyRotationY(float rotationY) {
        ((View) this.mComponent).setRotationY(rotationY);
    }

    public void setRotation(float rotation) {
        super.setRotation(getComponent().getEntity().getRotation() + rotation);
    }

    public void setSuperRotation(float rotation) {
        super.setRotation(rotation);
    }

    public void load(ContainerEntity containerEntity) {
        this.mComponent = ComponentHelper.getComponent(containerEntity, this.mViewPage);
        this.mComponent.load();
        this.mEntity = this.mComponent.getEntity();
        if ((this.mComponent instanceof VerticalImageComponent) || (this.mComponent instanceof HorizontalImageComponent)) {
            setLayerType(1, null);
        }
        if ((this.mComponent instanceof HLPaintingUIComponent) || (this.mComponent instanceof HLPuzzleGameUIComponent)) {
            this.mEntity.f7x = 0;
            this.mEntity.f8y = 0;
            this.mEntity.rotation = 0.0f;
            ((View) this.mComponent).getLayoutParams().width = BookController.getInstance().getViewPage().pageWidth;
            ((View) this.mComponent).getLayoutParams().height = BookController.getInstance().getViewPage().pageHeight;
        }
        if ((this.mComponent instanceof HLSWFFileComponent) && !AppUtils.detectPackage(getContext(), "com.adobe.flashplayer")) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(1, 20.0f);
            textView.setGravity(17);
            LayoutParams lp = new LayoutParams(-1, -1);
            textView.setText(C0048R.string.swfcomponentnotexists);
            textView.setTextColor(-1);
            textView.setBackgroundColor(Color.rgb(210, 210, 210));
            addView(textView, lp);
        }
        if (this.mEntity.ptList.size() > 0) {
            this.targetPonit = new PointF();
            this.targetPonit.x = ((PointF) this.mEntity.ptList.get(0)).x - (containerEntity.getWidth() / 2.0f);
            this.targetPonit.y = ((PointF) this.mEntity.ptList.get(0)).y - (containerEntity.getHeight() / 2.0f);
        }
        if (this.mEntity.isHideAtBegining) {
            setVisibility(8);
        }
        if (!(this.mComponent instanceof WebComponent) || WebUtils.isConnectingToInternet((Activity) getContext())) {
            addView((View) this.mComponent);
            if (this.mComponent instanceof VideoComponent) {
                ((VideoComponent) this.mComponent).getSurfaceView().setVisibility(0);
                ((VideoComponent) this.mComponent).getSurfaceView().setZOrderOnTop(true);
                ((VideoComponent) this.mComponent).getSurfaceView().bringToFront();
                ((VideoComponent) this.mComponent).bringToFront();
                bringToFront();
            }
            if (this.mComponent instanceof InputTextComponent) {
                InputTextComponent input = (InputTextComponent) this.mComponent;
                input.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        ViewCell.this.editChangeDo = false;
                    }

                    public void afterTextChanged(Editable arg0) {
                    }
                });
                input.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        EditText et = (EditText) arg0;
                        if (arg1) {
                            ViewCell.this.editChangeDo = false;
                        } else if (!ViewCell.this.editChangeDo) {
                            ViewCell.this.editChangeDo = true;
                            BookController.getInstance().runBehavior(ViewCell.this.getEntity(), "BEHAVIOR_ON_TEXT_CHANGE", et.getText().toString());
                            BookController.getInstance().runBehavior(ViewCell.this.getEntity(), "BEHAVIOR_ON_TEXT_CHANGE_FAILED", et.getText().toString());
                            Log.i("maker", "BEHAVIOR_ON_TEXT_CHANGE_FAILED and BEHAVIOR_ON_TEXT_CHANGE");
                        }
                    }
                });
                input.setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == 6 && !ViewCell.this.editChangeDo) {
                            ViewCell.this.editChangeDo = true;
                            BookController.getInstance().runBehavior(ViewCell.this.getEntity(), "BEHAVIOR_ON_TEXT_CHANGE", v.getText().toString());
                            BookController.getInstance().runBehavior(ViewCell.this.getEntity(), "BEHAVIOR_ON_TEXT_CHANGE_FAILED", v.getText().toString());
                            Log.i("maker", "BEHAVIOR_ON_TEXT_CHANGE_FAILED and BEHAVIOR_ON_TEXT_CHANGE");
                        }
                        return false;
                    }
                });
                ((View) this.mComponent).getLayoutParams().height = ScreenUtils.dip2px(this.mContext, 30.0f);
            }
            if (this.mComponent instanceof HLCounterComponent) {
                ((HLCounterComponent) this.mComponent).setCounterText();
            }
            if (this.mComponent instanceof TimerComponent) {
                ((TimerComponent) this.mComponent).syncBean();
            }
            setLayoutParams(new MarginLayoutParams(((View) this.mComponent).getLayoutParams().width, ((View) this.mComponent).getLayoutParams().height));
            if (this.mEntity.rotation != 0.0f) {
                super.setRotation(this.mEntity.rotation);
            }
            if (this.mEntity.alpha != 0.0f) {
                setAlpha(this.mEntity.alpha);
            }
            this.curRecord.mWidth = getLayoutParams().width;
            this.curRecord.mHeight = getLayoutParams().height;
            float x = getX();
            if (x == 0.0f) {
                x = (float) getEntity().f7x;
            }
            float y = getY();
            if (y == 0.0f) {
                y = (float) getEntity().f8y;
            }
            this.curRecord.f28mX = x;
            this.curRecord.f29mY = y;
            this.curRecord.mRotation = this.mEntity.rotation;
            if (this.mEntity.isAllowUserZoom && "zoom_inner".equals(this.mEntity.zoomType)) {
                this.isZoomInner = true;
            }
            if (this.mEntity.IsEnableGyroHor) {
                this.mSensorMgr = (SensorManager) this.mContext.getSystemService("sensor");
                this.mSensor = this.mSensorMgr.getDefaultSensor(1);
                this.mSensorListener = new SensorEventListener() {
                    private boolean isPhone = checkIsPhone(this.uiRot);
                    int uiRot = ((Activity) ViewCell.this.mContext).getWindowManager().getDefaultDisplay().getRotation();

                    public void onSensorChanged(SensorEvent e) {
                        int x = ((int) e.values[0]) * 2;
                        int y = ((int) e.values[1]) * 2;
                        int curUIRot = ((Activity) ViewCell.this.mContext).getWindowManager().getDefaultDisplay().getRotation();
                        int curChangeValue = 0;
                        if (this.isPhone) {
                            curChangeValue = BookSetting.IS_HOR ? -y : x;
                        } else if (BookSetting.IS_HOR) {
                            if (curUIRot == 0) {
                                curChangeValue = x;
                            } else if (curUIRot == 2) {
                                curChangeValue = -x;
                            }
                        } else if (curUIRot == 1) {
                            curChangeValue = -y;
                        } else if (curUIRot == 3) {
                            curChangeValue = y;
                        }
                        if (ViewCell.this.canChangeState) {
                            if (ViewCell.this.isPlayingLeftGoback && curChangeValue < 0) {
                                ViewCell.this.countdownTimer = null;
                                ViewCell.this.isPlayingLeftGoback = false;
                            }
                            if (ViewCell.this.isPlayingRightGoback && curChangeValue > 0) {
                                ViewCell.this.countdownTimer = null;
                                ViewCell.this.isPlayingRightGoback = false;
                            }
                        }
                        if (ViewCell.this.isPlayingLeftGoback) {
                            if (ViewCell.this.countdownTimer == null) {
                                ViewCell.this.canChangeState = false;
                                ViewCell.this.countdownTimer = new CountDownTimer((long) (((ViewCell.this.tSpeed * 2) + 2) * 20), 20) {
                                    public void onTick(long arg0) {
                                        ViewCell.this.setX(ViewCell.this.getX() - ((float) ViewCell.this.curSpeed));
                                        ViewCell.this.requestLayout();
                                        ViewCell.this.curSpeed--;
                                    }

                                    public void onFinish() {
                                        ViewCell.this.canChangeState = true;
                                        if (ViewCell.this.getX() < 0.0f) {
                                            ViewCell.this.setX(0.0f);
                                            ViewCell.this.requestLayout();
                                        } else if (ViewCell.this.getX() + ((float) ViewCell.this.getLayoutParams().width) > ((float) BookSetting.BOOK_WIDTH)) {
                                            ViewCell.this.setX((float) (BookSetting.BOOK_WIDTH - ViewCell.this.getLayoutParams().width));
                                            ViewCell.this.requestLayout();
                                        }
                                    }
                                };
                                ViewCell.this.countdownTimer.start();
                            }
                        } else if (!ViewCell.this.isPlayingRightGoback) {
                            ViewCell.this.setX(ViewCell.this.getX() - ((float) curChangeValue));
                            ViewCell.this.requestLayout();
                            if (ViewCell.this.getX() < 0.0f) {
                                ViewCell.this.isPlayingLeftGoback = true;
                                ViewCell.this.tSpeed = curChangeValue;
                                ViewCell.this.curSpeed = ViewCell.this.tSpeed;
                                ViewCell.this.setX(0.0f);
                                ViewCell.this.requestLayout();
                            } else if (ViewCell.this.getX() + ((float) ViewCell.this.getLayoutParams().width) > ((float) BookSetting.BOOK_WIDTH)) {
                                ViewCell.this.isPlayingRightGoback = true;
                                ViewCell.this.tSpeed = curChangeValue;
                                ViewCell.this.curSpeed = ViewCell.this.tSpeed;
                                ViewCell.this.setX((float) (BookSetting.BOOK_WIDTH - ViewCell.this.getLayoutParams().width));
                                ViewCell.this.requestLayout();
                            }
                        } else if (ViewCell.this.countdownTimer == null) {
                            ViewCell.this.canChangeState = false;
                            ViewCell.this.countdownTimer = new CountDownTimer((long) ((((-ViewCell.this.tSpeed) * 2) + 2) * 20), 20) {
                                public void onTick(long arg0) {
                                    ViewCell.this.setX(ViewCell.this.getX() - ((float) ViewCell.this.curSpeed));
                                    ViewCell.this.requestLayout();
                                    ViewCell.this.curSpeed++;
                                }

                                public void onFinish() {
                                    ViewCell.this.canChangeState = true;
                                    if (ViewCell.this.getX() < 0.0f) {
                                        ViewCell.this.setX(0.0f);
                                        ViewCell.this.requestLayout();
                                    } else if (ViewCell.this.getX() + ((float) ViewCell.this.getLayoutParams().width) > ((float) BookSetting.BOOK_WIDTH)) {
                                        ViewCell.this.setX((float) (BookSetting.BOOK_WIDTH - ViewCell.this.getLayoutParams().width));
                                        ViewCell.this.requestLayout();
                                    }
                                }
                            };
                            ViewCell.this.countdownTimer.start();
                        }
                    }

                    private boolean checkIsPhone(int uiRot2) {
                        if ((uiRot2 == 0 || uiRot2 == 2) && !BookSetting.IS_HOR) {
                            return true;
                        }
                        if ((uiRot2 == 1 || uiRot2 == 3) && BookSetting.IS_HOR) {
                            return true;
                        }
                        return false;
                    }

                    public void onAccuracyChanged(Sensor s, int accuracy) {
                    }
                };
                this.mSensorMgr.registerListener(this.mSensorListener, this.mSensor, 1);
            }
            this.initWidth = (float) getLayoutParams().width;
            this.initHeight = (float) getLayoutParams().height;
            this.mCurWidth = (int) this.initWidth;
            setRelativeObjectList();
            return;
        }
        setVisibility(8);
    }

    public void play() {
        this.mComponent.play();
    }

    public void pause() {
        this.mComponent.pause();
    }

    public void stop() {
        this.mComponent.stop();
        if (this.mComponent instanceof InputTextComponent) {
            ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(((InputTextComponent) this.mComponent).getWindowToken(), 0);
        }
    }

    public void resume() {
        this.mComponent.resume();
    }

    public void recyle() {
        AnimationHelper.stopAnimation(this);
        AnimationHelper.animatiorMap.remove(this);
        this.mComponent.stop();
        if (this.animationUpdateListener != null) {
            this.animationUpdateListener.mStop = true;
            this.animationUpdateListener = null;
        }
        if (this.mComponent instanceof ComponentPost) {
            ((ComponentPost) this.mComponent).recyle();
        }
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
            this.mComponent.show();
        }
    }

    public void hide() {
        if (getVisibility() == 0) {
            setVisibility(8);
            this.mComponent.hide();
        }
    }

    public void setFontSize(String fontSize) {
        if (this.mComponent instanceof ScrollTextViewComponent) {
            ((ScrollTextViewComponent) this.mComponent).setFontSize(fontSize);
        }
    }

    public void setAlpha(float alpha) {
        if (alpha <= 0.0f) {
            alpha = 0.0f;
        } else if (alpha >= 1.0f) {
            alpha = 1.0f;
        }
        super.setAlpha(alpha);
    }

    public void setLeftPadding(int padding) {
        setPadding(padding, 0, 0, 0);
        requestLayout();
        postInvalidate();
    }

    public void setTopPadding(int padding) {
        setPadding(0, padding, 0, 0);
        requestLayout();
        postInvalidate();
    }

    public void setBottomPadding(int padding) {
        setPadding(0, 0, 0, padding);
        requestLayout();
        postInvalidate();
    }

    public void setRightPadding(int padding) {
        setPadding(0, 0, padding, 0);
        requestLayout();
        postInvalidate();
    }

    public void setY(float y) {
        super.setY(this.mViewPage.getOffSetY() + y);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.isZoomInner) {
            super.onLayout(changed, l, t, r, b);
        } else {
            View v = getChildAt(0);
            if (v != null) {
                v.setPadding(-getPaddingLeft(), -getPaddingTop(), -getPaddingRight(), -getPaddingBottom());
                v.layout(getPaddingLeft(), getPaddingTop(), (r - l) - getPaddingRight(), (b - t) - getPaddingBottom());
            } else {
                return;
            }
        }
        if (this.mComponent instanceof VideoComponent) {
            HLMediaController controllerWindow = ((VideoComponent) this.mComponent).getControllerWindow();
            if (controllerWindow != null) {
                try {
                    controllerWindow.upDateWindowPosition();
                } catch (Exception e) {
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z;
        if (getVisibility() != 0 || (this.mComponent instanceof PDFDocumentViewComponentMU)) {
            return false;
        }
        if (this.mEntity.isAllowUserZoom) {
            doSomeThing(event);
        }
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case 0:
                    this.lastY4viewcell = (float) ((int) event.getRawY());
                    this.isslided = false;
                    this.downY = event.getRawY();
                    if (getEntity().isStroyTelling) {
                        setStartPosition();
                        if (getEntity().isMoveScale.booleanValue() && !this.isZoomInner) {
                            storeLayoutBeforeScale();
                            scaleMe((1.2f * ((float) this.widthBeforeScale)) / this.initWidth);
                        }
                        this.lastX = event.getRawX();
                        this.lastY = event.getRawY();
                        getGlobalVisibleRect(this.downRect);
                    }
                    BookController.getInstance().runBehavior(getEntity(), Behavior.BEHAVIOR_ON_CLICK);
                    break;
                case 1:
                    if (getEntity().isStroyTelling) {
                        if (getEntity().isMoveScale.booleanValue()) {
                            storeLayoutBeforeScale();
                            scaleMe((0.8333333f * ((float) this.widthBeforeScale)) / this.initWidth);
                        }
                        hotRectBeheavor(event);
                    }
                    if (Math.abs(this.mViewPage.getX()) <= 5.0f) {
                        BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_MOUSE_UP");
                        break;
                    }
                    break;
                case 2:
                    float dy = event.getRawY() - this.downY;
                    if (getEntity().isStroyTelling) {
                        if (this.isZoomInner) {
                            if (this.initWidth == ((float) this.mCurWidth) && !this.isTwoPointDown) {
                                moveMe(event.getRawX() - this.lastX, event.getRawY() - this.lastY, true);
                                this.lastX = event.getRawX();
                                this.lastY = event.getRawY();
                            }
                        } else if (!this.isTwoPointDown) {
                            moveMe(event.getRawX() - this.lastX, event.getRawY() - this.lastY, true);
                            this.lastX = event.getRawX();
                            this.lastY = event.getRawY();
                        }
                    } else if (this.isZoomInner) {
                        if (this.initWidth == ((float) this.mCurWidth) && BookSetting.BOOK_HEIGHT < BookController.getInstance().getViewPage().pageHeight) {
                            BookController.getInstance().getViewPage().moveDy(event.getRawY() - this.lastY4viewcell);
                            this.lastY4viewcell = event.getRawY();
                        }
                    } else if (!this.isTwoPointDown && BookSetting.BOOK_HEIGHT < BookController.getInstance().getViewPage().pageHeight) {
                        BookController.getInstance().getViewPage().moveDy(event.getRawY() - this.lastY4viewcell);
                        this.lastY4viewcell = event.getRawY();
                    }
                    if (dy <= ((float) flipdistance) || this.isslided) {
                        if (dy < ((float) (-flipdistance)) && !this.isslided) {
                            this.isslided = true;
                            BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_SLIDER_UP");
                            break;
                        }
                    } else {
                        BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_SLIDER_DOWN");
                        break;
                    }
                    break;
            }
        }
        if (event.getAction() == 1) {
            if (this.isTwoPointDown && this.isZoomInner) {
                if (this.endPositionx > 0) {
                    this.endPositionx = 0;
                } else if (((float) this.endPositionx) + getCurWidth() < ((float) getLayoutParams().width)) {
                    this.endPositionx = (int) (((float) getLayoutParams().width) - getCurWidth());
                }
                if (this.endPositiony > 0) {
                    this.endPositiony = 0;
                } else if (((float) this.endPositiony) + ((getCurWidth() * ((float) getLayoutParams().height)) / ((float) getLayoutParams().width)) < ((float) getLayoutParams().height)) {
                    this.endPositiony = (int) (((float) getLayoutParams().height) - ((getCurWidth() * ((float) getLayoutParams().height)) / ((float) getLayoutParams().width)));
                }
                getChildAt(0).setX((float) this.endPositionx);
                getChildAt(0).setY((float) this.endPositiony);
            }
            this.isTwoPointDown = false;
        }
        ((View) this.mComponent).onTouchEvent(event);
        PageEntity page = BookController.getInstance().mainViewPage.getEntity();
        if (event.getAction() == 0) {
            this.downEvent = MotionEvent.obtain(event);
            this.mViewPage.isHorMove = false;
            if (page.enablePageTurnByHand && page.isEnableNavigation()) {
                EventDispatcher.getInstance().onTouch(this.downEvent);
            }
            this.mViewPage.getDetector().onTouchEvent(this.downEvent);
            if (BookSetting.FLIP_CHANGE_PAGE && BookSetting.FLIPCODE == 1 && page.isEnableNavigation()) {
                this.mViewPage.doTouchAction4MovePageBetweenPage(this.downEvent);
            }
        } else {
            if (event.getAction() == 2 && this.downEvent != null) {
                float firstDx = event.getRawX() - this.downEvent.getRawX();
                float firstDy = event.getRawY() - this.downEvent.getRawY();
                ViewPage viewPage = this.mViewPage;
                if (Math.abs(firstDx) > Math.abs(firstDy)) {
                    z = true;
                } else {
                    z = false;
                }
                viewPage.isHorMove = z;
                this.downEvent = null;
            }
            Log.d("zhaoq", "is hor move" + this.mViewPage.isHorMove + " action is " + event.getRawX());
            this.mViewPage.getDetector().onTouchEvent(event);
            if (!this.mViewPage.isCommonPage) {
                if (!this.mViewPage.isHorMove || BookSetting.FLIPCODE != 1 || !page.isEnableNavigation()) {
                    if (page.enablePageTurnByHand && page.isEnableNavigation()) {
                        EventDispatcher.getInstance().onTouch(event);
                    }
                } else if (BookSetting.FLIP_CHANGE_PAGE) {
                    this.mViewPage.doTouchAction4MovePageBetweenPage(event);
                } else if (page.enablePageTurnByHand && page.isEnableNavigation()) {
                    EventDispatcher.getInstance().onTouch(event);
                }
            }
        }
        this.oldEvent = MotionEvent.obtain(event);
        return true;
    }

    private void setStartPosition() {
        this.posionXBeforeMove = getX();
        this.posionYBeforeMove = getY();
        this.posionCenterWidth = getX() + (((float) getLayoutParams().width) / 2.0f);
        this.posionCenterHeitht = getY() + (((float) getLayoutParams().height) / 2.0f);
        setRelativeStartPosition();
    }

    private void setRelativeStartPosition() {
        setRelativeObjectList();
        Iterator i$ = this.relatives.iterator();
        while (i$.hasNext()) {
            ((ViewCell) i$.next()).setStartPosition();
        }
    }

    private void storeLayoutBeforeScale() {
        this.postionXBeforeScale = getX();
        this.postionYBeforeScale = getY() - this.mViewPage.getOffSetY();
        this.widthBeforeScale = getLayoutParams().width;
        setRelativeObjectList();
        Iterator i$ = this.relatives.iterator();
        while (i$.hasNext()) {
            ((ViewCell) i$.next()).storeLayoutBeforeScale();
        }
    }

    public void doSomeThing(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            if (event.getAction() == 2 && this.isZoomInner && !this.isTwoPointDown) {
                int translateToPositionX = (int) ((getChildAt(0).getX() + event.getRawX()) - this.oldEvent.getRawX());
                int translateToPositionY = (int) ((getChildAt(0).getY() + event.getRawY()) - this.oldEvent.getRawY());
                if (translateToPositionX > 0) {
                    translateToPositionX = 0;
                } else if (((float) translateToPositionX) + getCurWidth() < ((float) getLayoutParams().width)) {
                    translateToPositionX = (int) (((float) getLayoutParams().width) - getCurWidth());
                }
                if (translateToPositionY > 0) {
                    translateToPositionY = 0;
                } else if (((float) translateToPositionY) + ((getCurWidth() * ((float) getLayoutParams().height)) / ((float) getLayoutParams().width)) < ((float) getLayoutParams().height)) {
                    translateToPositionY = (int) (((float) getLayoutParams().height) - ((getCurWidth() * ((float) getLayoutParams().height)) / ((float) getLayoutParams().width)));
                }
                getChildAt(0).setX((float) translateToPositionX);
                getChildAt(0).setY((float) translateToPositionY);
            }
        } else if (event.getPointerCount() == 2) {
            switch (event.getAction() & 255) {
                case 2:
                    float newDistance = (float) Math.sqrt((double) (((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1))) + ((event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1)))));
                    this.mCurWidth = (int) ((getCurWidth() * newDistance) / this.oldDistance);
                    if (((float) this.mCurWidth) >= this.initWidth * 4.0f) {
                        this.mCurWidth = (int) (this.initWidth * 4.0f);
                    } else if (((float) this.mCurWidth) <= this.initWidth) {
                        this.mCurWidth = (int) this.initWidth;
                    }
                    if ("zoom_inner".equals(this.mEntity.zoomType)) {
                        getChildAt(0).setLayoutParams(new LayoutParams(this.mCurWidth, (int) ((((float) this.mCurWidth) * this.initHeight) / this.initWidth)));
                        this.endPositionx = (int) (this.mid.x - (((this.mid.x - this.touchX) * ((float) this.mCurWidth)) / ((float) this.touchW)));
                        this.endPositiony = (int) (this.mid.y - (((this.mid.y - this.touchY) * ((float) this.mCurWidth)) / ((float) this.touchW)));
                        getChildAt(0).setX((float) this.endPositionx);
                        getChildAt(0).setY((float) this.endPositiony);
                    } else {
                        scaleMe(((float) this.mCurWidth) / this.initWidth);
                    }
                    this.oldDistance = newDistance;
                    return;
                case 5:
                    this.isTwoPointDown = true;
                    this.oldDistance = (float) Math.sqrt((double) (((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1))) + ((event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1)))));
                    this.mid.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
                    storeLayoutBeforeScale();
                    this.touchX = getChildAt(0).getX();
                    this.touchY = getChildAt(0).getY() - this.mViewPage.getOffSetY();
                    this.touchW = getChildAt(0).getLayoutParams().width;
                    return;
                default:
                    return;
            }
        }
    }

    private void setRelativeScale(float curScale2Init) {
        setRelativeObjectList();
        Iterator i$ = this.relatives.iterator();
        while (i$.hasNext()) {
            ViewCell cell = (ViewCell) i$.next();
            cell.scaleMe(((1.0f * curScale2Init) * ((float) cell.mCurWidth)) / cell.initWidth);
        }
    }

    private void scaleMe(float curScale2Init) {
        if (curScale2Init < 1.0f) {
            curScale2Init = 1.0f;
        }
        setLayoutParams(new LayoutParams((int) (this.initWidth * curScale2Init), (int) (this.initHeight * curScale2Init)));
        setX((this.postionXBeforeScale + ((float) (this.widthBeforeScale / 2))) - ((this.initWidth * curScale2Init) / 2.0f));
        setY((this.postionYBeforeScale + ((((float) this.widthBeforeScale) * (this.initHeight / this.initWidth)) / 2.0f)) - (((this.initWidth * curScale2Init) * (this.initHeight / this.initWidth)) / 2.0f));
        setRelativeScale(curScale2Init);
    }

    private float getCurWidth() {
        return (float) this.mCurWidth;
    }

    public void moveMe(float dx, float dy, boolean aa) {
        float[] resultPoint = {getX() + (((float) getLayoutParams().width) / 2.0f) + dx, getY() + (((float) getLayoutParams().height) / 2.0f) + dy};
        if (this.mEntity.ptList.size() != 0 && aa) {
            calcStroyTellPt(resultPoint);
        }
        float relativeDx = (resultPoint[0] - getX()) - (((float) getLayoutParams().width) / 2.0f);
        float relativeDy = (resultPoint[1] - getY()) - (((float) getLayoutParams().height) / 2.0f);
        setX(resultPoint[0] - (((float) getLayoutParams().width) / 2.0f));
        super.setY(resultPoint[1] - (((float) getLayoutParams().height) / 2.0f));
        relativeMove(relativeDx, relativeDy);
    }

    private void relativeMove(float dx, float dy) {
        setRelativeObjectList();
        Iterator i$ = this.relatives.iterator();
        while (i$.hasNext()) {
            ViewCell cell = (ViewCell) i$.next();
            float rate = cell.getEntity().getLinkPageObj().rate;
            cell.moveMe(dx * rate, dy * rate, false);
        }
    }

    private void calcStroyTellPt(float[] pos) {
        float targetDx = ScreenUtils.getHorScreenValue(this.targetPonit.x) - ((float) this.mEntity.f7x);
        float targetDy = ScreenUtils.getVerScreenValue(this.targetPonit.y) - ((float) this.mEntity.f8y);
        float k = targetDy / targetDx;
        float actualDx = (pos[0] - getX()) - (((float) getLayoutParams().width) / 2.0f);
        float actualDy = (pos[1] - getY()) - (((float) getLayoutParams().height) / 2.0f);
        if (Math.abs(targetDx) >= Math.abs(targetDy)) {
            pos[1] = getY() + (((float) getLayoutParams().height) / 2.0f) + (actualDx * k);
        } else {
            pos[0] = getX() + (((float) getLayoutParams().width) / 2.0f) + (actualDy / k);
        }
        if (pos[0] < Math.min(((float) this.mEntity.f7x) + (this.initWidth / 2.0f), ScreenUtils.getHorScreenValue(this.targetPonit.x) + (this.initWidth / 2.0f))) {
            pos[0] = Math.min(((float) this.mEntity.f7x) + (this.initWidth / 2.0f), ScreenUtils.getHorScreenValue(this.targetPonit.x) + (this.initWidth / 2.0f));
        }
        if (pos[1] < Math.min(((float) this.mEntity.f8y) + this.mViewPage.getOffSetY() + (this.initHeight / 2.0f), this.mViewPage.getOffSetY() + (this.initHeight / 2.0f) + ScreenUtils.getVerScreenValue(this.targetPonit.y))) {
            pos[1] = Math.min(((float) this.mEntity.f8y) + this.mViewPage.getOffSetY() + (this.initHeight / 2.0f), this.mViewPage.getOffSetY() + (this.initHeight / 2.0f) + ScreenUtils.getVerScreenValue(this.targetPonit.y));
        }
        if (pos[0] > Math.max(((float) this.mEntity.f7x) + (this.initWidth / 2.0f), ScreenUtils.getHorScreenValue(this.targetPonit.x) + (this.initWidth / 2.0f))) {
            pos[0] = Math.max(((float) this.mEntity.f7x) + (this.initWidth / 2.0f), ScreenUtils.getHorScreenValue(this.targetPonit.x) + (this.initWidth / 2.0f));
        }
        if (pos[1] > Math.max(((float) this.mEntity.f8y) + this.mViewPage.getOffSetY() + (this.initHeight / 2.0f), this.mViewPage.getOffSetY() + (this.initHeight / 2.0f) + ScreenUtils.getVerScreenValue(this.targetPonit.y))) {
            pos[1] = Math.max(((float) this.mEntity.f8y) + this.mViewPage.getOffSetY() + (this.initHeight / 2.0f), this.mViewPage.getOffSetY() + (this.initHeight / 2.0f) + ScreenUtils.getVerScreenValue(this.targetPonit.y));
        }
    }

    private void setRelativeObjectList() {
        if (BookController.getInstance().getViewPage() != null) {
            for (int i = 0; i < BookController.getInstance().getViewPage().getChildCount(); i++) {
                ViewCell view = (ViewCell) BookController.getInstance().getViewPage().getChildAt(i);
                if (this.mEntity.componentId.equals(view.getEntity().getLinkPageObj().entityID) && !this.relatives.contains(view)) {
                    this.relatives.add(view);
                }
            }
        }
    }

    private void hotRectBeheavor(MotionEvent event) {
        boolean needBack = true;
        Rect myRect = new Rect();
        getGlobalVisibleRect(myRect);
        this.curRecord.f28mX = getX();
        this.curRecord.f29mY = getY();
        if (!(this.mEntity.behaviors == null || this.mEntity.behaviors.size() == 0)) {
            Iterator<BehaviorEntity> it = this.mEntity.behaviors.iterator();
            while (it.hasNext()) {
                BehaviorEntity e = (BehaviorEntity) it.next();
                if (e.EventName.equals("BEHAVIOR_ON_OUT_SPOT") || e.EventName.equals("BEHAVIOR_ON_ENTER_SPOT")) {
                    ViewCell viewCell = BookController.getInstance().getViewPage().getCellByID(e.EventValue);
                    if (viewCell != null && viewCell.getVisibility() == 0) {
                        Rect hotRect = new Rect();
                        viewCell.getGlobalVisibleRect(hotRect);
                        if (!hotRect.contains(this.downRect) && hotRect.contains(myRect) && e.EventName.equals("BEHAVIOR_ON_ENTER_SPOT")) {
                            BookController.getInstance().runBehavior(e);
                            needBack = false;
                        }
                        if (!hotRect.contains(myRect) && hotRect.contains(this.downRect) && e.EventName.equals("BEHAVIOR_ON_OUT_SPOT")) {
                            BookController.getInstance().runBehavior(e);
                            needBack = false;
                        }
                    }
                }
            }
        }
        if (this.mEntity.isPushBack && needBack) {
            setRelativeObjectList();
            doBack();
        }
    }

    private void doBack() {
        ObjectAnimator backAnimator = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("posionCenterX", new float[]{this.posionCenterWidth}), PropertyValuesHolder.ofFloat("posionCenterY", new float[]{this.posionCenterHeitht - this.mViewPage.getOffSetY()})});
        backAnimator.setDuration(600);
        backAnimator.start();
        this.curRecord.f28mX = this.posionXBeforeMove;
        this.curRecord.f29mY = this.posionYBeforeMove;
        for (int i = 0; i < this.relatives.size(); i++) {
            ((ViewCell) this.relatives.get(i)).doBack();
        }
    }

    public float getPosionCenterX() {
        return getX() + (((float) getLayoutParams().width) / 2.0f);
    }

    public float getPosionCenterY() {
        return (getY() + (((float) getLayoutParams().height) / 2.0f)) - this.mViewPage.getOffSetY();
    }

    public void setPosionCenterX(float centerx) {
        setX(centerx - (((float) getLayoutParams().width) / 2.0f));
    }

    public void setPosionCenterY(float centery) {
        setY(centery - (((float) getLayoutParams().height) / 2.0f));
    }

    public void resetViewCell() {
        setX((float) this.mEntity.f7x);
        setY((float) this.mEntity.f8y);
        setAlpha(this.mEntity.alpha);
        setScaleX(1.0f);
        setScaleY(1.0f);
        super.setRotation(this.mEntity.getRotation());
        setPadding(0, 0, 0, 0);
        resetViewRecord();
        postInvalidate();
    }

    public void doSlideAction(float dy) {
        this.moveX = (int) (this.mEntity.sliderHorRate * dy);
        this.moveY = (int) (this.mEntity.sliderVerRate * dy);
    }

    public void doSlideAction4TweenPage(float rate) {
        setAlpha(this.mEntity.alpha + (Math.abs(rate) * (this.mEntity.slideBindingAlha - this.mEntity.alpha)));
        float scaleX = (((float) getLayoutParams().width) + (Math.abs(rate) * ((float) (this.mEntity.slideBindingWidth - getLayoutParams().width)))) / ((float) getLayoutParams().width);
        setScaleX(scaleX);
        float scaleY = (((float) getLayoutParams().height) + (Math.abs(rate) * ((float) (this.mEntity.slideBindingHeight - getLayoutParams().height)))) / ((float) getLayoutParams().height);
        setScaleY(scaleY);
        setX((((float) this.mEntity.f7x) - ((ScreenUtils.getHorScreenValue((float) this.mEntity.slideBindingX) - ((float) this.mEntity.f7x)) * rate)) - ((((float) getLayoutParams().width) * (1.0f - scaleX)) / 2.0f));
        setY((((float) this.mEntity.f8y) - ((ScreenUtils.getVerScreenValue((float) this.mEntity.slideBindingY) - ((float) this.mEntity.f8y)) * rate)) - ((((float) getLayoutParams().height) * (1.0f - scaleY)) / 2.0f));
    }
}
