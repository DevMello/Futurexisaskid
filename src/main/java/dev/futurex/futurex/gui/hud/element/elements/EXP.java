package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.player.InventoryUtil;
import dev.futurex.futurex.util.render.FontUtil;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Papa-Quill
 * @since 12/17/2020
 */

public class EXP extends HUDElement {
    public EXP() {
        super("EXP", 2, 57, Category.COMBAT, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("EXP: " + TextFormatting.WHITE + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = FutureX.fontManager.getCustomFont().getStringWidth("EXP: " + InventoryUtil.getItemCount(Items.EXPERIENCE_BOTTLE)) + 2;
    }
}
