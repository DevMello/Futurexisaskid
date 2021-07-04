package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.player.MotionUtil;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Speed extends HUDElement {
    public Speed() {
        super("Speed", 2, 46, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Speed " + TextFormatting.WHITE + MotionUtil.getSpeed(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth("Speed " + TextFormatting.WHITE + MotionUtil.getSpeed()) + 2;
    }
}
