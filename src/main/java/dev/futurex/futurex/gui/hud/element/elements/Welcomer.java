package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Welcomer extends HUDElement {
    public Welcomer() {
        super("Welcomer", 200, 2, Category.MISC, AnchorPoint.None);
    }

    public static Mode mode = new Mode("Mode", "Dynamic", "Static");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Welcome " + mc.player.getName() + "! :^)", this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = FutureX.fontManager.getCustomFont().getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
    }
}
