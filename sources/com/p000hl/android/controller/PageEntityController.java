package com.p000hl.android.controller;

import android.content.Context;
import com.p000hl.android.book.BookDecoder;
import com.p000hl.android.book.entity.PageEntity;
import java.util.HashMap;

/* renamed from: com.hl.android.controller.PageEntityController */
public class PageEntityController {
    private static PageEntityController _instance;
    private HashMap<String, PageEntity> pageEntityMap = new HashMap<>();

    public static PageEntityController getInstance() {
        if (_instance == null) {
            _instance = new PageEntityController();
        }
        return _instance;
    }

    public void clear() {
        this.pageEntityMap.clear();
    }

    public void recyle() {
        this.pageEntityMap.clear();
        _instance = null;
    }

    public PageEntity getPageEntityByPageId(Context context, String pageID) {
        if (this.pageEntityMap.containsKey(pageID)) {
            return (PageEntity) this.pageEntityMap.get(pageID);
        }
        return loadPageEntity(context, pageID);
    }

    private PageEntity loadPageEntity(Context context, String pageId) {
        PageEntity pageEntity = BookDecoder.getInstance().decodePageEntity(context, pageId);
        if (pageEntity == null) {
            return null;
        }
        this.pageEntityMap.put(pageId, pageEntity);
        return pageEntity;
    }
}
