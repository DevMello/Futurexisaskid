package dev.futurex.futurex.module.modules.player;

import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 12/11/2020
 */

public class FreeCam extends Module {
    public FreeCam() {
        super("FreeCam", Category.PLAYER, "Allows you to fly out of your body");
    }

    public static Slider speed = new Slider("Speed", 0.0D, 0.5D, 3.0D, 1);
    public static Checkbox playerModel = new Checkbox("Player Model", true);
    public static Checkbox cancelPackets = new Checkbox("Cancel Packets", true);
    public static Checkbox noClip = new Checkbox("NoClip", true);

    @Override
    public void setup() {
        addSetting(speed);
        addSetting(playerModel);
        addSetting(cancelPackets);
        addSetting(noClip);
    }
}
