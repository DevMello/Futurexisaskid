package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class SkyColor extends Module {
    public SkyColor() {
        super("SkyColor", Category.RENDER, "Changes the color of the sky");
    }

    public static dev.futurex.futurex.setting.checkbox.Checkbox color = new dev.futurex.futurex.setting.checkbox.Checkbox("Sky", true);
    public static ColorPicker skyPicker = new ColorPicker(color, "Sky Picker", new Color(250, 0, 250, 50));

    public static dev.futurex.futurex.setting.checkbox.Checkbox fog = new Checkbox("Fog", true);
    public static ColorPicker fogPicker = new ColorPicker(fog, "Fog Picker", new Color(250, 0, 250, 50));
    public static SubCheckbox fogCancel = new SubCheckbox(fog, "No Fog", true);

    @Override
    public void setup() {
        addSetting(color);
        addSetting(fog);
    }

    @SubscribeEvent
    public void onFogRender(EntityViewRenderEvent.FogColors event) {
        if (fog.getValue()) {
            event.setRed(fogPicker.getColor().getRed() / 255f);
            event.setGreen(fogPicker.getColor().getGreen() / 255f);
            event.setBlue(fogPicker.getColor().getBlue() / 255f);
        }
    }

    @SubscribeEvent
    public void fog(EntityViewRenderEvent.FogDensity event) {
        if (fog.getValue() && fogCancel.getValue()) {
            event.setDensity(0);
            event.setCanceled(true);
        }
    }
}