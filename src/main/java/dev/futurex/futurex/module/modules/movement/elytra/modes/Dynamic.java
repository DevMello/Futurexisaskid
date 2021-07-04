package dev.futurex.futurex.module.modules.movement.elytra.modes;

import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;
import dev.futurex.futurex.util.player.FlightUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class Dynamic extends ElytraMode {

    boolean boostable = true;

    @Override
    public void onVerticalMovement() {
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.motionY = ElytraFlight.ySpeed.getValue();

        else if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.motionY = ElytraFlight.ySpeed.getValue() * -1;
    }

    @Override
    public void onHorizontalMovement() {
        if (boostable)
            FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
    }

    @Override
    public void onRubberband() {
        boostable = false;
    }

    @Override
    public void noMovement() {
        FlightUtil.freezeFlight(ElytraFlight.fallSpeed.getValue(), ElytraFlight.yOffset.getValue());
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
