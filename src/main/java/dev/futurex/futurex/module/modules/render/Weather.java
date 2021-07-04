package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Weather extends Module {
    public Weather() {
        super("Weather", Category.RENDER, "Allows you to control weather client-side");
    }

    public static Mode mode = new Mode("Mode", "Clear", "Rain", "Thunder");
    public static Slider time = new Slider("Time", 0.0D, 6000.0D, 24000.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(time);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        MixinInterface.mc.world.setWorldTime((long) time.getValue());

        switch (mode.getValue()) {
            case 0:
                MixinInterface.mc.world.setRainStrength(0);
                break;
            case 1:
                MixinInterface.mc.world.setRainStrength(1);
                break;
            case 2:
                MixinInterface.mc.world.setRainStrength(2);
                break;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}