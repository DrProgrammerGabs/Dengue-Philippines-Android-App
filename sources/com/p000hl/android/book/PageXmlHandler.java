package com.p000hl.android.book;

import android.graphics.PointF;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ContainerEntity;
import com.p000hl.android.book.entity.CounterEntity;
import com.p000hl.android.book.entity.GifComponentEntity;
import com.p000hl.android.book.entity.GroupEntity;
import com.p000hl.android.book.entity.HLTableViewComponentEntity;
import com.p000hl.android.book.entity.HTMLComponentEntity;
import com.p000hl.android.book.entity.InputTextComponentEntity;
import com.p000hl.android.book.entity.MapComponentEntity;
import com.p000hl.android.book.entity.PDFComponentEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.book.entity.SWFFileEntity;
import com.p000hl.android.book.entity.SeniorAnimationEntity;
import com.p000hl.android.book.entity.SliderEffectComponentEntity;
import com.p000hl.android.book.entity.SubImageItem;
import com.p000hl.android.book.entity.TextComponentEntity;
import com.p000hl.android.book.entity.TextComponentLineEntity;
import com.p000hl.android.book.entity.TimerEntity;
import com.p000hl.android.book.entity.VideoComponentEntity;
import com.p000hl.android.book.entity.YouTubeVideoComponentEntity;
import com.p000hl.android.book.entity.moudle.Cell;
import com.p000hl.android.book.entity.moudle.MRenderBean;
import com.p000hl.android.book.entity.moudle.MaskBean;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.book.entity.moudle.OptionEntity;
import com.p000hl.android.book.entity.moudle.QuestionEntity;
import com.p000hl.android.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* renamed from: com.hl.android.book.PageXmlHandler */
public class PageXmlHandler implements ContentHandler {
    private BehaviorEntity Behavior = new BehaviorEntity();
    private AnimationEntity animation = new AnimationEntity();
    private Cell cell = null;
    private ContainerEntity container = new ContainerEntity();
    private MRenderBean curRenderBean;
    HashMap<String, String> curTabCommponent = new HashMap<>();
    HashMap<String, String> curTabMode = new HashMap<>();
    private boolean flg = true;
    private GroupEntity group = new GroupEntity();
    private boolean isAnimation = false;
    private boolean isAnimationModel = false;
    private boolean isBackground = false;
    private boolean isBehavior = false;
    private boolean isComponent = false;
    private boolean isContainerPosition = false;
    private boolean isContainers = false;
    private boolean isCounter = false;
    private boolean isGifComponent = false;
    private boolean isHorConnectLine = false;
    private boolean isInput = false;
    private boolean isLeftMRender = false;
    private boolean isLinkPage = false;
    private boolean isMap = false;
    private boolean isMaskSlider = false;
    private boolean isMiddleMRender = false;
    private boolean isMoudleComponent = false;
    private boolean isPDFComponent = false;
    private boolean isPaintUIComponent;
    private boolean isPlaySequence = false;
    private boolean isPlaySequenceDelay = false;
    private boolean isPoint = false;
    private boolean isQuestion = false;
    private boolean isRightRender = false;
    private boolean isSenior = false;
    private boolean isSliderEffectComponent = false;
    private boolean isSwfFile = false;
    private boolean isTable = false;
    private boolean isText = false;
    private boolean isTimer = false;
    private boolean isVerConnectLine = false;
    private boolean isYoutoBe = false;
    private boolean ispage = true;
    TextComponentLineEntity lineEntity = null;
    MaskBean maskBean = null;

    /* renamed from: p */
    private PointF f2p = new PointF();
    PageEntity page;
    QuestionEntity questionEntity = null;
    private SeniorAnimationEntity seniorAnimationEntity = null;
    private PointF stroyTellPt;
    private SubImageItem subImageItem = null;
    String tabParamKey = "";
    private String tagName = null;
    private String val = "";

    public PageXmlHandler(PageEntity page2) {
        this.page = page2;
    }

    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        this.val += new String(arg0, arg1, arg2);
    }

    public void endDocument() throws SAXException {
    }

    /* JADX WARNING: type inference failed for: r11v0, types: [com.hl.android.book.PageXmlHandler] */
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        boolean equals;
        this.val = this.val.trim();
        if (arg1.equals("PlaySequenceDelay")) {
            this.isPlaySequenceDelay = false;
        }
        if (this.flg) {
            if (this.ispage) {
                if (this.tagName.equals("ID")) {
                    this.page.setID(this.val);
                } else if (this.tagName.equals("Width")) {
                    this.page.setWidth(Float.parseFloat(this.val));
                } else if (this.tagName.equals("Height")) {
                    this.page.setHeight(Float.parseFloat(this.val));
                } else if (this.tagName.equals("Title")) {
                    this.page.setTitle(this.val);
                } else if (this.tagName.equals("IsGroupPlay")) {
                    this.page.IsGroupPlay = Boolean.parseBoolean(this.val);
                } else if (this.tagName.equals("Description")) {
                    this.page.setDescription(this.val);
                } else if (this.tagName.equals("Type")) {
                    this.page.setType(this.val);
                } else if (this.tagName.equals("LinkPageID")) {
                    this.page.setLinkPageID(this.val);
                } else if (this.tagName.equals("BeCoveredPageID")) {
                    this.page.beCoveredPageID = this.val;
                } else if (this.tagName.equals("EnableNavigation")) {
                    this.page.setEnableNavigation(Boolean.valueOf(this.val).booleanValue());
                } else if (this.tagName.toLowerCase(Locale.ENGLISH).equals("iscashsnapshot")) {
                    this.page.setSnapShotType(Boolean.parseBoolean(this.val));
                } else if (this.tagName.toLowerCase(Locale.ENGLISH).equals("snapid")) {
                    this.page.setSnapShotID(this.val);
                } else if (this.tagName.equals("EnablePageTurnByHand")) {
                    this.page.enablePageTurnByHand = Boolean.valueOf(this.val).booleanValue();
                } else if (this.tagName.equals("PageChangeEffectType")) {
                    this.page.setPageChangeEffectType(this.val);
                } else if (this.tagName.equals("PageChangeEffectDir")) {
                    this.page.setPageChangeEffectDir(this.val);
                } else if (this.tagName.equals("PageChangeEffectDuration")) {
                    this.page.setPageChangeEffectDuration(Long.valueOf(this.val).longValue());
                }
            }
            if (this.isContainers) {
                if (this.isSliderEffectComponent) {
                    if (this.tagName.equals("IsUseSlide")) {
                        ((SliderEffectComponentEntity) this.container.component).isUseSlide = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("IsPageTweenSlide")) {
                        ((SliderEffectComponentEntity) this.container.component).isPageTweenSlide = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("IsPageInnerSlide")) {
                        ((SliderEffectComponentEntity) this.container.component).isPageInnerSlide = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("SlideBindingX")) {
                        ((SliderEffectComponentEntity) this.container.component).slideBindingX = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("SlideBindingY")) {
                        ((SliderEffectComponentEntity) this.container.component).slideBindingY = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("SlideBindingWidth")) {
                        ((SliderEffectComponentEntity) this.container.component).slideBindingWidth = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("SlideBindingHeight")) {
                        ((SliderEffectComponentEntity) this.container.component).slideBindingHeight = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("SlideBindingAlpha")) {
                        ((SliderEffectComponentEntity) this.container.component).slideBindingAlpha = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("SwitchType")) {
                        ((SliderEffectComponentEntity) this.container.component).switchType = this.val;
                    } else if (this.tagName.equals("Repeat")) {
                        ((SliderEffectComponentEntity) this.container.component).repeat = Integer.parseInt(this.val);
                    } else if (this.tagName.equals("IsLoop")) {
                        ((SliderEffectComponentEntity) this.container.component).isLoop = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("IsEndToStart")) {
                        ((SliderEffectComponentEntity) this.container.component).isEndToStart = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("AniType")) {
                        if ("suckEffect".equals(this.val) || "rippleEffect".equals(this.val)) {
                            this.val = "transitionFade";
                        }
                        this.subImageItem.aniType = this.val;
                    } else if (this.tagName.equals("AniProperty")) {
                        this.subImageItem.aniProperty = this.val;
                    } else if (this.tagName.equals("Delay")) {
                        this.subImageItem.delay = Long.parseLong(this.val);
                    } else if (this.tagName.equals("Duration")) {
                        this.subImageItem.duration = Long.parseLong(this.val);
                    } else if (this.tagName.equals("SourceID")) {
                        this.subImageItem.sourceID = this.val;
                        ((SliderEffectComponentEntity) this.container.component).subItems.add(this.subImageItem);
                    }
                }
                if (this.isLinkPage) {
                    if (this.tagName.equals("LinkID")) {
                        this.container.component.getLinkPageObj().entityID = this.val;
                    } else if (this.tagName.equals("Rate")) {
                        this.container.component.getLinkPageObj().rate = Float.parseFloat(this.val);
                        this.isLinkPage = false;
                    }
                } else if (arg1.equals("ID") && !this.isComponent) {
                    this.container.f9ID = this.val;
                } else if (this.tagName.equals("Alpha") && !this.isComponent) {
                    this.container.component.alpha = Float.parseFloat(this.val);
                } else if (this.tagName.equals("AnimationRepeat") && !this.isComponent) {
                    this.container.component.animationRepeat = Integer.parseInt(this.val);
                } else if (this.tagName.equals("IsAllowUserZoom") || this.tagName.equals("IsAllowZoom")) {
                    this.container.component.isAllowUserZoom = Boolean.parseBoolean(this.val);
                } else if (this.tagName.equals("ClassName")) {
                    if (this.val.indexOf("HLGIFComponent") > 0) {
                        this.container.setComponent(new GifComponentEntity(this.container.component));
                        this.isGifComponent = true;
                    } else if (this.val.indexOf("HLSWFComponent") > 0) {
                        this.container.setComponent(new GifComponentEntity(this.container.component));
                        this.isGifComponent = true;
                    } else if (this.val.indexOf("HLRollingTextComponent") > 0 || this.val.indexOf("hlEnglishRollingText") > 0) {
                        this.container.setComponent(new TextComponentEntity(this.container.component));
                        this.isText = true;
                    } else if (this.val.indexOf("HLTextInputComponent") > 0) {
                        this.container.setComponent(new InputTextComponentEntity(this.container.component));
                        this.isInput = true;
                    } else if (this.val.indexOf("HLTableViewComponent") > 0) {
                        this.container.setComponent(new HLTableViewComponentEntity(this.container.component));
                        this.isTable = true;
                    } else if (this.val.indexOf("HLGoogleMapComponent") > 0) {
                        this.container.setComponent(new MapComponentEntity(this.container.component));
                        this.isMap = true;
                    } else if (this.val.indexOf("HLYouTubeVideoComponent") > 0) {
                        this.container.setComponent(new YouTubeVideoComponentEntity(this.container.component));
                        this.isYoutoBe = true;
                    } else if (this.val.indexOf("HLLocalPDFComponent") > 0) {
                        this.isPDFComponent = true;
                        this.container.setComponent(new PDFComponentEntity());
                    } else if (this.val.indexOf("HLTemplateComponent") > 0) {
                        this.isMoudleComponent = true;
                        this.container.setComponent(new MoudleComponentEntity(this.container.getComponent()));
                    } else if (this.val.endsWith("HLLocalVideoComponent")) {
                        this.container.setComponent(new VideoComponentEntity());
                    } else if (this.val.endsWith("HLHtml5Component")) {
                        this.container.setComponent(new HTMLComponentEntity());
                    } else if (this.val.indexOf("HLCounterComponent") > 0) {
                        this.container.setComponent(new CounterEntity());
                        this.isCounter = true;
                    } else if (this.val.indexOf("HLTimerComponent") > 0) {
                        this.isTimer = true;
                        this.container.setComponent(new TimerEntity());
                    } else if (this.val.indexOf("HLSWFFileComponent") > 0) {
                        this.container.setComponent(new SWFFileEntity());
                        this.isSwfFile = true;
                    } else if (this.val.indexOf("HLSliderEffectComponent") > 0) {
                        this.container.setComponent(new SliderEffectComponentEntity(this.container.component));
                        this.isSliderEffectComponent = true;
                    }
                    this.container.component.className = this.val;
                } else if (this.isTable) {
                    HLTableViewComponentEntity table = (HLTableViewComponentEntity) this.container.component;
                    if (this.tagName.equals("RequestHeader")) {
                        table.req.requestHeader = this.val;
                    } else if (this.tagName.equals("RequestURL")) {
                        table.req.requestURL = this.val;
                    } else if (this.tagName.equals("RequestMethod")) {
                        table.req.requestMethod = this.val;
                    } else if (this.tagName.equals("ParamName")) {
                        this.tabParamKey = this.val;
                    } else if (this.tagName.equals("ParamValue")) {
                        table.req.param.put(this.tabParamKey, this.val);
                    } else if (this.tagName.equals("CellWidth")) {
                        table.cellSize.cellWidth = this.val;
                    } else if (this.tagName.equals("CellHeight")) {
                        table.cellSize.cellHeight = this.val;
                    } else if (this.tagName.equals("HorGap")) {
                        table.cellSize.horGap = this.val;
                    } else if (this.tagName.equals("VerGap")) {
                        table.cellSize.verGap = this.val;
                    } else if (this.tagName.equals("TopOff")) {
                        table.cellSize.topOff = this.val;
                    } else if (this.tagName.equals("BottomOff")) {
                        table.cellSize.BottomOff = this.val;
                    } else if (this.tagName.equals("BottomOff")) {
                        table.cellSize.RightOff = this.val;
                    } else if (this.tagName.equals("BottomOff")) {
                        table.cellSize.LeftOff = this.val;
                    } else if (this.tagName.equals("BackgroundColor")) {
                        table.cellSize.BackgroundColor = this.val;
                    } else if (this.tagName.equals("BackgroundColor")) {
                        table.cellSize.BackgroundColor = this.val;
                    } else if (this.tagName.equals("ModelParent")) {
                        table.cellModel.ModelParent = this.val;
                    } else if (this.tagName.equals("ClikcOpenKey")) {
                        table.cellModel.ClikcOpenKey = this.val;
                    } else if (this.tagName.equals("IsClickOpenBrowser")) {
                        table.cellModel.IsClickOpenBrowser = this.val;
                    } else if (this.tagName.equals("ID")) {
                        table.cellModel.f12ID = this.val;
                    } else if (this.tagName.equals("ComponentType")) {
                        this.curTabCommponent.put("ComponentType", this.val);
                    } else if (this.tagName.equals("ImageSrc")) {
                        this.curTabCommponent.put("ImageSrc", this.val);
                    } else if (this.tagName.equals("ComponentID")) {
                        this.curTabCommponent.put("ComponentID", this.val);
                    } else if (this.tagName.equals("ComponentName")) {
                        this.curTabCommponent.put("ComponentName", this.val);
                    } else if (this.tagName.equals("text")) {
                        this.curTabCommponent.put("text", this.val);
                    } else if (this.tagName.equals("fontSize")) {
                        this.curTabCommponent.put("fontSize", this.val);
                    } else if (this.tagName.equals("fontColor")) {
                        this.curTabCommponent.put("fontColor", this.val);
                    } else if (this.tagName.equals("fontAlig")) {
                        this.curTabCommponent.put("fontAlig", this.val);
                    } else if (this.tagName.equals("Height")) {
                        this.curTabCommponent.put("Height", this.val);
                    } else if (this.tagName.equals("Width")) {
                        this.curTabCommponent.put("Width", this.val);
                    } else if (this.tagName.equals("Rotation")) {
                        this.curTabCommponent.put("Rotation", this.val);
                    } else if (this.tagName.equals("X")) {
                        this.curTabCommponent.put("X", this.val);
                    } else if (this.tagName.equals("Y")) {
                        this.curTabCommponent.put("Y", this.val);
                    } else if (this.tagName.equals("ModelID")) {
                        this.curTabMode.put("ModelID", this.val);
                    } else if (this.tagName.equals("ModelName")) {
                        this.curTabMode.put("ModelName", this.val);
                    } else if (this.tagName.equals("ModelType")) {
                        this.curTabMode.put("ModelType", this.val);
                    } else if (this.tagName.equals("ModelKey")) {
                        this.curTabMode.put("ModelKey", this.val);
                    } else if (this.tagName.equals("ModelParent")) {
                        this.curTabMode.put("ModelParent", this.val);
                    }
                } else if (!this.tagName.equals("Y")) {
                    if ((!this.isTable) && this.tagName.equals("Name")) {
                        this.container.name = this.val;
                    } else {
                        if ((!this.isTable) && this.tagName.equals("Width")) {
                            this.container.width = Float.parseFloat(this.val);
                        } else {
                            if ((!this.isTable) && this.tagName.equals("Height")) {
                                this.container.height = Float.parseFloat(this.val);
                            } else {
                                if ((!this.isTable) && this.tagName.equals("Rotation")) {
                                    this.container.setRotation(Float.parseFloat(this.val));
                                } else {
                                    if ((!this.isTable) && this.tagName.equals("X")) {
                                        if (this.isContainerPosition) {
                                            this.container.f10x = Float.parseFloat(this.val);
                                        }
                                    } else if (this.tagName.equals("IsPlayAnimationAtBegining")) {
                                        this.container.isPlayAnimationAtBegining = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("IsPlayVideoOrAudioAtBegining")) {
                                        this.container.isPlayVideoOrAudioAtBegining = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("IsHideAtBegining")) {
                                        this.container.isHideAtBegining = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("IsStroyTelling")) {
                                        this.container.IsStroyTelling = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("IsMoveScale")) {
                                        this.container.isMoveScale = Boolean.valueOf(this.val);
                                    } else if (this.tagName.equals("IsPushBack")) {
                                        this.container.isPushBack = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("AutoLoop")) {
                                        this.container.component.autoLoop = Boolean.valueOf(this.val).booleanValue();
                                        this.container.autoLoop = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("Delay")) {
                                        try {
                                            if (this.val != null) {
                                                this.container.component.delay = Double.valueOf(this.val.trim()).doubleValue();
                                            }
                                        } catch (Exception e) {
                                            this.container.component.delay = 0.0d;
                                        }
                                    } else if (this.tagName.equals("MultiMediaUrl")) {
                                        this.container.component.multiMediaUrl = this.val;
                                    } else if (this.tagName.equals("isSynchronized")) {
                                        this.container.component.isSynchronized = Boolean.getBoolean(this.val);
                                    } else if (this.tagName.equals("HtmlUrl")) {
                                        this.container.component.htmlUrl = this.val;
                                    } else if (this.tagName.equals("IsPlayOnetime")) {
                                        ((GifComponentEntity) this.container.component).setIsPlayOnetime(Boolean.valueOf(this.val).booleanValue());
                                    } else if (this.tagName.equals("GifDuration")) {
                                        ((GifComponentEntity) this.container.component).setGifDuration(Double.valueOf(this.val).doubleValue());
                                    } else if (this.tagName.equals("ImageType")) {
                                        this.container.component.imageType = this.val;
                                    } else if (this.tagName.equals("ImageScale")) {
                                        this.container.component.imageScale = Double.parseDouble(this.val);
                                    } else if (this.tagName.equals("ZoomType")) {
                                        this.container.component.zoomType = this.val;
                                    } else if (this.tagName.equals("LocalSourceID") || this.tagName.equals("SourceID")) {
                                        if (this.isGifComponent) {
                                            ((GifComponentEntity) this.container.component).getFrameList().add(this.val);
                                        } else {
                                            this.container.component.localSourceId = this.val;
                                        }
                                    } else if (this.tagName.equals("DownSourceID")) {
                                        this.container.component.downSourceID = this.val;
                                    } else if (this.tagName.equals("minValue")) {
                                        ((CounterEntity) this.container.component).minValue = Integer.valueOf(this.val).intValue();
                                    } else if (this.tagName.equals("maxValue")) {
                                        ((CounterEntity) this.container.component).maxValue = Integer.valueOf(this.val).intValue();
                                    } else if (this.tagName.equals("scope")) {
                                        ((CounterEntity) this.container.component).scope = this.val;
                                    } else if (this.tagName.equals("IsLoop") && this.isSwfFile) {
                                        ((SWFFileEntity) this.container.component).isLoop = Boolean.parseBoolean(this.val);
                                        this.isSwfFile = false;
                                    } else if (this.tagName.equals("IsEnableGyroHor")) {
                                        this.container.component.IsEnableGyroHor = Boolean.valueOf(this.val).booleanValue();
                                    } else if (this.tagName.equals("PtX")) {
                                        this.stroyTellPt = new PointF();
                                        this.stroyTellPt.x = Float.parseFloat(this.val);
                                    } else if (this.tagName.equals("PtY")) {
                                        this.stroyTellPt.y = Float.parseFloat(this.val);
                                        this.container.component.ptList.add(this.stroyTellPt);
                                    } else if (this.tagName.equals("IsPageTweenSlide")) {
                                        this.container.component.isPageTweenSlide = Boolean.parseBoolean(this.val);
                                    } else if (this.tagName.equals("IsPageInnerSlide")) {
                                        this.container.component.isPageInnerSlide = Boolean.parseBoolean(this.val);
                                    } else if (this.tagName.equals("SlideBindingX")) {
                                        this.container.component.slideBindingX = Integer.parseInt(this.val);
                                    } else if (this.tagName.equals("SlideBindingY")) {
                                        this.container.component.slideBindingY = Integer.parseInt(this.val);
                                    } else if (this.tagName.equals("SlideBindingWidth")) {
                                        this.container.component.slideBindingWidth = Integer.parseInt(this.val);
                                    } else if (this.tagName.equals("SlideBindingHeight")) {
                                        this.container.component.slideBindingHeight = Integer.parseInt(this.val);
                                    } else if (this.tagName.equals("SlideBindingAlpha")) {
                                        this.container.component.slideBindingAlha = Float.parseFloat(this.val);
                                    } else if (this.tagName.equals("SliderHorRate")) {
                                        this.container.component.sliderHorRate = Float.parseFloat(this.val);
                                    } else if (this.tagName.equals("SliderVerRate")) {
                                        this.container.component.sliderVerRate = Float.parseFloat(this.val);
                                    }
                                }
                            }
                        }
                    }
                } else if (this.isContainerPosition) {
                    this.container.f11y = Float.parseFloat(this.val);
                    this.isContainerPosition = false;
                }
                if (this.isCounter) {
                    if (this.tagName.equals("fontSize")) {
                        ((CounterEntity) this.container.component).fontSize = this.val;
                    } else if (this.tagName.equals("fontColor")) {
                        ((CounterEntity) this.container.component).fontColor = this.val;
                    }
                } else if (this.isTimer) {
                    if (this.tagName.equals("isPlayOrderbyDesc")) {
                        ((TimerEntity) this.container.component).isPlayOrderbyDesc = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("isPlayMillisecond")) {
                        ((TimerEntity) this.container.component).isPlayMillisecond = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("isStaticType")) {
                        ((TimerEntity) this.container.component).isStaticType = Boolean.parseBoolean(this.val);
                    } else if (this.tagName.equals("maxTimer")) {
                        ((TimerEntity) this.container.component).setMaxTimer(Integer.parseInt(this.val));
                    } else if (this.tagName.equals("fontSize")) {
                        ((TimerEntity) this.container.component).fontSize = this.val;
                    } else if (this.tagName.equals("fontColor")) {
                        ((TimerEntity) this.container.component).fontColor = this.val;
                    }
                } else if (this.isText) {
                    if (this.tagName.equals("htmlXML")) {
                        ((TextComponentEntity) this.container.component).setHtmlXML(this.val);
                    } else if (this.tagName.equals("bgcolor")) {
                        ((TextComponentEntity) this.container.component).setBgcolor(this.val);
                    } else if (this.tagName.equals("bgalhpa")) {
                        ((TextComponentEntity) this.container.component).setBgalpha(this.val);
                    } else if (this.tagName.equals("borderVisible")) {
                        ((TextComponentEntity) this.container.component).setBorderVisible(this.val);
                    } else if (this.tagName.equals("borderColor")) {
                        ((TextComponentEntity) this.container.component).setBorderColor(this.val);
                    } else if (this.tagName.equals("TextContent")) {
                        ((TextComponentEntity) this.container.component).setTextContent(this.val);
                    } else if (this.tagName.equals("LineHeight")) {
                        this.lineEntity.setLineHeight(this.val);
                        ((TextComponentEntity) this.container.component).getLineEntitys().add(this.lineEntity);
                    } else if (this.tagName.equals("color")) {
                        ((TextComponentEntity) this.container.component).setTextColor(this.val);
                    } else if (this.tagName.equals("TotalParaTextContent")) {
                        if (this.val.startsWith("@")) {
                            this.val = this.val.replaceFirst("@", "");
                        }
                        ((TextComponentEntity) this.container.component).setTotalParaTextContent(this.val);
                    } else if (this.tagName.equals("fontSize")) {
                        ((TextComponentEntity) this.container.component).setFontSize(this.val);
                    } else if (this.tagName.equals("fontWeight")) {
                        ((TextComponentEntity) this.container.component).setFontWeight(this.val);
                    } else if (this.tagName.equals("fontStyle")) {
                        ((TextComponentEntity) this.container.component).setFontStyle(this.val);
                    } else if (this.tagName.equals("fontFamily")) {
                        ((TextComponentEntity) this.container.component).setFontFamily(this.val);
                    } else if (this.tagName.equals("lineHeight")) {
                        ((TextComponentEntity) this.container.component).setLineHeight(this.val);
                    } else if (this.tagName.equals("textAlign")) {
                        ((TextComponentEntity) this.container.component).setTextAlign(this.val);
                    }
                } else if (this.isInput) {
                    InputTextComponentEntity inputTextComponentEntity = (InputTextComponentEntity) this.container.component;
                    if (this.tagName.equals("fontColor")) {
                        inputTextComponentEntity.setFontColor(this.val);
                    } else if (this.tagName.equals("fontAlig")) {
                        inputTextComponentEntity.setFontAlig(this.val);
                    } else if (this.tagName.equals("bordVisible")) {
                        inputTextComponentEntity.setBordVisible(this.val);
                    } else if (this.tagName.equals("text")) {
                        inputTextComponentEntity.setText(this.val);
                    } else if (this.tagName.equals("placeHolder")) {
                        inputTextComponentEntity.setPlaceHolder(this.val);
                    } else if (this.tagName.equals("fontSize")) {
                        inputTextComponentEntity.setFontSize(this.val);
                    } else if (this.tagName.equals("Height")) {
                        this.container.height = 30.0f;
                    }
                } else if (this.isMap) {
                    MapComponentEntity mapComponentEntity = (MapComponentEntity) this.container.component;
                    if (this.tagName.equals("PlaceID")) {
                        mapComponentEntity.setPlaceID(this.val);
                    } else if (this.tagName.equals("Lat")) {
                        mapComponentEntity.setLat(this.val);
                    } else if (this.tagName.equals("Lng")) {
                        mapComponentEntity.setLng(this.val);
                    } else if (this.tagName.equals("Address")) {
                        mapComponentEntity.setAddress(this.val);
                    }
                } else if (this.isYoutoBe) {
                    YouTubeVideoComponentEntity mapComponentEntity2 = (YouTubeVideoComponentEntity) this.container.component;
                    if (this.tagName.equals("ShareURL")) {
                        mapComponentEntity2.setShareURL(this.val);
                    } else if (this.tagName.equals("VideoID")) {
                        mapComponentEntity2.setVideoID(this.val);
                    }
                } else if (this.tagName.equals("HtmlFolder")) {
                    ((HTMLComponentEntity) this.container.component).setHtmlFolder(this.val);
                } else if (this.tagName.equals("IndexHtml")) {
                    ((HTMLComponentEntity) this.container.component).setIndexHtml(this.val);
                } else if (this.isPDFComponent) {
                    if (this.tagName.equals("PDFSourceID")) {
                        ((PDFComponentEntity) this.container.component).setPdfSourceID(this.val);
                    } else if (this.tagName.equals("PDFPageIndex")) {
                        ((PDFComponentEntity) this.container.component).setPdfPageIndex(this.val);
                    } else if (this.tagName.equals("IntailWidth")) {
                        ((PDFComponentEntity) this.container.component).setIntailWidth(this.val);
                    } else if (this.tagName.equals("IntailHeight")) {
                        ((PDFComponentEntity) this.container.component).setIntailHeight(this.val);
                    } else if (this.tagName.equals("IsAllowUserZoom")) {
                        ((PDFComponentEntity) this.container.component).setIsAllowUserZoom(this.val);
                    }
                }
                if (this.tagName.equals("VideoControlBarIsShow")) {
                    if (this.container.component instanceof VideoComponentEntity) {
                        ((VideoComponentEntity) this.container.component).setVideoControlBarIsShow(Boolean.parseBoolean(this.val));
                    }
                    this.container.component.showProgress = Boolean.parseBoolean(this.val);
                }
                if (this.tagName.equals("CoverSourceID") && (this.container.component instanceof VideoComponentEntity)) {
                    ((VideoComponentEntity) this.container.component).setCoverSourceID(this.val);
                }
                if (this.tagName.equals("IsOnlineSource")) {
                    this.container.component.setOnlineSource(Boolean.parseBoolean(this.val));
                }
                if (this.isMoudleComponent) {
                    MoudleComponentEntity componentEntity = (MoudleComponentEntity) this.container.component;
                    if (this.isPaintUIComponent && "lineThickness".equals(arg1)) {
                        componentEntity.lineThick = Integer.parseInt(this.val);
                    }
                    if (this.isVerConnectLine) {
                        if (componentEntity.cellList == null) {
                            componentEntity.cellList = new ArrayList<>();
                        }
                        if ("CellID".equals(arg1)) {
                            this.cell.mCellID = this.val;
                        } else if ("SourceID".equals(arg1) || "SourceID".equals(arg1)) {
                            this.cell.mSourceID = this.val;
                        } else if ("LinkID".equals(arg1)) {
                            this.cell.mLinkID = this.val;
                        } else if ("CellType".equals(arg1)) {
                            componentEntity.cellList.add(this.cell);
                        } else if ("LineGap".equals(arg1)) {
                            componentEntity.mLineGap = Integer.parseInt(this.val);
                        } else if ("RowGap".equals(arg1)) {
                            componentEntity.mRowOrColumnGap = Integer.parseInt(this.val);
                        } else if ("LineColor".equals(arg1)) {
                            componentEntity.lineColor = Integer.parseInt(this.val.substring(2), 16);
                        } else if ("LineThickness".equals(arg1)) {
                            componentEntity.lineThick = Integer.parseInt(this.val);
                        } else if ("LineAlpha".equals(arg1)) {
                            componentEntity.lineAlpha = Float.parseFloat(this.val);
                        }
                    } else if (this.isHorConnectLine) {
                        if (componentEntity.cellList == null) {
                            componentEntity.cellList = new ArrayList<>();
                        }
                        if ("CellID".equals(arg1)) {
                            this.cell.mCellID = this.val;
                        } else if ("SourceID".equals(arg1) || "SourceID".equals(arg1)) {
                            this.cell.mSourceID = this.val;
                        } else if ("LinkID".equals(arg1)) {
                            this.cell.mLinkID = this.val;
                        } else if ("CellType".equals(arg1)) {
                            componentEntity.cellList.add(this.cell);
                        } else if ("LineGap".equals(arg1)) {
                            componentEntity.mLineGap = Integer.parseInt(this.val);
                        } else if ("RowGap".equals(arg1)) {
                            componentEntity.mRowOrColumnGap = Integer.parseInt(this.val);
                        } else if ("LineColor".equals(arg1)) {
                            componentEntity.lineColor = Integer.parseInt(this.val.substring(2), 16);
                        } else if ("LineThickness".equals(arg1)) {
                            componentEntity.lineThick = Integer.parseInt(this.val);
                        } else if ("LineAlpha".equals(arg1)) {
                            componentEntity.lineAlpha = Float.parseFloat(this.val);
                        }
                    } else if (this.isQuestion) {
                        if ("SourceID".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.titleResource = this.val;
                        } else if ("Type".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.questionType = this.val;
                        } else if ("Score".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.score = Integer.parseInt(this.val);
                        } else if ("ImageSource".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.imgSource = this.val;
                        } else if ("SoundSource".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.soundSource = this.val;
                        } else if ("Title".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.getOptionList().add(new OptionEntity(this.val));
                        } else if ("Index".equalsIgnoreCase(this.tagName)) {
                            this.questionEntity.getRightAnswerList().add(Integer.valueOf(Integer.parseInt(this.val)));
                        }
                    } else if (this.isMaskSlider) {
                        if ("ID".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.imgSource = this.val;
                        } else if ("SourceW".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.sourceW = Float.parseFloat(this.val);
                        } else if ("SourceH".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.sourceH = Float.parseFloat(this.val);
                        } else if ("RectX".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.rectX = Float.parseFloat(this.val);
                        } else if ("RectY".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.rectY = Float.parseFloat(this.val);
                        } else if ("RectW".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.rectW = Float.parseFloat(this.val);
                        } else if ("RectH".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.rectH = Float.parseFloat(this.val);
                        } else if ("Title".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.title = this.val;
                        } else if ("Dec".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.dec = this.val;
                        } else if ("IsCenterZoom".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.isCenterZoom = Boolean.parseBoolean(this.val);
                        } else if ("AudioSourceID".equalsIgnoreCase(this.tagName)) {
                            this.maskBean.audioSourceID = this.val;
                            componentEntity.maskBeanList.add(this.maskBean);
                        } else if ("IsShowControllerPoint".equalsIgnoreCase(this.tagName)) {
                            componentEntity.isShowControllerPoint = Boolean.parseBoolean(this.val);
                        }
                    } else if ("ModuleID".equals(this.tagName)) {
                        componentEntity.setModuleID(this.val);
                        this.isPaintUIComponent = this.val.contains("HLPaintingUIComponent");
                        this.isVerConnectLine = this.val.contains("HLConnectLineUIComponent");
                        this.isHorConnectLine = this.val.contains("HLConnectHorLineUIComponent");
                        this.isMaskSlider = this.val.contains("HLMaskSliderImageUIComponent");
                    } else if ("sourceID".equals(this.tagName) || "SourceID".equals(this.tagName)) {
                        componentEntity.getSourceIDList().add(this.val);
                    } else if ("SelectedSourceID".equals(this.tagName)) {
                        componentEntity.getSelectSourceIDList().add(this.val);
                    } else if ("mouseUpSourceID".equals(this.tagName)) {
                        componentEntity.getSourceIDList().add(this.val);
                    } else if ("mouseDownSourceID".equals(this.tagName)) {
                        componentEntity.getDownIDList().add(this.val);
                    } else if ("itemWidth".equals(this.tagName) || "ItemWidth".equals(this.tagName)) {
                        componentEntity.setItemWidth(Double.valueOf(Double.parseDouble(this.val)).intValue());
                    } else if ("itemHeight".equals(this.tagName) || "ItemHeight".equals(this.tagName)) {
                        componentEntity.setItemHeight(Double.valueOf(this.val).intValue());
                    } else if ("TimerDelay".equals(this.tagName)) {
                        componentEntity.setTimerDelay(Long.valueOf(this.val).longValue());
                    } else if ("CellNumber".equals(this.tagName)) {
                        componentEntity.setCellNumber(Integer.valueOf(this.val).intValue());
                    } else if ("BookWidth".equalsIgnoreCase(this.tagName)) {
                        componentEntity.setBookWidth(Integer.valueOf(this.val).intValue());
                    } else if ("BookHeight".equalsIgnoreCase(this.tagName)) {
                        componentEntity.setBookHeight(Integer.valueOf(this.val).intValue());
                    } else if ("ServerAddress".equalsIgnoreCase(this.tagName)) {
                        componentEntity.setServerAddress(this.val);
                    } else if ("BgSourceID".equalsIgnoreCase(this.tagName)) {
                        componentEntity.setBgSourceID(this.val);
                    } else if ("Speed".equalsIgnoreCase(this.tagName)) {
                        componentEntity.speed = Integer.parseInt(this.val);
                    } else if ("IsAutoRotation".equalsIgnoreCase(this.tagName)) {
                        componentEntity.isAutoRotation = Boolean.parseBoolean(this.val);
                    } else if ("RotationType".equalsIgnoreCase(this.tagName)) {
                        componentEntity.rotationType = this.val;
                    } else if ("IsShowNavi".equalsIgnoreCase(this.tagName)) {
                        componentEntity.isShowNavi = Boolean.parseBoolean(this.val);
                    } else if ("RenderDes".equals(this.tagName)) {
                        componentEntity.renderDes.add(this.val);
                    } else if ("IsHorSlider".equalsIgnoreCase(this.tagName)) {
                        componentEntity.isHorSlider = Boolean.parseBoolean(this.val);
                    }
                    if ("SourceID".equals(this.tagName)) {
                        if (this.isLeftMRender || this.isRightRender || this.isMiddleMRender) {
                            this.curRenderBean.sourceID = this.val;
                        }
                    } else if ("SourceIndex".equals(this.tagName)) {
                        this.curRenderBean.sourceIndex = Integer.parseInt(this.val);
                        if (this.isLeftMRender) {
                            componentEntity.leftRenderBean.add(this.curRenderBean);
                        } else if (this.isMiddleMRender) {
                            componentEntity.middleRenderBean.add(this.curRenderBean);
                        } else if (this.isRightRender) {
                            componentEntity.rightRenderBean.add(this.curRenderBean);
                        }
                    }
                }
            }
            if (this.isBehavior) {
                if (this.tagName.equals("EventName")) {
                    this.Behavior.EventName = this.val;
                } else if (this.tagName.equals("FunctionObjectID")) {
                    this.Behavior.FunctionObjectID = this.val;
                } else if (this.tagName.equals("FunctionName")) {
                    this.Behavior.FunctionName = this.val;
                } else if (this.tagName.equals("IsRepeat")) {
                    this.Behavior.IsRepeat = Boolean.valueOf(this.val).booleanValue();
                } else if (this.tagName.equals("Value")) {
                    this.Behavior.Value = this.val;
                } else if (this.tagName.equals("EventValue")) {
                    this.Behavior.EventValue = this.val;
                }
            }
            if (this.isAnimation) {
                if (!this.isSenior) {
                    if (this.tagName.equals("ClassName")) {
                        this.animation.ClassName = this.val;
                        if (this.animation.ClassName == null || this.animation.ClassName.indexOf("::AnimationSenior") <= -1) {
                            this.isSenior = false;
                        } else {
                            this.isSenior = true;
                            this.animation.hEntitys = new ArrayList<>();
                        }
                    }
                } else if (this.isAnimationModel) {
                    if (this.tagName.equals("ObjX")) {
                        this.seniorAnimationEntity.f17mX = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("ObjY")) {
                        this.seniorAnimationEntity.f18mY = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("ObjWidth")) {
                        this.seniorAnimationEntity.mWidth = Float.valueOf(this.val).floatValue();
                    } else if (this.tagName.equals("ObjHeight")) {
                        this.seniorAnimationEntity.mHeight = Float.valueOf(this.val).floatValue();
                    } else if (this.tagName.equals("ObjRotation")) {
                        this.seniorAnimationEntity.mDegree = Float.valueOf(this.val).floatValue();
                    } else if (this.tagName.equals("ObjAlpha")) {
                        this.seniorAnimationEntity.mAlpha = Float.valueOf(this.val).floatValue();
                        this.animation.hEntitys.add(this.seniorAnimationEntity);
                        this.isAnimationModel = false;
                    } else if (this.tagName.equals("Duration")) {
                        this.seniorAnimationEntity.mDuration = Float.valueOf(this.val).floatValue();
                    }
                }
                if (!this.isPoint && this.tagName.equals("ClassName")) {
                    this.animation.ClassName = this.val;
                    if (this.animation.ClassName == null || this.animation.ClassName.indexOf("::CatmullRomMovePath") <= -1) {
                        this.isPoint = false;
                    } else {
                        this.isPoint = true;
                    }
                }
                if (this.isPoint) {
                    if (this.tagName.equals("PlayTimes")) {
                        if (StringUtils.isEmpty(this.val)) {
                            this.animation.Repeat = "0";
                        } else {
                            this.val = Integer.toString(Integer.parseInt(this.val) + 1);
                            this.animation.Repeat = this.val;
                        }
                    } else if (this.tagName.equals("Duration")) {
                        this.animation.Duration = this.val;
                    } else if (this.tagName.equals("Delay")) {
                        this.animation.Delay = getSecondStringFromMileconde(this.val);
                    } else if (this.tagName.equals("X")) {
                        this.f2p.x = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("Y")) {
                        this.f2p.y = Float.parseFloat(this.val);
                    } else if (this.tagName.equals("IsLoop") && !this.val.equals("false")) {
                        this.animation.Repeat = "-1";
                    }
                } else if (this.tagName.equals("CurrentAnimationIndex")) {
                    this.animation.CurrentAnimationIndex = this.val;
                } else if (this.tagName.equals("Repeat")) {
                    this.animation.Repeat = this.val;
                } else if (this.tagName.equals("Delay")) {
                    if (!this.isAnimationModel) {
                        this.animation.Delay = getSecondStringFromMileconde(this.val);
                    }
                } else if (this.tagName.equals("Duration")) {
                    if (!this.isAnimationModel) {
                        this.animation.Duration = this.val;
                    }
                } else if (this.tagName.equals("AnimationEnterOrQuit")) {
                    this.animation.AnimationEnterOrQuit = this.val;
                } else if (this.tagName.equals("AnimationTypeLabel")) {
                    this.animation.AnimationTypeLabel = this.val;
                } else if (this.tagName.equals("CustomProperties")) {
                    this.animation.CustomProperties = this.val;
                } else if (this.tagName.equals("AnimationType")) {
                    this.animation.AnimationType = this.val;
                } else if (this.tagName.equals("IsKeep")) {
                    this.animation.IsKeep = this.val;
                } else if (this.tagName.equals("EaseType")) {
                    this.animation.EaseType = this.val;
                } else if (this.tagName.equals("IsKeepEndStatus")) {
                    this.animation.isKeepEndStatus = Boolean.parseBoolean(this.val);
                }
            }
            if (this.isBackground) {
                if (this.tagName.equals("ID")) {
                    this.page.getBackground().f9ID = this.val;
                } else if (this.tagName.equals("Name")) {
                    this.page.getBackground().name = this.val;
                } else if (this.tagName.equals("Width")) {
                    this.page.getBackground().width = Float.parseFloat(this.val);
                } else if (this.tagName.equals("Height")) {
                    this.page.getBackground().height = Float.parseFloat(this.val);
                } else if (this.tagName.equals("Rotation")) {
                    this.page.getBackground().setRotation(Float.parseFloat(this.val));
                } else if (this.tagName.equals("X")) {
                    this.page.getBackground().f10x = Float.parseFloat(this.val);
                } else if (this.tagName.equals("Y")) {
                    this.page.getBackground().f11y = Float.parseFloat(this.val);
                } else if (this.tagName.equals("IsPlayAnimationAtBegining")) {
                    this.page.getBackground().isPlayAnimationAtBegining = Boolean.valueOf(this.val).booleanValue();
                } else if (this.tagName.equals("IsHideAtBegining")) {
                    this.page.getBackground().isHideAtBegining = Boolean.valueOf(this.val).booleanValue();
                } else if (this.tagName.equals("ClassName")) {
                    this.page.getBackground().component.className = this.val;
                } else if (this.tagName.equals("LocalSourceID")) {
                    this.page.getBackground().component.localSourceId = this.val;
                }
            }
            if (this.isPlaySequenceDelay) {
                this.page.getSequence().Delay.add(Long.valueOf(Long.parseLong(this.val)));
            }
        }
        if (this.isPlaySequence && arg1.equals("ID")) {
            this.group.ContainerID.add(this.val);
        }
        if (arg1.equals("Point")) {
            this.animation.Points.add(this.f2p);
            this.f2p = new PointF();
        }
        if (arg1.equals("Behavior")) {
            this.Behavior.triggerPageID = this.page.getID();
            this.container.behaviors.add(this.Behavior);
            this.Behavior = new BehaviorEntity();
        }
        if (arg1.equals("Component")) {
            this.isRightRender = false;
            this.isBackground = false;
            this.isBehavior = true;
            this.isMoudleComponent = false;
            this.isVerConnectLine = false;
            this.isPaintUIComponent = false;
            this.isHorConnectLine = false;
        }
        if (arg1.equals("Animations") || arg1.equals("Animation")) {
            if (this.animation.AnimationType != null) {
                if (!this.container.animations.contains(this.animation)) {
                    this.container.animations.add(this.animation);
                }
                this.animation = new AnimationEntity();
            } else if (this.animation.Points.size() > 0) {
                if (!this.container.animations.contains(this.animation)) {
                    this.container.animations.add(this.animation);
                }
                this.animation = new AnimationEntity();
            }
            this.isContainers = true;
            this.isAnimation = false;
            this.isPoint = false;
            this.isSenior = false;
        } else if (arg1.equals("Container")) {
            if (this.container.f9ID != null) {
                this.page.getContainers().add(this.container);
                this.container = new ContainerEntity();
                this.isGifComponent = false;
                this.isPDFComponent = false;
                this.isMoudleComponent = false;
            }
        } else if (arg1.equals("Containers")) {
            this.isContainers = false;
            this.isAnimation = false;
        }
        if (arg1.equals("Group")) {
            this.page.getSequence().Group.add(this.group);
            this.group = new GroupEntity();
        }
        if (arg1.equals("NavePageId")) {
            this.page.getNavePageIds().add(this.val);
        }
        this.val = "";
        this.flg = false;
    }

    public void endPrefixMapping(String arg0) throws SAXException {
    }

    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    public void processingInstruction(String arg0, String arg1) throws SAXException {
    }

    public void setDocumentLocator(Locator arg0) {
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startDocument() throws SAXException {
    }

    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        this.tagName = arg1;
        if (this.tagName.equals("Containers")) {
            this.ispage = false;
            this.isContainers = true;
        } else if (this.tagName.equals("Background")) {
            this.isContainers = false;
            this.isAnimation = false;
            this.isBackground = true;
            this.isPlaySequence = false;
        } else if (this.tagName.equals("Container")) {
            this.isContainerPosition = true;
            this.isInput = false;
            this.isMap = false;
            this.isTable = false;
            this.isYoutoBe = false;
        } else if (this.tagName.equals("Animations") || this.tagName.equals("Animation")) {
            this.isAnimation = true;
            this.isContainers = false;
        } else if (this.tagName.equals("Component")) {
            this.isComponent = true;
            this.isCounter = false;
            this.isText = false;
            this.isInput = false;
            this.isTable = false;
            this.isMap = false;
            this.isYoutoBe = false;
            this.isTimer = false;
            this.isQuestion = false;
            this.isMaskSlider = false;
            this.isSliderEffectComponent = false;
        } else if (this.tagName.equals("Question")) {
            this.isQuestion = true;
            MoudleComponentEntity componentEntity = (MoudleComponentEntity) this.container.component;
            this.questionEntity = new QuestionEntity();
            componentEntity.questionList.add(this.questionEntity);
        } else if (this.tagName.equals("Source")) {
            if (this.isMaskSlider) {
                this.maskBean = new MaskBean();
            }
        } else if (this.tagName.equals("LinkageObj")) {
            this.isLinkPage = true;
        } else if (this.tagName.equals("Behaviors")) {
            this.isComponent = false;
        } else if (this.tagName.equals("CellContainer") && this.isTable) {
            HLTableViewComponentEntity table = (HLTableViewComponentEntity) this.container.component;
            this.curTabCommponent = new HashMap<>();
            table.cellModel.commponentList.add(this.curTabCommponent);
        } else if (this.tagName.equals("Model") && this.isTable) {
            HLTableViewComponentEntity table2 = (HLTableViewComponentEntity) this.container.component;
            this.curTabMode = new HashMap<>();
            table2.cellModel.modelList.add(this.curTabMode);
        }
        if (this.isSliderEffectComponent && this.tagName.equals("Image")) {
            this.subImageItem = new SubImageItem();
        }
        if (this.isVerConnectLine) {
            if (this.tagName.equals("RightCell")) {
                this.cell = new Cell();
                this.cell.mCellType = "RIGHT_CELL";
            } else if (this.tagName.equals("LeftCell")) {
                this.cell = new Cell();
                this.cell.mCellType = "LEFT_CELL";
            }
        }
        if (this.isHorConnectLine) {
            if (this.tagName.equals("UpCell")) {
                this.cell = new Cell();
                this.cell.mCellType = "UP_CELL";
            } else if (this.tagName.equals("DownCell")) {
                this.cell = new Cell();
                this.cell.mCellType = "DOWN_CELL";
            }
        }
        if (this.isSenior && this.tagName.equals("AnimationModel")) {
            this.isAnimationModel = true;
            this.seniorAnimationEntity = new SeniorAnimationEntity();
        }
        if (arg1.equals("PlaySequence")) {
            this.isPlaySequence = true;
            this.isContainers = false;
            this.isAnimation = false;
            this.isBackground = false;
        }
        if (arg1.equals("PlaySequenceDelay")) {
            this.isPlaySequenceDelay = true;
            this.isPlaySequence = false;
            this.isContainers = false;
            this.isAnimation = false;
            this.isBackground = false;
        }
        if (this.tagName.equals("LeftRender")) {
            this.isLeftMRender = true;
        }
        if (this.tagName.equals("MiddleRender")) {
            this.isMiddleMRender = true;
            this.isLeftMRender = false;
        }
        if (this.tagName.equals("RightRender")) {
            this.isRightRender = true;
            this.isMiddleMRender = false;
        }
        if (this.isLeftMRender && this.tagName.equals("Render")) {
            this.curRenderBean = new MRenderBean();
        }
        if (this.isMiddleMRender && this.tagName.equals("Render")) {
            this.curRenderBean = new MRenderBean();
        }
        if (this.isRightRender && this.tagName.equals("Render")) {
            this.curRenderBean = new MRenderBean();
        }
        this.flg = true;
        if (this.tagName.equals("defaultTextLayoutFormat") && arg3 != null) {
            ((TextComponentEntity) this.container.component).setFontFamily(arg3.getValue("fontFamily"));
            ((TextComponentEntity) this.container.component).setFontSize(arg3.getValue("fontSize"));
            ((TextComponentEntity) this.container.component).setFontWeight(arg3.getValue("fontWeight"));
            ((TextComponentEntity) this.container.component).setFontStyle(arg3.getValue("fontStyle"));
            ((TextComponentEntity) this.container.component).setTextDecoration(arg3.getValue("textDecoration"));
            ((TextComponentEntity) this.container.component).setTextAlign(arg3.getValue("textAlign"));
            ((TextComponentEntity) this.container.component).setLineHeight(arg3.getValue("lineHeight"));
            ((TextComponentEntity) this.container.component).setTrackingLeft(arg3.getValue("trackingLeft"));
            ((TextComponentEntity) this.container.component).setTrackingRight(arg3.getValue("trackingRight"));
            ((TextComponentEntity) this.container.component).setTextColor(arg3.getValue("color"));
        }
    }

    public void startPrefixMapping(String arg0, String arg1) throws SAXException {
    }

    public static String getSecondStringFromMileconde(String val2) {
        return Integer.toString(Float.valueOf(Float.valueOf(Float.parseFloat(val2)).floatValue() * 1000.0f).intValue());
    }
}
