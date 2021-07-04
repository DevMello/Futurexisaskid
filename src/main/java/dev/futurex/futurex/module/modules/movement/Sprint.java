package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Automatically sprints");
    }

    public static Mode mode = new Mode("Mode", "Rage", "Legit");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                if (!(MixinInterface.mc.player.isSneaking()) && !(MixinInterface.mc.player.collidedHorizontally) && MixinInterface.mc.gameSettings.keyBindForward.isKeyDown() || MixinInterface.mc.gameSettings.keyBindLeft.isKeyDown() || MixinInterface.mc.gameSettings.keyBindRight.isKeyDown() || MixinInterface.mc.gameSettings.keyBindBack.isKeyDown() && MixinInterface.mc.player.getFoodStats().getFoodLevel() > 6f)
                    MixinInterface.mc.player.setSprinting(true);
                break;
            case 1:
                if (MixinInterface.mc.gameSettings.keyBindForward.isKeyDown() && !(MixinInterface.mc.player.collidedHorizontally) && !(MixinInterface.mc.player.isSneaking()) && !(MixinInterface.mc.player.isHandActive()) && MixinInterface.mc.player.getFoodStats().getFoodLevel() > 6f)
                    MixinInterface.mc.player.setSprinting(true);
                break;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
