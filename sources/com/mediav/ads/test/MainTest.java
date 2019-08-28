package com.mediav.ads.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.mediav.ads.sdk.adcore.Mvad;
import com.mediav.ads.sdk.adcore.Mvad.FLOAT_BANNER_SIZE;
import com.mediav.ads.sdk.adcore.Mvad.FLOAT_LOCATION;
import com.mediav.ads.sdk.interfaces.IMvBannerAd;
import com.mediav.ads.sdk.interfaces.IMvFloatbannerAd;
import com.mediav.ads.sdk.interfaces.IMvInterstitialAd;
import org.apache.http.protocol.HTTP;

public class MainTest extends Activity implements OnClickListener {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        LayoutParams lp = new LayoutParams(-1, -1);
        ll.setOrientation(1);
        setContentView(ll, lp);
        RelativeLayout adc = new RelativeLayout(this);
        ll.addView(adc, new LayoutParams(-1, -2));
        IMvBannerAd showBanner = Mvad.showBanner(adc, this, "11", Boolean.valueOf(true));
        IMvInterstitialAd showInterstitial = Mvad.showInterstitial(this, "123123213", Boolean.valueOf(true));
        IMvFloatbannerAd showFloatbannerAd = Mvad.showFloatbannerAd(this, "123", Boolean.valueOf(true), FLOAT_BANNER_SIZE.SIZE_DEFAULT, FLOAT_LOCATION.BOTTOM);
    }

    public void onClick(View v) {
        Button btn = (Button) v;
        if (btn.getTag().equals(HTTP.CONN_CLOSE)) {
            Mvad.closeInterstitial(this);
        } else if (btn.getTag().equals("Show")) {
            Mvad.showInterstitial(this, "1111", Boolean.valueOf(true));
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Mvad.activityDestroy(this);
    }
}
