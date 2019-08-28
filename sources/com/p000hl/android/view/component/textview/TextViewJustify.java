package com.p000hl.android.view.component.textview;

import android.graphics.Paint;
import android.widget.TextView;

/* renamed from: com.hl.android.view.component.textview.TextViewJustify */
public class TextViewJustify {
    static final float COMPLEXITY = 5.12f;
    static final String SYSTEM_NEWLINE = "\n";

    /* renamed from: p */
    static final Paint f57p = new Paint();

    public static void justifyText(TextView tv, float origWidth) {
        String s = (String) tv.getText();
        f57p.setTypeface(tv.getTypeface());
        String[] splits = s.split(SYSTEM_NEWLINE);
        float width = origWidth - 5.0f;
        for (int x = 0; x < splits.length; x++) {
            if (f57p.measureText(splits[x]) > width) {
                splits[x] = wrap(splits[x], width, f57p);
                String[] microSplits = splits[x].split(SYSTEM_NEWLINE);
                for (int y = 0; y < microSplits.length - 1; y++) {
                    microSplits[y] = justify(removeLast(microSplits[y], " "), width, f57p);
                }
                StringBuilder smb_internal = new StringBuilder();
                for (int z = 0; z < microSplits.length; z++) {
                    smb_internal.append(microSplits[z] + (z + 1 < microSplits.length ? SYSTEM_NEWLINE : ""));
                }
                splits[x] = smb_internal.toString();
            }
        }
        StringBuilder smb = new StringBuilder();
        String[] arr$ = splits;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            smb.append(arr$[i$] + SYSTEM_NEWLINE);
        }
        tv.setGravity(3);
        tv.setText(smb);
    }

    private static String wrap(String s, float width, Paint p) {
        String[] str = s.split("\\s");
        StringBuilder smb = new StringBuilder();
        smb.append(SYSTEM_NEWLINE);
        for (int x = 0; x < str.length; x++) {
            float length = p.measureText(str[x]);
            String[] pieces = smb.toString().split(SYSTEM_NEWLINE);
            try {
                if (p.measureText(pieces[pieces.length - 1]) + length > width) {
                    smb.append(SYSTEM_NEWLINE);
                }
            } catch (Exception e) {
            }
            smb.append(str[x] + " ");
        }
        return smb.toString().replaceFirst(SYSTEM_NEWLINE, "");
    }

    private static String removeLast(String s, String g) {
        if (!s.contains(g)) {
            return s;
        }
        int index = s.lastIndexOf(g);
        int indexEnd = index + g.length();
        if (index == 0) {
            return s.substring(1);
        }
        if (index == s.length() - 1) {
            return s.substring(0, index);
        }
        return s.substring(0, index) + s.substring(indexEnd);
    }

    private static String justifyOperation(String s, float width, Paint p) {
        float holder;
        double random = Math.random();
        while (true) {
            holder = (float) (random * 5.119999885559082d);
            if (!s.contains(Float.toString(holder))) {
                break;
            }
            random = Math.random();
        }
        String holder_string = Float.toString(holder);
        float lessThan = width;
        int current = 0;
        while (p.measureText(s) < lessThan && current < 100) {
            s = s.replaceFirst(" ([^" + holder_string + "])", " " + holder_string + "$1");
            lessThan = (p.measureText(holder_string) + lessThan) - p.measureText(" ");
            current++;
        }
        return s.replaceAll(holder_string, " ");
    }

    private static String justify(String s, float width, Paint p) {
        while (p.measureText(s) < width) {
            s = justifyOperation(s, width, p);
        }
        return s;
    }
}
