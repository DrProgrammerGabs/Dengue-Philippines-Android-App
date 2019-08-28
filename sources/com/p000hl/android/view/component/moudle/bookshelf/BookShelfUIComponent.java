package com.p000hl.android.view.component.moudle.bookshelf;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.DataUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.core.utils.WebUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.protocol.HTTP;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* renamed from: com.hl.android.view.component.moudle.bookshelf.BookShelfUIComponent */
public class BookShelfUIComponent extends RelativeLayout implements Component {
    private int bookHeight;
    private int bookWidth;
    List<ShelvesBook> books = new ArrayList();
    private GridView booksGridView;
    private ShelvesAdapter booksViewAdapter;
    private MoudleComponentEntity componentEntity;
    Handler downHandler = new Handler() {
        public void handleMessage(Message msg) {
            for (ShelvesBook book : BookShelfUIComponent.this.books) {
                book.mState = BookShelfUIComponent.this.getBookDownState(book);
                if (book.mState == 2) {
                    new DownloadBookTask(book, (Activity) BookShelfUIComponent.this.mContext).execute(new String[]{book.mBookUrl});
                }
            }
        }
    };
    public boolean isStop = false;
    private int layoutHeight;
    private int layoutWidth;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public LinearLayout progressBarContainer;

    /* renamed from: com.hl.android.view.component.moudle.bookshelf.BookShelfUIComponent$DownloadBookTask */
    class DownloadBookTask extends AsyncTask<String, Integer, String> {
        Activity activity;
        ViewGroup bookView;
        private ShelvesBook mBook;
        HorizontalProgressBar progressBar;
        double rate = 0.0d;

        public DownloadBookTask(ShelvesBook book, Activity activity2) {
            this.mBook = book;
            this.activity = activity2;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:183:0x03a9, code lost:
            r6 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:184:0x03aa, code lost:
            r6.printStackTrace();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x01c0, code lost:
            if (r14.length() >= (((long) r4) + r7)) goto L_0x020b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x01c2, code lost:
            android.widget.Toast.makeText(r26.activity, "网络故障，下载已中断", 1).show();
            publishProgress(new java.lang.Integer[]{java.lang.Integer.valueOf(2)});
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x01ec, code lost:
            r22 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x01ee, code lost:
            if (r0 == null) goto L_0x01f3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            r0.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:?, code lost:
            publishProgress(new java.lang.Integer[]{java.lang.Integer.valueOf(0)});
         */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x031c A[SYNTHETIC, Splitter:B:122:0x031c] */
        /* JADX WARNING: Removed duplicated region for block: B:125:0x0321 A[SYNTHETIC, Splitter:B:125:0x0321] */
        /* JADX WARNING: Removed duplicated region for block: B:128:0x0326  */
        /* JADX WARNING: Removed duplicated region for block: B:130:0x032b A[SYNTHETIC, Splitter:B:130:0x032b] */
        /* JADX WARNING: Removed duplicated region for block: B:133:0x0330 A[SYNTHETIC, Splitter:B:133:0x0330] */
        /* JADX WARNING: Removed duplicated region for block: B:139:0x033e A[SYNTHETIC, Splitter:B:139:0x033e] */
        /* JADX WARNING: Removed duplicated region for block: B:142:0x0343 A[SYNTHETIC, Splitter:B:142:0x0343] */
        /* JADX WARNING: Removed duplicated region for block: B:145:0x0348  */
        /* JADX WARNING: Removed duplicated region for block: B:147:0x034d A[SYNTHETIC, Splitter:B:147:0x034d] */
        /* JADX WARNING: Removed duplicated region for block: B:150:0x0352 A[SYNTHETIC, Splitter:B:150:0x0352] */
        /* JADX WARNING: Removed duplicated region for block: B:71:0x01fa  */
        /* JADX WARNING: Removed duplicated region for block: B:73:0x01ff A[SYNTHETIC, Splitter:B:73:0x01ff] */
        /* JADX WARNING: Removed duplicated region for block: B:76:0x0204 A[SYNTHETIC, Splitter:B:76:0x0204] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String doInBackground(java.lang.String... r27) {
            /*
                r26 = this;
                r9 = 0
                r20 = 0
                r3 = 0
                r16 = 0
                r10 = 0
                java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02fb }
                r22.<init>()     // Catch:{ Exception -> 0x02fb }
                r0 = r26
                com.hl.android.view.component.moudle.bookshelf.ShelvesBook r0 = r0.mBook     // Catch:{ Exception -> 0x02fb }
                r23 = r0
                r0 = r23
                java.lang.String r0 = r0.mLocalPath     // Catch:{ Exception -> 0x02fb }
                r23 = r0
                java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x02fb }
                java.lang.String r23 = "/book.zip"
                java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x02fb }
                java.lang.String r5 = r22.toString()     // Catch:{ Exception -> 0x02fb }
                java.io.File r14 = new java.io.File     // Catch:{ Exception -> 0x02fb }
                r14.<init>(r5)     // Catch:{ Exception -> 0x02fb }
                boolean r22 = r14.exists()     // Catch:{ Exception -> 0x02fb }
                if (r22 != 0) goto L_0x0034
                r14.createNewFile()     // Catch:{ Exception -> 0x02fb }
            L_0x0034:
                long r7 = r14.length()     // Catch:{ Exception -> 0x02fb }
                java.net.URL r21 = new java.net.URL     // Catch:{ Exception -> 0x02fb }
                r0 = r26
                com.hl.android.view.component.moudle.bookshelf.ShelvesBook r0 = r0.mBook     // Catch:{ Exception -> 0x02fb }
                r22 = r0
                r0 = r22
                java.lang.String r0 = r0.mBookUrl     // Catch:{ Exception -> 0x02fb }
                r22 = r0
                r21.<init>(r22)     // Catch:{ Exception -> 0x02fb }
                java.net.URLConnection r22 = r21.openConnection()     // Catch:{ Exception -> 0x02fb }
                r0 = r22
                java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x02fb }
                r3 = r0
                java.lang.String r22 = "User-Agent"
                java.lang.String r23 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.1.4322)"
                r0 = r22
                r1 = r23
                r3.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x02fb }
                java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02fb }
                r22.<init>()     // Catch:{ Exception -> 0x02fb }
                java.lang.String r23 = "bytes="
                java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x02fb }
                r0 = r22
                java.lang.StringBuilder r22 = r0.append(r7)     // Catch:{ Exception -> 0x02fb }
                java.lang.String r23 = "-"
                java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x02fb }
                java.lang.String r19 = r22.toString()     // Catch:{ Exception -> 0x02fb }
                java.lang.String r22 = "RANGE"
                r0 = r22
                r1 = r19
                r3.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x02fb }
                java.io.RandomAccessFile r17 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x02fb }
                java.lang.String r22 = "rwd"
                r0 = r17
                r1 = r22
                r0.<init>(r14, r1)     // Catch:{ Exception -> 0x02fb }
                r0 = r17
                r0.seek(r7)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                int r4 = r3.getContentLength()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                long r22 = r14.length()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r22
                double r0 = (double) r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                long r0 = (long) r4     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                long r24 = r24 + r7
                r0 = r24
                double r0 = (double) r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                double r22 = r22 / r24
                r0 = r22
                r2 = r26
                r2.rate = r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                double r0 = r0.rate     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r24 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                int r22 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1))
                if (r22 >= 0) goto L_0x0224
                int r22 = r3.getResponseCode()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r23 = 200(0xc8, float:2.8E-43)
                r0 = r22
                r1 = r23
                if (r0 == r1) goto L_0x00d4
                int r22 = r3.getResponseCode()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r23 = 206(0xce, float:2.89E-43)
                r0 = r22
                r1 = r23
                if (r0 != r1) goto L_0x02b2
            L_0x00d4:
                java.io.InputStream r10 = r3.getInputStream()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1024(0x400, float:1.435E-42)
                r0 = r22
                byte[] r15 = new byte[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r18 = 0
                r13 = 0
                r11 = 0
            L_0x00e3:
                r22 = 0
                r23 = 1024(0x400, float:1.435E-42)
                r0 = r22
                r1 = r23
                int r18 = r10.read(r15, r0, r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = -1
                r0 = r18
                r1 = r22
                if (r0 == r1) goto L_0x01b5
                r0 = r26
                com.hl.android.view.component.moudle.bookshelf.BookShelfUIComponent r0 = com.p000hl.android.view.component.moudle.bookshelf.BookShelfUIComponent.this     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r0 = r22
                boolean r0 = r0.isStop     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                if (r22 == 0) goto L_0x0123
                java.lang.String r22 = "pause"
                if (r17 == 0) goto L_0x010c
                r17.close()     // Catch:{ IOException -> 0x0379 }
            L_0x010c:
                if (r10 == 0) goto L_0x0111
                r10.close()     // Catch:{ IOException -> 0x037f }
            L_0x0111:
                if (r3 == 0) goto L_0x0116
                r3.disconnect()
            L_0x0116:
                if (r9 == 0) goto L_0x011b
                r9.close()     // Catch:{ IOException -> 0x0385 }
            L_0x011b:
                if (r20 == 0) goto L_0x0120
                r20.close()     // Catch:{ IOException -> 0x038b }
            L_0x0120:
                r16 = r17
            L_0x0122:
                return r22
            L_0x0123:
                r13 = r18
                r22 = 0
                r0 = r17
                r1 = r22
                r0.write(r15, r1, r13)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                long r22 = r14.length()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r22
                double r0 = (double) r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                long r0 = (long) r4     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                long r24 = r24 + r7
                r0 = r24
                double r0 = (double) r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                double r22 = r22 / r24
                r0 = r22
                r2 = r26
                r2.rate = r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
                r0 = r26
                double r0 = r0.rate     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                double r22 = r22 * r24
                r0 = r22
                r2 = r26
                r2.rate = r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                double r0 = r0.rate     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r24 = 4547007122018943789(0x3f1a36e2eb1c432d, double:1.0E-4)
                double r24 = r24 + r11
                int r22 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1))
                if (r22 <= 0) goto L_0x018a
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 0
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                double r11 = r0.rate     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
            L_0x018a:
                r0 = r26
                android.app.Activity r0 = r0.activity     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                boolean r22 = r22.isFinishing()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                if (r22 == 0) goto L_0x00e3
                r22 = 0
                if (r17 == 0) goto L_0x019d
                r17.close()     // Catch:{ IOException -> 0x0391 }
            L_0x019d:
                if (r10 == 0) goto L_0x01a2
                r10.close()     // Catch:{ IOException -> 0x0397 }
            L_0x01a2:
                if (r3 == 0) goto L_0x01a7
                r3.disconnect()
            L_0x01a7:
                if (r9 == 0) goto L_0x01ac
                r9.close()     // Catch:{ IOException -> 0x039d }
            L_0x01ac:
                if (r20 == 0) goto L_0x01b1
                r20.close()     // Catch:{ IOException -> 0x03a3 }
            L_0x01b1:
                r16 = r17
                goto L_0x0122
            L_0x01b5:
                long r22 = r14.length()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                long r0 = (long) r4     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r24 = r0
                long r24 = r24 + r7
                int r22 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1))
                if (r22 >= 0) goto L_0x020b
                r0 = r26
                android.app.Activity r0 = r0.activity     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                java.lang.String r23 = "网络故障，下载已中断"
                r24 = 1
                android.widget.Toast r22 = android.widget.Toast.makeText(r22, r23, r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22.show()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 2
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 0
                if (r17 == 0) goto L_0x01f3
                r17.close()     // Catch:{ IOException -> 0x03a9 }
            L_0x01f3:
                if (r10 == 0) goto L_0x01f8
                r10.close()     // Catch:{ IOException -> 0x03af }
            L_0x01f8:
                if (r3 == 0) goto L_0x01fd
                r3.disconnect()
            L_0x01fd:
                if (r9 == 0) goto L_0x0202
                r9.close()     // Catch:{ IOException -> 0x03b5 }
            L_0x0202:
                if (r20 == 0) goto L_0x0207
                r20.close()     // Catch:{ IOException -> 0x03bb }
            L_0x0207:
                r16 = r17
                goto L_0x0122
            L_0x020b:
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 0
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
            L_0x0224:
                r22 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
                r0 = r22
                r2 = r26
                r2.rate = r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 0
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                com.hl.android.view.component.moudle.bookshelf.ShelvesBook r0 = r0.mBook     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r0 = r22
                java.lang.String r0 = r0.mLocalPath     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r0 = r22
                com.p000hl.android.core.utils.ZipUtils.UnZipFolder(r5, r0)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                r0 = r22
                r2 = r26
                r2.rate = r0     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 0
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 1
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                java.lang.String r22 = "success"
                if (r17 == 0) goto L_0x029a
                r17.close()     // Catch:{ IOException -> 0x03c1 }
            L_0x029a:
                if (r10 == 0) goto L_0x029f
                r10.close()     // Catch:{ IOException -> 0x03c7 }
            L_0x029f:
                if (r3 == 0) goto L_0x02a4
                r3.disconnect()
            L_0x02a4:
                if (r9 == 0) goto L_0x02a9
                r9.close()     // Catch:{ IOException -> 0x03cd }
            L_0x02a9:
                if (r20 == 0) goto L_0x02ae
                r20.close()     // Catch:{ IOException -> 0x03d3 }
            L_0x02ae:
                r16 = r17
                goto L_0x0122
            L_0x02b2:
                r0 = r26
                android.app.Activity r0 = r0.activity     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                java.lang.String r23 = "网络故障，下载已中断"
                r24 = 1
                android.widget.Toast r22 = android.widget.Toast.makeText(r22, r23, r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22.show()     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = r0
                r23 = 0
                r24 = 2
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22[r23] = r24     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ Exception -> 0x03f6, all -> 0x03f1 }
                r22 = 0
                if (r17 == 0) goto L_0x02e3
                r17.close()     // Catch:{ IOException -> 0x03d9 }
            L_0x02e3:
                if (r10 == 0) goto L_0x02e8
                r10.close()     // Catch:{ IOException -> 0x03df }
            L_0x02e8:
                if (r3 == 0) goto L_0x02ed
                r3.disconnect()
            L_0x02ed:
                if (r9 == 0) goto L_0x02f2
                r9.close()     // Catch:{ IOException -> 0x03e5 }
            L_0x02f2:
                if (r20 == 0) goto L_0x02f7
                r20.close()     // Catch:{ IOException -> 0x03eb }
            L_0x02f7:
                r16 = r17
                goto L_0x0122
            L_0x02fb:
                r6 = move-exception
            L_0x02fc:
                r6.printStackTrace()     // Catch:{ all -> 0x033b }
                r22 = 1
                r0 = r22
                java.lang.Integer[] r0 = new java.lang.Integer[r0]     // Catch:{ all -> 0x033b }
                r22 = r0
                r23 = 0
                r24 = 2
                java.lang.Integer r24 = java.lang.Integer.valueOf(r24)     // Catch:{ all -> 0x033b }
                r22[r23] = r24     // Catch:{ all -> 0x033b }
                r0 = r26
                r1 = r22
                r0.publishProgress(r1)     // Catch:{ all -> 0x033b }
                r22 = 0
                if (r16 == 0) goto L_0x031f
                r16.close()     // Catch:{ IOException -> 0x036a }
            L_0x031f:
                if (r10 == 0) goto L_0x0324
                r10.close()     // Catch:{ IOException -> 0x036f }
            L_0x0324:
                if (r3 == 0) goto L_0x0329
                r3.disconnect()
            L_0x0329:
                if (r9 == 0) goto L_0x032e
                r9.close()     // Catch:{ IOException -> 0x0374 }
            L_0x032e:
                if (r20 == 0) goto L_0x0122
                r20.close()     // Catch:{ IOException -> 0x0335 }
                goto L_0x0122
            L_0x0335:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0122
            L_0x033b:
                r22 = move-exception
            L_0x033c:
                if (r16 == 0) goto L_0x0341
                r16.close()     // Catch:{ IOException -> 0x0356 }
            L_0x0341:
                if (r10 == 0) goto L_0x0346
                r10.close()     // Catch:{ IOException -> 0x035b }
            L_0x0346:
                if (r3 == 0) goto L_0x034b
                r3.disconnect()
            L_0x034b:
                if (r9 == 0) goto L_0x0350
                r9.close()     // Catch:{ IOException -> 0x0360 }
            L_0x0350:
                if (r20 == 0) goto L_0x0355
                r20.close()     // Catch:{ IOException -> 0x0365 }
            L_0x0355:
                throw r22
            L_0x0356:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0341
            L_0x035b:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0346
            L_0x0360:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0350
            L_0x0365:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0355
            L_0x036a:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x031f
            L_0x036f:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0324
            L_0x0374:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x032e
            L_0x0379:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x010c
            L_0x037f:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0111
            L_0x0385:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x011b
            L_0x038b:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0120
            L_0x0391:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x019d
            L_0x0397:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x01a2
            L_0x039d:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x01ac
            L_0x03a3:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x01b1
            L_0x03a9:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x01f3
            L_0x03af:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x01f8
            L_0x03b5:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0202
            L_0x03bb:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0207
            L_0x03c1:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x029a
            L_0x03c7:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x029f
            L_0x03cd:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02a9
            L_0x03d3:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02ae
            L_0x03d9:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02e3
            L_0x03df:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02e8
            L_0x03e5:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02f2
            L_0x03eb:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x02f7
            L_0x03f1:
                r22 = move-exception
                r16 = r17
                goto L_0x033c
            L_0x03f6:
                r6 = move-exception
                r16 = r17
                goto L_0x02fc
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.moudle.bookshelf.BookShelfUIComponent.DownloadBookTask.doInBackground(java.lang.String[]):java.lang.String");
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... values) {
            int result = values[0].intValue();
            this.bookView = (ViewGroup) BookShelfUIComponent.this.findViewById(this.mBook.viewID);
            if (this.bookView != null) {
                this.progressBar = (HorizontalProgressBar) this.bookView.findViewById(ShelvesAdapter.PROGRESSBAR_ID);
                if (this.progressBar != null) {
                    if (result == 1 && this.progressBar != null) {
                        this.progressBar.setVisibility(8);
                        Toast.makeText(this.activity, "下载完成", 1).show();
                        this.mBook.mState = 1;
                        DataUtils.savePreference((Activity) BookShelfUIComponent.this.mContext, this.mBook.mLocalPath, 1);
                        Log.d("hl", this.mBook.mBookID + " 下载完成");
                    }
                    if (result == 0) {
                        Log.d("hl", this.mBook.mBookID + "下载进度" + this.rate);
                        this.progressBar.setProgress((int) (this.rate * ((double) this.progressBar.getMax())));
                        this.progressBar.setVisibility(0);
                        this.mBook.mState = 2;
                    }
                    if (result == 2) {
                        this.progressBar.setProgress(0);
                        this.progressBar.setVisibility(8);
                        if (result == 0) {
                            Toast.makeText(this.activity, "网络故障，下载已中断", 1).show();
                        }
                        this.mBook.mState = 0;
                    }
                }
            }
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.bookshelf.BookShelfUIComponent$ShelvesAdapter */
    class ShelvesAdapter extends BaseAdapter {
        public static final int PROGRESSBAR_ID = 100000;
        private int imgHeight;
        private int imgWidth;
        private Context mContext;
        Bitmap oldBitmap = null;

        public ShelvesAdapter(Context context, int imgWidth2, int imgHeight2) {
            this.mContext = context;
            this.imgWidth = imgWidth2;
            this.imgHeight = imgHeight2;
        }

        public int getCount() {
            return BookShelfUIComponent.this.books.size();
        }

        public Object getItem(int arg0) {
            return BookShelfUIComponent.this.books.get(arg0);
        }

        public long getItemId(int arg0) {
            return (long) arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout bookView;
            if (convertView == null) {
                bookView = getBookView();
            } else {
                bookView = (RelativeLayout) convertView;
            }
            HorizontalProgressBar progressBar = (HorizontalProgressBar) bookView.findViewById(PROGRESSBAR_ID);
            ShelvesBook book = (ShelvesBook) getItem(position);
            bookView.setId(book.viewID);
            progressBar.setVisibility(8);
            Bitmap coverBitmap = BookShelfUIComponent.this.getCoverBitmap(book);
            if (coverBitmap != null) {
                bookView.setBackgroundDrawable(new BitmapDrawable(this.mContext.getResources(), coverBitmap));
            } else {
                bookView.setGravity(17);
                bookView.addView(new TextView(this.mContext));
            }
            if (this.oldBitmap != null) {
                this.oldBitmap.recycle();
                this.oldBitmap = null;
            }
            return bookView;
        }

        public RelativeLayout getBookView() {
            RelativeLayout bookView = new RelativeLayout(this.mContext);
            bookView.setLayoutParams(new LayoutParams(this.imgWidth, this.imgHeight));
            bookView.setGravity(80);
            HorizontalProgressBar progressBar = new HorizontalProgressBar(this.mContext);
            progressBar.setVisibility(8);
            progressBar.setId(PROGRESSBAR_ID);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, 10);
            lp.addRule(12);
            lp.setMargins(10, 0, 10, 0);
            bookView.addView(progressBar, lp);
            return bookView;
        }
    }

    public BookShelfUIComponent(Context context) {
        super(context);
        this.mContext = context;
    }

    public BookShelfUIComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        this.mContext = context;
    }

    public ComponentEntity getEntity() {
        return this.componentEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.componentEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.isStop = false;
        this.layoutWidth = getLayoutParams().width;
        this.layoutHeight = getLayoutParams().height;
        this.booksGridView = new GridView(this.mContext);
        addView(this.booksGridView, -1, -1);
        this.progressBarContainer = new LinearLayout(this.mContext);
        this.progressBarContainer.setOrientation(1);
        this.progressBarContainer.setGravity(1);
        TextView text = new TextView(this.mContext);
        text.setTextColor(-16777216);
        text.setText("获取书架数据，请稍候...");
        this.progressBarContainer.addView(text, this.layoutWidth, this.layoutHeight);
        this.progressBarContainer.addView(new ProgressBar(this.mContext), this.layoutWidth, this.layoutHeight);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.layoutWidth, this.layoutHeight);
        lp.addRule(13);
        addView(this.progressBarContainer, lp);
        this.bookWidth = this.componentEntity.getBookWidth();
        this.bookHeight = this.componentEntity.getBookHeight();
        if (HLSetting.FitScreen) {
            this.bookWidth = (int) (((float) this.bookWidth) * BookSetting.RESIZE_WIDTH);
            this.bookHeight = (int) (((float) this.bookHeight) * BookSetting.RESIZE_HEIGHT);
        } else {
            this.bookWidth = (int) (((float) this.bookWidth) * BookSetting.RESIZE_COUNT);
            this.bookHeight = (int) (((float) this.bookHeight) * BookSetting.RESIZE_COUNT);
        }
        setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap(this.componentEntity.getBgSourceID(), getContext(), this.layoutWidth, this.layoutHeight)));
        new AsyncTask<String, String, Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean doInBackground(String... params) {
                return Boolean.valueOf(BookShelfUIComponent.this.initBookShelvesData(params[0]));
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean result) {
                if (result.booleanValue()) {
                    BookShelfUIComponent.this.drawBookshelfView();
                    return;
                }
                Toast.makeText(BookShelfUIComponent.this.mContext, "同步数据出错", 1).show();
                BookShelfUIComponent.this.removeView(BookShelfUIComponent.this.progressBarContainer);
            }
        }.execute(new String[]{this.componentEntity.getServerAddress()});
    }

    public void showBook(ShelvesBook book) {
        try {
            BookSetting.BOOK_PATH = book.mLocalPath + "/book/";
            HLSetting.IsResourceSD = true;
            BookSetting.IS_SHELVES_COMPONENT = true;
            BookController.getInstance().openShelves();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public int getBookDownState(ShelvesBook book) {
        return DataUtils.getPreference((Activity) this.mContext, book.mLocalPath, 0);
    }

    /* access modifiers changed from: private */
    public void drawBookshelfView() {
        removeView(this.progressBarContainer);
        this.booksGridView.setNumColumns(this.layoutWidth / this.bookWidth);
        this.booksGridView.setGravity(1);
        this.booksViewAdapter = new ShelvesAdapter(this.mContext, this.bookWidth, this.bookHeight);
        this.booksGridView.setAdapter(this.booksViewAdapter);
        this.booksGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ShelvesBook book = (ShelvesBook) arg0.getItemAtPosition(arg2);
                if (new File(book.mLocalPath + "/book/book.xml").exists()) {
                    DataUtils.savePreference((Activity) BookShelfUIComponent.this.mContext, book.mLocalPath, 1);
                    book.mState = 1;
                    BookShelfUIComponent.this.showBook(book);
                } else if (book.mState != 2 && !StringUtils.isEmpty(book.mBookUrl)) {
                    book.mState = BookShelfUIComponent.this.getBookDownState(book);
                    if (book.mState == 1) {
                        DataUtils.savePreference((Activity) BookShelfUIComponent.this.mContext, book.mLocalPath, 0);
                        book.mState = 0;
                    }
                    DataUtils.savePreference((Activity) BookShelfUIComponent.this.mContext, book.mLocalPath, 2);
                    new DownloadBookTask(book, (Activity) BookShelfUIComponent.this.mContext).execute(new String[]{book.mBookUrl});
                }
            }
        });
        this.downHandler.sendEmptyMessageDelayed(1, 500);
    }

    /* access modifiers changed from: private */
    public boolean initBookShelvesData(String shelfUrlStr) {
        String key = this.mContext.getPackageName() + "." + this.componentEntity.getComponentId() + ".serverAddr";
        String content = DataUtils.getPreference((Activity) this.mContext, key, (String) null);
        if (StringUtils.isEmpty(content)) {
            if (WebUtils.isConnectingToInternet((Activity) this.mContext)) {
                content = WebUtils.getUrlContent(shelfUrlStr, HTTP.UTF_8);
                if (!StringUtils.isEmpty(content)) {
                    DataUtils.savePreference((Activity) this.mContext, key, content);
                }
            }
            if (StringUtils.isEmpty(content)) {
                Log.d("hl", "从服务器获取书架信息出错，并且同时本地数据也未存在");
                return false;
            }
        }
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            BookshelfXMLParseHandler xmlParseHandler = new BookshelfXMLParseHandler(this.books);
            XMLReader xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(xmlParseHandler);
            xmlReader.parse(new InputSource(new StringReader(content)));
            return true;
        } catch (Exception e) {
            Log.e("hl", "同步书架内容出错", e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public Bitmap getCoverBitmap(ShelvesBook book) {
        Bitmap bitmap = null;
        File coverFile = new File(book.mCoverPath);
        if (!coverFile.exists()) {
            return bitmap;
        }
        try {
            return BitmapUtils.load(new FileInputStream(coverFile), this.bookWidth, this.bookHeight);
        } catch (FileNotFoundException e) {
            return bitmap;
        }
    }

    public void load(InputStream is) {
    }

    public void play() {
        Log.d("hl", "play");
    }

    public void stop() {
        this.isStop = true;
        Log.d("hl", "stop");
    }

    public void hide() {
        Log.d("hl", "hide");
    }

    public void show() {
        Log.d("hl", "show");
    }

    public void resume() {
        Log.d("hl", "resume");
    }

    public void pause() {
        Log.d("hl", "pause");
    }
}
