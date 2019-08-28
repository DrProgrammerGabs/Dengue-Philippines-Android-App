package com.artifex.mupdf;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewSwitcher;
import com.p000hl.android.C0048R;

public class MuPDFActivity extends Activity {
    private static final int SEARCH_PROGRESS_DELAY = 200;
    private final int TAP_PAGE_MARGIN = 5;
    /* access modifiers changed from: private */
    public MuPDFCore core;
    /* access modifiers changed from: private */
    public Builder mAlertBuilder;
    private View mButtonsView;
    /* access modifiers changed from: private */
    public boolean mButtonsVisible;
    private ImageButton mCancelButton;
    /* access modifiers changed from: private */
    public ReaderView mDocView;
    private String mFileName;
    private TextView mFilenameView;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public LinkState mLinkState = LinkState.DEFAULT;
    private ImageButton mOutlineButton;
    /* access modifiers changed from: private */
    public TextView mPageNumberView;
    /* access modifiers changed from: private */
    public SeekBar mPageSlider;
    /* access modifiers changed from: private */
    public EditText mPasswordView;
    /* access modifiers changed from: private */
    public ImageButton mSearchBack;
    private ImageButton mSearchButton;
    /* access modifiers changed from: private */
    public ImageButton mSearchFwd;
    private AsyncTask<Integer, Integer, SearchTaskResult> mSearchTask;
    /* access modifiers changed from: private */
    public EditText mSearchText;
    private boolean mTopBarIsSearch;
    /* access modifiers changed from: private */
    public ViewSwitcher mTopBarSwitcher;

    private enum LinkState {
        DEFAULT,
        HIGHLIGHT,
        INHIBIT
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf(47);
        this.mFileName = new String(lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1));
        System.out.println("Trying to open " + path);
        try {
            this.core = new MuPDFCore(path);
            OutlineActivityData.set(null);
            return this.core;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlertBuilder = new Builder(this);
        if (this.core == null) {
            this.core = (MuPDFCore) getLastNonConfigurationInstance();
            if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
                this.mFileName = savedInstanceState.getString("FileName");
            }
        }
        if (this.core == null) {
            Intent intent = getIntent();
            if ("android.intent.action.VIEW".equals(intent.getAction())) {
                Uri uri = intent.getData();
                if (uri.toString().startsWith("content://media/external/file")) {
                    Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                    if (cursor.moveToFirst()) {
                        uri = Uri.parse(cursor.getString(0));
                    }
                }
                this.core = openFile(Uri.decode(uri.getEncodedPath()));
            }
            if (this.core != null && this.core.needsPassword()) {
                requestPassword(savedInstanceState);
                return;
            }
        }
        if (this.core == null) {
            AlertDialog alert = this.mAlertBuilder.create();
            alert.setTitle(C0048R.string.open_failed);
            alert.setButton(-1, "Dismiss", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MuPDFActivity.this.finish();
                }
            });
            alert.show();
            return;
        }
        createUI(savedInstanceState);
    }

    public void requestPassword(final Bundle savedInstanceState) {
        this.mPasswordView = new EditText(this);
        this.mPasswordView.setInputType(128);
        this.mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
        AlertDialog alert = this.mAlertBuilder.create();
        alert.setTitle(C0048R.string.enter_password);
        alert.setView(this.mPasswordView);
        alert.setButton(-1, "Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (MuPDFActivity.this.core.authenticatePassword(MuPDFActivity.this.mPasswordView.getText().toString())) {
                    MuPDFActivity.this.createUI(savedInstanceState);
                } else {
                    MuPDFActivity.this.requestPassword(savedInstanceState);
                }
            }
        });
        alert.setButton(-2, "Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MuPDFActivity.this.finish();
            }
        });
        alert.show();
    }

    public void createUI(Bundle savedInstanceState) {
        if (this.core != null) {
            this.mDocView = new ReaderView(this) {
                private boolean showButtonsDisabled;

                public boolean onSingleTapUp(MotionEvent e) {
                    if (e.getX() < ((float) (super.getWidth() / 5))) {
                        super.moveToPrevious();
                    } else if (e.getX() > ((float) ((super.getWidth() * 4) / 5))) {
                        super.moveToNext();
                    } else if (!this.showButtonsDisabled) {
                        if (MuPDFActivity.this.mLinkState == LinkState.INHIBIT || ((MuPDFPageView) MuPDFActivity.this.mDocView.getDisplayedView()) == null) {
                        }
                        if (-1 != -1) {
                            MuPDFActivity.this.mDocView.setDisplayedViewIndex(-1);
                        } else if (!MuPDFActivity.this.mButtonsVisible) {
                            MuPDFActivity.this.showButtons();
                        } else {
                            MuPDFActivity.this.hideButtons();
                        }
                    }
                    return super.onSingleTapUp(e);
                }

                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (!this.showButtonsDisabled) {
                        MuPDFActivity.this.hideButtons();
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                public boolean onScaleBegin(ScaleGestureDetector d) {
                    this.showButtonsDisabled = true;
                    return super.onScaleBegin(d);
                }

                public boolean onTouchEvent(MotionEvent event) {
                    if (event.getActionMasked() == 0) {
                        this.showButtonsDisabled = false;
                    }
                    return super.onTouchEvent(event);
                }

                /* access modifiers changed from: protected */
                public void onChildSetup(int i, View v) {
                    if (SearchTaskResult.get() == null || SearchTaskResult.get().pageNumber != i) {
                        ((PageView) v).setSearchBoxes(null);
                    } else {
                        ((PageView) v).setSearchBoxes(SearchTaskResult.get().searchBoxes);
                    }
                    ((PageView) v).setLinkHighlighting(MuPDFActivity.this.mLinkState == LinkState.HIGHLIGHT);
                }

                /* access modifiers changed from: protected */
                public void onMoveToChild(int i) {
                    if (MuPDFActivity.this.core != null) {
                        MuPDFActivity.this.mPageNumberView.setText(String.format("%d/%d", new Object[]{Integer.valueOf(i + 1), Integer.valueOf(MuPDFActivity.this.core.countPages())}));
                        MuPDFActivity.this.mPageSlider.setMax(MuPDFActivity.this.core.countPages() - 1);
                        MuPDFActivity.this.mPageSlider.setProgress(i);
                        if (SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber != i) {
                            SearchTaskResult.set(null);
                            MuPDFActivity.this.mDocView.resetupChildren();
                        }
                    }
                }

                /* access modifiers changed from: protected */
                public void onSettle(View v) {
                    ((PageView) v).addHq();
                }

                /* access modifiers changed from: protected */
                public void onUnsettle(View v) {
                    ((PageView) v).removeHq();
                }
            };
            this.mDocView.setAdapter(new MuPDFPageAdapter(this, this.core));
            makeButtonsView();
            this.mFilenameView.setText(this.mFileName);
            this.mPageSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MuPDFActivity.this.mDocView.setDisplayedViewIndex(seekBar.getProgress());
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    MuPDFActivity.this.updatePageNumView(progress);
                }
            });
            this.mSearchButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MuPDFActivity.this.searchModeOn();
                }
            });
            this.mCancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MuPDFActivity.this.searchModeOff();
                }
            });
            this.mSearchBack.setEnabled(false);
            this.mSearchFwd.setEnabled(false);
            this.mSearchText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    boolean haveText = s.toString().length() > 0;
                    MuPDFActivity.this.mSearchBack.setEnabled(haveText);
                    MuPDFActivity.this.mSearchFwd.setEnabled(haveText);
                    if (SearchTaskResult.get() != null && !MuPDFActivity.this.mSearchText.getText().toString().equals(SearchTaskResult.get().txt)) {
                        SearchTaskResult.set(null);
                        MuPDFActivity.this.mDocView.resetupChildren();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            this.mSearchText.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == 6) {
                        MuPDFActivity.this.search(1);
                    }
                    return false;
                }
            });
            this.mSearchText.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == 0 && keyCode == 66) {
                        MuPDFActivity.this.search(1);
                    }
                    return false;
                }
            });
            this.mSearchBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MuPDFActivity.this.search(-1);
                }
            });
            this.mSearchFwd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MuPDFActivity.this.search(1);
                }
            });
            if (this.core.hasOutline()) {
                this.mOutlineButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        OutlineItem[] outline = MuPDFActivity.this.core.getOutline();
                        if (outline != null) {
                            OutlineActivityData.get().items = outline;
                            MuPDFActivity.this.startActivityForResult(new Intent(MuPDFActivity.this, OutlineActivity.class), 0);
                        }
                    }
                });
            } else {
                this.mOutlineButton.setVisibility(8);
            }
            this.mDocView.setDisplayedViewIndex(getPreferences(0).getInt("page" + this.mFileName, 0));
            if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false)) {
                showButtons();
            }
            if (savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false)) {
                searchModeOn();
            }
            RelativeLayout layout = new RelativeLayout(this);
            layout.addView(this.mDocView);
            layout.addView(this.mButtonsView);
            layout.addView(new LinearLayout(this), new MarginLayoutParams(-1, -1));
            layout.setBackgroundResource(C0048R.drawable.tiled_background);
            setContentView(layout);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode >= 0) {
            this.mDocView.setDisplayedViewIndex(resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Object onRetainNonConfigurationInstance() {
        MuPDFCore mycore = this.core;
        this.core = null;
        return mycore;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!(this.mFileName == null || this.mDocView == null)) {
            outState.putString("FileName", this.mFileName);
            Editor edit = getPreferences(0).edit();
            edit.putInt("page" + this.mFileName, this.mDocView.getDisplayedViewIndex());
            edit.commit();
        }
        if (!this.mButtonsVisible) {
            outState.putBoolean("ButtonsHidden", true);
        }
        if (this.mTopBarIsSearch) {
            outState.putBoolean("SearchMode", true);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        killSearch();
        if (this.mFileName != null && this.mDocView != null) {
            Editor edit = getPreferences(0).edit();
            edit.putInt("page" + this.mFileName, this.mDocView.getDisplayedViewIndex());
            edit.commit();
        }
    }

    public void onDestroy() {
        if (this.core != null) {
            this.core.onDestroy();
        }
        this.core = null;
        super.onDestroy();
    }

    /* access modifiers changed from: 0000 */
    public void showButtons() {
        if (this.core != null && !this.mButtonsVisible) {
            this.mButtonsVisible = true;
            int index = this.mDocView.getDisplayedViewIndex();
            updatePageNumView(index);
            this.mPageSlider.setMax(this.core.countPages() - 1);
            this.mPageSlider.setProgress(index);
            if (this.mTopBarIsSearch) {
                this.mSearchText.requestFocus();
                showKeyboard();
            }
            Animation anim = new TranslateAnimation(0.0f, 0.0f, (float) (-this.mTopBarSwitcher.getHeight()), 0.0f);
            anim.setDuration(200);
            anim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    MuPDFActivity.this.mTopBarSwitcher.setVisibility(0);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                }
            });
            this.mTopBarSwitcher.startAnimation(anim);
            Animation anim2 = new TranslateAnimation(0.0f, 0.0f, (float) this.mPageSlider.getHeight(), 0.0f);
            anim2.setDuration(200);
            anim2.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    MuPDFActivity.this.mPageSlider.setVisibility(0);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    MuPDFActivity.this.mPageNumberView.setVisibility(0);
                }
            });
            this.mPageSlider.startAnimation(anim2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideButtons() {
        if (this.mButtonsVisible) {
            this.mButtonsVisible = false;
            hideKeyboard();
            Animation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-this.mTopBarSwitcher.getHeight()));
            anim.setDuration(200);
            anim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    MuPDFActivity.this.mTopBarSwitcher.setVisibility(4);
                }
            });
            this.mTopBarSwitcher.startAnimation(anim);
            Animation anim2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) this.mPageSlider.getHeight());
            anim2.setDuration(200);
            anim2.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    MuPDFActivity.this.mPageNumberView.setVisibility(4);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    MuPDFActivity.this.mPageSlider.setVisibility(4);
                }
            });
            this.mPageSlider.startAnimation(anim2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void searchModeOn() {
        this.mTopBarIsSearch = true;
        this.mSearchText.requestFocus();
        showKeyboard();
        this.mTopBarSwitcher.showNext();
    }

    /* access modifiers changed from: 0000 */
    public void searchModeOff() {
        this.mTopBarIsSearch = false;
        hideKeyboard();
        this.mTopBarSwitcher.showPrevious();
        SearchTaskResult.set(null);
        this.mDocView.resetupChildren();
    }

    /* access modifiers changed from: 0000 */
    public void updatePageNumView(int index) {
        if (this.core != null) {
            this.mPageNumberView.setText(String.format("%d/%d", new Object[]{Integer.valueOf(index + 1), Integer.valueOf(this.core.countPages())}));
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeButtonsView() {
        this.mButtonsView = getLayoutInflater().inflate(C0048R.C0050layout.buttons, null);
        this.mFilenameView = (TextView) this.mButtonsView.findViewById(C0048R.C0049id.docNameText);
        this.mPageSlider = (SeekBar) this.mButtonsView.findViewById(C0048R.C0049id.pageSlider);
        this.mPageNumberView = (TextView) this.mButtonsView.findViewById(C0048R.C0049id.pageNumber);
        this.mSearchButton = (ImageButton) this.mButtonsView.findViewById(C0048R.C0049id.searchButton);
        this.mCancelButton = (ImageButton) this.mButtonsView.findViewById(C0048R.C0049id.cancel);
        this.mOutlineButton = (ImageButton) this.mButtonsView.findViewById(C0048R.C0049id.outlineButton);
        this.mTopBarSwitcher = (ViewSwitcher) this.mButtonsView.findViewById(C0048R.C0049id.switcher);
        this.mSearchBack = (ImageButton) this.mButtonsView.findViewById(C0048R.C0049id.searchBack);
        this.mSearchFwd = (ImageButton) this.mButtonsView.findViewById(C0048R.C0049id.searchForward);
        this.mSearchText = (EditText) this.mButtonsView.findViewById(C0048R.C0049id.searchText);
        this.mTopBarSwitcher.setVisibility(4);
        this.mPageNumberView.setVisibility(4);
        this.mPageSlider.setVisibility(4);
    }

    /* access modifiers changed from: 0000 */
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        if (imm != null) {
            imm.showSoftInput(this.mSearchText, 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(this.mSearchText.getWindowToken(), 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public void killSearch() {
        if (this.mSearchTask != null) {
            this.mSearchTask.cancel(true);
            this.mSearchTask = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void search(int direction) {
        hideKeyboard();
        if (this.core != null) {
            killSearch();
            final ProgressDialogX progressDialog = new ProgressDialogX(this);
            progressDialog.setProgressStyle(1);
            progressDialog.setTitle(getString(C0048R.string.searching_));
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    MuPDFActivity.this.killSearch();
                }
            });
            progressDialog.setMax(this.core.countPages());
            this.mSearchTask = new AsyncTask<Integer, Integer, SearchTaskResult>() {
                /* access modifiers changed from: protected */
                public SearchTaskResult doInBackground(Integer... params) {
                    int index;
                    if (SearchTaskResult.get() == null) {
                        index = MuPDFActivity.this.mDocView.getDisplayedViewIndex();
                    } else {
                        index = SearchTaskResult.get().pageNumber + params[0].intValue();
                    }
                    while (index >= 0 && index < MuPDFActivity.this.core.countPages() && !isCancelled()) {
                        publishProgress(new Integer[]{Integer.valueOf(index)});
                        RectF[] searchHits = MuPDFActivity.this.core.searchPage(index, MuPDFActivity.this.mSearchText.getText().toString());
                        if (searchHits != null && searchHits.length > 0) {
                            return new SearchTaskResult(MuPDFActivity.this.mSearchText.getText().toString(), index, searchHits);
                        }
                        index += params[0].intValue();
                    }
                    return null;
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(SearchTaskResult result) {
                    progressDialog.cancel();
                    if (result != null) {
                        MuPDFActivity.this.mDocView.setDisplayedViewIndex(result.pageNumber);
                        SearchTaskResult.set(result);
                        MuPDFActivity.this.mDocView.resetupChildren();
                        return;
                    }
                    MuPDFActivity.this.mAlertBuilder.setTitle(C0048R.string.text_not_found);
                    AlertDialog alert = MuPDFActivity.this.mAlertBuilder.create();
                    alert.setButton(-1, "Dismiss", null);
                    alert.show();
                }

                /* access modifiers changed from: protected */
                public void onCancelled() {
                    super.onCancelled();
                    progressDialog.cancel();
                }

                /* access modifiers changed from: protected */
                public void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    progressDialog.setProgress(values[0].intValue());
                }

                /* access modifiers changed from: protected */
                public void onPreExecute() {
                    super.onPreExecute();
                    MuPDFActivity.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!progressDialog.isCancelled()) {
                                progressDialog.show();
                            }
                        }
                    }, 200);
                }
            };
            this.mSearchTask.execute(new Integer[]{new Integer(direction)});
        }
    }
}
