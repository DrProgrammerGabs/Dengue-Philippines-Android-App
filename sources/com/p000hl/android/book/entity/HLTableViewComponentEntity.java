package com.p000hl.android.book.entity;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.hl.android.book.entity.HLTableViewComponentEntity */
public class HLTableViewComponentEntity extends ComponentEntity {
    public CellModel cellModel = new CellModel();
    public CellSize cellSize = new CellSize();
    public RequestEntity req = new RequestEntity();

    /* renamed from: com.hl.android.book.entity.HLTableViewComponentEntity$CellModel */
    public class CellModel {
        public String ClikcOpenKey;
        public String Height;

        /* renamed from: ID */
        public String f12ID;
        public String IsClickOpenBrowser;
        public String ModelParent;
        public String Rotation;
        public String Width;

        /* renamed from: X */
        public String f13X;

        /* renamed from: Y */
        public String f14Y;
        public ArrayList<HashMap<String, String>> commponentList = new ArrayList<>();
        public ArrayList<HashMap<String, String>> modelList = new ArrayList<>();

        public CellModel() {
        }
    }

    /* renamed from: com.hl.android.book.entity.HLTableViewComponentEntity$CellSize */
    public class CellSize {
        public String BackgroundColor;
        public String BottomOff;
        public String IsClickOpenBrowser;
        public String LeftOff;
        public String RightOff;
        public String cellHeight;
        public String cellWidth;
        public String horGap;
        public String topOff;
        public String verGap;

        public CellSize() {
        }
    }

    /* renamed from: com.hl.android.book.entity.HLTableViewComponentEntity$RequestEntity */
    public class RequestEntity {
        public HashMap<String, String> param = new HashMap<>();
        public String requestHeader;
        public String requestMethod;
        public String requestURL;

        public RequestEntity() {
        }
    }

    public HLTableViewComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }
}
