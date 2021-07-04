package dev.futurex.futurex.module.modules.render;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class NoBob extends Module {
    public NoBob() {
        super("NoBob", Category.RENDER, "Prevents the bobbing animation");
    }

    public static Mode mode = new Mode("Mode", "Vanilla", "Settings");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == 0)
            MixinInterface.mc.player.distanceWalkedModified = 4.0f;
        else
            MixinInterface.mc.gameSettings.viewBobbing = false;
    }
}
