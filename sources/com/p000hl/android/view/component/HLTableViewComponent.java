package com.p000hl.android.view.component;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.HLTableViewComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.AppUtils;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.core.utils.WebUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.HLTableViewComponent */
public class HLTableViewComponent extends GridView implements Component, ComponentListener, ComponentPost {
    public static JSONObject curItemMap = null;
    private Context _context;
    GridBookAdapter adapter = new GridBookAdapter();
    AnimatorSet animsationSet;
    JSONArray array;
    Bitmap[][] bitmaps;
    /* access modifiers changed from: private */
    public int defaultSize = 1;
    public HLTableViewComponentEntity entity = null;
    int initHeight = 0;
    int initWidth = 0;
    int initX = 0;
    int initY = 0;
    private boolean isSendAutoPage = false;
    private OnComponentCallbackListener onComponentCallbackListener;

    /* renamed from: com.hl.android.view.component.HLTableViewComponent$GridBookAdapter */
    class GridBookAdapter extends BaseAdapter {
        GridBookAdapter() {
        }

        public int getCount() {
            if (HLTableViewComponent.this.array == null || HLTableViewComponent.this.array.length() == 0) {
                return HLTableViewComponent.this.defaultSize;
            }
            return HLTableViewComponent.this.array.length();
        }

        public Object getItem(int arg0) {
            return HLTableViewComponent.this.array.opt(arg0);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (HLTableViewComponent.this.array == null || HLTableViewComponent.this.array.length() <= 0) {
                return HLTableViewComponent.this.getCellView(-1);
            }
            return HLTableViewComponent.this.getCellView(arg0);
        }
    }

    public HLTableViewComponent(Context context) {
        super(context);
        setNumColumns(-1);
        this._context = context;
        setSelector(new ColorDrawable(0));
    }

    public HLTableViewComponent(Context context, ComponentEntity entity2) {
        super(context);
        setEntity(entity2);
        this._context = context;
        setSelector(new ColorDrawable(0));
    }

    public void load() {
        this.initX = getEntity().f7x;
        this.initY = getEntity().f8y;
        this.defaultSize = getLayoutParams().height / ((int) Float.parseFloat(this.entity.cellSize.cellHeight));
        if (this.entity.isHideAtBegining) {
            setVisibility(8);
        }
        this.array = new JSONArray();
        setAdapter(this.adapter);
        String url = this.entity.req.requestHeader + this.entity.req.requestURL;
        new AsyncTask<String, String, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(String... arg0) {
                String url = arg0[0];
                if (HLTableViewComponent.this.entity.req.requestMethod.indexOf(HttpPost.METHOD_NAME) > -1) {
                    return WebUtils.postDataToUrl(url, HLTableViewComponent.this.entity.req.param);
                }
                if (HLTableViewComponent.this.entity.req.requestMethod.indexOf(HttpHead.METHOD_NAME) > -1) {
                    return WebUtils.headDataToUrl(url, HLTableViewComponent.this.entity.req.param);
                }
                if (HLTableViewComponent.this.entity.req.requestMethod.indexOf(HttpPut.METHOD_NAME) > -1) {
                    return WebUtils.putDataToUrl(url, HLTableViewComponent.this.entity.req.param);
                }
                if (HLTableViewComponent.this.entity.req.requestMethod.indexOf(HttpDelete.METHOD_NAME) > -1) {
                    return WebUtils.deleteDataToUrl(url, HLTableViewComponent.this.entity.req.param);
                }
                if (HLTableViewComponent.this.entity.req.requestMethod.indexOf(HttpOptions.METHOD_NAME) > -1) {
                    return WebUtils.optDataToUrl(url, HLTableViewComponent.this.entity.req.param);
                }
                return WebUtils.getDataToUrl(url, HLTableViewComponent.this.entity.req.param);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String result) {
                try {
                    if (StringUtils.isEmpty(result)) {
                        HLTableViewComponent.this.array = new JSONArray();
                    } else if (StringUtils.isEmpty(HLTableViewComponent.this.entity.cellModel.ModelParent)) {
                        HLTableViewComponent.this.array = new JSONArray(result);
                    } else {
                        JSONObject json = new JSONObject(result);
                        HLTableViewComponent.this.array = json.optJSONArray(HLTableViewComponent.this.entity.cellModel.ModelParent);
                    }
                    HLTableViewComponent.this.setAdapter(HLTableViewComponent.this.adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url});
        if (!StringUtils.isEmpty(this.entity.cellSize.BackgroundColor)) {
            setBackgroundColor(AppUtils.parseColor(this.entity.cellSize.BackgroundColor));
        }
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HLTableViewComponent.curItemMap = (JSONObject) arg0.getItemAtPosition(arg2);
                BookController.getInstance().runBehavior(HLTableViewComponent.this.entity, Behavior.BEHAVIOR_ON_LIST_EACHITEM_CLICK);
                BookController.getInstance().runBehavior(HLTableViewComponent.this.entity, Behavior.BEHAVIOR_ON_LIST_ITEM_CLICK);
                if ("true".equals(HLTableViewComponent.this.entity.cellModel.IsClickOpenBrowser)) {
                    String url = HLTableViewComponent.curItemMap.optString(HLTableViewComponent.this.entity.cellModel.ClikcOpenKey);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    intent.setData(Uri.parse(url));
                    HLTableViewComponent.this.getContext().startActivity(intent);
                }
            }
        });
    }

    public void load(InputStream is) {
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (HLTableViewComponentEntity) entity2;
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void recyle() {
    }

    public void callBackListener() {
        if (!this.isSendAutoPage) {
            this.onComponentCallbackListener.setPlayComplete();
            this.isSendAutoPage = true;
        }
    }

    public static Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    /* JADX WARNING: type inference failed for: r13v0 */
    /* JADX WARNING: type inference failed for: r13v1, types: [android.view.View] */
    /* JADX WARNING: type inference failed for: r13v2 */
    /* JADX WARNING: type inference failed for: r13v3 */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: type inference failed for: r13v7 */
    /* JADX WARNING: type inference failed for: r13v8 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.widget.RelativeLayout getCellView(int r26) {
        /*
            r25 = this;
            android.widget.RelativeLayout r17 = new android.widget.RelativeLayout
            r0 = r25
            android.content.Context r0 = r0._context
            r22 = r0
            r0 = r17
            r1 = r22
            r0.<init>(r1)
            float r22 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            r0 = r25
            com.hl.android.book.entity.HLTableViewComponentEntity r0 = r0.entity
            r23 = r0
            r0 = r23
            com.hl.android.book.entity.HLTableViewComponentEntity$CellSize r0 = r0.cellSize
            r23 = r0
            r0 = r23
            java.lang.String r0 = r0.cellWidth
            r23 = r0
            float r23 = java.lang.Float.parseFloat(r23)
            float r22 = r22 * r23
            r0 = r22
            int r5 = (int) r0
            float r22 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            r0 = r25
            com.hl.android.book.entity.HLTableViewComponentEntity r0 = r0.entity
            r23 = r0
            r0 = r23
            com.hl.android.book.entity.HLTableViewComponentEntity$CellSize r0 = r0.cellSize
            r23 = r0
            r0 = r23
            java.lang.String r0 = r0.cellHeight
            r23 = r0
            float r23 = java.lang.Float.parseFloat(r23)
            float r22 = r22 * r23
            r0 = r22
            int r4 = (int) r0
            android.widget.AbsListView$LayoutParams r15 = new android.widget.AbsListView$LayoutParams
            r15.<init>(r5, r4)
            r0 = r17
            r0.setLayoutParams(r15)
            r0 = r25
            com.hl.android.book.entity.HLTableViewComponentEntity r0 = r0.entity
            r22 = r0
            r0 = r22
            com.hl.android.book.entity.HLTableViewComponentEntity$CellModel r0 = r0.cellModel
            r22 = r0
            r0 = r22
            java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> r0 = r0.commponentList
            r22 = r0
            java.util.Iterator r7 = r22.iterator()
        L_0x0069:
            boolean r22 = r7.hasNext()
            if (r22 == 0) goto L_0x02df
            java.lang.Object r12 = r7.next()
            java.util.HashMap r12 = (java.util.HashMap) r12
            float r23 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            java.lang.String r22 = "Width"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            float r22 = java.lang.Float.parseFloat(r22)
            float r22 = r22 * r23
            r0 = r22
            int r14 = (int) r0
            float r23 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            java.lang.String r22 = "Height"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            float r22 = java.lang.Float.parseFloat(r22)
            float r22 = r22 * r23
            r0 = r22
            int r10 = (int) r0
            android.widget.RelativeLayout$LayoutParams r11 = new android.widget.RelativeLayout$LayoutParams
            r11.<init>(r14, r10)
            float r23 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            java.lang.String r22 = "X"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            float r22 = java.lang.Float.parseFloat(r22)
            float r22 = r22 * r23
            r0 = r22
            int r0 = (int) r0
            r22 = r0
            r0 = r22
            r11.leftMargin = r0
            float r23 = com.p000hl.android.common.BookSetting.PAGE_RATIO
            java.lang.String r22 = "Y"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            float r22 = java.lang.Float.parseFloat(r22)
            float r22 = r22 * r23
            r0 = r22
            int r0 = (int) r0
            r22 = r0
            r0 = r22
            r11.topMargin = r0
            r13 = 0
            java.lang.String r22 = "ComponentType"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            java.lang.String r23 = "HLTableCellImageComponent"
            int r22 = r22.indexOf(r23)
            if (r22 <= 0) goto L_0x01a2
            android.widget.ImageView r9 = new android.widget.ImageView
            r0 = r25
            android.content.Context r0 = r0._context
            r22 = r0
            r0 = r22
            r9.<init>(r0)
            android.widget.ImageView$ScaleType r22 = android.widget.ImageView.ScaleType.FIT_XY
            r0 = r22
            r9.setScaleType(r0)
            java.lang.String r22 = "ImageSrc"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            r0 = r25
            r1 = r22
            android.graphics.Bitmap r3 = r0.getBitmap(r1, r14, r10)
            r9.setImageBitmap(r3)
            r22 = 2131165187(0x7f070003, float:1.7944584E38)
            r0 = r22
            r9.setId(r0)
            r13 = r9
            r22 = -1
            r0 = r26
            r1 = r22
            if (r0 <= r1) goto L_0x02d8
            r0 = r25
            org.json.JSONArray r0 = r0.array
            r22 = r0
            r0 = r22
            r1 = r26
            org.json.JSONObject r6 = r0.optJSONObject(r1)
            r0 = r25
            com.hl.android.book.entity.HLTableViewComponentEntity r0 = r0.entity
            r22 = r0
            r0 = r22
            com.hl.android.book.entity.HLTableViewComponentEntity$CellModel r0 = r0.cellModel
            r22 = r0
            r0 = r22
            java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> r0 = r0.modelList
            r22 = r0
            java.util.Iterator r8 = r22.iterator()
        L_0x014b:
            boolean r22 = r8.hasNext()
            if (r22 == 0) goto L_0x02d8
            java.lang.Object r16 = r8.next()
            java.util.HashMap r16 = (java.util.HashMap) r16
            java.lang.String r22 = "ModelID"
            r0 = r16
            r1 = r22
            java.lang.Object r22 = r0.get(r1)
            java.lang.String r22 = (java.lang.String) r22
            java.lang.String r23 = "ComponentID"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x014b
            java.lang.String r22 = "ModelKey"
            r0 = r16
            r1 = r22
            java.lang.Object r22 = r0.get(r1)
            java.lang.String r22 = (java.lang.String) r22
            r0 = r22
            java.lang.String r18 = r6.optString(r0)
            boolean r22 = com.p000hl.android.core.utils.StringUtils.isEmpty(r18)
            if (r22 != 0) goto L_0x014b
            com.hl.android.view.component.HLTableViewComponent$3 r22 = new com.hl.android.view.component.HLTableViewComponent$3
            r0 = r22
            r1 = r25
            r0.<init>(r9)
            r23 = 1
            r0 = r23
            java.lang.String[] r0 = new java.lang.String[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r18
            r22.execute(r23)
            goto L_0x014b
        L_0x01a2:
            java.lang.String r22 = "ComponentType"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            java.lang.String r23 = "HLTableCellLabelComponent"
            int r22 = r22.indexOf(r23)
            if (r22 <= 0) goto L_0x02d8
            android.widget.TextView r21 = new android.widget.TextView
            r0 = r25
            android.content.Context r0 = r0._context
            r22 = r0
            r21.<init>(r22)
            java.lang.String r22 = "text"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            android.text.Spanned r22 = android.text.Html.fromHtml(r22)
            r21.setText(r22)
            r22 = 2131165186(0x7f070002, float:1.7944582E38)
            r21.setId(r22)
            java.lang.String r22 = "fontSize"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            float r22 = java.lang.Float.parseFloat(r22)
            float r20 = com.p000hl.android.core.utils.ScreenUtils.getHorScreenValue(r22)
            r22 = 0
            r0 = r21
            r1 = r22
            r2 = r20
            r0.setTextSize(r1, r2)
            r22 = 0
            r0 = r21
            r1 = r22
            r2 = r20
            r0.setTextSize(r1, r2)
            java.lang.String r22 = "fontColor"
            r0 = r22
            java.lang.Object r22 = r12.get(r0)
            java.lang.String r22 = (java.lang.String) r22
            int r22 = com.p000hl.android.core.utils.AppUtils.parseColor(r22)
            r21.setTextColor(r22)
            java.lang.String r22 = "center"
            java.lang.String r23 = "fontSize"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x02a2
            r22 = 1
            r0 = r25
            r1 = r22
            r0.setGravity(r1)
        L_0x0228:
            r13 = r21
            r22 = -1
            r0 = r26
            r1 = r22
            if (r0 <= r1) goto L_0x02d8
            r0 = r25
            org.json.JSONArray r0 = r0.array
            r22 = r0
            r0 = r22
            r1 = r26
            org.json.JSONObject r6 = r0.optJSONObject(r1)
            r0 = r25
            com.hl.android.book.entity.HLTableViewComponentEntity r0 = r0.entity
            r22 = r0
            r0 = r22
            com.hl.android.book.entity.HLTableViewComponentEntity$CellModel r0 = r0.cellModel
            r22 = r0
            r0 = r22
            java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> r0 = r0.modelList
            r22 = r0
            java.util.Iterator r8 = r22.iterator()
        L_0x0256:
            boolean r22 = r8.hasNext()
            if (r22 == 0) goto L_0x02d8
            java.lang.Object r16 = r8.next()
            java.util.HashMap r16 = (java.util.HashMap) r16
            java.lang.String r22 = "ModelID"
            r0 = r16
            r1 = r22
            java.lang.Object r22 = r0.get(r1)
            java.lang.String r22 = (java.lang.String) r22
            java.lang.String r23 = "ComponentID"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x0256
            java.lang.String r22 = "ModelKey"
            r0 = r16
            r1 = r22
            java.lang.Object r22 = r0.get(r1)
            java.lang.String r22 = (java.lang.String) r22
            java.lang.String r23 = "text"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            java.lang.String r23 = (java.lang.String) r23
            r0 = r22
            r1 = r23
            java.lang.String r19 = r6.optString(r0, r1)
            android.text.Spanned r22 = android.text.Html.fromHtml(r19)
            r21.setText(r22)
            goto L_0x0256
        L_0x02a2:
            java.lang.String r22 = "left"
            java.lang.String r23 = "fontSize"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x02bd
            r22 = 3
            r0 = r25
            r1 = r22
            r0.setGravity(r1)
            goto L_0x0228
        L_0x02bd:
            java.lang.String r22 = "right"
            java.lang.String r23 = "fontSize"
            r0 = r23
            java.lang.Object r23 = r12.get(r0)
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x0228
            r22 = 5
            r0 = r25
            r1 = r22
            r0.setGravity(r1)
            goto L_0x0228
        L_0x02d8:
            r0 = r17
            r0.addView(r13, r11)
            goto L_0x0069
        L_0x02df:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.HLTableViewComponent.getCellView(int):android.widget.RelativeLayout");
    }

    private Bitmap getBitmap(String bitMapResource, int width, int height) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(bitMapResource);
        if (bitmap != null) {
            return bitmap;
        }
        Bitmap bitmap2 = BitmapUtils.getBitMap(bitMapResource, getContext(), width, height);
        BitmapManager.putBitmapCache(bitMapResource, bitmap2);
        return bitmap2;
    }
}
