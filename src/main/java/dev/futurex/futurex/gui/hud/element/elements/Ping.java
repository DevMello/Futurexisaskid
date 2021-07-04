package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Ping extends HUDElement {
    public Ping() {
        super("Ping", 2, 24, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Ping " + TextFormatting.WHITE + (!mc.isSingleplayer() ? mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : -1) + " ms", this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth("Ping " + (!mc.isSingleplayer() ? mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : -1) + " ms") + 2;
    }
}
