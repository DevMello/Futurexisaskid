package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.managers.notification.Notification;
import dev.futurex.futurex.managers.notification.NotificationManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT, "Pulls you out of the void");
    }

    public static Mode mode = new Mode("Mode", "Float", "Freeze", "SlowFall", "Teleport", "Timer");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (MixinInterface.mc.player.posY <= 0) {
            NotificationManager.addNotification(new Notification("Attempting to get you out of the void!", Notification.Type.Info));

            switch (mode.getValue()) {
                case 0:
                    MixinInterface.mc.player.motionY = 0.5;
                    break;
                case 1:
                    MixinInterface.mc.player.motionY = 0;
                    break;
                case 2:
                    MixinInterface.mc.player.motionY /= 4;
                    break;
                case 3:
                    MixinInterface.mc.player.setPosition(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY + 2, MixinInterface.mc.player.posZ);
                    break;
                case 4:
                    MixinInterface.mc.timer.tickLength = 50f / 0.1f;
                    break;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
