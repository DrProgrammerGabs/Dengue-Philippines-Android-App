package com.p000hl.android.view.component.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.ContainerEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.core.utils.ReflectHelp;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.ComponentListener;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.util.HashMap;

/* renamed from: com.hl.android.view.component.helper.ComponentHelper */
public class ComponentHelper {
    public static final String IMAGE_TYPE_HOR = "text_hor_image";
    public static final String IMAGE_TYPE_NORMAL = "normal_image";
    public static final String IMAGE_TYPE_VER = "text_ver_image";
    private static HashMap<String, String> componentMap = new HashMap<>();

    static {
        componentMap.put("com.hl.flex.components.objects.hlImage::HLLocalImageComponent", "com.hl.android.view.component.ImageComponent");
        componentMap.put("com.hl.flex.components.objects.hlButton::HLLocalButtonComponent", "com.hl.android.view.component.HLLocalButtonComponent");
        componentMap.put("com.hl.flex.components.objects.hlVideo::HLLocalVideoComponent", "com.hl.android.view.component.VideoComponent");
        componentMap.put("com.hl.flex.components.objects.hlAudio::HLMp3Component", "com.hl.android.view.component.AudioComponent");
        componentMap.put("com.hl.flex.components.objects.html::HLHtmlComponent", "com.hl.android.view.component.WebComponent");
        componentMap.put("com.hl.flex.components.objects.hlImage::HLGIFComponent", "com.hl.android.view.component.ImageGifComponent");
        componentMap.put("com.hl.flex.components.objects.hlText.hlRollingText::HLRollingTextComponent", "com.hl.android.view.component.ScrollTextViewComponent");
        componentMap.put("com.hl.flex.components.objects.hlSwf::HLLocalPDFComponent", "com.hl.android.view.component.PDFDocumentViewComponentMU");
        componentMap.put("com.hl.flex.components.objects.template::HLTemplateComponent", "com.hl.flex.components.objects.template::HLTemplateComponent");
        componentMap.put("com.hl.flex.components.objects.hlText.hlEnglishRollingText::HLEnglishRollingTextComponent", "com.hl.android.view.component.ScrollTextViewComponentEN");
        componentMap.put("com.hl.flex.components.objects.swf::HLSWFComponent", "com.hl.android.view.component.ImageGifComponent");
        componentMap.put("com.hl.flex.components.objects.html::HLHtml5Component", "com.hl.android.view.component.HTMLComponent");
        componentMap.put("com.hl.flex.components.objects.counter::HLCounterComponent", "com.hl.android.view.component.HLCounterComponent");
        componentMap.put("com.hl.flex.components.objects.hltimer::HLTimerComponent", "com.hl.android.view.component.TimerComponent");
        componentMap.put("com.hl.flex.components.objects.swf::HLSWFFileComponent", "com.hl.android.view.component.HLSWFFileComponent");
        componentMap.put("com.hl.flex.components.objects.effect::HLSliderEffectComponent", "com.hl.android.view.component.HLSliderEffectComponent");
        componentMap.put("com.hl.flex.components.objects.hlTextInput::HLTextInputComponent", "com.hl.android.view.component.InputTextComponent");
        componentMap.put("com.hl.flex.components.objects.hlmap::HLGoogleMapComponent", "com.hl.android.view.component.MapComponent");
        componentMap.put("com.hl.flex.components.objects.hltableView::HLTableViewComponent", "com.hl.android.view.component.HLTableViewComponent");
        componentMap.put("com.hl.flex.components.objects.hlVideo::HLYouTubeVideoComponent", "com.hl.android.view.component.YouTubeVideoComponent");
    }

    public static String getComponentClassName(String key) {
        return (String) componentMap.get(key);
    }

    public static Component getComponent(ContainerEntity entity, View currentViewPage) {
        int x;
        int y;
        entity.getComponent().isHideAtBegining = entity.isHideAtBegining;
        Class[] argsType = {Context.class, ComponentEntity.class};
        Object[] argsValue = {currentViewPage.getContext(), entity.component};
        entity.getComponent().setRotation(entity.getRotation());
        Component component = null;
        if (getComponentClassName(entity.getComponent().getClassName()) == null) {
            Log.d("wdy", "没有控件：" + entity.getComponent().getClassName() + ",请添加！！！");
            return null;
        }
        try {
            if (entity.getComponent().getClassName().indexOf("HLTemplateComponent") > 0) {
                component = ComponentMoudleHelper.getComponent(((MoudleComponentEntity) entity.component).getModuleID(), argsType, argsValue);
                if (component == null) {
                    return null;
                }
            }
            if (component == null && entity.getComponent().getClassName().indexOf("HLLocalImageComponent") > 0) {
                entity.component.autoLoop = entity.autoLoop;
                String imgType = entity.getComponent().getImageType();
                if (imgType != null) {
                    if (imgType.contains(IMAGE_TYPE_HOR)) {
                        component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.VerticalImageComponent", argsType, argsValue);
                    } else if (imgType.contains(IMAGE_TYPE_VER)) {
                        component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.HorizontalImageComponent", argsType, argsValue);
                    }
                }
            }
            if (component == null) {
                entity.component.autoLoop = entity.autoLoop;
                component = (Component) ReflectHelp.newInstance(getComponentClassName(entity.getComponent().getClassName().trim()), argsType, argsValue);
            }
            component.getEntity().oldHeight = entity.getHeight();
            component.getEntity().oldWidth = entity.getWidth();
            if (entity.getRotation() != 0.0f) {
                float rotation = entity.getRotation();
                float ratioMHeight = ScreenUtils.getVerScreenValue(entity.getHeight());
                float ratioMWidth = ScreenUtils.getHorScreenValue(entity.getWidth());
                float ratioMX = ScreenUtils.getHorScreenValue(entity.getX());
                x = (int) (((((double) ratioMX) - (((double) (ratioMHeight / 2.0f)) * Math.sin((((double) rotation) * 3.141592653589793d) / 180.0d))) - ((double) (ratioMWidth / 2.0f))) + (((double) (ratioMWidth / 2.0f)) * Math.cos((((double) rotation) * 3.141592653589793d) / 180.0d)));
                y = (int) (((((double) ScreenUtils.getVerScreenValue(entity.getY())) + (((double) (ratioMWidth / 2.0f)) * Math.sin((((double) rotation) * 3.141592653589793d) / 180.0d))) - ((double) (ratioMHeight / 2.0f))) + (((double) (ratioMHeight / 2.0f)) * Math.cos((((double) rotation) * 3.141592653589793d) / 180.0d)));
            } else {
                x = (int) ScreenUtils.getHorScreenValue(entity.getX());
                y = (int) ScreenUtils.getVerScreenValue(entity.getY());
            }
            int width = (int) ScreenUtils.getHorScreenValue(entity.getWidth());
            int height = (int) ScreenUtils.getVerScreenValue(entity.getHeight());
            if (component.getEntity().isPageInnerSlide) {
                component.getEntity().slideBindingWidth = (int) ScreenUtils.getHorScreenValue((float) component.getEntity().slideBindingWidth);
                component.getEntity().slideBindingHeight = (int) ScreenUtils.getVerScreenValue((float) component.getEntity().slideBindingHeight);
                component.getEntity().slideBindingX = (int) ScreenUtils.getHorScreenValue((float) component.getEntity().slideBindingX);
                component.getEntity().slideBindingY = (int) ScreenUtils.getVerScreenValue((float) component.getEntity().slideBindingY);
            }
            ((View) component).setLayoutParams(new LayoutParams(width, height));
            component.getEntity().setAnims(entity.getAnimations());
            component.getEntity().isPlayAnimationAtBegining = entity.isPlayAnimationAtBegining;
            component.getEntity().isPlayVideoOrAudioAtBegining = entity.isPlayVideoOrAudioAtBegining();
            component.getEntity().isHideAtBegining = entity.isHideAtBegining;
            component.getEntity().f7x = x;
            component.getEntity().f8y = y;
            component.getEntity().isStroyTelling = entity.IsStroyTelling;
            component.getEntity().isMoveScale = entity.isMoveScale;
            component.getEntity().isPushBack = entity.isPushBack;
            component.getEntity().autoLoop = entity.autoLoop;
            component.getEntity().setComponentId(entity.getID());
            component.getEntity().behaviors = entity.behaviors;
            if (component instanceof ComponentListener) {
                ((ComponentListener) component).registerCallbackListener((OnComponentCallbackListener) currentViewPage);
            }
        } catch (Exception e) {
            Log.e("hl", "加载组件" + entity.getComponent().getClassName() + "出错", e);
        }
        return component;
    }
}
