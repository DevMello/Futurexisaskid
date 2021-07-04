package dev.futurex.futurex.gui.hud.element.elements;

import dev.futurex.futurex.FutureX;
import dev.futurex.futurex.module.modules.client.HUD;
import dev.futurex.futurex.util.client.color.ThemeColor;
import dev.futurex.futurex.util.render.FontUtil;
import dev.futurex.futurex.gui.hud.element.AnchorPoint;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.setting.slider.Slider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class WaterMark extends HUDElement {
	public WaterMark() {
		super("WaterMark", 2, 2, Category.MISC, AnchorPoint.TopRight);
	}

	public static Slider scale = new Slider("Scale", 0.0D, 1.0D, 10.0D, 1);

	@Override
	public void setup() {
		addSetting(scale);
	}

	@Override
	public void renderElement() {
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale.getValue(), scale.getValue(), scale.getValue());
		FontUtil.drawString("FutureX " + TextFormatting.WHITE + FutureX.VERSION, this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
		GlStateManager.popMatrix();

		width = (int) (scale.getValue() * FutureX.fontManager.getCustomFont().getStringWidth("FutureX " + TextFormatting.WHITE + FutureX.VERSION) + 2);
		height = (int) (scale.getValue() * (mc.fontRenderer.FONT_HEIGHT + 3));
	}
}
