package com.mediav.ads.sdk.adcore;

import android.app.Activity;
import android.view.ViewGroup;
import com.mediav.ads.sdk.interfaces.IBridge;
import com.mediav.ads.sdk.interfaces.IMvBannerAd;
import com.mediav.ads.sdk.interfaces.IMvFloatbannerAd;
import com.mediav.ads.sdk.interfaces.IMvInterstitialAd;

public class Mvad {
    private static IMvFloatbannerAd floatbannerAd = null;
    private static IMvInterstitialAd interstitialAd = null;
    private static IBridge mvad = null;

    public enum FLOAT_BANNER_SIZE {
        SIZE_DEFAULT,
        SIZE_MATCH_PARENT,
        SIZE_640X100,
        SIZE_936x120,
        SIZE_728x90
    }

    public enum FLOAT_LOCATION {
        TOP,
        BOTTOM
    }

    public static IMvBannerAd showBanner(ViewGroup adContainer, Activity activity, String adSpaceid, Boolean isTest) {
        if (mvad == null) {
            mvad = UpdateBridge.getBridge(activity);
            if (mvad == null) {
                return null;
            }
        }
        try {
            return (IMvBannerAd) mvad.getBanner(adContainer, activity, adSpaceid, isTest);
        } catch (Exception e) {
            CLog.m4e("初始化插屏失败:" + e.getMessage());
            return null;
        }
    }

    public static IMvFloatbannerAd showFloatbannerAd(Activity activity, String adSpaceid, Boolean isTest, FLOAT_BANNER_SIZE size, FLOAT_LOCATION location) {
        if (mvad == null) {
            mvad = UpdateBridge.getBridge(activity);
            if (mvad == null) {
                return null;
            }
        }
        try {
            if (floatbannerAd == null) {
                floatbannerAd = (IMvFloatbannerAd) mvad.getFloatingBanner(activity, adSpaceid, isTest, Integer.valueOf(size.ordinal()), Integer.valueOf(location.ordinal()));
            } else {
                mvad.getFloatingBanner(activity, adSpaceid, isTest, Integer.valueOf(size.ordinal()), Integer.valueOf(location.ordinal()));
            }
            return floatbannerAd;
        } catch (Exception e) {
            CLog.m4e("初始化插屏失败:" + e.getMessage());
            return null;
        }
    }

    public static IMvFloatbannerAd closeFloatbannerAd(Activity activity) {
        if (floatbannerAd != null) {
            floatbannerAd.closeAds();
        }
        return null;
    }

    public static IMvInterstitialAd showInterstitial(Activity activity, String adSpaceid, Boolean isTest) {
        if (mvad == null) {
            mvad = UpdateBridge.getBridge(activity);
            if (mvad == null) {
                return null;
            }
        }
        try {
            if (interstitialAd == null) {
                interstitialAd = (IMvInterstitialAd) mvad.getInterstitial(activity, adSpaceid, isTest);
            } else {
                interstitialAd.showAds(activity);
            }
            return interstitialAd;
        } catch (Exception e) {
            CLog.m4e("初始化插屏失败:" + e.getMessage());
            return null;
        }
    }

    public static IMvInterstitialAd closeInterstitial(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.closeAds();
        }
        return null;
    }

    public static void activityDestroy(Activity activity) {
        if (mvad != null) {
            mvad.activityDestroy(activity);
        }
    }

    public static void unregisterAdReceiver(Activity activity) {
        if (mvad != null) {
            mvad.unregisterMediavReceiver(activity);
        }
    }
}
