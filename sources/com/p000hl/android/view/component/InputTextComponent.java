package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.InputTextComponentEntity;
import com.p000hl.android.core.utils.AppUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.InputTextComponent */
public class InputTextComponent extends EditText implements Component {
    private InputTextComponentEntity _entity;
    public ViewRecord initRecord;

    public InputTextComponent(Context context) {
        super(context);
    }

    public InputTextComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        setSingleLine();
    }

    public ComponentEntity getEntity() {
        return this._entity;
    }

    public void setEntity(ComponentEntity entity) {
        this._entity = (InputTextComponentEntity) entity;
    }

    public void load() {
        setSingleLine(true);
        setImeOptions(6);
        setHint(this._entity.getPlaceHolder());
        setText(this._entity.getText());
        setTextColor(AppUtils.parseColor(this._entity.getFontColor()));
        if ("center".equals(this._entity.getFontAlig())) {
            setGravity(1);
        } else if ("left".equals(this._entity.getFontAlig())) {
            setGravity(3);
        } else if ("right".equals(this._entity.getFontAlig())) {
            setGravity(5);
        }
        if (this._entity.getFontSize() != null) {
            float horScreenValue = ScreenUtils.getHorScreenValue((float) Integer.parseInt(this._entity.getFontSize()));
            setTextSize(2, (float) Integer.parseInt(this._entity.getFontSize()));
            Log.i("maker", "fontsize " + this._entity.getFontSize());
        }
        if ("true".equals(this._entity.getBordVisible())) {
            setBackgroundResource(C0048R.drawable.border_input);
        }
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

    @SuppressLint({"NewApi"})
    public ViewRecord getCurrentRecord() {
        ViewRecord curRecord = new ViewRecord();
        curRecord.mHeight = getLayoutParams().width;
        curRecord.mWidth = getLayoutParams().height;
        curRecord.f28mX = getX();
        curRecord.f29mY = getY();
        curRecord.mRotation = getRotation();
        return curRecord;
    }
}
