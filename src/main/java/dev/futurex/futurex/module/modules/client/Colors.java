package dev.futurex.futurex.module.modules.client;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.slider.SubSlider;

import java.awt.*;

/**
 * @author bon & linustouchtips
 * @since 12/17/2020
 */

public class Colors extends Module {
	public Colors() {
		super("Colors", Category.CLIENT, "The client-wide color scheme.");
		this.enable();
		this.setDrawn(false);
	}

	public static Checkbox rainbow = new Checkbox("Rainbow", true);
	public static SubSlider saturation = new SubSlider(rainbow, "Saturation", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider brightness = new SubSlider(rainbow, "Brightness", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider difference = new SubSlider(rainbow, "Difference", 1.0D, 30.0D, 100.0D, 0);
	public static SubSlider speed = new SubSlider(rainbow, "Speed", 1.0D, 30.0D, 100.0D, 0);

	public static Checkbox clientColor = new Checkbox("Color", true);
	public static ColorPicker clientPicker = new ColorPicker(clientColor, "Color Picker", new Color(175, 32, 32, 250));

	@Override
	public void setup() {
		addSetting(rainbow);
		addSetting(clientColor);
	}

	@Override
	public void onDisable() {
		this.enable();
	}

	/*
	@Override
	public void onUpdate() {
		if (nullCheck())
			return;

		if (ClickGUI.blurChat.getValue()) {
			if (mc.currentScreen instanceof GuiChat)
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			else {
				try {
					mc.entityRenderer.getShaderGroup().deleteShaderGroup();
				} catch (Exception e) {

				}
			}
		}
	}
	*/
}
