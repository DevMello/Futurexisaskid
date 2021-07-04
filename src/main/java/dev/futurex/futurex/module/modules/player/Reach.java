package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class Reach extends Module {
    public Reach() {
        super("Reach", Category.PLAYER, "Increases players reach distance");
    }

    public static Slider distance = new Slider("Distance", 0.0D, 6.0D, 10.0D, 1);

    @Override
    public void setup() {
        addSetting(distance);
    }

    @Override
    public String getHUDData() {
        return " " + distance.getValue();
    }
}
