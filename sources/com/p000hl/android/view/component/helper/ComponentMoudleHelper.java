package com.p000hl.android.view.component.helper;

import android.util.Log;
import com.p000hl.android.core.utils.ReflectHelp;
import com.p000hl.android.view.component.inter.Component;
import java.util.HashMap;

/* renamed from: com.hl.android.view.component.helper.ComponentMoudleHelper */
public class ComponentMoudleHelper {
    static HashMap<String, String> moduleMap = new HashMap<>();

    static {
        moduleMap.put("moduleuicomponent.module.magazine::HLHorSlideshowImageUIComponent", "com.hl.android.view.component.moudle.slide.HorizontalSlide");
        moduleMap.put("moduleuicomponent.module.magazine::HLVerSlideshowImageUIComponent", "com.hl.android.view.component.moudle.slide.VerticleSlide");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseVerScrollNoSelectedComponent", "com.hl.android.view.component.moudle.HLMouseVerScrollNoSelectedComponent");
        moduleMap.put("moduleuicomponent.photo.multipleleaf::HLVerSliderImageUIComponent", "com.hl.android.view.component.moudle.HLViewFlipperVerticleInter");
        moduleMap.put("moduleuicomponent.module.realEstate::HLVerImageSequenceFrameUIComponent", "com.hl.android.view.component.moudle.imageseq.HL3DViewVerFlipper");
        moduleMap.put("moduleuicomponent.module.realEstate::HLHorImageSequenceFrameUIComponent", "com.hl.android.view.component.moudle.imageseq.HL3DViewHorFlipper");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseHorScrollUIComponent", "com.hl.android.view.component.moudle.slide.HorizontalSlideClick");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseVerScrollUIComponent", "com.hl.android.view.component.moudle.slide.VerticleSlideClick");
        moduleMap.put("moduleuicomponent.drawer::HLVerBottomUIComponent", "com.hl.android.view.component.moudle.HLVerBottomUIComponent");
        moduleMap.put("moduleuicomponent.drawer::HLHorRightUIComponent", "com.hl.android.view.component.moudle.HLHorRightUIComponent");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseHorCellScrollUIComponent", "com.hl.android.view.component.moudle.slidecell.HorizontalSlideCellClick");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseVerCellScrollUIComponent", "com.hl.android.view.component.moudle.slidecell.VerticalSlideCellClick");
        moduleMap.put("moduleuicomponent.bookshelf::HLBookShelfUIComponent", "com.hl.android.view.component.moudle.bookshelf.BookShelfUIComponent");
        moduleMap.put("moduleuicomponent.connectline::HLConnectLineUIComponent", "com.hl.android.view.component.moudle.ConnectLineComponent");
        moduleMap.put("moduleuicomponent.connectline::HLConnectHorLineUIComponent", "com.hl.android.view.component.moudle.ConnectHorLineComponent");
        moduleMap.put("moduleuicomponent.multiMedia.game.puzzleGame::HLPuzzleGameUIComponent", "com.hl.android.view.component.moudle.HLPuzzleGameUIComponent");
        moduleMap.put("moduleuicomponent.photo.signleaf.paint::HLPaintingUIComponent", "com.hl.android.view.component.moudle.HLPaintingUIComponent");
        moduleMap.put("moduleuicomponent.camera::HLCameraUIComponent", "com.hl.android.view.component.moudle.HLCameraUIComponent");
        moduleMap.put("moduleuicomponent.sliderhand::HLSliderSliceComponent", "com.hl.android.view.component.moudle.HLSliderSliceComponent");
        moduleMap.put("moduleuicomponent.photo.multipleleaf::HLMaskSliderImageUIComponent", "com.hl.android.view.component.moudle.masksliderimag.MaskSliderImage");
        moduleMap.put("moduleuicomponent.photo.multipleleaf::HLVerSlideImageSelectUIComponent", "com.hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseCatalogVScrollUIComponent", "com.hl.android.view.component.moudle.HLMouseCatalogVScrollUIComponent");
        moduleMap.put("moduleuicomponent.photo.signleaf.panoroma::HLPanoromaUIComponent", "com.hl.android.view.component.moudle.HLPanoromaUIComponent");
        moduleMap.put("moduleuicomponent.mousescroll::HLMouseVerInteractScrollUIComponent", "com.hl.android.view.component.moudle.HLMouseVerInteractScrollUIComponent");
    }

    public static Component getComponent(String moudleId, Class[] argsType, Object[] argsValue) {
        Component component = null;
        try {
            if (moudleId.equals("moduleuicomponent.photo.multipleleaf::HLHorSliderImageUIComponent")) {
                return (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLViewFlipperInter", argsType, argsValue);
            }
            if (moudleId.contains("HL360RotatingImageUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HL3DViewFlipper", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.photo.multipleleaf::HLVerSliderImageUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLViewFlipperVerticleInter", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.module.magazine::HLCarouselImageUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLCarouselImageUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.multiMedia.game.puzzleGame::HLPuzzleGameUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLPuzzleGameUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.exam::HLExamUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.QuestionComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.camera::HLCameraUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLCameraUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.sliderhand::HLSliderSliceComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLSliderSliceComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.photo.multipleleaf::HLVerSlideImageSelectUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.mousescroll::HLMouseCatalogVScrollUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLMouseCatalogVScrollUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.photo.signleaf.panoroma::HLPanoromaUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLPanoromaUIComponent", argsType, argsValue);
            } else if (moudleId.contains("moduleuicomponent.mousescroll::HLMouseVerInteractScrollUIComponent")) {
                component = (Component) ReflectHelp.newInstance("com.hl.android.view.component.moudle.HLMouseVerInteractScrollUIComponent", argsType, argsValue);
            } else if (moduleMap.containsKey(moudleId)) {
                component = (Component) ReflectHelp.newInstance((String) moduleMap.get(moudleId), argsType, argsValue);
            } else {
                Log.e("hl", "we do not surpport moudle " + moudleId);
            }
            return component;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
