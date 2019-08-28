package org.vudroid.core.acts;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import java.util.Collections;
import java.util.List;

public class UriBrowserAdapter extends BaseAdapter {
    private List<Uri> uris = Collections.emptyList();

    public int getCount() {
        return this.uris.size();
    }

    public Uri getItem(int i) {
        return (Uri) this.uris.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View browserItem = LayoutInflater.from(viewGroup.getContext()).inflate(C0048R.C0050layout.browseritem, viewGroup, false);
        ImageView imageView = (ImageView) browserItem.findViewById(C0048R.C0049id.browserItemIcon);
        ((TextView) browserItem.findViewById(C0048R.C0049id.browserItemText)).setText(((Uri) this.uris.get(i)).getLastPathSegment());
        imageView.setImageResource(C0048R.drawable.book);
        return browserItem;
    }

    public void setUris(List<Uri> uris2) {
        this.uris = uris2;
        notifyDataSetInvalidated();
    }
}
