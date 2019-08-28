package com.p000hl.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.mediav.ads.sdk.adcore.Mvad;
import com.mediav.ads.sdk.adcore.Mvad.FLOAT_BANNER_SIZE;
import com.mediav.ads.sdk.adcore.Mvad.FLOAT_LOCATION;
import com.p000hl.android.book.BookDecoder;
import com.p000hl.android.book.DESedeCoder;
import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.book.entity.SnapshotEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.BookState;
import com.p000hl.android.controller.EventDispatcher;
import com.p000hl.android.controller.PageEntityController;
import com.p000hl.android.core.helper.AnimationHelper;
import com.p000hl.android.core.helper.BookHelper;
import com.p000hl.android.core.helper.LogHelper;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.ViewPage;
import com.p000hl.android.view.component.AudioComponent;
import com.p000hl.android.view.component.HLCounterComponent;
import com.p000hl.android.view.component.WaterStain;
import com.p000hl.callback.Action;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.http.HttpStatus;

@SuppressLint({"HandlerLeak"})
/* renamed from: com.hl.android.HLActivity */
public class HLActivity extends HLLayoutActivity {
    private static final int REQUEST_RESOLVE_ERROR = 0;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    public View bfView;
    Display disp = null;
    private boolean doCreateBeforResume = false;
    EventDispatcher eventDispatcher = new EventDispatcher();
    /* access modifiers changed from: private */
    public boolean isStop = false;
    protected Handler loadHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    HLActivity.this.doLoadAndInit();
                    return;
                default:
                    return;
            }
        }
    };
    Canvas mCurPageCanvas;
    Canvas mNextPageCanvas;
    private boolean mResolvingError = false;
    public ProgressDialog progressDialog;
    public Handler progressHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                HLActivity.this.progressDialog.show();
            } else if (what == 1) {
                HLActivity.this.progressDialog.dismiss();
            } else if (what == 2) {
                HLActivity.this.doLoadAndInit();
            }
        }
    };
    public WaterStain waterStain;

    /* renamed from: com.hl.android.HLActivity$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (!HLActivity.this.isStop) {
                HLActivity.this.startReadBook();
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    /* renamed from: com.hl.android.HLActivity$StartPageCompleteCallBack */
    private class StartPageCompleteCallBack implements Action {
        private long mStartTime;

        public StartPageCompleteCallBack() {
            this.mStartTime = 0;
            this.mStartTime = System.currentTimeMillis();
        }

        public boolean doAction() {
            double startPageTime = BookController.getInstance().getBook().getBookInfo().getStartPageTime();
            if (startPageTime == 0.0d) {
                BookController.getInstance().getBook().getBookInfo().setStartPageTime(3.0d);
                startPageTime = 3.0d;
            }
            int leftTime = (int) (1000.0d * startPageTime);
            if (leftTime < 0) {
                leftTime = 0;
            }
            if (BookController.getInstance().getBook().getBookInfo().getId().equals("-1396921599784")) {
                leftTime = 6000;
            }
            new MyCount((long) leftTime, 100).start();
            return true;
        }
    }

    private void addBFView() {
        LayoutParams lp1 = new LayoutParams(BookSetting.SCREEN_WIDTH, BookSetting.SCREEN_HEIGHT);
        this.bfView = new View(this);
        addContentView(this.bfView, lp1);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        isStoragePermissionGranted(this);
    }

    private void doOncreate() {
        BookSetting.IS_CLOSED = false;
        resetBookState();
        this.isStop = false;
        EventDispatcher.getInstance().init(this);
        BookController.getInstance().setHLActivity(this);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("正在加载");
        this.progressDialog.setMessage("我们正在努力加载数据文件，请耐心等候");
        this.progressDialog.setCancelable(false);
        if (this.preLoadAction != null) {
            this.preLoadAction.doAction();
            return;
        }
        this.loadHandler.sendEmptyMessageDelayed(1, 100);
        this.doCreateBeforResume = true;
    }

    private void setGoogleAd() {
        try {
            Log.d("SunYongle", "google广告开始加载");
            String adSpaceid = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.getString("DOMOB_PID");
            Log.d("ad", "google_ad_id=" + adSpaceid);
            if (adSpaceid != null && !adSpaceid.trim().equals("")) {
                RelativeLayout ad = new RelativeLayout(this);
                RelativeLayout.LayoutParams layoutParams4360layout = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams4360layout.addRule(14);
                String adPos = BookController.getInstance().getBook().getBookInfo().position;
                if (!TextUtils.isEmpty(adPos) || !"top".equals(adPos)) {
                    layoutParams4360layout.addRule(12);
                } else {
                    layoutParams4360layout.addRule(10);
                }
                layoutParams4360layout.addRule(14);
                layoutParams4360layout.addRule(10);
                this.coverLayout.addView(ad, layoutParams4360layout);
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint({"NewApi"})
    public boolean isStoragePermissionGranted(Activity activity) {
        if (VERSION.SDK_INT < 23) {
            doOncreate();
            return true;
        } else if (activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0 && activity.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0 && activity.checkSelfPermission("android.permission.CAMERA") == 0) {
            doOncreate();
            return true;
        } else {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CALL_PHONE", "android.permission.READ_PHONE_STATE", "android.permission.CAMERA"}, 1);
            return false;
        }
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                List<String> deniedPermissionList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != 0 && !permissions[i].equals("android.permission.CALL_PHONE")) {
                        deniedPermissionList.add(permissions[i]);
                    }
                }
                if (!deniedPermissionList.isEmpty()) {
                    Toast.makeText(this, "Not enough permissions, ganted  please and restart", 0).show();
                    return;
                }
            }
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0 && checkSelfPermission("android.permission.READ_PHONE_STATE") == 0 && checkSelfPermission("android.permission.CAMERA") == 0) {
                doOncreate();
            }
        }
    }

    public void resetBookState() {
        AnimationHelper.animatiorMap.clear();
    }

    public void doLoadAndInit() {
        Log.d("SunYongle", "doLoadAndInit,初始化开始");
        Book book = null;
        InputStream hashDatInputStream = FileUtils.getInstance().getFileInputStream(this, "hash.dat");
        HLSetting.IsHaveBookMark = false;
        if (hashDatInputStream == null) {
            book = loadWaterStainAndDecodeBookXml(null);
        } else if (checkIsCorrect()) {
            try {
                byte[] aa = read(FileUtils.readFile("book.dat"));
                String str = new String(DESedeCoder.decrypt(Base64.decode(FileUtils.inputStream2String(FileUtils.readFile("book.xml")), 2), validCompile(aa, Integer.parseInt(new String(DESedeCoder.decrypt(Base64.decode("DED1TerlRtY=", 2), "ywa;sdgfasdweunmxoina".getBytes())))).getBytes()));
                book = BookDecoder.getInstance().decode(new ByteArrayInputStream(str.getBytes()));
                setWaterStain(book);
            } catch (IOException e) {
            }
        } else {
            book = loadWaterStainAndDecodeBookXml(null);
        }
        if (book == null) {
            Toast.makeText(this, "书籍文件不存在，请检查", 0).show();
            BookSetting.IS_READER = false;
            finish();
            return;
        }
        BookController.getInstance().setBook(book);
        if (BookController.getInstance().getBook().getBookInfo().adType == 3) {
            set360Ad();
        } else if (BookController.getInstance().getBook().getBookInfo().adType == 4) {
            setGoogleAd();
        }
        String bookType = book.getBookInfo().getBookType();
        HLSetting.display = getWindowManager().getDefaultDisplay();
        BookHelper.setPageScreenSize(this, bookType);
        if (BookSetting.IS_HOR_VER || BookSetting.IS_HOR) {
            setRequestedOrientation(0);
        } else {
            setRequestedOrientation(1);
        }
        try {
            SnapshotEntity firstSnap = (SnapshotEntity) book.getSnapshots().get(1);
            BookSetting.SNAPSHOTS_WIDTH = Integer.valueOf(firstSnap.width).intValue();
            BookSetting.SNAPSHOTS_HEIGHT = Integer.valueOf(firstSnap.height).intValue();
        } catch (Exception e2) {
        }
        try {
            if (book.getBookInfo().bookFlipType.equals("corner_flip")) {
                BookSetting.FLIPCODE = 0;
                HLSetting.FlipTime = 1000;
            } else if (book.getBookInfo().bookFlipType.equals("slider_flip")) {
                BookSetting.FLIPCODE = 1;
                HLSetting.FlipTime = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            } else if (book.getBookInfo().bookFlipType.equals("hard_flip")) {
                BookSetting.FLIPCODE = 2;
                HLSetting.FlipTime = 1000;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (HLSetting.isSettingTumb && VERSION.SDK_INT >= 11) {
            HLSetting.isHoneyComb = true;
            if (VERSION.SDK_INT < 14) {
                BookSetting.SCREEN_HEIGHT -= 45;
            }
            if (VERSION.SDK_INT >= 14) {
                BookSetting.SCREEN_HEIGHT -= 35;
            }
        }
        BookDecoder.getInstance().initBookItemList();
        setMetaData();
        BookController.getInstance().setHLBookLayout(this.contentLayout);
        layout(book, getResources().getConfiguration().orientation);
        addBFView();
        ViewPage viewPage = new ViewPage(this, null, null);
        BookController.getInstance().loadStartPage(viewPage);
        StartPageCompleteCallBack startPageCompleteCallBack = new StartPageCompleteCallBack();
        viewPage.setPageCompletion(startPageCompleteCallBack);
        book.device_id = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        BookController.getInstance().playStartPage(viewPage);
    }

    private Book loadWaterStainAndDecodeBookXml(Book book) {
        Book book2 = BookDecoder.getInstance().decode(this, "book.xml");
        setWaterStain(book2);
        return book2;
    }

    private void setWaterStain(Book book) {
        if (!HLSetting.IsHaveBookMark) {
            if (book.getBookInfo().isFree) {
                HLSetting.IsShowBookMark = true;
                HLSetting.IsShowBookMarkLabel = false;
            } else {
                HLSetting.IsShowBookMark = false;
                HLSetting.IsShowBookMarkLabel = false;
            }
        }
        if (HLSetting.IsShowBookMark || HLSetting.IsShowBookMarkLabel) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            this.waterStain = new WaterStain(this);
            this.coverLayout.addView(this.waterStain, layoutParams);
        }
    }

    private boolean checkIsCorrect() {
        try {
            byte[] aa = read(FileUtils.readFile("book.dat"));
            String key = validCompile(aa, Integer.parseInt(new String(DESedeCoder.decrypt(Base64.decode("DED1TerlRtY=", 2), "ywa;sdgfasdweunmxoina".getBytes()))));
            String compareStr485 = validCompile(aa, Integer.parseInt(new String(DESedeCoder.decrypt(Base64.decode("VPglhR9kDeo=", 2), "ywa;sdgfeeasdweunmxoina".getBytes()))));
            byte[] data = DESedeCoder.decrypt(Base64.decode(FileUtils.inputStream2String(FileUtils.readFile("hash.dat")), 2), key.getBytes());
            if (data == null) {
                return false;
            }
            return new String(data).equals(compareStr485);
        } catch (IOException e) {
            Log.e("wdy", "解密对比时出错！！！！！");
            return false;
        }
    }

    public static byte[] read(InputStream input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        while (true) {
            try {
                int n = input.read(buf);
                if (-1 != n) {
                    out.write(buf, 0, n);
                } else {
                    input.close();
                    return out.toByteArray();
                }
            } catch (IOException e) {
                throw e;
            } catch (Throwable th) {
                input.close();
                throw th;
            }
        }
    }

    public String validCompile(byte[] content, int s) throws IOException {
        new ByteArrayInputStream(content);
        return validCompile3(content, s);
    }

    public byte[] unzip2(InputStream in) throws IOException {
        byte[] aa = null;
        try {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(in));
            while (true) {
                ZipEntry entry = zis.getNextEntry();
                if (entry != null) {
                    byte[] date = new byte[2048];
                    if (!entry.isDirectory()) {
                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                        while (true) {
                            int count = zis.read(date);
                            if (count == -1) {
                                break;
                            }
                            bos1.write(date, 0, count);
                        }
                        aa = bos1.toByteArray();
                        bos1.flush();
                        bos1.close();
                    }
                } else {
                    zis.close();
                    return aa;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String validCompile3(byte[] content, int s) throws IOException {
        int step = 1;
        byte[] newContent = new byte[(content.length - 4)];
        for (int i = 4; i < content.length; i++) {
            newContent[i - 4] = content[i];
        }
        System.out.println(new String(content));
        if (newContent.length > s) {
            step = newContent.length / s;
        }
        int point = 0;
        ArrayList<Byte> al = new ArrayList<>();
        for (int i2 = 0; i2 < s; i2++) {
            al.add(new Byte(newContent[point]));
            point += step;
            if (point >= newContent.length) {
                point %= newContent.length;
            }
        }
        System.out.println(al);
        byte[] aa = new byte[al.size()];
        for (int j = 0; j < al.size(); j++) {
            aa[j] = ((Byte) al.get(j)).byteValue();
        }
        return getMD5Byte(aa);
    }

    private String getMD5Byte(byte[] str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & 255).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(byteArray[i] & 255));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & 255));
            }
        }
        return md5StrBuff.toString();
    }

    /* access modifiers changed from: private */
    public void startReadBook() {
        setupViews();
        if (BookSetting.IS_HOR_VER) {
            playBookChange1();
            if (!StringUtils.isEmpty(BookController.getInstance().getBook().getBookInfo().backgroundMusicId)) {
                BookController.getInstance().playBackgroundMusic();
            }
        } else {
            if (BookSetting.IS_HOR) {
                setRequestedOrientation(0);
            } else {
                setRequestedOrientation(1);
            }
            BookHelper.setupScreen(this);
            setFlipView();
            BookController.getInstance().playBook();
        }
        this.coverLayout.setVisibility(0);
    }

    private void playBookChange1() {
        boolean z;
        if (ScreenUtils.getScreenWidth(this) < ScreenUtils.getScreenHeight(this)) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            BookSetting.IS_HOR = true;
        }
        if (z) {
            BookSetting.IS_HOR = false;
        }
        BookHelper.setupScreen(this);
        setFlipView();
        if (BookController.getInstance().section == null) {
            BookController.getInstance().changePageById((String) ((SectionEntity) BookController.getInstance().book.getSections().get(0)).getPages().get(0));
        } else if (StringUtils.isEmpty(BookController.getInstance().getViewPage().getEntity().getLinkPageID())) {
            BookController.getInstance().changePageById((String) ((SectionEntity) BookController.getInstance().book.getSections().get(0)).getPages().get(0));
        } else {
            BookHelper.setupScreen(this);
            setFlipView();
            BookController.getInstance().changePageById(BookController.getInstance().getViewPage().getEntity().getLinkPageID());
        }
    }

    private void postLayout() {
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                HLActivity.this.layout(BookController.getInstance().getBook());
            }
        }.start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        LogHelper.trace("onConfigurationChanged", "onConfigurationChanged", false);
        super.onConfigurationChanged(newConfig);
        Log.d("hl", " i am onConfigurationChanged");
        if (BookController.getInstance().getBook() != null) {
            postLayout();
            layout(BookController.getInstance().getBook(), newConfig.orientation);
            if (getRequestedOrientation() != 0 && getRequestedOrientation() != 1 && BookController.getInstance().getViewPage() != null) {
                PageEntity pageEntity = BookController.getInstance().getViewPage().getEntity();
                if (BookSetting.IS_HOR_VER && !StringUtils.isEmpty(pageEntity.getLinkPageID())) {
                    if (getResources().getConfiguration().orientation == 1 && (pageEntity.getType().equals(ViewPage.PAGE_TYPE_HOR) || pageEntity.getType().equals(ViewPage.PAGE_TYPE_NONE))) {
                        BookSetting.IS_HOR = false;
                        BookHelper.setupScreen(this);
                        setFlipView();
                        postLayout();
                        BookController.getInstance().changePageById(pageEntity.getLinkPageID());
                    }
                    if (getResources().getConfiguration().orientation == 2 && pageEntity.getType().equals(ViewPage.PAGE_TYPE_VER)) {
                        BookSetting.IS_HOR = true;
                        BookHelper.setupScreen(this);
                        setFlipView();
                        postLayout();
                        BookController.getInstance().changePageById(pageEntity.getLinkPageID());
                    }
                }
                relayoutGlobalButton();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent touchevent) {
        this.eventDispatcher.onTouch(touchevent);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        try {
            if (!(BookController.getInstance() == null || BookController.getInstance().getViewPage() == null)) {
                BookController.getInstance().getViewPage().stop();
                BookController.getInstance().getViewPage().clean();
            }
            if (PageEntityController.getInstance() != null) {
                PageEntityController.getInstance().recyle();
            }
            if (BookState.getInstance() != null) {
                BookState.getInstance().recyle();
            }
            if (this.gallery != null) {
                this.gallery.recycle();
            }
        } catch (Exception e) {
        }
        if (this.recycleAction != null) {
            this.recycleAction.doAction();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (BookController.getInstance().getViewPage() != null) {
            pause();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.coverLayout.setTag(null);
        this.mWindowManager.removeView(this.coverLayout);
        this.mWindowManager.removeView(this.pushLayout);
        this.mWindowManager.removeView(this.relpushListLayout);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        try {
            this.mWindowManager.removeView(this.coverLayout);
            this.mWindowManager.removeView(this.pushLayout);
            this.mWindowManager.removeView(this.relpushListLayout);
        } catch (Exception e) {
        }
        Log.d("XIXI", "_________");
        this.mWindowManager.addView(this.coverLayout, this.wmParams);
        this.mWindowManager.addView(this.pushLayout, this.pushParams);
        this.mWindowManager.addView(this.relpushListLayout, this.pushListParams);
        this.coverLayout.setTag("addToWindowManager");
        if (this.doCreateBeforResume) {
            this.coverLayout.setVisibility(8);
            this.doCreateBeforResume = false;
        }
        resume();
        this.coverLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                HLActivity.this.mainLayout.dispatchTouchEvent(event);
                return true;
            }
        });
    }

    private void setMetaData() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), 128);
            if (appInfo.metaData != null) {
                HLSetting.IsAD = appInfo.metaData.getBoolean("ISAD");
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void pause() {
        if (BookController.getInstance().getViewPage() != null) {
            try {
                BookController.getInstance().getViewPage().pause();
                if (HLSetting.PlayBackGroundMusic && BookController.getInstance().getBackgroundMusic() != null) {
                    BookController.getInstance().getBackgroundMusic().pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void resume() {
        if (BookController.getInstance().getViewPage() != null) {
            try {
                if (HLSetting.PlayBackGroundMusic && BookController.getInstance().getBackgroundMusic() != null) {
                    BookController.getInstance().getBackgroundMusic().resume();
                }
                BookController.getInstance().resume();
            } catch (Exception e) {
                Log.d("hl", "resume error");
            }
        }
    }

    public void onBackPressed() {
        try {
            BookController.getInstance().mainPageID = null;
            if (BookController.getInstance().commonPage != null) {
                for (int i = 0; i < BookController.getInstance().commonPage.getChildCount(); i++) {
                    ViewCell v = (ViewCell) BookController.getInstance().commonPage.getChildAt(i);
                    if (v.getComponent() instanceof HLCounterComponent) {
                        ((HLCounterComponent) v.getComponent()).reset();
                    }
                }
                BookController.getInstance().commonPage.stop();
                BookController.getInstance().commonPage.clean();
                BookController.getInstance().commonPage = null;
            }
            if (BookController.getInstance().getViewPage() != null) {
                for (int i2 = 0; i2 < BookController.getInstance().getViewPage().getChildCount(); i2++) {
                    ViewCell v2 = (ViewCell) BookController.getInstance().getViewPage().getChildAt(i2);
                    if (v2.getComponent() instanceof HLCounterComponent) {
                        ((HLCounterComponent) v2.getComponent()).reset();
                    }
                }
                BookController.getInstance().getViewPage().stop();
                BookController.getInstance().getViewPage().clean();
            }
            AudioComponent audioComponent = BookController.getInstance().getBackgroundMusic();
            if (audioComponent != null) {
                audioComponent.stop();
            }
            if (BookController.getInstance().section.isShelves) {
                BookController.getInstance().closeShelves();
            } else {
                if (BookSetting.IS_SHELVES && PageEntityController.getInstance() != null) {
                    PageEntityController.getInstance().clear();
                }
                BookSetting.IS_CLOSED = true;
                this.progressDialog.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public View get360AdView() {
        return null;
    }

    private void set360Ad() {
        try {
            Log.d("SunYongle", "360广告开始加载");
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), 128);
            String adSpaceid = info.metaData.getString("DOMOB_PID");
            if (adSpaceid != null && !adSpaceid.trim().equals("")) {
                Log.d("SunYongle", "360banner广告开始加载");
                if ("top".equals(BookController.getInstance().getBook().getBookInfo().position)) {
                    Mvad.showFloatbannerAd(this, adSpaceid, Boolean.valueOf(false), FLOAT_BANNER_SIZE.SIZE_DEFAULT, FLOAT_LOCATION.TOP);
                } else {
                    Mvad.showFloatbannerAd(this, adSpaceid, Boolean.valueOf(false), FLOAT_BANNER_SIZE.SIZE_DEFAULT, FLOAT_LOCATION.BOTTOM);
                }
                String adInsID = info.metaData.getString("DOMOB_INS_ID");
                if (adInsID != null && !adInsID.trim().equals("")) {
                    Log.d("SunYongle", "360inster广告开始加载");
                    Mvad.showInterstitial(this, adInsID, Boolean.valueOf(false)).showAds(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SunYongle", "360广告出错:" + e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SunYongle", "google广告开始连接");
        if (requestCode == 0) {
            this.mResolvingError = false;
            if (resultCode == -1) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
