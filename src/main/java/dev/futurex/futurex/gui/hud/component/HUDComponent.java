package dev.futurex.futurex.gui.hud.component;

import dev.futurex.futurex.module.modules.client.ClickGUI;
import dev.futurex.futurex.util.client.MathUtil;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.util.render.GUIUtil;
import dev.futurex.futurex.util.render.Render2DUtil;
import dev.futurex.futurex.util.render.builder.Render2DBuilder;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.managers.AnimationManager;
import dev.futurex.futurex.gui.hud.HUDFrame;
import dev.futurex.futurex.setting.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class HUDComponent {

    public HUDElement hudElement;
    public HUDFrame frame;
    public List<HUDSubComponent> subComponents = new ArrayList<>();
    public AnimationManager animationFrameManager;
    public AnimationManager animationComponentManager;

    public HUDComponent(HUDFrame frame, HUDElement hudElement) {
        this.hudElement = hudElement;
        this.frame = frame;

        this.animationFrameManager = new AnimationManager((int) ClickGUI.animationSpeed.getValue(), false);
        this.animationComponentManager = new AnimationManager(200, hudElement.isDrawn());

        for (Setting setting : hudElement.settingList)
            subComponents.add(new HUDSubComponent(frame, setting));
    }

    public void drawComponent(int x, int y, int height, int width) {
        double preOffset = frame.offset;

        if (animationFrameManager.getAnimationFactor() > 0.05) {
            for (HUDSubComponent subComponent : subComponents) {
                frame.offset += animationFrameManager.getAnimationFactor();
                subComponent.drawSubComponent(x, y, height, width);
            }

            animationFrameManager.tick();
        }

        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x, (int) (y + height + 1 + (preOffset * height)), (x + width) - 1, (int) (y + height * 2 + (preOffset * height)))) {
            color = 0xCC383838;

            if (GUIUtil.ldown) {
                hudElement.toggleElement();
                animationComponentManager.toggle();
            }

            if (GUIUtil.rdown) {
                hudElement.toggleState();
                animationFrameManager.setState(hudElement.isOpened());
            }
        }

        Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x, y + height + (preOffset * height), (x + (width * MathUtil.clamp((float) animationComponentManager.getAnimationFactor(), 0, 1))), y + height * 2 + (preOffset * height), 0, ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(hudElement.getName(), x + 4, (float) (y + height + 4 + (preOffset * height)), -1);

        animationComponentManager.tick();

        if (hudElement.hasSettings() && ClickGUI.indicators.getValue())
            FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
    }
}
