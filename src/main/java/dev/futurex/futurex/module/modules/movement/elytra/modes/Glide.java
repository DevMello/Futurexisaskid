package dev.futurex.futurex.module.modules.movement.elytra.modes;

import dev.futurex.futurex.util.player.FlightUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;
import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Glide extends ElytraMode {

    @Override
    public void onHorizontalMovement() {
        FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
