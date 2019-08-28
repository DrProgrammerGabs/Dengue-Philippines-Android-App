package com.p000hl.android.view.component.moudle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.DataUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.moudle.HLCameraUIComponent */
public class HLCameraUIComponent extends RelativeLayout implements Component, Callback {
    protected final int CAMERA_POSITION = 65554;
    protected final int CAMERA_SHUTTER = 65555;
    /* access modifiers changed from: private */
    public ImageView backOrFrontImage;
    public Camera camera;
    /* access modifiers changed from: private */
    public boolean canShutter;
    /* access modifiers changed from: private */
    public int currentPictureId;
    private Bitmap currentShowBitmap;
    public SurfaceHolder holder;
    /* access modifiers changed from: private */
    public boolean isBack = true;
    PictureCallback jpeg = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                HLCameraUIComponent.this.currentPictureId = HLCameraUIComponent.this.currentPictureId + 1;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(HLCameraUIComponent.this.savePath + File.separator + HLCameraUIComponent.this.saveName + "_" + HLCameraUIComponent.this.currentPictureId + ".jpg"));
                DataUtils.savePreference((Activity) HLCameraUIComponent.this.mContext, HLCameraUIComponent.this.saveName, HLCameraUIComponent.this.currentPictureId);
                bitmap.compress(CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                camera.stopPreview();
                bitmap.recycle();
                Toast.makeText(HLCameraUIComponent.this.mContext, "Save picture success", 0).show();
                HLCameraUIComponent.this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + HLCameraUIComponent.this.sdCardPath + File.separator + "hlPicture")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    OnClickListener listener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 65554:
                    HLCameraUIComponent.this.removeView(HLCameraUIComponent.this.showImage);
                    HLCameraUIComponent.this.shutter.setImageResource(C0048R.drawable.scan_disable);
                    HLCameraUIComponent.this.canShutter = true;
                    CameraInfo cameraInfo = new CameraInfo();
                    int cameraCount = Camera.getNumberOfCameras();
                    int i = 0;
                    while (true) {
                        if (i < cameraCount) {
                            Camera.getCameraInfo(i, cameraInfo);
                            if (HLCameraUIComponent.this.isBack) {
                                if (cameraInfo.facing == 1) {
                                    HLCameraUIComponent.this.camera.stopPreview();
                                    HLCameraUIComponent.this.camera.release();
                                    HLCameraUIComponent.this.camera = null;
                                    HLCameraUIComponent.this.camera = Camera.open(i);
                                    try {
                                        HLCameraUIComponent.this.camera.setPreviewDisplay(HLCameraUIComponent.this.holder);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    HLCameraUIComponent.this.camera.startPreview();
                                    HLCameraUIComponent.this.isBack = false;
                                    HLCameraUIComponent.this.camera.autoFocus(null);
                                }
                            } else if (cameraInfo.facing == 0) {
                                HLCameraUIComponent.this.camera.stopPreview();
                                HLCameraUIComponent.this.camera.release();
                                HLCameraUIComponent.this.camera = null;
                                HLCameraUIComponent.this.camera = Camera.open(i);
                                try {
                                    HLCameraUIComponent.this.camera.setPreviewDisplay(HLCameraUIComponent.this.holder);
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                                HLCameraUIComponent.this.camera.startPreview();
                                HLCameraUIComponent.this.isBack = true;
                                HLCameraUIComponent.this.camera.autoFocus(null);
                            }
                            i++;
                        }
                    }
                    if (HLCameraUIComponent.this.isBack) {
                        HLCameraUIComponent.this.backOrFrontImage.setImageResource(C0048R.drawable.camer_b);
                        return;
                    } else {
                        HLCameraUIComponent.this.backOrFrontImage.setImageResource(C0048R.drawable.camer_f);
                        return;
                    }
                case 65555:
                    HLCameraUIComponent.this.removeView(HLCameraUIComponent.this.showImage);
                    if (HLCameraUIComponent.this.canShutter) {
                        HLCameraUIComponent.this.camera.takePicture(null, null, HLCameraUIComponent.this.jpeg);
                        HLCameraUIComponent.this.shutter.setImageResource(C0048R.drawable.scan_enable);
                        HLCameraUIComponent.this.canShutter = false;
                        return;
                    }
                    HLCameraUIComponent.this.camera.startPreview();
                    HLCameraUIComponent.this.shutter.setImageResource(C0048R.drawable.scan_disable);
                    HLCameraUIComponent.this.canShutter = true;
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    private ComponentEntity mEntity;
    /* access modifiers changed from: private */
    public String saveName;
    /* access modifiers changed from: private */
    public String savePath;
    /* access modifiers changed from: private */
    public String sdCardPath;
    /* access modifiers changed from: private */
    public ImageView showImage;
    /* access modifiers changed from: private */
    public ImageView shutter;
    public SurfaceView surface;

    public HLCameraUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mEntity = entity;
        this.mContext = context;
    }

    public void surfaceChanged(SurfaceHolder holder2, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder2) {
        try {
            this.camera.setPreviewDisplay(holder2);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder2) {
        try {
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.surface = null;
        } catch (Exception e) {
        }
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                if (this.camera == null) {
                    Log.d("wdy", "无摄像头，不加载摄像头视图，返回！！");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("wdy", "打开摄像头失败，不加载摄像头视图，返回！！");
                return;
            }
        }
        this.sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.savePath = this.sdCardPath + File.separator + this.mContext.getPackageName() + File.separator + "HLPicture";
        this.saveName = this.mContext.getPackageName() + BookSetting.BOOK_PATH.replaceAll("/", "") + "_" + BookController.getInstance().getViewPage().getEntity().getID() + "_" + this.mEntity.getComponentId();
        this.currentPictureId = DataUtils.getPreference((Activity) this.mContext, this.saveName, 0);
        this.currentShowBitmap = BitmapFactory.decodeStream(FileUtils.getInstance().getFileInputStreamFilePath(this.savePath + File.separator + this.saveName + "_" + this.currentPictureId + ".jpg"));
        File mfilepath = new File(this.savePath);
        if (!mfilepath.isDirectory() || !mfilepath.exists()) {
            mfilepath.mkdirs();
        }
        LayoutParams surfaceOrImagelp = new LayoutParams(-1, -1);
        this.surface = new SurfaceView(this.mContext);
        this.holder = this.surface.getHolder();
        this.holder.addCallback(this);
        addView(this.surface, surfaceOrImagelp);
        if (this.currentShowBitmap != null) {
            this.showImage = new ImageView(this.mContext);
            this.showImage.setImageBitmap(this.currentShowBitmap);
            this.showImage.setScaleType(ScaleType.FIT_XY);
            addView(this.showImage, surfaceOrImagelp);
        }
        if (Camera.getNumberOfCameras() >= 1) {
            this.backOrFrontImage = new ImageView(this.mContext);
            this.backOrFrontImage.setId(65554);
            this.backOrFrontImage.setImageResource(C0048R.drawable.camer_b);
            LayoutParams positionlp = new LayoutParams(-2, -2);
            positionlp.addRule(10);
            positionlp.addRule(9);
            addView(this.backOrFrontImage, positionlp);
            this.backOrFrontImage.setOnClickListener(this.listener);
        }
        this.shutter = new ImageView(this.mContext);
        this.shutter.setId(65555);
        if (this.currentShowBitmap != null) {
            this.shutter.setImageResource(C0048R.drawable.scan_enable);
            this.canShutter = false;
        } else {
            this.shutter.setImageResource(C0048R.drawable.scan_disable);
            this.canShutter = true;
        }
        LayoutParams shutterlp = new LayoutParams(-2, -2);
        shutterlp.addRule(12);
        shutterlp.addRule(14);
        addView(this.shutter, shutterlp);
        this.shutter.setOnClickListener(this.listener);
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }
}
