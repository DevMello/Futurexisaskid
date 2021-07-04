package dev.futurex.futurex.module.modules.movement.elytra.modes;

import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;
import dev.futurex.futurex.util.player.FlightUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class Deer extends ElytraMode {

    @Override
    public void onVerticalMovement() {
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
            mc.player.motionY = ElytraFlight.ySpeed.getValue();
        }
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
