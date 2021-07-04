package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Time extends HUDElement {
    public Time() {
        super("Time", 2, 35, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString(new SimpleDateFormat("h:mm a").format(new Date()), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = (int) (FontUtil.getStringWidth(new SimpleDateFormat("h:mm a").format(new Date())) + 2);
    }
}