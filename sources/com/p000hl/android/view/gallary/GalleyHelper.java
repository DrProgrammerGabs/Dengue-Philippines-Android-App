package com.p000hl.android.view.gallary;

import android.content.Context;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.ReflectHelp;
import com.p000hl.android.view.gallary.base.AbstractGalley;

/* renamed from: com.hl.android.view.gallary.GalleyHelper */
public class GalleyHelper {
    public static AbstractGalley getGalley(Context context) {
        Class[] argsType = {Context.class};
        Object[] argsValue = {context};
        try {
            if (BookSetting.GALLEYCODE == 0) {
                return (AbstractGalley) ReflectHelp.newInstance("com.hl.android.view.gallary.GalleyCommon", argsType, argsValue);
            }
            if (BookSetting.GALLEYCODE == 1) {
                return (AbstractGalley) ReflectHelp.newInstance("com.hl.android.view.gallary.Galley3D4ShowSnaps", argsType, argsValue);
            }
            return (AbstractGalley) ReflectHelp.newInstance("com.hl.android.view.gallary.GalleyCommon", argsType, argsValue);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
