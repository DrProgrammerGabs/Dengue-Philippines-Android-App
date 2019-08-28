package com.p000hl.android.book.entity.moudle;

import com.p000hl.android.book.entity.ComponentEntity;
import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.moudle.MoudleComponentEntity */
public class MoudleComponentEntity extends ComponentEntity {
    private String bgSourceID;
    private int bookHeight;
    private int bookWidth;
    public ArrayList<Cell> cellList;
    private int cellNumber;
    private ArrayList<String> downIDList;
    public boolean isAutoRotation;
    public boolean isHorSlider;
    public boolean isShowControllerPoint;
    public boolean isShowNavi;
    private int itemHeight;
    private int itemWidth;
    public ArrayList<MRenderBean> leftRenderBean;
    public float lineAlpha;
    public int lineColor;
    public int lineThick;
    public int mLineGap;
    public int mRowOrColumnGap;
    public ArrayList<MaskBean> maskBeanList;
    public ArrayList<MRenderBean> middleRenderBean;
    private String moduleID;
    public ArrayList<QuestionEntity> questionList;
    public ArrayList<String> renderDes;
    public ArrayList<MRenderBean> rightRenderBean;
    public String rotationType;
    private ArrayList<String> selectSourceIDList;
    private String serverAddress;
    private ArrayList<String> sourceIDList;
    public int speed;
    private long timerDelay;

    public MoudleComponentEntity(ComponentEntity component) {
        this.leftRenderBean = new ArrayList<>();
        this.middleRenderBean = new ArrayList<>();
        this.rightRenderBean = new ArrayList<>();
        this.maskBeanList = new ArrayList<>();
        this.isShowNavi = true;
        this.lineColor = -16777216;
        this.lineThick = 1;
        this.lineAlpha = 1.0f;
        this.isHorSlider = true;
        this.renderDes = new ArrayList<>();
        this.cellNumber = 1;
        this.isShowControllerPoint = false;
        this.speed = 5;
        this.isAutoRotation = true;
        this.rotationType = "anticlosewise";
        this.questionList = new ArrayList<>();
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }

    public int getCellNumber() {
        return this.cellNumber;
    }

    public void setCellNumber(int cellNumber2) {
        this.cellNumber = cellNumber2;
    }

    public String getModuleID() {
        return this.moduleID;
    }

    public void setModuleID(String moduleID2) {
        this.moduleID = moduleID2;
    }

    public ArrayList<String> getSourceIDList() {
        if (this.sourceIDList == null) {
            this.sourceIDList = new ArrayList<>();
        }
        return this.sourceIDList;
    }

    public void setSourceIDList(ArrayList<String> sourceIDList2) {
        this.sourceIDList = sourceIDList2;
    }

    public ArrayList<String> getSelectSourceIDList() {
        if (this.selectSourceIDList == null) {
            this.selectSourceIDList = new ArrayList<>();
        }
        return this.selectSourceIDList;
    }

    public void setSelectSourceIDList(ArrayList<String> selectSourceIDList2) {
        this.selectSourceIDList = selectSourceIDList2;
    }

    public int getItemWidth() {
        return this.itemWidth;
    }

    public void setItemWidth(int itemWidth2) {
        this.itemWidth = itemWidth2;
    }

    public int getItemHeight() {
        return this.itemHeight;
    }

    public void setItemHeight(int itemHeight2) {
        this.itemHeight = itemHeight2;
    }

    public long getTimerDelay() {
        return this.timerDelay;
    }

    public void setTimerDelay(long timerDelay2) {
        this.timerDelay = timerDelay2;
    }

    public ArrayList<String> getDownIDList() {
        if (this.downIDList == null) {
            this.downIDList = new ArrayList<>();
        }
        return this.downIDList;
    }

    public void setDownIDList(ArrayList<String> downIDList2) {
        this.downIDList = downIDList2;
    }

    public int getBookWidth() {
        return this.bookWidth;
    }

    public void setBookWidth(int bookWidth2) {
        this.bookWidth = bookWidth2;
    }

    public int getBookHeight() {
        return this.bookHeight;
    }

    public void setBookHeight(int bookHeight2) {
        this.bookHeight = bookHeight2;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setServerAddress(String serverAddress2) {
        this.serverAddress = serverAddress2;
    }

    public String getBgSourceID() {
        return this.bgSourceID;
    }

    public void setBgSourceID(String bgSourceID2) {
        this.bgSourceID = bgSourceID2;
    }

    public MoudleComponentEntity() {
        this.leftRenderBean = new ArrayList<>();
        this.middleRenderBean = new ArrayList<>();
        this.rightRenderBean = new ArrayList<>();
        this.maskBeanList = new ArrayList<>();
        this.isShowNavi = true;
        this.lineColor = -16777216;
        this.lineThick = 1;
        this.lineAlpha = 1.0f;
        this.isHorSlider = true;
        this.renderDes = new ArrayList<>();
        this.cellNumber = 1;
        this.isShowControllerPoint = false;
        this.speed = 5;
        this.isAutoRotation = true;
        this.rotationType = "anticlosewise";
        this.questionList = new ArrayList<>();
        this.cellList = new ArrayList<>();
    }
}
