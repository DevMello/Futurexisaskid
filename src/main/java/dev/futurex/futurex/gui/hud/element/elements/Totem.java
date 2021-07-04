package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.FutureX;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Totem extends HUDElement {
    public Totem() {
        super("Totem", 2, 57, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Totems: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth("Totems: " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING)) + 2;
    }
}
