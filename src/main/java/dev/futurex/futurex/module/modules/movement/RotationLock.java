package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;

import java.util.Objects;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class RotationLock extends Module {
    public RotationLock() {
        super("RotationLock", Category.MOVEMENT, "Locks player rotation");
    }

    public static Checkbox playerYaw = new Checkbox("Yaw", true);
    public static Checkbox playerPitch = new Checkbox("Pitch", true);

    @Override
    public void setup() {
        addSetting(playerYaw);
        addSetting(playerPitch);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        int angle = 90;

        float yaw = MixinInterface.mc.player.rotationYaw;
        float pitch = MixinInterface.mc.player.rotationPitch;

        yaw = Math.round(yaw / angle) * angle;
        pitch = Math.round(pitch / angle) * angle;

        if (playerYaw.getValue())
            MixinInterface.mc.player.rotationYaw = yaw;

        if (playerPitch.getValue())
            MixinInterface.mc.player.rotationPitch = pitch;

        if (MixinInterface.mc.player.isRiding())
            Objects.requireNonNull(MixinInterface.mc.player.getRidingEntity()).rotationYaw = yaw;
    }
}
