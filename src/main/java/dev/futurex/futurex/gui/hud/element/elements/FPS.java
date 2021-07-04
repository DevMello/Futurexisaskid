package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class FPS extends HUDElement {
    public FPS() {
        super("FPS", 2, 13, Category.INFO, AnchorPoint.BottomRight);
    }

    public static Checkbox average = new Checkbox("Average", false);

    @Override
    public void setup() {
        addSetting(average);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("FPS" + TextFormatting.WHITE + " " + Minecraft.getDebugFPS(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth(Minecraft.getDebugFPS() + " FPS") + 2;
    }
}
