package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Server extends HUDElement {
    public Server() {
        super("Server", 2, 46, Category.INFO, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        String server;
        if (!mc.isSingleplayer())
            server = mc.getCurrentServerData().serverIP;
        else
            server = "SinglePlayer";

        FontUtil.drawString(server, this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);

        width = FutureX.fontManager.getCustomFont().getStringWidth(server) + 2;
    }
}
