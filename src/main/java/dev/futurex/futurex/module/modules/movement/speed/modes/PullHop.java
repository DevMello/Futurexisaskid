package dev.futurex.futurex.module.modules.movement.speed.modes;

import dev.futurex.futurex.event.events.player.MoveEvent;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.modules.movement.speed.SpeedMode;
import dev.futurex.futurex.util.player.MotionUtil;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class PullHop extends SpeedMode {

    @Override
    public void onMotionUpdate() {
        lastDist = Math.sqrt(Math.pow(MixinInterface.mc.player.posX - MixinInterface.mc.player.prevPosX, 2) + Math.pow(MixinInterface.mc.player.posZ - MixinInterface.mc.player.prevPosZ, 2));
    }

    @Override
    public void handleSpeed(MoveEvent event) {
        if (MixinInterface.mc.player.onGround) {
            MixinInterface.mc.player.motionY = 0.3994f;
            event.setY(0.3994f);
        }

        else
            MixinInterface.mc.player.motionY--;

        MotionUtil.impressMoveSpeed(event, 0.2, 0.6f);
    }
}
