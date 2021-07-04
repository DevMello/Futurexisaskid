package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Direction extends HUDElement {
    public Direction() {
        super("Direction", 2, 68, Category.INFO, AnchorPoint.BottomLeft);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString(RotationUtil.getFacing() + " " + TextFormatting.WHITE + RotationUtil.getTowards(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth(RotationUtil.getFacing() + " " + TextFormatting.WHITE + RotationUtil.getTowards()) + 2;
    }
}
