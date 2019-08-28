package com.mediav.ads.sdk.interfaces;

import android.app.Activity;
import android.view.ViewGroup;

public interface IBridge {
    void activityDestroy(Activity activity);

    Object getBanner(ViewGroup viewGroup, Activity activity, String str, Boolean bool);

    Object getFloatingBanner(Activity activity, String str, Boolean bool, Integer num, Integer num2);

    Object getInterstitial(Activity activity, String str, Boolean bool);

    void unregisterMediavReceiver(Activity activity);
}
