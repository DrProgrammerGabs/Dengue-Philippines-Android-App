package com.p000hl.android.view.component.bookmark;

import com.p000hl.android.HLLayoutActivity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.DataUtils;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.bookmark.BookMarkManager */
public class BookMarkManager {
    private static final String MARKLISTKEY = "com.hl.appbook.marklist";
    private static ArrayList<String> markList;

    public static ArrayList<String> getMarkList(HLLayoutActivity activity) {
        markList = (ArrayList) DataUtils.getSerializable(activity, MARKLISTKEY + BookController.getInstance().getBook().getBookInfo().getId());
        if (markList == null) {
            markList = new ArrayList<>();
        }
        return markList;
    }

    public static void deleteMark(HLLayoutActivity activity, int pos) {
        markList = getMarkList(activity);
        markList.remove(pos);
        DataUtils.saveSerializable(activity, MARKLISTKEY + BookController.getInstance().getBook().getBookInfo().getId(), markList);
        activity.refreshMark();
    }

    public static void addCurPage(HLLayoutActivity activity) {
        String markKey = MARKLISTKEY + BookController.getInstance().getBook().getBookInfo().getId();
        String curPageId = BookController.getInstance().getViewPage().getEntity().getID();
        if (!markList.contains(curPageId)) {
            markList.add(curPageId);
            DataUtils.saveSerializable(activity, markKey, markList);
            activity.refreshMark();
        }
    }
}
