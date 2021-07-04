package dev.futurex.futurex.module.modules.movement.elytra.modes;

import dev.futurex.futurex.util.player.FlightUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;
import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class PitchNCP extends ElytraMode {

    @Override
    public void onVerticalMovement() {
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.rotationPitch = (float) ElytraFlight.rotationNCP.getValue() * -1;

        else if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.rotationPitch = (float) ElytraFlight.rotationNCP.getValue();
    }

    @Override
    public void onHorizontalMovement() {
        FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
    }

    @Override
    public void noMovement() {
        FlightUtil.freezeFlight(ElytraFlight.fallSpeed.getValue(), ElytraFlight.yOffset.getValue());
    }

    @Override
    public void onRotation() {
        RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());

        if (ElytraFlight.rotationLock.getValue())
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
    }
}
