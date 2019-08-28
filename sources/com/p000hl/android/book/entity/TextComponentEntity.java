package com.p000hl.android.book.entity;

import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.TextComponentEntity */
public class TextComponentEntity extends ComponentEntity {
    private String bgalpha;
    private String bgcolor;
    private String borderColor;
    private String borderVisible;
    private String defaultTextLayoutFormat;
    private String fontFamily;
    private String fontSize;
    private String fontStyle;
    private String fontWeight;
    private String htmlXML;
    private ArrayList<TextComponentLineEntity> lineEntitys = new ArrayList<>();
    private String lineHeight;
    private String textAlign;
    private String textColor;
    private String textContent;
    private String textDecoration;
    private String totalParaTextContent;
    private String trackingLeft;
    private String trackingRight;

    public TextComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }

    public ArrayList<TextComponentLineEntity> getLineEntitys() {
        return this.lineEntitys;
    }

    public void setLineEntitys(ArrayList<TextComponentLineEntity> lineEntitys2) {
        this.lineEntitys = lineEntitys2;
    }

    public String getHtmlXML() {
        return this.htmlXML;
    }

    public void setHtmlXML(String htmlXML2) {
        this.htmlXML = htmlXML2;
    }

    public String getBgcolor() {
        return this.bgcolor;
    }

    public void setBgcolor(String bgcolor2) {
        this.bgcolor = bgcolor2;
    }

    public String getBgalpha() {
        return this.bgalpha;
    }

    public void setBgalpha(String bgalphs) {
        this.bgalpha = bgalphs;
    }

    public String getBorderVisible() {
        return this.borderVisible;
    }

    public void setBorderVisible(String borderVisible2) {
        this.borderVisible = borderVisible2;
    }

    public String getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(String borderColor2) {
        this.borderColor = borderColor2;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public void setTextContent(String textContext) {
        this.textContent = textContext;
    }

    public String getDefaultTextLayoutFormat() {
        return this.defaultTextLayoutFormat;
    }

    public void setDefaultTextLayoutFormat(String defaultTextLayoutFormat2) {
        this.defaultTextLayoutFormat = defaultTextLayoutFormat2;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public void setFontFamily(String fontFamily2) {
        this.fontFamily = fontFamily2;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(String fontSize2) {
        this.fontSize = fontSize2;
    }

    public String getFontWeight() {
        return this.fontWeight;
    }

    public void setFontWeight(String fontWeight2) {
        this.fontWeight = fontWeight2;
    }

    public String getFontStyle() {
        return this.fontStyle;
    }

    public void setFontStyle(String fontStyle2) {
        this.fontStyle = fontStyle2;
    }

    public String getTextDecoration() {
        return this.textDecoration;
    }

    public void setTextDecoration(String textDecoration2) {
        this.textDecoration = textDecoration2;
    }

    public String getTextAlign() {
        return this.textAlign;
    }

    public void setTextAlign(String textAlign2) {
        this.textAlign = textAlign2;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public void setTextColor(String textColor2) {
        this.textColor = textColor2;
    }

    public String getTotalParaTextContent() {
        return this.totalParaTextContent;
    }

    public void setTotalParaTextContent(String totalParaTextContent2) {
        this.totalParaTextContent = totalParaTextContent2;
    }

    public String getLineHeight() {
        return this.lineHeight;
    }

    public void setLineHeight(String lineHeight2) {
        this.lineHeight = lineHeight2;
    }

    public String getTrackingLeft() {
        return this.trackingLeft;
    }

    public void setTrackingLeft(String trackingLeft2) {
        this.trackingLeft = trackingLeft2;
    }

    public String getTrackingRight() {
        return this.trackingRight;
    }

    public void setTrackingRight(String trackingRight2) {
        this.trackingRight = trackingRight2;
    }
}
