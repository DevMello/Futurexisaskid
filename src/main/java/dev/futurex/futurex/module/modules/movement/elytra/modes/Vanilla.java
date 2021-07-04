package dev.futurex.futurex.module.modules.movement.elytra.modes;

import dev.futurex.futurex.util.player.FlightUtil;
import dev.futurex.futurex.util.player.rotation.RotationUtil;
import dev.futurex.futurex.module.modules.movement.ElytraFlight;
import dev.futurex.futurex.module.modules.movement.elytra.ElytraMode;

public class Vanilla extends ElytraMode {

    @Override
    public void onHorizontalMovement() {
        mc.player.capabilities.setFlySpeed((float) (ElytraFlight.hSpeed.getValue() / 23));
        mc.player.capabilities.isFlying = true;

        if (mc.player.capabilities.isCreativeMode)
            return;

        mc.player.capabilities.allowFlying = true;
    }

    @Override
    public void noMovement() {
        FlightUtil.freezeFlight(ElytraFlight.fallSpeed.getValue(), 0);
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
