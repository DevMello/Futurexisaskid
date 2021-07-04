package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class WebTeleport extends Module {
    public WebTeleport() {
        super("WebTeleport", Category.MOVEMENT, "Allows you to fall through webs faster");
    }

    public static Mode mode = new Mode("Mode", "Normal", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.isInWeb) {
            switch (mode.getValue()) {
                case 0:
                    for (int i = 0; i < 10; i++)
                        MixinInterface.mc.player.motionY--;
                    break;
                case 1:
                    MixinInterface.mc.player.isInWeb = false;
                    break;
            }
        }
    }
}
