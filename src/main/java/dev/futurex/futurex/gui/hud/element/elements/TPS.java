package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.managers.TickManager;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class TPS extends HUDElement {
    public TPS() {
        super("TPS", 2, 57, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("TPS " + TextFormatting.WHITE + TickManager.getTPS(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth(TickManager.getTPS() + " TPS") + 2;
    }
}
