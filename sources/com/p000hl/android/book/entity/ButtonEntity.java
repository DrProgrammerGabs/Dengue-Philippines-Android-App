package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.ButtonEntity */
public class ButtonEntity {
    public static String HOME_PAGE_BTN = "home_page_btn";
    public static String NEXT_PAGE_BTN = "next_page_btn";
    public static String OPEN_NAVIGATE_BTN = "open_navigate_btn";
    public static String PRE_PAGE_BTN = "pre_page_btn";
    public static String VER_HOME_PAGE_BTN = "ver_home_page_btn";
    public static String VER_NEXT_PAGE_BTN = "ver_next_page_btn";
    public static String VER_OPEN_NAVIGATE_BTN = "ver_open_navigate_btn";
    public static String VER_PRE_PAGE_BTN = "ver_pre_page_btn";
    private int height;
    private boolean isVisible;
    private String selectedSource;
    private String source;
    private String type;
    private int width;

    /* renamed from: x */
    private float f5x;

    /* renamed from: y */
    private float f6y;

    public float getX() {
        return this.f5x;
    }

    public void setX(float x) {
        this.f5x = x;
    }

    public float getY() {
        return this.f6y;
    }

    public void setY(float y) {
        this.f6y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean isVisible2) {
        this.isVisible = isVisible2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getSelectedSource() {
        return this.selectedSource;
    }

    public void setSelectedSource(String selectedSource2) {
        this.selectedSource = selectedSource2;
    }
}
