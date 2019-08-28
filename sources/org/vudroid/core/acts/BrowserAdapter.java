package org.vudroid.core.acts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BrowserAdapter extends BaseAdapter {
    private final Context context;
    private File currentDirectory;
    private List<File> files = Collections.emptyList();
    private final FileFilter filter;

    public BrowserAdapter(Context context2, FileFilter filter2) {
        this.context = context2;
        this.filter = filter2;
    }

    public int getCount() {
        return this.files.size();
    }

    public File getItem(int i) {
        return (File) this.files.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View browserItem = LayoutInflater.from(this.context).inflate(C0048R.C0050layout.browseritem, viewGroup, false);
        ImageView imageView = (ImageView) browserItem.findViewById(C0048R.C0049id.browserItemIcon);
        File file = (File) this.files.get(i);
        TextView textView = (TextView) browserItem.findViewById(C0048R.C0049id.browserItemText);
        textView.setText(file.getName());
        if (file.equals(this.currentDirectory.getParentFile())) {
            imageView.setImageResource(C0048R.drawable.arrowup);
            textView.setText(file.getAbsolutePath());
        } else if (file.isDirectory()) {
            imageView.setImageResource(C0048R.drawable.folderopen);
        } else {
            imageView.setImageResource(C0048R.drawable.book);
        }
        return browserItem;
    }

    public void setCurrentDirectory(File currentDirectory2) {
        File[] fileArray = currentDirectory2.listFiles(this.filter);
        ArrayList<File> files2 = new ArrayList<>(fileArray != null ? Arrays.asList(fileArray) : Collections.emptyList());
        this.currentDirectory = currentDirectory2;
        Collections.sort(files2, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (!o1.isFile() || !o2.isDirectory()) {
                    return o1.getName().compareTo(o2.getName());
                }
                return 1;
            }
        });
        if (currentDirectory2.getParentFile() != null) {
            files2.add(0, currentDirectory2.getParentFile());
        }
        setFiles(files2);
    }

    public void setFiles(List<File> files2) {
        this.files = files2;
        notifyDataSetInvalidated();
    }

    public File getCurrentDirectory() {
        return this.currentDirectory;
    }
}
